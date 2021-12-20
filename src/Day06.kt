import java.io.File
import java.math.BigInteger

/** Simulate the breeding process via a frequency table for the given number of days. */
fun simulate(f: List<Int>, days: Int): BigInteger =
    generateSequence(Array(9) { i -> f.count{ it == i }.toBigInteger() }) {
        Array(9) { i -> if(i == 6) it[7] + it[0] else it[(i + 1) % 9] }
    }.elementAt(days).sumOf { it }

fun main() {
    val numbers = File("inputs/day6.txt").readLines()[0].split(",").map { it.toInt() }

    println("Part 1: ${simulate(numbers, 80)}")
    println("Part 2: ${simulate(numbers, 256)}")
}