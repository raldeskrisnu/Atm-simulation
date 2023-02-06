package test.app

fun String.isNumber(): Boolean {
    try {
        Integer.parseInt(this)
    } catch (e: Exception) {
        println("Input is not a number!")
        return false
    }
    return true
}
