import kotlin.math.pow

fun main() {

    // region Part 1

    val cardStrengthMapPart1 = mapOf(
        '2' to 1,
        '3' to 2,
        '4' to 3,
        '5' to 4,
        '6' to 5,
        '7' to 6,
        '8' to 7,
        '9' to 8,
        'T' to 9,
        'J' to 10,
        'Q' to 11,
        'K' to 12,
        'A' to 13
    )

    val cardStrengthMapPart2 = mapOf(
        'J' to 1,
        '2' to 2,
        '3' to 3,
        '4' to 4,
        '5' to 5,
        '6' to 6,
        '7' to 7,
        '8' to 8,
        '9' to 9,
        'T' to 10,
        'Q' to 11,
        'K' to 12,
        'A' to 13
    )

    fun determineHandTypePart1(cardCounts: Map<Char, Int>): HandType {
        return when {
            cardCounts.values.any { it == 5 } -> HandType.FIVE_OF_A_KIND
            cardCounts.values.any { it == 4 } -> HandType.FOUR_OF_A_KIND
            cardCounts.values.count { it == 3 } == 1
                    && cardCounts.values.count { it == 2 } == 1 -> HandType.FULL_HOUSE

            cardCounts.values.any { it == 3 } -> HandType.THREE_OF_A_KIND
            cardCounts.values.count { it == 2 } == 2 -> HandType.TWO_PAIR
            cardCounts.values.any { it == 2 } -> HandType.ONE_PAIR
            else -> HandType.HIGH_CARD
        }
    }

    fun determineHandTypePart2(cardCounts: Map<Char, Int>): HandType {
        if (!cardCounts.containsKey('J')) return determineHandTypePart1(cardCounts)

        val numberOfJokerCards = cardCounts['J']
        val cardCountsWithoutJoker = cardCounts.toMutableMap()
        cardCountsWithoutJoker.remove('J')

        val hand :HandType = if (cardCountsWithoutJoker.isEmpty()) {
            HandType.FIVE_OF_A_KIND
        } else {
            when {
                cardCountsWithoutJoker.values.any { it == 5 } -> HandType.FIVE_OF_A_KIND
                cardCountsWithoutJoker.values.any { it == 4 } -> HandType.FOUR_OF_A_KIND
                cardCountsWithoutJoker.values.count { it == 3 } == 1
                        && cardCountsWithoutJoker.values.count { it == 2 } == 1 -> HandType.FULL_HOUSE

                cardCountsWithoutJoker.values.any { it == 3 } -> HandType.THREE_OF_A_KIND
                cardCountsWithoutJoker.values.count { it == 2 } == 2 -> HandType.TWO_PAIR
                cardCountsWithoutJoker.values.any { it == 2 } -> HandType.ONE_PAIR
                else -> HandType.HIGH_CARD
            }
        }

        val newHandType: HandType = when (hand) {
            HandType.HIGH_CARD -> {
                when (numberOfJokerCards) {
                    1 -> {
                        HandType.ONE_PAIR
                    }

                    2 -> {
                        HandType.THREE_OF_A_KIND
                    }

                    3 -> {
                        HandType.FOUR_OF_A_KIND
                    }

                    4 -> {
                        HandType.FIVE_OF_A_KIND
                    }

                    else -> {
                        HandType.HIGH_CARD
                    }
                }
            }

            HandType.ONE_PAIR -> {
                when (numberOfJokerCards) {
                    1 -> {
                        HandType.THREE_OF_A_KIND
                    }

                    2 -> {
                        HandType.FOUR_OF_A_KIND
                    }

                    3 -> {
                        HandType.FIVE_OF_A_KIND
                    }

                    else -> {
                        HandType.ONE_PAIR
                    }
                }
            }

            HandType.TWO_PAIR -> {
                when (numberOfJokerCards) {
                    1 -> {
                        HandType.FULL_HOUSE
                    }

                    else -> {
                        HandType.TWO_PAIR
                    }
                }
            }

            HandType.THREE_OF_A_KIND -> {
                when (numberOfJokerCards) {
                    1 -> {
                        HandType.FOUR_OF_A_KIND
                    }

                    2 -> {
                        HandType.FIVE_OF_A_KIND
                    }

                    else -> {
                        HandType.THREE_OF_A_KIND
                    }
                }
            }

            HandType.FULL_HOUSE -> HandType.FULL_HOUSE
            HandType.FOUR_OF_A_KIND -> {
                when (numberOfJokerCards) {
                    1 -> {
                        HandType.FIVE_OF_A_KIND
                    }

                    else -> {
                        HandType.FOUR_OF_A_KIND
                    }
                }
            }

            HandType.FIVE_OF_A_KIND -> HandType.FIVE_OF_A_KIND


            //newHandOrdinal = if (hand.ordinal + numberOfJokerCards!! == 4) 5 else hand.ordinal + numberOfJokerCards
            //newHandType = HandType.entries[newHandOrdinal]
        }

        return newHandType
    }

    fun calculateFitnessScorePart1(cards: String, handType: HandType): Int {
        var fitnessScore = handType.baseScore

        cards.forEachIndexed { index, card ->
            fitnessScore += (13f.pow(4 - index) * cardStrengthMapPart1[card]!!).toInt()
        }

        return fitnessScore
    }

    fun calculateFitnessScorePart2(cards: String, handType: HandType): Int {
        var fitnessScore = handType.baseScore

        cards.forEachIndexed { index, card ->
            fitnessScore += (13f.pow(4 - index) * cardStrengthMapPart2[card]!!).toInt()
        }

        return fitnessScore
    }

    fun part1(input: List<String>): Int {
        val gameHandsList = input.map { line ->
            val (cards, bid) = line.split(" ", limit = 2)
            val cardCounts = cards.groupingBy { it }.eachCount()

            val handType = determineHandTypePart1(cardCounts)
            val fitnessScore = calculateFitnessScorePart1(cards, handType)

            println("$cards is ${handType.description}")
            GameHand(cards, bid.toInt(), fitnessScore, handType)
        }.sortedBy { it.fitnessScore }

        val totalWinnings = gameHandsList.withIndex().sumOf { (index, gameHand) ->
            gameHand.getWinnings(index + 1)
        }

        return totalWinnings
    }

    // endregion Part 1

    // region Part 2

    fun part2(input: List<String>): Int {
        val gameHandsList = input.map { line ->
            val (cards, bid) = line.split(" ", limit = 2)
            val cardCounts = cards.groupingBy { it }.eachCount()

            val handType = determineHandTypePart2(cardCounts)
            val fitnessScore = calculateFitnessScorePart2(cards, handType)

            println("$cards is ${handType.description}")
            GameHand(cards, bid.toInt(), fitnessScore, handType)
        }.sortedBy { it.fitnessScore }

        val totalWinnings = gameHandsList.withIndex().sumOf { (index, gameHand) ->
            gameHand.getWinnings(index + 1)
        }

        return totalWinnings
    }

    // endregion Part 2

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

private enum class HandType(val description: String, val baseScore: Int) {
    HIGH_CARD("High Card", 1_000_000),
    ONE_PAIR("One Pair", 2_000_000),
    TWO_PAIR("Two Pair", 3_000_000),
    THREE_OF_A_KIND("Three of a Kind", 4_000_000),
    FULL_HOUSE("Full House", 5_000_000),
    FOUR_OF_A_KIND("Four of a Kind", 6_000_000),
    FIVE_OF_A_KIND("Five of a Kind", 7_000_000);
}

private data class GameHand(
    private val cards: String,
    private val bid: Int,
    val fitnessScore: Int,
    private val handType: HandType
) {

    fun getWinnings(rankValue: Int): Int {
        return bid * rankValue
    }
}