import java.io.File

data class Point20(val r: Int, val c: Int) {
    fun adjacent(): List<Point20> = (-1 .. 1).flatMap { dr -> (-1 .. 1).map { dc -> Point20(r + dr, c + dc) } }
}

data class State(val points: Set<Point20>, val default: Boolean)

/** Enhance an image consisting of light/dark points. */
fun enhance(state: State, index: BooleanArray): State {
    val min = Point20(state.points.minOf { it.r }, state.points.minOf { it.c })
    val max = Point20(state.points.maxOf { it.r }, state.points.maxOf { it.r })

    val points = HashSet<Point20>()
    for (r in min.r - 1 .. max.r + 1) {
        for (c in min.c - 1 .. max.c + 1) {
            val num = Point20(r, c).adjacent().map {
                if (it.r in (min.r .. max.r) && it.c in (min.c .. max.c)) state.points.contains(it) else state.default
            }.map { if (it) 1 else 0 }.joinToString("")

            val localIndex = Integer.parseInt(num, 2)
            if (index[localIndex]) points.add(Point20(r, c))
        }
    }

    return State(points, index[if(state.default) 511 else 0])
}

fun main() {
    val (rawEnhance, rawImage) = splitEmptyLines(File("inputs/day20.txt").readLines()).toList()
    val enhancer = rawEnhance[0].map { it == '#' }.toBooleanArray()
    val image = rawImage.flatMapIndexed { r, str -> str.mapIndexed { c, char -> if (char == '#') Point20(r, c) else null }}
        .filterNotNull()
        .toSet()

    val sequence = generateSequence(State(image, false)) { enhance(it, enhancer)}
    println("Part 1: ${sequence.elementAt(2).points.size}")
    println("Part 2: ${sequence.elementAt(50).points.size}")
}