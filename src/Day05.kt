import kotlin.time.measureTime

fun main() {

    val numberRegex = "\\d+".toRegex()
    val numberPairRegex = "\\d+ \\d+".toRegex()

    // region Part 1

    fun part1(input: List<String>): Long {

        // seeds are on the first line
        val seeds = numberRegex.findAll(input.first()).map { it.value.toLong() }.toList()

        // get map starting indexes
        val seedToSoilIndex = input.indexOf("seed-to-soil map:")
        val soilToFertilizerIndex = input.indexOf("soil-to-fertilizer map:")
        val fertilizerToWaterIndex = input.indexOf("fertilizer-to-water map:")
        val waterToLightIndex = input.indexOf("water-to-light map:")
        val lightToTemperatureIndex = input.indexOf("light-to-temperature map:")
        val temperatureToHumidityIndex = input.indexOf("temperature-to-humidity map:")
        val humidityToLocationIndex = input.indexOf("humidity-to-location map:")

        // create seed-to-soil map
        val seedToSoilMap = AlmanacMap()
        for (index in seedToSoilIndex until soilToFertilizerIndex) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                seedToSoilMap.ranges.add(almanacRange)
            }
        }

        // create soil-to-fertilizer map
        val soilToFertilizerMap = AlmanacMap()
        for (index in soilToFertilizerIndex until fertilizerToWaterIndex) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                soilToFertilizerMap.ranges.add(almanacRange)
            }
        }

        // create fertilizer-to-water map
        val fertilizerToWaterMap = AlmanacMap()
        for (index in fertilizerToWaterIndex until waterToLightIndex) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                fertilizerToWaterMap.ranges.add(almanacRange)
            }
        }

        // create water-to-light map
        val waterToLightMap = AlmanacMap()
        for (index in waterToLightIndex until lightToTemperatureIndex) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                waterToLightMap.ranges.add(almanacRange)
            }
        }

        // create light-to-temperature map
        val lightToTemperatureMap = AlmanacMap()
        for (index in lightToTemperatureIndex until temperatureToHumidityIndex) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                lightToTemperatureMap.ranges.add(almanacRange)
            }
        }

        // create temperature-to-humidity map
        val temperatureToHumidityMap = AlmanacMap()
        for (index in temperatureToHumidityIndex until humidityToLocationIndex) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                temperatureToHumidityMap.ranges.add(almanacRange)
            }
        }

        // create humidity-to-location map
        val humidityToLocationMap = AlmanacMap()
        for (index in humidityToLocationIndex until input.size) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                humidityToLocationMap.ranges.add(almanacRange)
            }
        }

        var lowestLocation: Long = Long.MAX_VALUE

        // go through all maps
        for (seed in seeds) {
            val soil = seedToSoilMap.getMapping(seed)
            val fertilizer = soilToFertilizerMap.getMapping(soil)
            val water = fertilizerToWaterMap.getMapping(fertilizer)
            val light = waterToLightMap.getMapping(water)
            val temperature = lightToTemperatureMap.getMapping(light)
            val humidity = temperatureToHumidityMap.getMapping(temperature)
            val location = humidityToLocationMap.getMapping(humidity)

            if (location < lowestLocation) lowestLocation = location
        }

        return lowestLocation
    }

    // endregion Part 1

    // region Part 2

    fun part2(input: List<String>): Long {
        // seeds are pairs of <rangeStart, rangeLength>
        val seedsRangeList: List<LongRange>

        val seedPairsList = numberPairRegex.findAll(input.first()).map { numberPairMatchResult ->
            numberRegex.findAll(numberPairMatchResult.value).map { numberMatchResult ->
                numberMatchResult.value.toLong()
            }.toList()
        }.toList()

        seedsRangeList = seedPairsList.map { LongRange(it[0], it[0] + it[1] - 1) }

        // get map starting indexes
        val seedToSoilIndex = input.indexOf("seed-to-soil map:")
        val soilToFertilizerIndex = input.indexOf("soil-to-fertilizer map:")
        val fertilizerToWaterIndex = input.indexOf("fertilizer-to-water map:")
        val waterToLightIndex = input.indexOf("water-to-light map:")
        val lightToTemperatureIndex = input.indexOf("light-to-temperature map:")
        val temperatureToHumidityIndex = input.indexOf("temperature-to-humidity map:")
        val humidityToLocationIndex = input.indexOf("humidity-to-location map:")

        // create seed-to-soil map
        val seedToSoilMap = AlmanacMap()
        for (index in seedToSoilIndex until soilToFertilizerIndex) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                seedToSoilMap.ranges.add(almanacRange)
            }
        }

        // create soil-to-fertilizer map
        val soilToFertilizerMap = AlmanacMap()
        for (index in soilToFertilizerIndex until fertilizerToWaterIndex) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                soilToFertilizerMap.ranges.add(almanacRange)
            }
        }

        // create fertilizer-to-water map
        val fertilizerToWaterMap = AlmanacMap()
        for (index in fertilizerToWaterIndex until waterToLightIndex) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                fertilizerToWaterMap.ranges.add(almanacRange)
            }
        }

        // create water-to-light map
        val waterToLightMap = AlmanacMap()
        for (index in waterToLightIndex until lightToTemperatureIndex) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                waterToLightMap.ranges.add(almanacRange)
            }
        }

        // create light-to-temperature map
        val lightToTemperatureMap = AlmanacMap()
        for (index in lightToTemperatureIndex until temperatureToHumidityIndex) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                lightToTemperatureMap.ranges.add(almanacRange)
            }
        }

        // create temperature-to-humidity map
        val temperatureToHumidityMap = AlmanacMap()
        for (index in temperatureToHumidityIndex until humidityToLocationIndex) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                temperatureToHumidityMap.ranges.add(almanacRange)
            }
        }

        // create humidity-to-location map
        val humidityToLocationMap = AlmanacMap()
        for (index in humidityToLocationIndex until input.size) {
            val result = numberRegex.findAll(input[index]).map { it.value.toLong() }.toList()
            if (result.isNotEmpty() && result.size == 3) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = result
                val almanacRange = AlmanacRange(sourceRangeStart, destinationRangeStart, rangeLength)
                humidityToLocationMap.ranges.add(almanacRange)
            }
        }

        var lowestLocation: Long = Long.MAX_VALUE

        var counter = 0
        // go through all maps
        for (seedRange in seedsRangeList) {
            println("seedRange: ${++counter}, numberOfSeeds: ${seedRange.last - seedRange.first}")
            val duration = measureTime {
                for (seed in seedRange) {
                    val soil = seedToSoilMap.getMapping(seed)
                    val fertilizer = soilToFertilizerMap.getMapping(soil)
                    val water = fertilizerToWaterMap.getMapping(fertilizer)
                    val light = waterToLightMap.getMapping(water)
                    val temperature = lightToTemperatureMap.getMapping(light)
                    val humidity = temperatureToHumidityMap.getMapping(temperature)
                    val location = humidityToLocationMap.getMapping(humidity)

                    if (location < lowestLocation) lowestLocation = location
                }
            }
            println("seedRange: $counter took ${duration.inWholeSeconds} seconds")
        }

        return lowestLocation
    }

    // endregion Part 2

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

private data class AlmanacMap(
    val ranges: MutableList<AlmanacRange> = mutableListOf()
) {
    fun getMapping(key: Long): Long {
        for (range in ranges) {
            val rangeEnd = range.sourceRangeStart + range.rangeLength - 1
            if (range.sourceRangeStart <= key && key <= rangeEnd) {
                val distanceFromStart = key - range.sourceRangeStart
                return range.destinationRangeStart + distanceFromStart
            }
        }
        return key
    }
}

private data class AlmanacRange(
    val sourceRangeStart: Long,
    val destinationRangeStart: Long,
    val rangeLength: Long
)

