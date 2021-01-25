package me.jacobtread.kni

open class Notice(
    var index: Int,
    var level: Level,
    var subject: String,
    var body: String,
    var teacher: String,
) {
    override fun toString(): String {
        return "Notice(level=$level, subject='$subject', body='$body', teacher='$teacher')"
    }
}

open class MeetingNotice(
    index: Int,
    level: Level,
    subject: String,
    body: String,
    teacher: String,
    var place: String,
    var date: String,
    var time: String,
) : Notice(index, level, subject, body, teacher) {
    override fun toString(): String {
        return "MeetingNotice(level=$level, subject='$subject', body='$body', teacher='$teacher', place='$place', date='$date', time='$time')"
    }
}

enum class Level {

    ALL,
    JUNIORS,
    SENIORS,
    OTHER;

    companion object {
        fun fromString(name: String): Level {
            for (value in values()) {
                if (value.name == name.toUpperCase()) {
                    return value;
                }
            }
            return OTHER;
        }
    }

}