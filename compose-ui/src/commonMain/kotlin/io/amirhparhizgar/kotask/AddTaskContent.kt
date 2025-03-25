package io.amirhparhizgar.kotask

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = state.title,
            onValueChange = component::onTitleChange,
        )
        Spacer(Modifier.width(8.dp))
        OutlinedButton(
            onClick = component::onAddClick,
            shape = MaterialTheme.shapes.small,
        ) {
            Text("Add")
        }
    }
}

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
