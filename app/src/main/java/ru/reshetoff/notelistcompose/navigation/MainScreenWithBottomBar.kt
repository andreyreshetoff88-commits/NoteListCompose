package ru.reshetoff.notelistcompose.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.reshetoff.common.State
import ru.reshetoff.common.ui.AppColors
import ru.reshetoff.groups_presentation.ui.GroupsScreen
import ru.reshetoff.notelistcompose.R
import ru.reshetoff.profile_presentation.ui.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithBottomBar(
    syncState: State<Boolean>
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val title = when (syncState) {
        is State.Loading -> "Загрузка..."
        is State.Error -> "Ошибка"
        else -> if (selectedItem == 0) "Группы" else "Профиль"
    }

    val bottomItems = listOf(
        BottomNavScreen.Groups to R.drawable.ic_group,
        BottomNavScreen.Profile to R.drawable.ic_profile
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        color = AppColors.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFE8DCC8),
                tonalElevation = 0.dp,
                modifier = Modifier.clip(
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
            ) {
                bottomItems.forEachIndexed { index, (screen, iconRes) ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        icon = {
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = screen.title,
                                modifier = Modifier.size(32.dp),
                                tint = if (selectedItem == index) Color(0xFFFFA726) else Color.Gray
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            when (selectedItem) {
                0 -> GroupsScreen()
                1 -> ProfileScreen()
            }
        }
    }
}