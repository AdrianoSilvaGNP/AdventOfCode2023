fun main() {
    val numberRegex = "\\d+".toRegex()
    val symbolRegex = "[^(.|\\d\\n)]".toRegex()

    // region Part 1

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

    // endregion Part 1

    // region Part 2

    // Key: (line, index), Value: Gear
    val part2GearsMap = mutableMapOf<Pair<Int, Int>, Gear>()

    fun extractSchematicLinePart2(currentLine: String, index: Int): SchematicLinePart2 {
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

        //extract symbols
        val schematicSymbols = mutableListOf<SchematicSymbol>()
        val symbolResults = symbolRegex.findAll(currentLine)
        for (symbolResult in symbolResults) {
            schematicSymbols.add(SchematicSymbol(symbolResult.range.first, symbolResult.value == "*"))
        }

        return SchematicLinePart2(index, schematicNumbers, schematicSymbols)
    }

    /**
     * validates if the number indexes of [SchematicNumber]
     * are adjacent to the indexes of [SchematicLinePart2.symbols]
     */
    fun horizontalLineNumberValidationPart2(schematicLinePart2: SchematicLinePart2, schematicNumber: SchematicNumber): Boolean {
        var isValid = false
        for (symbol in schematicLinePart2.symbols) {
            if (schematicNumber.range.any { it == symbol.position + 1 || it == symbol.position - 1 }) {
                if (symbol.isGearSymbol) {
                    //check if Gear already exists in Map
                    val gear = part2GearsMap[Pair(schematicLinePart2.lineNumber, symbol.position)]
                    if (gear != null) {
                        // add number to gear
                        gear.addAdjacentNumber(schematicNumber.value)
                    } else {
                        // put new Gear
                        part2GearsMap[Pair(schematicLinePart2.lineNumber, symbol.position)] =
                            Gear(mutableListOf(schematicNumber.value))
                    }
                } else {
                    isValid = true
                    break
                }
            }
        }
        return isValid
    }

    /**
     * validates if the number indexes of [SchematicNumber]
     * are contained or adjacent to the indexes of [SchematicLinePart2.symbols]
     */
    fun verticalLineNumberValidationPart2(schematicLinePart2: SchematicLinePart2, schematicNumber: SchematicNumber): Boolean {
        var isValid = false
        for (symbol in schematicLinePart2.symbols) {
            if (schematicNumber.range.any { it == symbol.position + 1 || it == symbol.position - 1 }
                || schematicNumber.range.contains(symbol.position)) {
                if (symbol.isGearSymbol) {

                    //check if Gear already exists in Map
                    val gear = part2GearsMap[Pair(schematicLinePart2.lineNumber, symbol.position)]
                    if (gear != null) {
                        // add number to gear
                        gear.addAdjacentNumber(schematicNumber.value)
                    } else {
                        // put new Gear
                        part2GearsMap[Pair(schematicLinePart2.lineNumber, symbol.position)] = Gear(mutableListOf(schematicNumber.value))
                    }

                } else {
                    isValid = true
                    break
                }
            }
        }
        return isValid
    }

    fun part2(input: List<String>): Int {
        part2GearsMap.clear()
        var partNumberSum = 0

        input.forEachIndexed { index, line ->
            // Look at previous, current and next line
            var previousSchematicLine: SchematicLinePart2? = null
            var nextSchematicLine: SchematicLinePart2? = null

            // extract all lines
            val currentSchematicLine = extractSchematicLinePart2(line, index)
            if (index > 0) {
                previousSchematicLine = extractSchematicLinePart2(input[index - 1], index - 1)
            }
            if (index < input.indices.last) {
                nextSchematicLine = extractSchematicLinePart2(input[index + 1], index + 1)
            }

            for (schematicNumber in currentSchematicLine.schematicNumbers) {
                var isValid = false

                // validate through all lines
                if (previousSchematicLine != null) {
                    isValid = verticalLineNumberValidationPart2(previousSchematicLine!!, schematicNumber)
                }

                if (!isValid && currentSchematicLine.symbols.isNotEmpty()) {
                    isValid = horizontalLineNumberValidationPart2(currentSchematicLine, schematicNumber)
                }

                if (!isValid && nextSchematicLine != null) {
                    isValid = verticalLineNumberValidationPart2(nextSchematicLine!!, schematicNumber)
                }

                // if number part valid, add value
                if (isValid) {
                    // I just needed the Gear ratios! LOL Solved and even more difficult problem...
                    //partNumberSum += schematicNumber.value
                }
            }


        }

        for(gear in part2GearsMap.values) {
            partNumberSum += gear.getCalculatedValue()
        }

        //partNumberSum.println()
        return partNumberSum
    }

    // endregion Part 2

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

class SchematicLine(
    val schematicNumbers: List<SchematicNumber>,
    val symbolPositions: List<Int>
)

class SchematicNumber(
    val range: IntRange,
    val value: Int,
)

class SchematicLinePart2(
    val lineNumber: Int,
    val schematicNumbers: List<SchematicNumber>,
    val symbols: List<SchematicSymbol>
)

class SchematicSymbol(
    val position: Int,
    val isGearSymbol: Boolean = false
)

class Gear(
    private val adjacentNumbers: MutableList<Int>
) {

    fun addAdjacentNumber(number: Int) {
        adjacentNumbers.add(number)
    }

    fun getCalculatedValue() : Int {
        return if (adjacentNumbers.size == 2) {
            adjacentNumbers[0] * adjacentNumbers[1]
        } else {
            //adjacentNumbers.sum()
            0
        }
    }
}