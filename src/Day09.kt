import java.io.File

data class Heightmap(val data: List<List<Int>>) {
    private val rows = data.size
    private val columns = data[0].size

    /** Fetch the height at the given coordinates. */
    fun get(r: Int, c: Int) = this.data[r][c]
    /** Fetch the height at the given coordinate pair. */
    fun get(rc: Pair<Int, Int>) = this.data[rc.first][rc.second]
    /** Return an iterator over all valid points in the heightmap. */
    fun points() = (0 until this.rows).flatMap { r -> (0 until this.columns).map { c -> r to c }}

    /** Determine whether this coordinate is a relative low point. */
    fun low(r: Int, c: Int) = adjacent(r, c).all { get(it) > get(r, c) }

    /** Follow a point to it's terminating low point if it is in a basin; return null if it is not part of a basin. */
    fun follow(r: Int, c: Int): Pair<Int, Int>? = when {
        get(r, c) == 9 -> null
        low(r, c) -> r to c
        else -> {
            val lowPoints = adjacent(r, c).filter { get(it) < get(r, c) }.map { (x, y) -> follow(x, y) }.toSet()
            if (lowPoints.size == 1) lowPoints.first() else null
        }
    }

    /** Find all valid adjacent points to the given point. */
    private fun adjacent(r: Int, c: Int) = listOf((r + 1) to c, (r - 1) to c, r to c + 1, r to c - 1).filter {
            (r, c) -> r >= 0 && r < this.rows && c >= 0 && c < this.columns
    }
}

fun main() {
    val lines = File("inputs/day9.txt").readLines()
    val heightmap = Heightmap(lines.map { it.toCharArray().map { c -> c.digitToInt() } })

    println("Part 1: ${heightmap.points().filter { heightmap.low(it.first, it.second) }.sumOf { heightmap.get(it) + 1 }}")

    val basins = heightmap.points().mapNotNull { (x, y) -> heightmap.follow(x, y) }
        .groupBy { it }.values
        .map { it.size }
    println("Part 2: ${basins.sorted().reversed().take(3).fold(1) { a, b -> a * b }}")
}