import java.io.File

data class Point(val x: Int, val y: Int)
data class Line(val start: Point, val end: Point) {
    fun cardinal(): Boolean = (start.x == end.x) || (start.y == end.y)
    fun points() = when {
        start.x == end.x -> ordered(start.y, end.y).map { Point(start.x, it) }
        start.y == end.y -> ordered(start.x, end.x).map { Point(it, start.y) }
        else -> (ordered(start.x, end.x) zip ordered(start.y, end.y)).map { Point(it.first, it.second) }
    }
}

/** Generate a properly ordered range over 2 integers. */
fun ordered(a: Int, b: Int) = IntProgression.fromClosedRange(a, b, if (a > b) -1 else 1)

/** Count number of overlaps of the given lines. */
fun overlaps(lines: List<Line>): Int {
    val counts = HashMap<Point, Int>()
    for (line in lines) {
        for (point in line.points()) counts.compute(point) { _, c -> (c ?: 0) + 1 }
    }

    return counts.count { it.value > 1 }
}

val LINE_REGEX = "(\\d+),(\\d+) -> (\\d+),(\\d+)".toRegex()
fun main() {
    val lines = File("inputs/day5.txt").readLines().map { line ->
        val (x1, y1, x2, y2) = LINE_REGEX.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
        Line(Point(x1, y1), Point(x2, y2))
    }

    println("Part 1: ${overlaps(lines.filter { it.cardinal() })}")
    println("Part 2: ${overlaps(lines)}")
}