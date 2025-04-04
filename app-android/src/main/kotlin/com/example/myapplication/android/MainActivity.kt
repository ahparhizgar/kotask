package com.example.myapplication.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.retainedComponent
import io.amirhparhizgar.kotask.MultiPaneTasksComponent
import io.amirhparhizgar.kotask.RootContent
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root =
            retainedComponent { componentContext ->
                getKoin().get<MultiPaneTasksComponent> { parametersOf(componentContext) }
            }

        setContent {
            RootContent(component = root)
        }
    }
}
