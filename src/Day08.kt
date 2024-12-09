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
    val antennaMap: MutableMap<Char, Set<Coordinate>> = mutableMapOf()
    val antiNodes = mutableSetOf<Coordinate>()
    val regexMatcher = Regex("""[0-9a-zA-Z]""")
    val map = generateMap(input) { char, coordinate ->
        if (char != '.' && regexMatcher.containsMatchIn(char.toString())) {
            antennaMap[char] = antennaMap.getOrDefault(char, setOf()) + coordinate
        }
        char
    }
    antennaMap.keys.forEach { key ->
        val pairOfAntennas = mutableSetOf<Pair<Coordinate, Coordinate>>()
        antennaMap[key]!!.combinations(2).map {
            val pair = Pair(it.first(), it.last())
            val reversed = Pair(it.last(), it.first())
            if(pair !in pairOfAntennas && reversed !in pairOfAntennas) {
                pairOfAntennas.add(pair)
            }
        }
        pairOfAntennas.forEach { (antenna1, antenna2) ->
            val extendedVector = extendVector(antenna1, antenna2).toList()
            extendedVector.forEach { coordinate ->
                if (map.outOfBounds(coordinate).not()) {
                    antiNodes.add(coordinate)
                }
            }
        }
    }
    return antiNodes.size
}

private fun part2(input: List<String>): Int {
    val antennaMap: MutableMap<Char, Set<Coordinate>> = mutableMapOf()
    val antiNodes = mutableSetOf<Coordinate>()
    val regexMatcher = Regex("""[0-9a-zA-Z]""")
    val map = generateMap(input) { char, coordinate ->
        if (char != '.' && regexMatcher.containsMatchIn(char.toString())) {
            antennaMap[char] = antennaMap.getOrDefault(char, setOf()) + coordinate
        }
        char
    }
    antennaMap.keys.forEach { key ->
        val pairOfAntennas = mutableSetOf<Pair<Coordinate, Coordinate>>()
        antennaMap[key]!!.combinations(2).map {
            val pair = Pair(it.first(), it.last())
            val reversed = Pair(it.last(), it.first())
            if(pair !in pairOfAntennas && reversed !in pairOfAntennas) {
                pairOfAntennas.add(pair)
            }
        }
        pairOfAntennas.forEach { (antenna1, antenna2) ->
            val extendedVector = coordinatesFromEdgeToEdge(map,antenna1, antenna2).asSequence()
            antiNodes.addAll(extendedVector)
        }
    }
    return antiNodes.size
}

fun extendVector(coord1: Coordinate, coord2: Coordinate): Pair<Coordinate, Coordinate> {
    val rowDifference = coord1.row - coord2.row
    val columnDifference = coord1.column - coord2.column

    val newStart = Coordinate(coord1.row + rowDifference, coord1.column + columnDifference)
    val newEnd = Coordinate(coord2.row - rowDifference, coord2.column - columnDifference)

    return Pair(newStart, newEnd)
}

fun coordinatesFromEdgeToEdge(map: List<List<Any>>, coord1: Coordinate, coord2: Coordinate): List<Coordinate> {
    val rowDifference = coord1.row - coord2.row
    val columnDifference = coord1.column - coord2.column
    var currentCoordinate = coord1
    val coordinates = mutableSetOf<Coordinate>()
    while(map.outOfBounds(currentCoordinate).not()) {
        coordinates.add(currentCoordinate)
        currentCoordinate = Coordinate(currentCoordinate.row + rowDifference, currentCoordinate.column + columnDifference)
    }
    currentCoordinate = Coordinate(currentCoordinate.row - rowDifference, currentCoordinate.column - columnDifference)
    while(map.outOfBounds(currentCoordinate).not()) {
        coordinates.add(currentCoordinate)
        currentCoordinate = Coordinate(currentCoordinate.row - rowDifference, currentCoordinate.column - columnDifference)
    }

    return coordinates.toList()
}