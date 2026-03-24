package com.akas62083.qm.screens.home.dialogs

import android.app.AlertDialog
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.AlertDialog
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
        onDismissRequest = {},
        text = {
            Text("kage")
        },
        dismissButton = {},
        confirmButton = {}
    )
}