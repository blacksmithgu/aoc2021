import java.io.File
import java.util.regex.Pattern

/** Simple wrapper for (index of win, score of that win). */
data class WinAt(val index: Int, val score: Int)

/** Trivial wrapper for an int array which allows looking for Bingo. */
class Board(private val values: IntArray) {
    /** Find the number that this board wins at, at the corresponding score. */
    fun winsAt(numbers: List<Int>): WinAt {
        val index = numbers.indices.first { this.winning(numbers.slice(0..it).toSet()) }
        return WinAt(index, this.score(numbers.slice(0..index).toSet(), numbers[index]))
    }

    /** Determine if a board is winning with the given set of called numbers. */
    private fun winning(called: Set<Int>): Boolean {
        for (r in 0..4) if ((0..4).all { called.contains(at(r, it)) }) return true
        for (c in 0..4) if ((0..4).all { called.contains(at(it, c)) }) return true
        return false
    }

    /** Determine the score for a board assuming it is winning. */
    private fun score(guesses: Set<Int>, last: Int): Int = last * values.filter { !guesses.contains(it) }.sum()

    /** Determine the value at a position on the board in (row, column) coordinates. */
    private inline fun at(r: Int, c: Int): Int = values[r * 5 + c]
}

fun main() {
    // Parse out the Bingo boards first; each one is represented as a 5x5 array.
    val rawLines = File("inputs/day4.txt").readLines()
    // The numbers being guessed in the game.
    val numbers = rawLines[0].split(",").map { it.toInt() }
    // The bingo boards that we'll be checking off.
    val boards = rawLines.asSequence()
        .drop(2)
        .filter { it.trim().isNotEmpty() }
        .chunked(5)
        .map { group -> group.joinToString(" ").trim() }
        .map { line -> Board(line.split(Pattern.compile("\\s+")).map { it.toInt() }.toIntArray()) }
        .toList()

    // Map each board to the time it wins, and the score.
    val ratings = boards.map { it.winsAt(numbers) }

    // Part 1: Find the first winning board and compute it's score.
    println("Part 1: ${ratings.minByOrNull { it.index }!!.score}")
    println("Part 2: ${ratings.maxByOrNull { it.index }!!.score}")
}