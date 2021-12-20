import java.io.File
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.absoluteValue

fun main() {
    val crabs = File("inputs/day7.txt").readLines().single().split(",").map { it.toInt() }
    fun dist1(p: Int) = crabs.sumOf { abs(it - p) }
    fun dist2(p: Int) = crabs.sumOf { abs(it - p) * (abs(it - p) + 1) / 2 }

    println("Part 1: ${(crabs.minOf{it} .. crabs.maxOf{it}).minOf { dist1(it) }}")
    println("Part 2: ${(crabs.minOf{it} .. crabs.maxOf{it}).minOf { dist2(it) }}")
}