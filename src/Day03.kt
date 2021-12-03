import java.io.File

fun majority1(lines: List<String>, index: Int) = lines.count { it[index] == '1' } * 2 >= lines.size

fun main() {
    val binary = File("inputs/day3.txt").readLines()

    // Part 1: Find the more common and less common bit.
    run {
        val more = CharArray(binary[0].length) { if(majority1(binary, it)) '1' else '0' }.concatToString()
        val less = more.map { if (it == '1') '0' else '1' }.toCharArray().concatToString()

        println("Part 1: ${more.toInt(2) * less.toInt(2)}")
    }

    // Part 2: Binary bifurcation to find two rating values.
    run {
        fun search(index: Int, options: List<String>, less: Boolean): String =
            if (options.size == 1) options[0]
            else {
                val target = if(majority1(options, index) xor less) '1' else '0'
                search(index + 1, options.filter { it[index] == target }, less)
            }

        val oxygen = search(0, binary, false).toInt(2)
        val co2 = search(0, binary, true).toInt(2)

        println("Part 2: ${oxygen * co2}")
    }
}