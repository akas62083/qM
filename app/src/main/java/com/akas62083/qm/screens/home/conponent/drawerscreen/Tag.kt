package com.akas62083.qm.screens.home.conponent.drawerscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akas62083.qm.db.tagandpoint.TagWithPoints
import com.akas62083.qm.screens.home.HomeUiState

@Composable
fun Tag(
    uiState: HomeUiState,
    onTagClicked: (TagWithPoints) -> Unit,
    addTagDialogToggle: () -> Unit
) {
    Column {
        if (uiState.tagWithPoints.isNotEmpty()) {
            uiState.tagWithPoints.forEach {
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    Text(
                        text = it.tag.name,
                        modifier = Modifier.background(
                            it.tag.color,
                            CutCornerShape(5.dp)
                        )
                            .padding(10.dp)
                            .clickable { onTagClicked(it) }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.fillMaxHeight()) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(it.points.size.toString() + "個のポイント")
                    }
                }
            }
        } else {
            Text("タグがありません >_<")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { addTagDialogToggle() },
                shape = CutCornerShape(5.dp)
            ) {
                Text(text = "タグを追加")
            }
        }
    }
}