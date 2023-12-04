fun main() {
    fun part1(input: List<String>): Int {
        // get the fist and last number from string
        var sum = 0

        for (line in input) {
            val firstDigit = line.find { it.isDigit() }
            val lastDigit = line.findLast { it.isDigit() }
            val number = "$firstDigit$lastDigit".toInt()
            sum += number
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        // numbers spelled out in words also count
        val numbersWrittenToNumerical = mapOf(
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9"
        )
        var sum = 0

        for (line in input) {
            // overlapping counts LOL! "nineight" -> "98"
            val transformedLine = StringBuilder()
            var i = 0

            while (i < line.length) {
                var matched = false

                for ((word, number) in numbersWrittenToNumerical) {
                    if (line.regionMatches(i, word, 0, word.length)) {
                        // if the word is matched, append the number
                        transformedLine.append(number)
                        matched = true
                    }
                }

                if (!matched) {
                    transformedLine.append(line[i])
                }
                i++
            }

            // Extract numbers from the transformed string
            val first = transformedLine.first { it.isDigit() }
            val last = transformedLine.last { it.isDigit() }
            val number = "$first$last".toInt()

            sum += number
        }


        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 238)
    check(part2(testInput) == 190)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}