package ru.reshetoff.register_presentation.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.reshetoff.common.Constants.EMPTY_FIELD_ERROR
import ru.reshetoff.common.ui.AppColors
import ru.reshetoff.common.ui.Padding
import ru.reshetoff.common.ui.TextSize
import ru.reshetoff.register_domain.model.Country
import ru.reshetoff.register_presentation.ui.components.CountryPicker

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val countiesState by viewModel.countiesState.collectAsStateWithLifecycle()
    var displayName by remember { mutableStateOf("") }
    var displayNameError by remember { mutableStateOf<String?>(null) }
    var phoneNumber by remember { mutableStateOf("") }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }
    var selectedCountry by remember { mutableStateOf<Country?>(null) }

    LaunchedEffect(displayName) {
        if (displayNameError != null)
            displayNameError = null
    }

    LaunchedEffect(phoneNumber) {
        if (phoneNumberError != null)
            phoneNumberError = null
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text(text = "Имя") },
                placeholder = { Text(text = "Иван Иванов") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                isError = displayNameError != null,
                supportingText = {
                    if (displayNameError != null) {
                        Text(
                            text = displayNameError!!,
                            modifier = Modifier.fillMaxWidth(),
                            color = AppColors.textError,
                            fontSize = TextSize.small
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.inputFocus,
                    unfocusedBorderColor = AppColors.inputBorder,
                    focusedLabelColor = AppColors.primary,
                    cursorColor = AppColors.inputCursor
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Padding.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CountryPicker(
                    countriesState = countiesState,
                    onCountrySelected = { country ->
                        selectedCountry = country
                    },
                    modifier = Modifier.width(120.dp)
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text(text = "Номер телефона") },
                    placeholder = { selectedCountry?.let { Text(text = it.phoneMask) } },
                    modifier = Modifier.fillMaxWidth().offset(y = 4.dp),
                    shape = MaterialTheme.shapes.medium,
                    isError = phoneNumberError != null,
                    supportingText = {
                        if (phoneNumberError != null) {
                            Text(
                                text = phoneNumberError!!,
                                modifier = Modifier.fillMaxWidth(),
                                color = AppColors.textError,
                                fontSize = TextSize.small
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.inputFocus,
                        unfocusedBorderColor = AppColors.inputBorder,
                        focusedLabelColor = AppColors.primary,
                        cursorColor = AppColors.inputCursor
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
            }
        }

        Button(
            onClick = {
                if (displayName.isBlank()) {
                    displayNameError = EMPTY_FIELD_ERROR
                    return@Button
                }

                if (phoneNumber.isBlank()) {
                    phoneNumberError = EMPTY_FIELD_ERROR
                    return@Button
                }

                Log.d("ololo", "RegisterScreen: on register click")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Padding.large)
                .padding(bottom = Padding.extraLarge)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.primary
            )
        ) {
            Text(
                text = "Регистрация",
                color = Color.White,
                fontSize = TextSize.medium
            )
        }
    }
}