import java.io.File
import java.util.*

data class Point11(val r: Int, val c: Int) {
    fun adjacent() = sequence {
        for (dr in -1 .. 1) {
            for (dc in -1 .. 1) {
                if (dr == 0 && dc == 0) continue
                if (r + dr < 0 || r + dr >= 10) continue
                if (c + dc < 0 || c + dc >= 10) continue
                yield(Point11(r + dr, c + dc))
            }
        }
    }.toList()
}

val POINTS = (0 .. 9).flatMap { r -> (0 .. 9).map { c -> Point11(r, c) } }
fun innerStep(state: Array<Array<Int>>): Pair<Int, Boolean> {
    var changed = false
    var bursts = 0
    for (point in POINTS) {
        if (state[point.r][point.c] >= 10) {
            bursts += 1
            state[point.r][point.c] = -1
            for (adj in point.adjacent().filter { state[it.r][it.c] != -1 }) {
                state[adj.r][adj.c] += 1
                changed = true
            }
        }
    }
    return bursts to changed
}

fun step(state: Array<Array<Int>>): Pair<Int, Boolean> {
    var flashes = 0
    for (point in POINTS) state[point.r][point.c] += 1

    var (bursts, changed) = innerStep(state)
    flashes += bursts
    while (changed) {
        val (rb, rc) = innerStep(state)
        flashes += rb
        changed = rc
    }

    for (point in POINTS) state[point.r][point.c] = state[point.r][point.c].coerceAtLeast(0)

    return flashes to (flashes == 100)
}

fun main() {
    val n1 = File("inputs/day11.txt").readLines().map { it.map { c -> c.digitToInt() }.toTypedArray() }.toTypedArray()

    println("Part 1: ${(1 .. 100).sumOf { step(n1).first }}")

    val n2 = File("inputs/day11.txt").readLines().map { it.map { c -> c.digitToInt() }.toTypedArray() }.toTypedArray()
    println("Part 2: ${(1 .. 100000).first { step(n2).second }}")
}