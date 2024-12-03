import kotlin.time.measureTime

private val day = object {}::class.java.name.substringBefore("Kt").substringAfter("Day").toInt()
fun main() {

    fun part1(input: List<String>): Int {
        val regex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
        val multPairs = input.flatMap { line ->
            regex.findAll(line).map { matchResult ->
                matchResult.destructured.let { (first, second) ->
                    Pair(
                        first.toInt(),
                        second.toInt()
                    )
                }
            }
        }

        return multPairs.sumOf { (first, second) -> first * second }
    }

    fun part2(input: List<String>): Long {
        var enabled = true
        val regex = Regex("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)")
        return regex.findAll(input.toString()).map { match ->
            if (match.groups[0]!!.value == "do()") enabled = true
            else if (match.groups[0]!!.value == "don't()") enabled = false
            else if (enabled) return@map match.groups[1]!!.value.toLong() * match.groups[2]!!.value.toLong()
            0
        }.sum()
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