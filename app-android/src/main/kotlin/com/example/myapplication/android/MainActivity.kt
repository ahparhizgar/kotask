package com.example.myapplication.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.retainedComponent
import io.amirhparhizgar.kotask.AllTasksComponent
import io.amirhparhizgar.kotask.RootContent
import io.amirhparhizgar.kotask.list.TaskListComponent
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root =
            retainedComponent { componentContext ->
                getKoin().get<AllTasksComponent> { parametersOf(componentContext) }
            }

        setContent {
            RootContent(component = root)
        }
    }
}
