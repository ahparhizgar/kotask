package io.amirhparhizgar.kotask

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StarButton(
    isStarred: Boolean,
    onStarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onStarClick,
        modifier = modifier,
    ) {
        Icon(
            if (isStarred) Icons.Rounded.Star else Icons.Outlined.Star,
            contentDescription = if (isStarred) "Unstar task" else "Star task",
            tint = if (isStarred) {
                Color(0xFF4CAF50)
            } else {
                MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.3f,
                )
            },
        )
    }
}

@Preview
@Composable
fun StarButtonUnstarredPreview() {
    StarButton(
        isStarred = false,
        onStarClick = {},
    )
}

@Preview
@Composable
fun StarButtonStarredPreview() {
    StarButton(
        isStarred = true,
        onStarClick = {},
    )
}
