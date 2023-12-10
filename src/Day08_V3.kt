package adventofcode2023

import java.io.File

object Day08 {
    private fun getSteps(
        instructions: String,
        nodes: Map<String, Pair<String, String>>,
        location: String,
        endsWith: String
    ): Long {
        var current = location
        var steps = 0

        while (!current.endsWith(endsWith)) {
            current = if (instructions[steps % instructions.length] == 'L') nodes[current]!!.first else nodes[current]!!.second
            steps++
        }

        return steps.toLong()
    }

    private fun lcm(a: Long, b: Long): Long {
        var ma = a
        var mb = b
        var remainder: Long

        while (mb != 0L) {
            remainder = ma % mb
            ma = mb
            mb = remainder
        }

        return a * b / ma
    }

    fun part1(instructions: String, nodes: Map<String, Pair<String, String>>) {
        println(getSteps(instructions, nodes, "AAA", "ZZZ"))
    }

    fun part2(instructions: String, nodes: Map<String, Pair<String, String>>) {
        println(
            nodes.filter { it.key.endsWith('A') }.map {
                getSteps(instructions, nodes, it.key, "Z")
            }.reduce { acc, i -> lcm(acc, i) }
        )
    }
}

fun main() {
    val inputs = File("./src/Day08.txt").readLines()
    val instructions = inputs[0]
    val nodes = inputs.drop(2).associate {
        val node = it.replace(Regex("[^A-Z ]"), "").replace(Regex("\\s+"), " ").split(" ")
        node[0] to Pair(node[1], node[2])
    }

    Day08.part1(instructions, nodes)
    Day08.part2(instructions, nodes)
}