package demo.placewascalledonanodewhichwasplacedalready

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

val items = listOf(
    Item("Item 1", randomColor(), Spans(6, 6, 6, 6, 6, 6)),
    Item("Item 2", randomColor(), Spans(6, 6, 6, 6, 6, 6)),
    Item("Item 3", randomColor(), Spans(6, 6, 6, 6, 6, 6)),
    Item("Item 4", randomColor(), Spans(6, 6, 6, 6, 6, 6)),
    Item("Item 5", randomColor(), Spans(6, 6, 6, 3, 3, 3)),
    Item("Item 6", randomColor(), Spans(6, 6, 6, 3, 3, 3)),
    Item("Item 7", randomColor(), Spans(6, 6, 6, 3, 3, 3)),
    Item("Item 8", randomColor(), Spans(6, 6, 6, 3, 3, 3)),
    Item("Item 9", randomColor(), Spans(6, 6, 6, 6, 6, 6)),
    Item("Item 10", randomColor(), Spans(6, 6, 6, 6, 6, 6)),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var screenSize by remember { mutableStateOf(IntSize.Zero) }
            val density = LocalDensity.current
            LazyVerticalGrid(
                columns = GridCells.Fixed(12),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize().onSizeChanged {
                    // TODO: bad perf due to Recomposition loop
                    //   https://developer.android.com/develop/ui/compose/phases#recomp-loop
                    screenSize = it / density.density.roundToInt()
                },
            ) {
                items(
                    count = items.size,
                    span = { index ->
                        GridItemSpan(items[index].span.get(screenSize.width))
                    }
                ) { index ->
                    Box(
                        modifier = Modifier
                            .defaultMinSize(minHeight = 200.dp)
                            .fillMaxSize()
                            .background(color = items[index].color),
                    ) {
                        Text(text = items[index].name)
                    }
                }
            }
        }
    }
}

private fun randomColor(): Color = Color(
    red = (0..255).random(),
    green = (0..255).random(),
    blue = (0..255).random(),
)

data class Item(
    val name: String,
    val color: Color,
    val span: Spans,
)

data class Spans(
    val span320: Int,
    val span411: Int,
    val span600: Int,
    val span800: Int,
    val span1080: Int,
    val span1440: Int,
) {
    fun get(screenWidth: Int): Int = when {
        screenWidth < 411 -> span320
        screenWidth < 600 -> span411
        screenWidth < 800 -> span600
        screenWidth < 1080 -> span800
        screenWidth < 1440 -> span1080
        else -> span1440
    }
}