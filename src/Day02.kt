fun main() {
    val redRegex = "\\d+ red".toRegex()
    val greenRegex = "\\d+ green".toRegex()
    val blueRegex = "\\d+ blue".toRegex()

    fun extractValues(subsetString: String): Subset {
        val redValue = redRegex.find(subsetString)?.value?.filter { it.isDigit() }?.toInt() ?: 0
        val greenValue = greenRegex.find(subsetString)?.value?.filter { it.isDigit() }?.toInt() ?: 0
        val blueValue = blueRegex.find(subsetString)?.value?.filter { it.isDigit() }?.toInt() ?: 0
        return Subset(redValue, greenValue, blueValue)
    }

    fun part1(input: List<String>): Int {
        var idSum = 0
        val targetBag = Subset(12, 13, 14)

        for (line in input) {
            // separate semicolon substring
            val gameId = line.substringBefore(":").filter { it.isDigit() }.toInt()
            var subsetValidCounter = 0

            val subsetStringList = line.substringAfter(":").split(";")
            for (subsetString in subsetStringList) {
                val subset = extractValues(subsetString)

                if (subset.red <= targetBag.red
                    && subset.green <= targetBag.green
                    && subset.blue <= targetBag.blue
                ) {
                    subsetValidCounter++
                }
            }

            if (subsetValidCounter == subsetStringList.size) {
                idSum += gameId
            }

        }
        return idSum
    }

    fun part2(input: List<String>): Int {
        var powerSum = 0

        for (line in input) {
            var minNecessaryRed = 0
            var minNecessaryGreen = 0
            var minNecessaryBlue = 0

            val subsetStringList = line.substringAfter(":").split(";")
            for (subsetString in subsetStringList) {
                // extract red, green and blue

                val subset = extractValues(subsetString)

                if (minNecessaryRed < subset.red) minNecessaryRed = subset.red
                if (minNecessaryGreen < subset.green) minNecessaryGreen = subset.green
                if (minNecessaryBlue < subset.blue) minNecessaryBlue = subset.blue
            }

            val gamePowerValue = minNecessaryRed * minNecessaryGreen * minNecessaryBlue
            powerSum += gamePowerValue
        }
        return powerSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private class Subset(
    val red: Int,
    val green: Int,
    val blue: Int
)