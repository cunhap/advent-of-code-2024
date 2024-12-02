fun main() {
    fun part1(input: List<String>): Int {
        val pairs = input.map { it.split("\\s+".toRegex()) }
            .map { it[0].toInt() to it[1].toInt() }
        val leftElements = pairs.map { it.first }.sorted()
        val rightElements = pairs.map { it.second }.sorted()
        return leftElements.zip(rightElements)
            .sumOf { (left, right) -> if (left > right) left - right else right - left }

    }

    fun part2(input: List<String>): Int {
        val pairs = input.map { it.split("\\s+".toRegex()) }
            .map { it[0].toInt() to it[1].toInt() }
        val leftElements = pairs.map { it.first }.sorted()
        val rightElements = pairs.map { it.second }.sorted()
        val countNumberOfRightElements = rightElements.groupingBy { it }.eachCount()
        return leftElements.sumOf { element -> element * (countNumberOfRightElements[element] ?: 0) }
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
