package com.akas62083.qm.screens.home.dialogs_bottomsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.akas62083.qm.screens.home.HomeUiState

@Composable
fun DeletePointDialog(
   uiState: HomeUiState,
   cancel: () -> Unit,
   delete: () -> Unit
) {
    AlertDialog(
        shape = CutCornerShape(5.dp),
        onDismissRequest = {
            cancel()
        },
        text = {
            Column {
                Text("以下の項目を削除しますか?", )
                Text(("地点：" + uiState.deletePoint?.name) ?: "unknown")
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
                    delete()
                    cancel()
                }
            ) {
                Text("削除")
            }
        }
    )
}