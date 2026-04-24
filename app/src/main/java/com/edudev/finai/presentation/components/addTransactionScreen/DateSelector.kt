package com.edudev.finai.presentation.components.addTransactionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edudev.finai.R
import com.edudev.finai.ui.theme.FinAITheme
import com.edudev.finai.ui.theme.MintEmerald
import com.edudev.finai.ui.theme.OnSurfaceVariant
import com.edudev.finai.ui.theme.OnSurfaceWhite
import com.edudev.finai.ui.theme.Onyx
import com.edudev.finai.ui.theme.SurfaceContainerHighest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(modifier: Modifier = Modifier, selectedDate: Date, onDateSelected: (Date) -> Unit) {
    val dateFormat = SimpleDateFormat("MM  /  dd  /  yyyy", Locale.getDefault())
    val showDatePicker = remember { mutableStateOf(false) }

    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.time
        )

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.label_date),
            style =
            MaterialTheme.typography.labelSmall.copy(
                color = MintEmerald,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(SurfaceContainerHighest)
                .clickable { showDatePicker.value = true }
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = OnSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = dateFormat.format(selectedDate),
                modifier = Modifier.padding(start = 12.dp),
                style =
                MaterialTheme.typography.bodyMedium.copy(
                    color = OnSurfaceWhite,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }

    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val calendarUtc =
                                Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC")).apply {
                                    timeInMillis = millis
                                }
                            val calendarLocal =
                                Calendar.getInstance().apply {
                                    set(Calendar.YEAR, calendarUtc.get(Calendar.YEAR))
                                    set(Calendar.MONTH, calendarUtc.get(Calendar.MONTH))
                                    set(Calendar.DAY_OF_MONTH, calendarUtc.get(Calendar.DAY_OF_MONTH))
                                    set(Calendar.HOUR_OF_DAY, 0)
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }

                            onDateSelected(calendarLocal.time)
                            showDatePicker.value = false
                        } ?: run {
                            showDatePicker.value = false
                        }
                    }
                ) {
                    Text(stringResource(R.string.btn_ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) {
                    Text(stringResource(R.string.btn_cancel))
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111412)
@Composable
private fun DateSelectorPreview() {
    FinAITheme {
        Box(Modifier.background(Onyx).padding(16.dp)) {
            DateSelector(
                selectedDate = Date(),
                onDateSelected = {}
            )
        }
    }
}
