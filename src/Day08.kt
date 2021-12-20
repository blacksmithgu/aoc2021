import java.io.File
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.absoluteValue

data class Display(val segments: List<String>, val output: List<String>)

val UNIQUE_LENGTHS = setOf(2, 3, 4, 7)
val SHAPES = mapOf("abcefg" to 0, "cf" to 1, "acdeg" to 2, "acdfg" to 3, "bcdf" to 4,
    "abdfg" to 5, "abdefg" to 6, "acf" to 7, "abcdefg" to 8, "abcdfg" to 9)

fun substitute(source: String, guesses: Map<Char, Char>): String =
    source.map{ guesses[it] ?: 'x' }.sorted().joinToString("")

fun solve(display: Display, guesses: HashMap<Char, Char>, current: Char): Int? {
    if (current == 'h') {
        // Check if this is a valid substitution...
        val possible = display.segments.map { substitute(it, guesses) }
        if (!possible.containsAll(SHAPES.keys)) return null

        return display.output.map { SHAPES[substitute(it, guesses)] }.joinToString("").toInt()
    }

    // Try all possible permutations of a-g.
    for (target in ('a' .. 'g').filter { !guesses.containsValue(it) }) {
        guesses[current] = target
        val result = solve(display, guesses, current + 1)
        if (result != null) return result
        guesses.remove(current)
    }

    return null
}

fun main() {
    val displays = File("inputs/day8.txt").readLines().map {
        val (test, out) = it.split(" | ")
        Display(test.split(" "), out.split(" "))
    }

    println("Part 1: ${displays.sumOf { d -> d.output.sumOf { if (UNIQUE_LENGTHS.contains(it.length)) 1L else 0L } }}")
    println("Part 2: ${displays.sumOf { solve(it, HashMap(), 'a')!! }}")
}