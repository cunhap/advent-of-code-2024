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

private fun createMap(
    input: List<String>,
): Pair<List<List<Char>>, Coordinate?> {
    var startingPosition: Coordinate? = null
    val map = input.mapIndexed { indexRow, row ->
        row.mapIndexed { indexColumn, columnChar ->
            val coordinate = Coordinate(indexRow, indexColumn)
            if (columnChar == '^') {
                startingPosition = coordinate
                '.'
            } else columnChar
        }
    }
    return Pair(map, startingPosition)
}

private fun traverseMap(
    startingPosition: Coordinate,
    startingDirection: Direction,
    map: List<List<Char>>,
    obstacle: Coordinate? = null
): Set<Coordinate>? {
    val visited = mutableSetOf(startingPosition to startingDirection)
    var direction = startingDirection
    var nextCoordinate = startingPosition + DIRECTION_VECTORS[direction]!!
    var loop = false
    do {
        val nextChar = map[nextCoordinate.row][nextCoordinate.column]
        if (nextChar == '.' && nextCoordinate != obstacle) {
            visited.add(nextCoordinate to direction)
            nextCoordinate += DIRECTION_VECTORS[direction]!!
        } else {
            nextCoordinate -= DIRECTION_VECTORS[direction]!!
            direction = direction.turnRight()
            nextCoordinate += DIRECTION_VECTORS[direction]!!
            if(visited.contains(nextCoordinate to direction)) {
                loop = true
            }
        }
    } while (map.outOfBounds(nextCoordinate).not() && !loop)
    return if(loop) null else visited.map { it.first }.toSet()
}

private fun part1(input: List<String>): Int {

    val pair = createMap(input)
    val map = pair.first
    val startingPosition = pair.second

    startingPosition ?: return 0

    val visited = traverseMap(startingPosition, Direction.UP, map)  ?: emptySet()

    return visited.size
}

private fun part2(input: List<String>): Int {
    val pair = createMap(input)
    val map = pair.first
    val startingPosition = pair.second

    startingPosition ?: return 0

    val visited = traverseMap(startingPosition, Direction.UP, map) ?: emptySet()

    return visited.count {
        it != startingPosition && traverseMap(startingPosition, Direction.UP, map, it) == null
    }
}