import kotlin.time.measureTime

private val day = object {}::class.java.name.substringBefore("Kt").substringAfter("Day")
fun main() {

    val xmasMap = mapOf("X" to "M", "M" to "A", "A" to "S")

    fun searchXmas(
        char: Char,
        startingCoordinate: Coordinate,
        searchCoordinate: Coordinate,
        input: List<List<Char>>
    ): Boolean {
        val nextCoordinate = startingCoordinate + searchCoordinate

        if (nextCoordinate.row < 0 || nextCoordinate.row >= input.size) return false
        if (nextCoordinate.column < 0 || nextCoordinate.column >= input[nextCoordinate.row].size) return false
        val nextChar = input[nextCoordinate.row][nextCoordinate.column]
        if (xmasMap[char.toString()] == nextChar.toString()) {
            if (nextChar == 'S') return true
            return searchXmas(nextChar, nextCoordinate, searchCoordinate, input)
        }
        return false
    }

    fun searchCrossMas(
        char: Char,
        startingCoordinate: Coordinate,
        searchCoordinate: Coordinate,
        input: List<List<Char>>
    ): Boolean {
        val previousCoordinate = startingCoordinate - searchCoordinate
        val nextCoordinate = startingCoordinate + searchCoordinate

        if (input.outOfBounds(nextCoordinate)) return false
        if (input.outOfBounds(previousCoordinate)) return false

        val nextChar = input[nextCoordinate.row][nextCoordinate.column]
        val previousChar = input[previousCoordinate.row][previousCoordinate.column]

        return char == 'A' && (previousChar == 'M' && nextChar == 'S' || previousChar == 'S' && nextChar == 'M')
    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.toCharArray().toList() }
        val searchCoordinates = DIRECTION_VECTORS.values
        var numberOfXmas = 0
        input.forEachIndexed { rowIndex, line ->
            line.forEachIndexed { columnIndex, char ->
                if (char == 'X') {
                    searchCoordinates.forEach {
                        if (searchXmas('X', Coordinate(rowIndex, columnIndex), it, map)) {
                            numberOfXmas++
                        }
                    }
                }
            }
        }
        return numberOfXmas
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toCharArray().toList() }
        val crossSearchCoordinates = listOf(
            DIRECTION_VECTORS[Direction.DIAGONAL_DOWN_LEFT]!!,
            DIRECTION_VECTORS[Direction.DIAGONAL_DOWN_RIGHT]!!
        )
        var numberOfCrossMas = 0
        map.forEachIndexed { rowIndex, line ->
            line.forEachIndexed { columnIndex, char ->
                if (char == 'A') {
                    val crossMas = crossSearchCoordinates.all {
                        searchCrossMas('A', Coordinate(rowIndex, columnIndex), it, map)
                    }
                    if (crossMas) {
                        numberOfCrossMas++
                    }
                }
            }
        }
        return numberOfCrossMas
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


fun List<List<Char>>.countPattern(
    targetChar: Char,
    searchFn: (Char, Coordinate) -> Int
): Int = flatMapIndexed { rowIndex, line ->
    line.mapIndexedNotNull { columnIndex, char ->
        if (char == targetChar) {
            searchFn(char, Coordinate(rowIndex, columnIndex))
        } else null
    }
}.sum()

