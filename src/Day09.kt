
fun main() {
    data class Chunk(val size: Int, val id: Int? = null)

    val chunks = readln().map(Char::digitToInt).foldIndexed(mutableMapOf<Int, Chunk>()) { i, acc, size ->
        acc.apply { this[values.sumOf { it.size }] = Chunk(size, if (i % 2 == 0) i / 2 else null) }
    }

    fun Map<Int, Chunk>.toFS() = entries.sortedBy { it.key }.map { it.value }
        .flatMap { (size, id) -> (0..<size).map { id } }

    fun part1() = chunks.toFS().toMutableList().also { fs ->
        var (free, file) = 0 to fs.lastIndex
        while (true) {
            while (fs[free] != null) free++
            while (fs[file] == null) file--
            if (free > file) break
            fs[free] = fs[file]
            fs[file] = null
        }
    }

    fun part2() = chunks.toMutableMap().apply {
        for (id in mapNotNull { it.value.id }.sortedDescending()) {
            val (fileIndex, fileChunk) = entries.single { it.value.id == id }
            val (freeIndex, freeChunk) = entries
                .filter { it.value.id == null && it.key < fileIndex && it.value.size >= fileChunk.size }
                .minByOrNull { it.key } ?: continue

            this[fileIndex] = Chunk(fileChunk.size)
            this[freeIndex] = fileChunk

            val remainingFree = freeChunk.size - fileChunk.size
            if (remainingFree > 0)
                this[freeIndex + fileChunk.size] = Chunk(remainingFree)
        }
    }.toFS()

    fun List<Int?>.checksum() = mapIndexedNotNull { i, it -> it?.times(i)?.toLong() }.sum()

    sequenceOf(::part1, ::part2).map { it().checksum() }.forEach(::println)
}

/*
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

private fun part1(input: List<String>): Long {
    val line = input.first()
    var fileNumber = 0
    var filledIndices = mutableListOf<Pair<Int, String>>()
    val emptyIndices = mutableListOf<Int>()
    val expandedInput = mutableListOf<String>()
    line.forEachIndexed { index, c ->
        val numberOfChars = c.toString().toInt()
        val newOutput = mutableListOf<String>()
        val outputChar = if (index % 2 == 0) {
            fileNumber.toString().also { fileNumber++ }
        } else {
            "."
        }
        for (i in 0 until numberOfChars) {
            newOutput.add(outputChar)
        }
        if (newOutput.isNotEmpty()) {
            expandedInput.addAll(newOutput)
            val lastNewIndex = expandedInput.size - 1
            val firstNewIndex = expandedInput.size - numberOfChars
            if (outputChar == ".") emptyIndices.addAll(firstNewIndex..lastNewIndex) else filledIndices.addAll(
                (firstNewIndex..lastNewIndex).map { Pair(it, outputChar) }
            )

        }
    }

    while (filledIndices.last().first > emptyIndices.first()) {
        val movingFilled = filledIndices.last()
        val movingEmpty = emptyIndices.first()
        filledIndices.add(movingEmpty, Pair(movingEmpty, movingFilled.second))
        filledIndices = filledIndices.dropLast(1).toMutableList()
        emptyIndices.removeAt(0)
        emptyIndices.add(movingFilled.first)
    }

    return filledIndices.sumOf { it.first * it.second.toLong() }
}

data class DataFile(val id: Long, val startIndex: Int, val endIndex: Int) {
    val size = endIndex - startIndex + 1
}

data class EmptySpace(val startIndex: Int, val endIndex: Int)

private fun part2(input: List<String>): Long {
    val line = input.first()
    var dataFiles = mutableListOf<DataFile>()
    val emptySpaces = mutableListOf<EmptySpace>()
    val expandedInput = mutableListOf<String>()
    val mapOfSizeToEmptySpaces = mutableMapOf<Int, MutableList<EmptySpace>>()
    var fileNumber = 0
    line.forEachIndexed { index, c ->
        val numberOfChars = c.toString().toInt()
        val newOutput = mutableListOf<String>()
        val outputChar = if (index % 2 == 0) {
            fileNumber.toString().also { fileNumber++ }
        } else {
            "."
        }
        for (i in 0 until numberOfChars) {
            newOutput.add(outputChar)
        }
        if (newOutput.isNotEmpty()) {
            expandedInput.addAll(newOutput)
            val lastNewIndex = expandedInput.size - 1
            val firstNewIndex = expandedInput.size - numberOfChars
            if (outputChar == ".") {
                emptySpaces.add(EmptySpace(firstNewIndex, lastNewIndex))
                val size = lastNewIndex - firstNewIndex + 1
                mapOfSizeToEmptySpaces.putIfAbsent(size, mutableListOf())
                mapOfSizeToEmptySpaces[size]!!.add(EmptySpace(firstNewIndex, lastNewIndex))
            } else {
                dataFiles.add(DataFile(outputChar.toLong(), firstNewIndex, lastNewIndex))
            }
        }
    }

    mapOfSizeToEmptySpaces.values.forEach { it.sortBy { emptySpace -> emptySpace.startIndex } }

    var dataFileIterator = dataFiles.size - 1
    while (dataFileIterator >= 0) {
        val dataFileToMove = dataFiles[dataFileIterator]
        val sizeOfDataFile = dataFileToMove.size
        val emptySpaceKey = if(mapOfSizeToEmptySpaces.containsKey(sizeOfDataFile)) {
            sizeOfDataFile
        } else {
            mapOfSizeToEmptySpaces.keys.firstOrNull { it > sizeOfDataFile }
        }
        if(emptySpaceKey == null) {
            dataFileIterator--
            continue
        }
        val emptySpace = mapOfSizeToEmptySpaces[emptySpaceKey]!!.firstOrNull()
        if(emptySpace == null) {
            dataFileIterator--
            continue
        }
        mapOfSizeToEmptySpaces[emptySpaceKey]!!.removeAt(0)
        if(emptySpaceKey != sizeOfDataFile) {
            val newKeyPair = Pair(emptySpaceKey - sizeOfDataFile, EmptySpace(emptySpace.startIndex + sizeOfDataFile, emptySpace.endIndex))
            mapOfSizeToEmptySpaces.getOrPut(newKeyPair.first) { mutableListOf() }.add(0, newKeyPair.second)
            mapOfSizeToEmptySpaces.values.forEach { it.sortBy { emptySpace -> emptySpace.startIndex } }
        }
        dataFiles.removeAt(dataFileIterator)
        dataFiles.add(dataFileIterator, DataFile(dataFileToMove.id, emptySpace.startIndex, emptySpace.startIndex + sizeOfDataFile - 1))
        dataFileIterator--
    }

    return dataFiles.sumOf { datafile -> (datafile.startIndex .. datafile.endIndex).sumOf { it * datafile.id } }
}
 */