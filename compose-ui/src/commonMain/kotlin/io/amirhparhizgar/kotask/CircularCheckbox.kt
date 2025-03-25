package io.amirhparhizgar.kotask

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CircularCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(if (checked) Color(0xFF4CAF50) else Color.Transparent)
            .border(
                width = 2.dp,
                color = if (checked) {
                    Color(0xFF4CAF50)
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.38f,
                    )
                },
                shape = CircleShape,
            ).clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Icon(
                Icons.Rounded.Check,
                contentDescription = "done",
                tint = Color.White,
            )
        }
    }
}

@Screenshot
@Preview
@Composable
fun CircularCheckboxUncheckedPreview() {
    CircularCheckbox(
        checked = false,
        onCheckedChange = {},
    )
}

@Screenshot
@Preview
@Composable
fun CircularCheckboxCheckedPreview() {
    CircularCheckbox(
        checked = true,
        onCheckedChange = {},
    )
}
