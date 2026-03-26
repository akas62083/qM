package com.akas62083.qm.screens.home.dialogs_bottomsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.akas62083.qm.screens.home.HomeUiState

@Composable
fun EditPointNameDialog(
    uiState: HomeUiState,
    cancel: () -> Unit,
    edit: () -> Unit,
    changePointName: (String) -> Unit
) {
    AlertDialog(
        shape = CutCornerShape(5.dp),
        onDismissRequest = {
        },
        title = {
            Column {
                Text("名前を変更する")
                TextField(
                    value = uiState.pointName,
                    onValueChange = { changePointName(it) },
                    label = { Text("名前") }
                )
                if(!uiState.pointNameEnabled && uiState.pointName.length > 20) {
                    Text(
                        text = "名前は20文字以内で入力してください",
                        color = Color.Red,
                    )
                }
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
                enabled = uiState.pointNameEnabled,
                onClick = {
                    edit()
                    cancel()
                }
            ) {
                Text("変更")
            }
        }
    )
}