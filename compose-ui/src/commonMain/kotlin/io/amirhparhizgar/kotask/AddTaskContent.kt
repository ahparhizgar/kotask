package io.amirhparhizgar.kotask

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var isChecked by remember { mutableStateOf(false) }
    var isStarred by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularCheckbox(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
        )
        Spacer(Modifier.width(8.dp))
        BasicTextField(
            modifier = Modifier.weight(1f),
            value = state.title,
            onValueChange = component::onTitleChange,
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
