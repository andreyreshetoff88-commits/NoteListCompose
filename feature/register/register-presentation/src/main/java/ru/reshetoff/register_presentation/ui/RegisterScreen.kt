package ru.reshetoff.register_presentation.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.reshetoff.common.Constants.EMPTY_FIELD_ERROR
import ru.reshetoff.common.R
import ru.reshetoff.common.State
import ru.reshetoff.common.ui.AppColors
import ru.reshetoff.common.ui.Padding
import ru.reshetoff.common.ui.TextSize
import ru.reshetoff.register_domain.model.Country
import ru.reshetoff.register_domain.model.RegisterRequest
import ru.reshetoff.register_presentation.ui.components.CountryPicker
import ru.reshetoff.register_presentation.ui.components.MaskedPhoneField

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val countiesState by viewModel.countiesState.collectAsStateWithLifecycle()
    val registerState by viewModel.registerState.collectAsStateWithLifecycle()
    var displayName by remember { mutableStateOf("") }
    var displayNameError by remember { mutableStateOf<String?>(null) }
    var phoneNumber by remember { mutableStateOf("") }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }
    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(displayName) {
        if (displayNameError != null)
            displayNameError = null
    }

    LaunchedEffect(phoneNumber) {
        if (phoneNumberError != null)
            phoneNumberError = null
    }

    LaunchedEffect(email) {
        if (emailError != null)
            emailError = null
    }

    LaunchedEffect(password) {
        if (passwordError != null)
            passwordError = null
    }

    LaunchedEffect(confirmPassword) {
        if (confirmPasswordError != null)
            confirmPasswordError = null
    }

    LaunchedEffect(registerState) {
        when (registerState) {
            is State.Success -> onRegisterSuccess()
            is State.Error -> Toast.makeText(
                context,
                (registerState as State.Error).message,
                Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words
                ),
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

                MaskedPhoneField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    mask = selectedCountry?.phoneMask,
                    countryCode = selectedCountry?.phoneCode,
                    label = "Номер телефона",
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 4.dp),
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
                    enabled = selectedCountry != null
                )
            }

            Spacer(modifier = Modifier.height(Padding.small))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                placeholder = { Text(text = "example@mail.com") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) {
                        Text(
                            text = emailError!!,
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(Padding.small))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Пароль") },
                placeholder = { Text(text = "Введите пароль") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                isError = passwordError != null,
                supportingText = {
                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
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
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val icon = if (passwordVisible)
                        painterResource(id = R.drawable.ic_visibility_off)
                    else
                        painterResource(id = R.drawable.ic_visibility)
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = icon, contentDescription = null)
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(Padding.small))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(text = "Подтвердите пароль") },
                placeholder = { Text(text = "Введите пароль еще раз") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                isError = confirmPasswordError != null,
                supportingText = {
                    if (confirmPasswordError != null) {
                        Text(
                            text = confirmPasswordError!!,
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
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val icon = if (confirmPasswordVisible)
                        painterResource(id = R.drawable.ic_visibility_off)
                    else
                        painterResource(id = R.drawable.ic_visibility)
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(painter = icon, contentDescription = null)
                    }
                },
                singleLine = true
            )
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

                if (email.isBlank()) {
                    emailError = EMPTY_FIELD_ERROR
                    return@Button
                }

                if (!isValidEmail(email.trim())) {
                    emailError = "Введите корректный email (пример: name@domain.com)"
                    return@Button
                }

                if (password.isBlank()) {
                    passwordError = EMPTY_FIELD_ERROR
                    return@Button
                }

                if (!isValidPassword(password)) {
                    passwordError = "Пароль должен содержать: 8+ символов, цифру, заглавную и " +
                            "строчную букву, спецсимвол (@#\\$%^&+=!_)"
                    return@Button
                }

                if (confirmPassword.isBlank()) {
                    confirmPasswordError = EMPTY_FIELD_ERROR
                    return@Button
                }

                if (confirmPassword != password) {
                    confirmPasswordError = "Пароли не совпадают"
                    return@Button
                }

                viewModel.register(
                    RegisterRequest(
                        displayName = displayName,
                        phoneNumber = "+${selectedCountry?.phoneCode}$phoneNumber",
                        email = email,
                        password = password
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Padding.large)
                .padding(bottom = Padding.extraLarge)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.primary
            ),
            enabled = registerState !is State.Loading
        ) {
            if (registerState is State.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = "Регистрация",
                    color = Color.White,
                    fontSize = TextSize.medium
                )
            }
        }
    }
}

private fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return email.matches(emailRegex)
}

private fun isValidPassword(password: String): Boolean {
    val passwordRegex = Regex(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!_])[^\\s]{8,}$"
    )
    return passwordRegex.matches(password)
}