fun main() {
    val numberRegex = "\\d+".toRegex()

    // region Part 1

    fun extractScratchCardsPart1(input: List<String>, scratchCardsListPart1: MutableList<ScratchCardPart1>) {
        for (line in input) {
            val winningNumbersString = line.substringAfter(":").substringBefore("|")
            val gameNumbersString = line.substringAfter("|")

            val winningNumbers = numberRegex.findAll(winningNumbersString).map { it.value.toInt() }.toList()
            val gameNumbers = numberRegex.findAll(gameNumbersString).map { it.value.toInt() }.toList()

            scratchCardsListPart1.add(ScratchCardPart1(winningNumbers, gameNumbers))
        }
    }

    fun part1(input: List<String>): Int {
        val scratchCardsListPart1 = mutableListOf<ScratchCardPart1>()

        extractScratchCardsPart1(input, scratchCardsListPart1)

        return scratchCardsListPart1.sumOf { it.getPoints() }
    }

    // endregion Part 1

    // region Part 2

    fun extractScratchCardsPart2(input: List<String>, originalScratchCardsList: MutableList<ScratchCardPart2>) {
        for (line in input) {
            val cardNumberString = line.substringBefore(":")
            val winningNumbersString = line.substringAfter(":").substringBefore("|")
            val gameNumbersString = line.substringAfter("|")

            val cardNumber = numberRegex.find(cardNumberString)?.value?.toInt() ?: 0
            val winningNumbers = numberRegex.findAll(winningNumbersString).map { it.value.toInt() }.toList()
            val gameNumbers = numberRegex.findAll(gameNumbersString).map { it.value.toInt() }.toList()

            originalScratchCardsList.add(ScratchCardPart2(cardNumber, winningNumbers, gameNumbers))
        }
    }

    fun part2(input: List<String>): Int {
        val originalScratchCardsList = mutableListOf<ScratchCardPart2>()
        // <CardNumber, Instances>
        val instancesMap = mutableMapOf<Int, Int>()

        extractScratchCardsPart2(input, originalScratchCardsList)

        for (scratchCard in originalScratchCardsList) {
            // set original Cards instances
            instancesMap[scratchCard.cardNumber] = 1
        }

        for (scratchCard in originalScratchCardsList) {
            val matchingNumbers = scratchCard.getMatchingNumbersCount()
            val instances = instancesMap[scratchCard.cardNumber] ?: 0
            for (i in 1..instances) {
                for (j in 1..matchingNumbers) {
                    // increment instance counter
                    instancesMap.replace(
                        /* key = */ scratchCard.cardNumber + j,
                        /* value = */ instancesMap.getValue(scratchCard.cardNumber + j) + 1)
                }
            }
        }

        return instancesMap.values.sum()
    }

    // endregion Part 2

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

private data class ScratchCardPart1(
    private val winningNumbers: List<Int>,
    private val gameNumbers: List<Int>
) {
    fun getPoints(): Int {
        var points = 0
        gameNumbers.forEach { gameNumber ->
            if (winningNumbers.contains(gameNumber)) {
                points = addPoint(points)
            }
        }
        return points
    }

    private fun addPoint(points: Int): Int {
        return if (points == 0) {
            1
        } else {
            points * 2
        }
    }
}

private data class ScratchCardPart2(
    val cardNumber: Int,
    private val winningNumbers: List<Int>,
    private val gameNumbers: List<Int>
) {

    fun getMatchingNumbersCount(): Int {
        var matchingNumbersCount = 0
        gameNumbers.forEach { gameNumber ->
            if (winningNumbers.contains(gameNumber)) {
                matchingNumbersCount++
            }
        }
        return matchingNumbersCount
    }
}