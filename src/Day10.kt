import kotlin.math.abs
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
    val trailHeads = mutableListOf<Coordinate>()
    val map = generateMap(input) { char, coordinate ->
        if (char == '0') {
            trailHeads.add(coordinate)
        }
        char.digitToInt()
    }

    return trailHeads.sumOf { calculatePaths(map, it).countFullPathScore(true) }
}

private fun part2(input: List<String>): Int {
    val trailHeads = mutableListOf<Coordinate>()
    val map = generateMap(input) { char, coordinate ->
        if (char == '0') {
            trailHeads.add(coordinate)
        }
        char.digitToInt()
    }

    return trailHeads.sumOf { calculatePaths(map, it).countFullPathScore(false) }
}

fun calculatePaths(map: List<List<Int>>, coordinate: Coordinate): Node {
    val allowedDirections = listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
    if (map[coordinate.row][coordinate.column] == 9) {
        return Node(coordinate, 9, mutableListOf())
    }
    val childNodes = mutableListOf<Node>()
    allowedDirections.forEach { direction ->
        val nextCoordinate = coordinate.move(direction)
        if (nextCoordinate.isValid(coordinate, map)) {
            childNodes.add(calculatePaths(map, nextCoordinate))
        }
    }

    return Node(coordinate, map[coordinate.row][coordinate.column], childNodes)
}

private fun Node.countFullPathScore(distinct: Boolean): Int {
    if(distinct) {
        val visited = mutableSetOf<Coordinate>()
        fun dfs(node: Node) {
            if (node.value == 9) {
                visited.add(node.coordinate)
            }
            node.children.forEach { dfs(it) }
        }
        dfs(this)
        return visited.size
    } else {
        if (value == 9) {
            return 1
        }
        return children.sumOf { it.countFullPathScore(false) }
    }
}

private fun Coordinate.isValid(previousCoordinate: Coordinate, map: List<List<Int>>): Boolean {
    if (map.outOfBounds(this)) return false
    val previousNumber = map[previousCoordinate.row][previousCoordinate.column]
    val currentNumber = map[row][column]
    return currentNumber - previousNumber == 1
}