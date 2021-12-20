package aoc2020

import java.io.File

fun main() {
    val lines = File("inputs/2020/day3.txt").readLines()
    fun count(r: Int, d: Int) =
        lines.indices.step(d).withIndex().count { (index, v) -> lines[v][(index * r) % lines[v].length] == '#' }

    println("Part 1: ${count(3, 1)}")
    println("Part 2: ${count(1, 1).toLong() * count(3, 1) * count(5, 1) * count(7, 1) * count(1, 2)}")
}