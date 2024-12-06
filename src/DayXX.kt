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

private fun part1(input: List<String>): Int {
    return 0
}

private fun part2(input: List<String>): Int {
    return 0
}