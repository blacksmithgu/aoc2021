import java.io.File

fun main() {
    val depths = File("inputs/day1.txt").readLines().map { it.toInt() }

    // Part 1: Measure number of times that a depth measurement increases.
    val increases = depths.windowed(2).count { it[0] < it[1] }
    println("Part 1: $increases")

    // Part 2: Measure number of times that sliding window of measurement increases.
    val sumIncreases = depths.windowed(3) { it.sum() }.windowed(2).count { it[0] < it[1] }
    println("Part 2: $sumIncreases")
}