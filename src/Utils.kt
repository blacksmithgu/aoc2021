/** Split an input of empty lines, returning the independent groups. */
fun splitEmptyLines(input: Iterable<String>): Sequence<List<String>> = sequence {
    val current = ArrayList<String>()
    for (line in input) {
        if (line.trim().isEmpty()) {
            yield(current.toList())
            current.clear()
        } else {
            current.add(line)
        }
    }
    yield(current.toList())
}