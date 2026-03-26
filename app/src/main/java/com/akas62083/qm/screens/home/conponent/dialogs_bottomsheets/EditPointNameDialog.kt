package com.akas62083.qm.screens.home.conponent.dialogs_bottomsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.akas62083.qm.screens.home.AddOrEditEntity
import com.akas62083.qm.screens.home.HomeUiState

@Composable
fun EditPointNameDialog(
    uiState: HomeUiState,
    onValueChange: (String) -> Unit,
    cancel: () -> Unit,
    confirm: () -> Unit,
) {
    val state = uiState.addOrEditEntity as AddOrEditEntity.EditPointName
    AlertDialog(
        shape = CutCornerShape(5.dp),
        onDismissRequest = {
        },
        title = {
            Column {
                Text("名前を変更する")
                TextField(
                    value = state.text,
                    onValueChange = { onValueChange(it) },
                    label = { Text("名前") }
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    cancel()
                }
            ) {
                Text("キャンセル")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    confirm()
                }
            ) {
                Text("変更")
            }
        }
    )
}