import me.jacobtread.kni.*

fun main() {
    val kni = KNI("demo.school.kiwi");
    val noticeObject: Notices = kni.retrieve("01/01/2020")
    print (noticeObject)
}