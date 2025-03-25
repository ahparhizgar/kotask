package io.amirhparhizgar.kotask

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.Job
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddTaskContent(
    component: AddTaskComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()

    Card(shape = MaterialTheme.shapes.small) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircularCheckbox(
                checked = false,
                onCheckedChange = { /*noop*/ },
            )
            Spacer(Modifier.width(8.dp))
            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .onPreviewKeyEvent {
                        if (it.key == Key.Enter && it.type == KeyEventType.KeyDown) {
                            component.onAddClick()
                            true
                        } else {
                            false
                        }
                    },
                value = state.title,
                onValueChange = component::onTitleChange,
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        component.onAddClick()
                    },
                ),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (state.title.isEmpty()) {
                            Text(
                                "Add a task",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }
    }
}

@Screenshot
@Preview
@Composable
fun AddTaskContentPreview() {
    AddTaskContent(component = PreviewAddTaskComponent())
}

class PreviewAddTaskComponent(
    override val state: MutableValue<AddTaskComponent.State>,
) : AddTaskComponent {
    constructor(
        state: AddTaskComponent.State = AddTaskComponent.State(),
    ) : this(MutableValue(state))

    override fun onTitleChange(newTitle: String) {}

    override fun onDueDateChange(newDueDate: LocalDate?) {}

    override fun onAddClick(): Job = Job()
}
