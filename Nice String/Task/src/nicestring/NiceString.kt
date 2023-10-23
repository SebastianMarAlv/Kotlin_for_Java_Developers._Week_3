package nicestring

fun String.isNice(): Boolean {
    val first = listOf("bu", "ba", "be").map { it in this }.none { it }
    val second = this.count { it in setOf('a', 'e', 'i', 'o', 'u') } >= 3
    val third = this.zipWithNext().any { (u, v) -> u == v }
    return first && (second || third) || second && third
}