import java.io.File

data class Instruction(val direction: String, val amount: Int)
data class DepthState(val aim: Int, val depth: Int)

fun main() {
    val instrs = File("inputs/day2.txt").readLines().map {
        val split = it.split(" ")
        Instruction(split[0], split[1].toInt())
    }

    // Part 1: Compute horizontal and vertical distance traveled and output their product.
    run {
        val hor = instrs.filter { it.direction == "forward" }.sumOf { it.amount }
        val depth =
            instrs.filter { it.direction != "forward" }.sumOf { if (it.direction == "down") it.amount else -it.amount }
        println("Part 1: ${hor * depth}")
    }

    // Part 2: Compute horizontal and vertical distance traveled in the presence of 'aim'.
    run {
        val hor = instrs.filter { it.direction == "forward" }.sumOf { it.amount }
        val depth = instrs.fold(DepthState(0, 0)) { acc, instruction ->
            when (instruction.direction) {
                "forward" -> DepthState(acc.aim, acc.depth + acc.aim * instruction.amount)
                "down" -> DepthState(acc.aim + instruction.amount, acc.depth)
                else -> DepthState(acc.aim - instruction.amount, acc.depth)
            }
        }.depth

        println("Part 2: ${hor * depth}")
    }
}