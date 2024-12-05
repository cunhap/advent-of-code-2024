import kotlin.time.measureTime

class PageValidator(input: List<String>) {
    private val precedenceMap = buildPrecedenceMap(input)
    private val updates = input.dropWhile { it.isNotEmpty() }.drop(1)

    private fun buildPrecedenceMap(input: List<String>): Map<Int, Set<Int>> =
        input.takeWhile { it.isNotEmpty() }
            .fold(mutableMapOf()) { acc, line ->
                val (page, precedingPage) = line.split("|").map(String::toInt)
                acc.apply { merge(precedingPage, setOf(page)) { old, new -> old + new } }
            }

    private fun isValidTransition(from: Int, to: Int): Boolean =
        precedenceMap[to]?.contains(from) ?: false

    fun part1(): Int = updates
        .map { it.split(",").map(String::toInt) }
        .filter { nums -> nums.zipWithNext().all { isValidTransition(it.first, it.second) } }
        .sumOf { it[(it.size - 1) / 2] }

    fun part2(): Int {
        var sum = 0
        updates.forEach { line ->
            val pageUpdate = line.split(",").map(String::toInt).toMutableList()
            if (!pageUpdate.zipWithNext().all { isValidTransition(it.first, it.second) }) {
                val validSequence = mutableListOf<Int>()
                var index = 0

                while (true) {
                    if (pageUpdate.last() == pageUpdate[index]) {
                        validSequence.add(pageUpdate[index])
                        if (validSequence.zipWithNext().all { isValidTransition(it.first, it.second) }) {
                            sum += validSequence[(validSequence.size - 1) / 2]
                            break
                        }
                        pageUpdate.clear()
                        pageUpdate.addAll(validSequence)
                        validSequence.clear()
                        index = 0
                    } else {
                        if (isValidTransition(pageUpdate[index], pageUpdate[index + 1])) {
                            validSequence.add(pageUpdate[index])
                            index++
                        } else {
                            pageUpdate[index] = pageUpdate[index + 1].also {
                                pageUpdate[index + 1] = pageUpdate[index]
                            }
                        }
                    }
                }
            }
        }
        return sum
    }
}

fun main() {
    val day = object {}::class.java.name.substringBefore("Kt").substringAfter("Day").toInt()

    fun runParts(input: List<String>, label: String) {
        val validator = PageValidator(input)
        measureTime { println("$label Part 1: ${validator.part1()}") }.also { println("Time Part 1: $it") }
        measureTime { println("$label Part 2: ${validator.part2()}") }.also { println("Time Part 2: $it") }
    }

    runParts(readInput("Day${day}_test"), "Test")
    runParts(readInput("Day${day}"), "Result")
}