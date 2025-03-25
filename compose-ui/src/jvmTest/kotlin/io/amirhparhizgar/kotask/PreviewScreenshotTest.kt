package io.amirhparhizgar.kotask

import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.provideRoborazziContext
import io.github.takahirom.roborazzi.captureRoboImage
import io.kotest.core.spec.style.FunSpec
import sergio.sastre.composable.preview.scanner.jvm.JvmAnnotationScanner
import java.io.File

@OptIn(ExperimentalTestApi::class, InternalComposeUiApi::class, ExperimentalRoborazziApi::class)
class PreviewScreenshotTest : FunSpec({
    val packages = arrayOf("io.amirhparhizgar")
    val annotationToScanClassName =
        Screenshot::class.qualifiedName.toString()

    val previews = JvmAnnotationScanner(
        annotationToScanClassName = annotationToScanClassName,
    ).scanPackageTrees(*packages)
        .getPreviews()
    val roborazziContext = provideRoborazziContext()
    if (roborazziContext.options.taskType.isEnabled()) {
        previews.forEach { preview ->
            test(preview.methodName) {
                runDesktopComposeUiTest {
                    setContent {
                        preview()
                    }
                    waitForIdle()
                    scene.size = scene.calculateContentSize()
                    onRoot().captureRoboImage(
                        file = File("build/outputs/roborazzi/${preview.methodName}.png"),
                        roborazziOptions = roborazziContext.options,
                    )
                }
            }
        }
    }
})
