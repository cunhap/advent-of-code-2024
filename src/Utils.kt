import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

@Throws(java.security.NoSuchAlgorithmException::class)
        /**
         * Converts string to md5 hash.
         */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

// Data classes and enums
data class Coordinate(val row: Int, val column: Int) {
    operator fun plus(other: Coordinate) = Coordinate(row + other.row, column + other.column)
    operator fun minus(other: Coordinate) = Coordinate(row - other.row, column - other.column)
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT,
    DIAGONAL_UP_LEFT, DIAGONAL_UP_RIGHT,
    DIAGONAL_DOWN_LEFT, DIAGONAL_DOWN_RIGHT, NONE;

    fun turnRight(): Direction {
        return when(this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
            DIAGONAL_UP_LEFT -> DIAGONAL_UP_RIGHT
            DIAGONAL_UP_RIGHT -> DIAGONAL_DOWN_RIGHT
            DIAGONAL_DOWN_RIGHT -> DIAGONAL_DOWN_LEFT
            DIAGONAL_DOWN_LEFT -> DIAGONAL_UP_LEFT
            else -> NONE
        }
    }

    fun turnLeft(): Direction {
        return when(this) {
            UP -> LEFT
            LEFT -> DOWN
            DOWN -> RIGHT
            RIGHT -> UP
            DIAGONAL_UP_LEFT -> DIAGONAL_DOWN_LEFT
            DIAGONAL_DOWN_LEFT -> DIAGONAL_DOWN_RIGHT
            DIAGONAL_DOWN_RIGHT -> DIAGONAL_UP_RIGHT
            DIAGONAL_UP_RIGHT -> DIAGONAL_UP_LEFT
            else -> NONE
        }
    }

    fun opposite(): Direction {
        return when(this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
            DIAGONAL_UP_LEFT -> DIAGONAL_DOWN_RIGHT
            DIAGONAL_DOWN_RIGHT -> DIAGONAL_UP_LEFT
            DIAGONAL_UP_RIGHT -> DIAGONAL_DOWN_LEFT
            DIAGONAL_DOWN_LEFT -> DIAGONAL_UP_RIGHT
            else -> NONE
        }
    }
}

val DIRECTION_VECTORS = mapOf(
    Direction.UP to Coordinate(-1, 0),
    Direction.DOWN to Coordinate(1, 0),
    Direction.LEFT to Coordinate(0, -1),
    Direction.RIGHT to Coordinate(0, 1),
    Direction.DIAGONAL_UP_LEFT to Coordinate(-1, -1),
    Direction.DIAGONAL_UP_RIGHT to Coordinate(-1, 1),
    Direction.DIAGONAL_DOWN_LEFT to Coordinate(1, -1),
    Direction.DIAGONAL_DOWN_RIGHT to Coordinate(1, 1),
    Direction.NONE to Coordinate(0, 0)
)

interface MapElement {
    val coordinate: Coordinate
}

typealias PuzzleMap = Map<Coordinate, MapElement>

fun shortestPath(element1: MapElement, element2: MapElement): List<Coordinate> {
    val directionVector = calculateDirectionVector(element2.coordinate, element1.coordinate)
    val path = mutableListOf<Coordinate>()
    var analyzingCoordinate = element1.coordinate
    val startingDirection = getDirectionOfVector(directionVector)
    val firstDirection = startingDirection.first
    when(firstDirection) {
        Direction.UP -> {
            while (analyzingCoordinate.row > element2.coordinate.row) {
                analyzingCoordinate = analyzingCoordinate.copy(row = analyzingCoordinate.row - 1)
                path.add(analyzingCoordinate)
            }
        }
        Direction.DOWN -> {
            while (analyzingCoordinate.row < element2.coordinate.row) {
                analyzingCoordinate = analyzingCoordinate.copy(row = analyzingCoordinate.row + 1)
                path.add(analyzingCoordinate)
            }
        }
        else -> {}
    }

    when(startingDirection.second) {
        Direction.LEFT -> {
            while (analyzingCoordinate.column > element2.coordinate.column) {
                analyzingCoordinate = analyzingCoordinate.copy(column = analyzingCoordinate.column - 1)
                path.add(analyzingCoordinate)
            }
        }
        Direction.RIGHT -> {
            while (analyzingCoordinate.column < element2.coordinate.column) {
                analyzingCoordinate = analyzingCoordinate.copy(column = analyzingCoordinate.column + 1)
                path.add(analyzingCoordinate)
            }
        }
        else -> {}
    }

    return path
}

private fun calculateDirectionVector(element2: Coordinate, element1: Coordinate): Coordinate {
    val directionVector = Coordinate(
        element2.row - element1.row,
        element2.column - element1.column
    )
    return directionVector
}

private fun getDirectionOfVector(directionVector: Coordinate): Pair<Direction, Direction> {
    val startingDirection = when {
        directionVector.row > 0 && directionVector.column > 0 -> Direction.DOWN to Direction.RIGHT
        directionVector.row > 0 && directionVector.column < 0 -> Direction.DOWN to Direction.LEFT
        directionVector.row < 0 && directionVector.column > 0 -> Direction.UP to Direction.RIGHT
        directionVector.row < 0 && directionVector.column < 0 -> Direction.UP to Direction.LEFT
        directionVector.row == 0 && directionVector.column > 0 -> Direction.NONE to Direction.RIGHT
        directionVector.row == 0 && directionVector.column < 0 -> Direction.NONE to Direction.LEFT
        directionVector.row > 0 -> Direction.DOWN to Direction.NONE
        directionVector.row < 0 -> Direction.UP to Direction.NONE
        else -> throw IllegalStateException("Invalid direction vector: $directionVector")
    }
    return startingDirection
}

fun <T> Collection<T>.dropBlanks() = this.filter { it.toString().isNotBlank() }

infix fun String.diff(other: String): Int =
    indices.count { this[it] != other[it] } + (length - other.length).absoluteValue

fun List<String>.columnToString(column: Int): String =
    this.map { it[column] }.joinToString("")

fun rotateMap(inputMap: List<String>): List<String> {
    val numberOfColumns = inputMap[0].length
    val columns = (0 until numberOfColumns).map { i ->
        inputMap.map { it[i] }.joinToString("")
    }
    return columns
}

fun List<List<Any>>.outOfBounds(coordinate: Coordinate): Boolean {
    return coordinate.row < 0 || coordinate.row >= this.size || coordinate.column < 0 || coordinate.column >= this[0].size
}

fun distance(coord1: Coordinate, coord2: Coordinate): Int {
    val rowDifference = (coord1.row - coord2.row).toDouble()
    val columnDifference = (coord1.column - coord2.column).toDouble()
    return sqrt(rowDifference.pow(2) + columnDifference.pow(2)).toInt()
}

fun <T> Collection<T>.combinations(r: Int): Set<Set<T>> {
    if (r == 0) return setOf(emptySet())
    if (this.isEmpty()) return emptySet()
    val element = this.first()
    val rest = this.drop(1)
    val combinationsWithoutElement = rest.combinations(r)
    val combinationsWithElement = rest.combinations(r - 1).map { it + element }
    return combinationsWithoutElement + combinationsWithElement
}

fun generateMap(
    input: List<String>,
    processingFunction: (Char, Coordinate) -> Char
): List<List<Char>>{
    val map = input.mapIndexed { indexRow, row ->
        row.mapIndexed { indexColumn, columnChar ->
            val coordinate = Coordinate(indexRow, indexColumn)
            processingFunction(columnChar, coordinate)
        }
    }
    return map
}
