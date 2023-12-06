
fun main() {

    val numberRegex = "\\d+".toRegex()

    // region Part 1

    fun part1(input: List<String>): Int {
        val races = mutableListOf<Race>()

        val times = numberRegex.findAll(input[0]).map { it.value.toInt() }.toList()
        val distances = numberRegex.findAll(input[1]).map { it.value.toInt() }.toList()

        times.forEachIndexed { index, time ->
            races.add(Race(time, distances[index]))
        }

        for (race in races) {
            for (holdTime in 0..race.time) {
                val boatDistance = holdTime * (race.time - holdTime)
                if (boatDistance > race.recordDistance) race.solutions++
            }
        }

        // use fold to multiply all values
        // accumulator starts at 1 and for each race acc = acc * race.solutions
        val result = races.fold(1) { acc: Int, race: Race ->
            acc * race.solutions
        }
        return result
    }

    // endregion Part 1

    // region Part 2

    fun part2(input: List<String>): Int {
        val time = numberRegex.findAll(input[0]).map { it.value.toInt() }.fold("") { acc, i ->
            acc + i
        }.toLong()

        val distance = numberRegex.findAll(input[1]).map { it.value.toInt() }.fold("") { acc, i ->
            acc + i
        }.toLong()

        val race = RacePart2(time, distance)

        for (holdTime in 0..race.time) {
            val boatDistance = holdTime * (race.time - holdTime)
            if (boatDistance > race.recordDistance) race.solutions++
        }

        return race.solutions
    }

    // endregion Part 2

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}


private data class Race(
    val time: Int,
    val recordDistance: Int,
    var solutions: Int = 0
)

private data class RacePart2(
    val time: Long,
    val recordDistance: Long,
    var solutions: Int = 0
)