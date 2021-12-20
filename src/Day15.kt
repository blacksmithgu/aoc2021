import java.io.File
import java.util.*
import kotlin.collections.HashMap

data class P(val r: Int, val c: Int) {
    fun adjacent(bounds: P) = listOf(P(r - 1, c), P(r + 1, c), P(r, c - 1), P(r, c + 1)).filter {
        it.r >= 0 && it.r < bounds.r && it.c >= 0 && it.c < bounds.c
    }
}

data class CurrentPath(val head: P, val score: Int) : Comparable<CurrentPath> {
    override operator fun compareTo(other: CurrentPath): Int = score.compareTo(other.score)
}

fun solve(grid: Array<IntArray>): CurrentPath? {
    val queue = PriorityQueue<CurrentPath>()
    val best = HashMap<P, Int>()
    val bounds = P(grid.size, grid[0].size)

    queue.add(CurrentPath(P(0, 0), 0))

    while (queue.isNotEmpty()) {
        val next = queue.poll()!!
        if (next.head == P(grid.size - 1, grid[0].size - 1)) return next
        if (best.getOrDefault(next.head, 999999999) <= next.score) continue
        best[next.head] = next.score

        for (adj in next.head.adjacent(bounds))
            queue.add(CurrentPath(adj, next.score + grid[adj.r][adj.c]))
    }

    return null
}

fun main() {
    val cavern = File("inputs/day15.txt").readLines()
        .map { line -> line.map { it.digitToInt() }.toIntArray() }
        .toTypedArray()

    println("Part 1: ${solve(cavern)!!.score}")

    val supercavern = Array(cavern.size * 5) { r -> IntArray(cavern[0].size * 5) { c ->
        val original = cavern[r % cavern.size][c % cavern.size]
        (original + r / cavern.size + c / cavern[0].size - 1) % 9 + 1
    }}

    println("Part 2: ${solve(supercavern)!!.score}")
}