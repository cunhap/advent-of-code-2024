import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.absoluteValue

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

data class Coordinate(val row: Int, val column: Int)

interface MapElement {
    val coordinate: Coordinate
}

typealias PuzzleMap = Map<Coordinate, MapElement>

enum class Direction {
    UP, DOWN, LEFT, RIGHT, NONE
}

fun shortestPath(element1: MapElement, element2: MapElement): List<Coordinate> {
    val directionVector = Coordinate(
        element2.coordinate.row - element1.coordinate.row,
        element2.coordinate.column - element1.coordinate.column
    )
    val path = mutableListOf<Coordinate>()
    var analyzingCoordinate = element1.coordinate
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