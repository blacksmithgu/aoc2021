import java.io.File
import java.util.*

val BRACKETS = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
val CORRUPT_SCORES = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
val END_SCORES = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)

fun firstInvalid(line: String): Char? {
    val stack = Stack<Char>()
    for (char in line) {
        if (!BRACKETS.containsKey(char)) {
            if (stack.isEmpty()) return char
            else if (char != BRACKETS[stack.pop()]) return char
        } else {
            stack.push(char)
        }
    }

    return null
}

fun complete(line: String): Long {
    val stack = Stack<Char>()
    for (char in line) {
        if (!BRACKETS.containsKey(char)) stack.pop()
        else stack.push(char)
    }

    return stack.reversed().fold(0L) { acc, n -> acc * 5 + END_SCORES[n]!! }
}

fun main() {
    val lines = File("inputs/day10.txt").readLines()

    println("Part 1: ${lines.mapNotNull { firstInvalid(it) }.sumOf { CORRUPT_SCORES[it]!! }}")
    val scores = lines.filter { firstInvalid(it) == null }.map { complete(it) }.sorted()
    println("Part 2: ${scores[scores.size / 2]}")
}