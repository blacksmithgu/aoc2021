import java.io.File

data class Instr(val axis: Char, val pos: Int)

fun fold(points: Set<Pair<Int, Int>>, instr: Instr): Set<Pair<Int, Int>> = points.map {
    if (instr.axis == 'x' && it.first > instr.pos) (instr.pos - (it.first - instr.pos)) to (it.second)
    else if (instr.axis == 'y' && it.second > instr.pos) it.first to (instr.pos - (it.second - instr.pos))
    else it
}.toSet()

fun main() {
    val lines = File("inputs/day13.txt").readLines()
    val pairs = lines.takeWhile { it.trim() != "" }.map {
        val (f, s) = it.split(",")
        f.toInt() to s.toInt()
    }.toSet()

    val instructions = lines.drop(pairs.size + 1).map {
        val (f, s) = it.split("=")
        Instr(f.last(), s.toInt())
    }

    println("Part 1: ${fold(pairs, instructions[0]).size}")
    println("Part 2: ")
    val grid = instructions.fold(pairs) { acc, next -> fold(acc, next) }
    for (y in grid.minOf { it.second } .. grid.maxOf { it.second }) {
        for (x in grid.minOf { it.first } .. grid.maxOf { it.first }) {
            if ((x to y) in grid) print("#")
            else print(".")
        }
        println()
    }
}