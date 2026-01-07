package com.example.uwe_shopping_app.ui.components.search
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class SortOption {
    NEWEST,
    OLDEST,
    NAME_ASC,
    NAME_DESC,
    RATING_ASC,
    RATING_DESC,
}

data class SearchFilterState(
    val minPrice: Float = 0f,
    val maxPrice: Float = 1500f,
    val sortBy: SortOption = SortOption.NEWEST
)


@Composable
fun SearchFilterSheet(
    state: SearchFilterState,
    onStateChange: (SearchFilterState) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit
) {
    Column(modifier = Modifier.padding(20.dp)) {

        Text("Filter", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(24.dp))

        Text("Price", fontWeight = FontWeight.SemiBold)

        RangeSlider(
            value = state.minPrice..state.maxPrice,
            valueRange = 0f..1500f,
            onValueChange = {
                onStateChange(
                    state.copy(
                        minPrice = it.start,
                        maxPrice = it.endInclusive
                    )
                )
            },
            colors = SliderDefaults.colors(
                activeTrackColor = Color.Black,
                thumbColor = Color.Black
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("$${state.minPrice.toInt()}")
            Text("$${state.maxPrice.toInt()}")
        }

        Spacer(Modifier.height(24.dp))

        Text("Sort By", fontWeight = FontWeight.SemiBold)

        SortOptionButton("Newest", state.sortBy == SortOption.NEWEST) {
            onStateChange(state.copy(sortBy = SortOption.NEWEST))
        }
        SortOptionButton("Oldest", state.sortBy == SortOption.OLDEST) {
            onStateChange(state.copy(sortBy = SortOption.OLDEST))
        }
        SortOptionButton("A → Z", state.sortBy == SortOption.NAME_ASC) {
            onStateChange(state.copy(sortBy = SortOption.NAME_ASC))
        }
        SortOptionButton("Z → A", state.sortBy == SortOption.NAME_DESC) {
            onStateChange(state.copy(sortBy = SortOption.NAME_DESC))
        }
        SortOptionButton(
            "Rating: High → Low",
            state.sortBy == SortOption.RATING_DESC
        ) {
            onStateChange(state.copy(sortBy = SortOption.RATING_DESC))
        }
        SortOptionButton(
            "Rating: Low → High",
            state.sortBy == SortOption.RATING_ASC
        ) {
            onStateChange(state.copy(sortBy = SortOption.RATING_ASC))
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onReset) { Text("Reset") }
            Button(
                onClick = onApply,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Text("Apply", color = Color.White)
            }
        }
    }
}

@Composable
private fun SortOptionButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = if (selected)
            ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        else
            ButtonDefaults.outlinedButtonColors(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            label,
            color = if (selected) Color.White else Color.Unspecified
        )
    }
}