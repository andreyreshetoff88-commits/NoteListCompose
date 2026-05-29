package ru.reshetoff.common.ui

import androidx.compose.ui.graphics.Color

object AppColors {
    // Основные
    val primary = Color(0xFFFFA726)        // оранжевый (акцент)
    val background = Color(0xFFFFF4E0)     // песочный фон
    val surface = Color(0xFFE8DCC8)        // фон меню (темнее)

    // Текст
    val textPrimary = Color(0xFF333333)    // основной текст
    val textSecondary = Color.Gray         // второстепенный текст
    val textError = Color.Red              // текст ошибки

    // Поля ввода
    val inputBorder = Color(0xFFE0E0E0)    // рамка поля ввода (не в фокусе)
    val inputFocus = Color(0xFFFFA726)     // рамка поля ввода (в фокусе)
    val inputCursor = Color(0xFFFFA726)    // курсор в поле ввода

    // Кнопки
    val buttonBackground = Color(0xFFFFA726)
    val buttonText = Color.White

    // Иконки
    val iconActive = Color(0xFFFFA726)
    val iconInactive = Color.Gray

    // Фон меню
    val bottomNavBackground = Color(0xFFE8DCC8)
}