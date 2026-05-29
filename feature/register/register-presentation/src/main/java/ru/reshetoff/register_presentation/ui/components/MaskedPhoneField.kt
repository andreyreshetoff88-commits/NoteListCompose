package ru.reshetoff.register_presentation.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import ru.reshetoff.common.ui.AppColors
import ru.reshetoff.register_presentation.utils.PhoneMaskFormatter

@Composable
fun MaskedPhoneField(
    value: String,
    onValueChange: (String) -> Unit,
    mask: String?,
    countryCode: String?,  // ← добавить код страны
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }

    androidx.compose.runtime.LaunchedEffect(value, mask) {
        val formatted = if (!mask.isNullOrEmpty()) {
            PhoneMaskFormatter.format(mask, value)
        } else {
            value
        }
        textFieldValue = TextFieldValue(
            text = formatted,
            selection = androidx.compose.ui.text.TextRange(formatted.length)
        )
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            if (!mask.isNullOrEmpty() && countryCode != null) {
                // Извлекаем все цифры из вставленного текста
                var rawDigits = newValue.text.filter { it.isDigit() }

                // Убираем код страны из начала, если он есть
                if (rawDigits.startsWith(countryCode)) {
                    rawDigits = rawDigits.removePrefix(countryCode)
                }
                // Альтернатива: если номер начинается с +996 или 996
                else if (rawDigits.startsWith("996")) {
                    rawDigits = rawDigits.removePrefix("996")
                }

                onValueChange(rawDigits)
            } else {
                onValueChange(newValue.text)
            }
        },
        label = { Text(label) },
        placeholder = {
            Text(
                text = mask?.let { PhoneMaskFormatter.getHintForMask(it) } ?: "",
                color = Color.Gray
            )
        },
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        isError = isError,
        supportingText = supportingText,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColors.inputFocus,
            unfocusedBorderColor = AppColors.inputBorder,
            focusedLabelColor = AppColors.primary,
            cursorColor = AppColors.inputCursor
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        singleLine = true,
        enabled = enabled
    )
}