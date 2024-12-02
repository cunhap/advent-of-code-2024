import kotlin.time.measureTime

private val day = object {}::class.java.name.substringBefore("Kt").substringAfter("Day").toInt()
fun main() {

    fun part1(input: List<String>): Int {
        return 0
    }


    fun part2(input: List<String>): Long {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    part1(testInput).also { println("Part 1 Test = $it") }
    part2(testInput).also { println("Part 2 Test = $it") }

    val input = readInput("Day${day}")
    measureTime {
        println("Result Part 1: ${part1(input)}")
    }.also { println("Time Part 1: $it") }
    measureTime {
        println("Result Part 2: ${part2(input)}")
    }.also { println("Time Part 2: $it") }
}