package aoc2020

import java.io.File

fun main() {
    val lines = File("inputs/2020/day6.txt").readLines()
    val groups = sequence {
        val current = ArrayList<String>()
        for (line in lines) {
            if (line.trim().isEmpty() && current.size > 0) {
                yield(current.toList())
                current.clear()
            } else {
                current.add(line)
            }
        }
        if (current.size > 0) yield(current)
    }.toList()

    println("Part 1: ${groups.sumOf { it.joinToString("").toList().distinct().size }}")
    println("Part 2: ${groups.sumOf { g -> g.joinToString("").groupBy { it }.count { it.value.size == g.size } } }")
}