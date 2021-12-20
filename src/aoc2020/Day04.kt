package aoc2020

import splitEmptyLines
import java.io.File

val REQUIRED_PART1 = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

fun validate(pass: Map<String, String>): Boolean {
    if (!REQUIRED_PART1.all { pass.containsKey(it) }) return false

    val byr = pass["byr"]!!.toInt()
    if (byr < 1920 || byr > 2002) return false

    val iyr = pass["iyr"]!!.toInt()
    if (iyr < 2010 || iyr > 2020) return false

    val eyr = pass["eyr"]!!.toInt()
    if (eyr < 2020 || eyr > 2030) return false

    val heightMatch = Regex("(\\d+)(cm|in)").matchEntire(pass["hgt"]!!) ?: return false
    if (!setOf("cm", "in").contains(heightMatch.groupValues[2])) return false
    val height = heightMatch.groupValues[1].toInt()
    if (heightMatch.groupValues[2] == "cm" && (height < 150 || height > 193)) return false
    if (heightMatch.groupValues[2] == "in" && (height < 59 || height > 76)) return false

    Regex("#[0-9a-f]{6}").matchEntire(pass["hcl"]!!) ?: return false
    Regex("amb|blu|brn|gry|grn|hzl|oth").matchEntire(pass["ecl"]!!) ?: return false
    Regex("\\d{9}").matchEntire(pass["pid"]!!) ?: return false
    return true
}

fun main() {
    val lines = File("inputs/2020/day4.txt").readLines()
    val passports = splitEmptyLines(lines).map { group ->
        val result = HashMap<String, String>()
        for (line in group) {
            for (piece in line.split(" ")) {
                val parts = piece.split(":")
                result[parts[0]] = parts[1]
            }
        }
        result
    }.toList()

    println("Part 1: ${passports.count { m -> REQUIRED_PART1.all { m.containsKey(it) } }}")
    println("Part 2: ${passports.count { validate(it) }}")
}