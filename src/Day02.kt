import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val levels = input.map { level -> level.split("\\s+".toRegex()).map { it.toInt() } }
        val result = levels.count { level ->
            level.isSafe()
        }
        return result
    }


    fun part2(input: List<String>): Int {
        val levels = input.map { level -> level.split("\\s+".toRegex()).map { it.toInt() } }
        return levels.count { level ->
            level.isSafe() || level.indices
                .map { i -> level.subList(0, i) + level.subList(i + 1, level.size) }
                .any { it.isSafe() }
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4) {
        "Expected 4, got ${part2(testInput)}"
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

fun isSafeStep(levelPair: Pair<Int, Int>): Boolean {
    val safeRange = 1..3
    val step = (levelPair.first - levelPair.second).absoluteValue
    val safeStep = safeRange.contains(step)
    return safeStep
}

fun List<Int>.isSafe(): Boolean {
    val windows = this.zipWithNext()
    val isIncreasing = windows.first().first < windows.first().second
    return windows
        .all {
            (isIncreasing == it.first < it.second || !isIncreasing == it.first > it.second) &&
                    isSafeStep(it)
        }
}