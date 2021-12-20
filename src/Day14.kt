import java.io.File

fun step(input: Map<String, Long>, rules: Map<String, String>) = HashMap<String, Long>().apply {
    for ((key, value) in input) {
        val ruleMapping = rules[key]
        if (ruleMapping != null) {
            this.compute(key[0] + ruleMapping) { _, v -> (v ?: 0L) + value }
            this.compute(ruleMapping + key[1]) { _, v -> (v ?: 0L) + value }
        } else {
            this.compute(key) { _, v -> (v ?: 0L) + value }
        }
    }
}

fun elements(map: Map<String, Long>, last: Char) = HashMap<Char, Long>().apply {
    for ((key, value) in map) this.compute(key[0]) { _, v -> (v ?: 0L) + value }
    this.compute(last) { _, v -> (v ?: 0L) + 1 }
}

fun main() {
    val lines = File("inputs/day14.txt").readLines()
    val origin = lines[0]
    val originMap = origin.windowed(2).groupBy { it }.mapValues { it.value.size.toLong() }
    val transforms = lines.drop(2).associate {
        val (target, out) = it.split(" -> ")
        target to out
    }

    val transformed = generateSequence(originMap) { step(it, transforms) }.take(41).toList()
    val part1 = elements(transformed[10], origin.last())
    val part2 = elements(transformed[40], origin.last())
    println("Part 1: ${part1.maxOf { it.value } - part1.minOf { it.value }}")
    println("Part 2: ${part2.maxOf { it.value } - part2.minOf { it.value }}")
}