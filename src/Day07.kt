import kotlin.time.measureTime

fun main() {
    val day = object {}::class.java.name.substringBefore("Kt").substringAfter("Day")

    fun runParts(input: List<String>, label: String) {
        measureTime { println("$label Part 1: ${part1(input)}") }.also { println("Time Part 1: $it") }
        measureTime { println("$label Part 2: ${part2(input)}") }.also { println("Time Part 2: $it") }
    }

    runParts(readInput("Day${day}_test"), "Test")
    runParts(readInput("Day${day}"), "Result")
}

private fun part1(input: List<String>): Long {
    return input.sumOf { equation ->
        val (result, parts) = equation.split(": ").map { it.trim() }
        val equationMembers = parts.split(" ").map { it.toLong() }
        val operators = listOf("+", "*")
        val valid = isSolution(equationMembers, result.toLong(), operators, equationMembers[0], 1)
        if(valid) result.toLong() else 0
    }
}

private fun part2(input: List<String>): Long {
    return input.sumOf { equation ->
        val (result, parts) = equation.split(": ").map { it.trim() }
        val equationMembers = parts.split(" ").map { it.toLong() }
        val operators = listOf("+", "*", "||")
        val valid = isSolution(equationMembers, result.toLong(), operators, equationMembers[0], 1)
        if(valid) result.toLong() else 0
    }
}

private fun isSolution(equation: List<Long>, result: Long, operators: List<String>, accumulator: Long, workingIndex: Int): Boolean {
    if(workingIndex == equation.size) {
        return accumulator == result
    }

    if(accumulator > result) {
        return false
    }

    return operators.any { operator ->
        val newAccumulator = when(operator) {
            "+" -> accumulator + equation[workingIndex]
            "*" -> accumulator * equation[workingIndex]
            "||" -> "$accumulator${equation[workingIndex]}".toLong()
            else -> throw IllegalArgumentException("Invalid operator")
        }
        isSolution(equation, result, operators, newAccumulator, workingIndex + 1)
    }
}