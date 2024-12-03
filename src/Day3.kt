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
        val regex = Regex("do\\(\\)|don't\\(\\)|mul\\((\\d+),(\\d+)\\)")
        return regex.findAll(input.toString()).fold(0) { acc, matchResult ->
            val operation = matchResult.groups[0]!!.value
            when (operation) {
                "do()" -> enabled = true
                "don't()" -> enabled = false
                else -> if (enabled) {
                    val value1 = matchResult.groups[1]?.value?.toLong()
                    val value2 = matchResult.groups[2]?.value?.toLong()
                    if (value1 != null && value2 != null) {
                        return@fold acc + (value1 * value2)
                    }
                }
            }
            acc
        }
    }

    670 + 3350 + 2680
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