package ru.reshetoff.register_presentation.ui.components

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import ru.reshetoff.common.State
import ru.reshetoff.common.ui.AppColors
import ru.reshetoff.common.ui.Padding
import ru.reshetoff.register_domain.model.Country

@Composable
fun CountryPicker(
    countriesState: State<List<Country>>,
    onCountrySelected: (Country) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    var selectedCountry by remember { mutableStateOf<Country?>(null) }

    LaunchedEffect(countriesState) {
        if (countriesState is State.Success && !countriesState.data.isNullOrEmpty() && selectedCountry == null) {
            selectedCountry = countriesState.data!![0]
            onCountrySelected(selectedCountry!!)
        }
    }

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(shape = MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = AppColors.inputBorder,
                shape = MaterialTheme.shapes.medium
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { expanded = true }
    ) {
        when (countriesState) {
            is State.Empty -> {}
            is State.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                    )
                }
            }

            is State.Success -> {
                if (!countriesState.data.isNullOrEmpty()) {
                    val countries = countriesState.data!!

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Padding.medium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selectedCountry != null) {
                            SubcomposeAsyncImage(
                                model = selectedCountry!!.flagUrl,
                                contentDescription = selectedCountry!!.countryCode,
                                modifier = Modifier.size(24.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = "+${selectedCountry!!.phoneCode}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = Padding.small),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        containerColor = AppColors.background,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.width(120.dp)
                    ) {
                        countries.forEach { country ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        AsyncImage(
                                            model = country.flagUrl,
                                            contentDescription = country.countryCode,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            text = "+${country.phoneCode}",
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                },
                                onClick = {
                                    selectedCountry = country
                                    onCountrySelected(country)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            is State.Error -> {
                Log.d("ololo", "CountryPicker: ${countriesState.message}")
            }
        }
    }
}