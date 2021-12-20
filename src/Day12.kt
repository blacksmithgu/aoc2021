import java.io.File
import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList

data class Part(val first: String, val second: String)
data class Path(val parts: List<String>) {
    fun valid1() = parts.groupBy { it }.filterKeys { !large(it) }.count { it.value.size > 1 } == 0
    fun valid2() = parts.groupBy { it }.filterKeys { !large(it) }.count { it.value.size > 2 } == 0
            && parts.groupBy { it }.filterKeys { !large(it) }.count { it.value.size == 2 } <= 1
            && parts.count { it == "start" } <= 1
            && parts.count { it == "end" } <= 1
}

fun large(cave: String) = cave.all { it.isUpperCase() }

fun countPaths(adjacency: Map<String, List<String>>, start: String, end: String, valid: Predicate<Path>): Int {
    val valids = LinkedList<Path>()
    val distinct = HashSet<Path>()
    valids.add(Path(listOf(start)))
    distinct.add(Path(listOf(start)))

    val finished = ArrayList<Path>()
    while (valids.isNotEmpty()) {
        val next = valids.poll()!!
        if (next.parts.last() == end) {
            finished.add(next)
            continue
        }

        for (adj in adjacency[next.parts.last()]!!) {
            val newPath = Path(next.parts.plus(adj))
            if (distinct.contains(newPath)) continue
            if (!valid.test(newPath)) continue
            valids.add(newPath)
        }
    }

    return finished.size
}

fun main() {
    val paths = File("inputs/day12.txt").readLines().map {
        val parts = it.split("-")
        Part(parts[0], parts[1])
    }

    /** Adjacent map which maps caves to adjacent caves. */
    val adjacency = (paths + paths.map { Part(it.second, it.first) }).distinct()
        .groupBy { it.first }
        .mapValues { it.value.map { p -> p.second } }

    println("Part 1: ${countPaths(adjacency, "start", "end") { it.valid1() }}")
    println("Part 1: ${countPaths(adjacency, "start", "end") { it.valid2() }}")
}