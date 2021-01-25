package me.jacobtread.kni


abstract class KNIImpl(host: String, var isHTTPS: Boolean = true) {

    protected var url: String

    protected var userAgent: String = "KAMAR/ Linux/ Android/"
    private var kamarKey: String = "vtku";
    protected var contentType: String = "application/x-www-form-urlencoded"

    init {
        url = host
        if (!(url.startsWith("https://") && url.startsWith("http://"))) {
            url = if (isHTTPS) {
                "https://$url"
            } else {
                "http://$url"
            }
        }
        if (!url.endsWith("/")) {
            url += '/'
        }
        url += "api/api.php"
    }

    abstract fun retrieve(date: String = getDate()): Notices;

    abstract fun getDate(): String

    fun getBody(date: String): String = "Key=${encode(kamarKey)}&Command=GetNotices&ShowAll=YES&Date=${encode(date)}"

    abstract fun encode(data: String): String;
}
