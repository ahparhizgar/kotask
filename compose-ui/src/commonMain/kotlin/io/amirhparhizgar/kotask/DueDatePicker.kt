package io.amirhparhizgar.kotask

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun DueDateOptions(
    onSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val tomorrow = today + DatePeriod(days = 1)
    val nextWeek = today.nextMonday()
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Text(
                text = "Due",
                style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(4.dp))
            DueDateOption(
                text = "Today",
                date = today,
                onSelect = onSelect,
            )

            DueDateOption(
                text = "Tomorrow",
                date = tomorrow,
                onSelect = onSelect,
            )

            DueDateOption(
                text = "Next week",
                date = nextWeek,
                onSelect = onSelect,
            )
        }
    }
}

private fun LocalDate.nextMonday(): LocalDate {
    val daysUntilMonday = (7 + DayOfWeek.MONDAY.ordinal - dayOfWeek.ordinal) % 7
    return plus(DatePeriod(days = if (daysUntilMonday == 0) 7 else daysUntilMonday))
}

@Composable
private fun DueDateOption(
    text: String,
    date: LocalDate,
    onSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dayName = remember(date) {
        when (date.dayOfWeek) {
            DayOfWeek.MONDAY -> "Mon"
            DayOfWeek.TUESDAY -> "Tue"
            DayOfWeek.WEDNESDAY -> "Wed"
            DayOfWeek.THURSDAY -> "Thu"
            DayOfWeek.FRIDAY -> "Fri"
            DayOfWeek.SATURDAY -> "Sat"
            DayOfWeek.SUNDAY -> "Sun"
        }
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect(date) }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = null,
            )
            Text(text = text)
        }
        Text(
            text = dayName,
            color = Color.Gray,
        )
    }
}

@Composable
@Preview
fun DueDateOptionsPreview() {
    MaterialTheme {
        DueDateOptions(onSelect = {})
    }
}
