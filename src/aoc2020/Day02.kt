package aoc2020

import java.io.File

data class Policy(val min: Int, val max: Int, val c: Char, val password: String)
val LINE_REGEX = Regex("(\\d+)-(\\d+) (.):(.+)")

fun main() {
    val lines = File("inputs/2020/day2.txt").readLines().map {
        val (min, max, ch, pass) = LINE_REGEX.matchEntire(it)!!.groupValues.drop(1)
        Policy(min.toInt(), max.toInt(), ch[0], pass)
    }

    println("Part 1: ${lines.count { (it.min..it.max).contains(it.password.count { c -> c == it.c }) }}")
    println("Part 2: ${lines.count { (it.password[it.min] == it.c) xor (it.password[it.max] == it.c) }}")
}