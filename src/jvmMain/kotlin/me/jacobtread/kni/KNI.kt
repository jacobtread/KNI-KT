package me.jacobtread.kni

import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList

class KNI(host: String, isHTTPS: Boolean = true) : KNIImpl(host, isHTTPS) {

    private var dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var documentBuilder: DocumentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder()

    override fun retrieve(date: String): Notices {
        val noticesObject = Notices(date)
        try {
            with(URL(this.url).openConnection() as HttpURLConnection) {
                requestMethod = "POST"
                doOutput = true
                doInput = true
                addRequestProperty("User-Agent", this@KNI.userAgent)
                addRequestProperty("Content-Type", this@KNI.contentType)
                outputStream.use {
                    it.write(getBody(date).toByteArray(StandardCharsets.UTF_8))
                }
                inputStream.use {
                    val document = documentBuilder.parse(it)
                    val errorNodeList = document.getElementsByTagName("Error")
                    if (errorNodeList.length > 0) {
                        val errorNode = errorNodeList.item(0)
                        noticesObject.errorMessage = errorNode.textContent
                    } else {
                        val notices: ArrayList<Notice> = ArrayList()
                        val generalNodeList: NodeList = document.getElementsByTagName("General")
                        val meetingNodeList: NodeList = document.getElementsByTagName("Meeting")
                        val total = generalNodeList.length + meetingNodeList.length
                        if (total > 0) {
                            for (i in 0 until total) {
                                val nodeList: NodeList = if (i >= generalNodeList.length) {
                                    meetingNodeList
                                } else {
                                    generalNodeList
                                }
                                val index = if (nodeList == meetingNodeList) {
                                    i - generalNodeList.length
                                } else {
                                    i
                                }
                                val node: Node = nodeList.item(index)
                                val notice: Notice? = parseNotice(node)
                                if (notice != null) {
                                    notices += notice
                                }
                            }
                        }
                        noticesObject.notices = notices
                    }
                }

            }
        } catch (e: Exception) {
            noticesObject.errorCause = e
            noticesObject.errorMessage = e.message
        }
        return noticesObject
    }

    private fun parseNotice(node: Node): Notice? {
        val isMeeting: Boolean = node.nodeName == "Meeting"
        val attributes: NamedNodeMap = node.attributes
        var index = 0
        if (attributes.length > 0) {
            val indexNode: Node = attributes.getNamedItem("index")
            if (indexNode.textContent != null) {
                try {
                    index = Integer.parseInt(indexNode.textContent)
                } catch (e: NumberFormatException) {
                }
            }
        }

        var level: Level? = null
        var subject: String? = null
        var body: String? = null
        var teacher: String? = null

        var place: String? = null
        var date: String? = null
        var time: String? = null

        val childNodes: NodeList = node.childNodes
        if (childNodes.length < 4) {
            return null
        }
        for (i in 0 until childNodes.length) {
            val childNode: Node = childNodes.item(i)
            val name: String = childNode.nodeName
            val content: String = childNode.textContent
            when (name) {
                "Level" -> level = Level.fromString(content)
                "Subject" -> subject = content
                "Body" -> body = content
                "Teacher" -> teacher = content
            }
            if (isMeeting) {
                when (name) {
                    "PlaceMeet" -> place = content
                    "DateMeet" -> date = content
                    "TimeMeet" -> time = content
                }
            }
        }
        if (level == null || subject == null || body == null || teacher == null) {
            return null
        }
        if (isMeeting) {
            if (place == null || date == null || time == null) {
                return null
            }
            return MeetingNotice(index, level, subject, body, teacher, place, date, time)
        }
        return Notice(index, level, subject, body, teacher)
    }

    override fun getDate(): String {
        return dateFormat.format(Date())
    }

    override fun encode(data: String): String {
        val value: String? = URLEncoder.encode(data, StandardCharsets.UTF_8)
        return value ?: ""
    }

}