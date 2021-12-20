import java.io.File

sealed class Explosion {
    data class Same(val snail: Snail) : Explosion()
    data class Exploded(val snail: Snail, val left: Long?, val right: Long?): Explosion()
}

sealed class Snail {
    class Constant(val value: Long): Snail()
    class Node(val left: Snail, val right: Snail): Snail()

    override fun toString(): String = when (this) {
        is Constant -> value.toString()
        is Node -> "[$left,$right]"
    }

    companion object {
        fun parse(line: String, index: Int): Pair<Snail, Int> =
            if (line[index] == '[') {
                val (left, lindex) = parse(line, index + 1)
                val (right, rindex) = parse(line, lindex + 1)
                Node(left, right) to (rindex + 1)
            } else Constant(line[index].digitToInt().toLong()) to (index + 1)

        fun magnitude(value: Snail): Long = when(value) {
            is Constant -> value.value
            is Node -> 3L * magnitude(value.left) + 2L * magnitude(value.right)
        }

        private fun increment(value: Snail, amount: Long, left: Boolean): Snail = when (value) {
            is Constant -> Constant(value.value + amount)
            is Node -> if (left) Node(increment(value.left, amount, true), value.right)
                       else Node(value.left, increment(value.right, amount, false))
        }

        private fun explode(value: Snail, depth: Int): Explosion {
            if (value is Node) {
                if (value.left is Constant && value.right is Constant && depth == 4)
                    return Explosion.Exploded(Constant(0), value.left.value, value.right.value)

                var explosion = explode(value.left, depth + 1)
                if (explosion is Explosion.Exploded) {
                    return if (explosion.right != null)
                        Explosion.Exploded(Node(explosion.snail, increment(value.right, explosion.right!!, true)), explosion.left, null)
                    else Explosion.Exploded(Node(explosion.snail, value.right), explosion.left, null)
                }

                explosion = explode(value.right, depth + 1)
                if (explosion is Explosion.Exploded) {
                    return if (explosion.left != null)
                        Explosion.Exploded(Node(increment(value.left, explosion.left!!, false), explosion.snail), null, explosion.right)
                    else Explosion.Exploded(Node(value.left, explosion.snail), null, explosion.right)
                }
            }

            return Explosion.Same(value)
        }

        fun split(value: Snail): Pair<Snail, Boolean> {
            if (value is Constant && value.value >= 10) return Node(Constant(value.value / 2), Constant(value.value - (value.value / 2))) to true
            else if (value is Node) {
                val (lresult, lsplit) = split(value.left)
                if (lsplit) return Node(lresult, value.right) to true
                val (rresult, rsplit) = split(value.right)
                if (rsplit) return Node(value.left, rresult) to true
            }

            return value to false
        }

        private fun reduce(value: Snail): Snail {
            var current = value
            var changed = false

            do {
                val exploded = explode(current, 0)
                if (exploded is Explosion.Exploded) {
                    println("Exploded ${current} -> ${exploded.snail}")
                    current = exploded.snail
                    changed = true
                    continue
                }

                val (result, change) = split(current)
                if (change) println("Split ${current} -> ${result}")
                current = result
                changed = change
            } while (changed)

            return current
        }

        fun add(left: Snail, right: Snail) = reduce(Node(left, right))
    }
}

fun main() {
    val lines = File("inputs/day18.txt").readLines()
    val snails = lines.map { Snail.parse(it, 0).first }

    println("Part 1: ${Snail.magnitude(snails.reduce { acc, snail -> Snail.add(acc, snail) })}")
    println("Part 2: ${snails.maxOf { f -> snails.maxOf { s -> Snail.magnitude(Snail.add(f, s)) } }}")
}