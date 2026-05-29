package ru.reshetoff.register_presentation.utils

object PhoneMaskFormatter {
    private const val PLACEHOLDER = 'X'

    fun format(mask: String, input: String): String {
        val cleanInput = input.filter { it.isDigit() }
        val result = StringBuilder()
        var inputIndex = 0

        for (maskChar in mask) {
            if (inputIndex >= cleanInput.length) break

            when (maskChar) {
                PLACEHOLDER -> {
                    result.append(cleanInput[inputIndex])
                    inputIndex++
                }
                else -> result.append(maskChar)
            }
        }

        return result.toString()
    }

    fun getRawNumber(formatted: String): String {
        return formatted.filter { it.isDigit() }
    }

    fun getHintForMask(mask: String): String {
        return mask.replace(PLACEHOLDER, 'X')
    }
}