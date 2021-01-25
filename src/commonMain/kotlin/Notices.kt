package me.jacobtread.kni

class Notices(var date: String) {

    var errorMessage: String? = null
    var errorCause: Throwable? = null
    var notices: List<Notice>? = null

    fun isSuccess(): Boolean {
        return errorCause == null && errorMessage == null && notices != null;
    }

    override fun toString(): String {
        return "Notices(date='$date', errorMessage=$errorMessage, errorCause=$errorCause, notices=$notices)"
    }

}