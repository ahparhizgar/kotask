package io.amirhparhizgar.kotask.test.fake

import io.amirhparhizgar.kotask.list.Task
import io.amirhparhizgar.kotask.taskoperation.TaskOperationComponent
import kotlinx.coroutines.Job

class FakeTaskOperationComponent(override val task: Task) :
    TaskOperationComponent {
    override fun setDone(done: Boolean): Job = Job()
}
