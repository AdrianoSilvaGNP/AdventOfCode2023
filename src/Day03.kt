fun main() {
    val numberRegex = "\\d+".toRegex()
    val symbolRegex = "[^(.|\\d\\n)]".toRegex()

    fun extractSchematicLine(currentLine: String): SchematicLine {
        // extract numbers and their positions
        val schematicNumbers = mutableListOf<SchematicNumber>()
        val numberResults = numberRegex.findAll(currentLine)
        for (numberResult in numberResults) {
            schematicNumbers.add(
                SchematicNumber(
                    numberResult.range,
                    numberResult.value.toInt()
                )
            )
        }

        //extract symbol positions
        val symbolPositions = mutableListOf<Int>()
        val symbolResults = symbolRegex.findAll(currentLine)
        for (symbolResult in symbolResults) {
            symbolPositions.add(symbolResult.range.first)
        }

        return SchematicLine(schematicNumbers, symbolPositions)
    }

    /**
     * validates if the number indexes of [SchematicNumber]
     * are adjacent to the indexes of [SchematicLine.symbolPositions]
     */
    fun horizontalLineNumberValidation(schematicLine: SchematicLine, schematicNumber: SchematicNumber): Boolean {
        var isValid = false
        for (symbolPosition in schematicLine.symbolPositions) {
            if (schematicNumber.range.any { it == symbolPosition + 1 || it == symbolPosition - 1 }) {
                isValid = true
                break
            }
        }
        return isValid
    }

    /**
     * validates if the number indexes of [SchematicNumber]
     * are contained or adjacent to the indexes of [SchematicLine.symbolPositions]
     */
    fun verticalLineNumberValidation(schematicLine: SchematicLine, schematicNumber: SchematicNumber): Boolean {
        var isValid = false
        for (symbolPosition in schematicLine.symbolPositions) {
            if (schematicNumber.range.any { it == symbolPosition + 1 || it == symbolPosition - 1 }
                || schematicNumber.range.contains(symbolPosition)) {
                isValid = true
                break
            }
        }
        return isValid
    }

    fun part1(input: List<String>): Int {
        var partNumberSum = 0

        input.forEachIndexed { index, line ->
            // Look at previous, current and next line
            var previousSchematicLine: SchematicLine? = null
            var nextSchematicLine: SchematicLine? = null

            // extract all lines
            val currentSchematicLine = extractSchematicLine(line)
            if (index > 0) {
                previousSchematicLine = extractSchematicLine(input[index - 1])
            }
            if (index < input.indices.last) {
                nextSchematicLine = extractSchematicLine(input[index + 1])
            }

            for (schematicNumber in currentSchematicLine.schematicNumbers) {
                var isValid = false

                // validate through all lines
                if (previousSchematicLine != null) {
                    isValid = verticalLineNumberValidation(previousSchematicLine, schematicNumber)
                }

                if (!isValid && currentSchematicLine.symbolPositions.isNotEmpty()) {
                    isValid = horizontalLineNumberValidation(currentSchematicLine, schematicNumber)
                }

                if (!isValid && nextSchematicLine != null) {
                    isValid = verticalLineNumberValidation(nextSchematicLine, schematicNumber)
                }

                // if number part valid, add value
                if (isValid) {
                    partNumberSum += schematicNumber.value
                }
            }
        }

        return partNumberSum
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    //check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    //part2(input).println()
}

class SchematicLine(
    val schematicNumbers: List<SchematicNumber>,
    val symbolPositions: List<Int>
)

class SchematicNumber(
    val range: IntRange,
    val value: Int,
)