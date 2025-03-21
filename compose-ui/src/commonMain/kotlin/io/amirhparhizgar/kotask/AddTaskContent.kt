package io.amirhparhizgar.kotask

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun AddTaskContent(
    component: AddTaskComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()
    Row(modifier = modifier) {
        TextField(
            value = state.title,
            onValueChange = component::onTitleChange,
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = component::onAddClick,
        ) {
            Text("Add")
        }
    }
}
