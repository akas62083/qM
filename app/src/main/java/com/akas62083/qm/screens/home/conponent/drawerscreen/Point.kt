package com.akas62083.qm.screens.home.conponent.drawerscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.screens.home.HomeUiState

@Composable
fun Point(
    uiState: HomeUiState,
    clickedDownMenuPoint: (PointWithTags) -> Unit
) {
    Column {
        if (uiState.pointWithTags.isNotEmpty()) {
            uiState.pointWithTags.forEach {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                        .height(IntrinsicSize.Min)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(7.dp)
                        .clickable { clickedDownMenuPoint(it) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "flag",
                        modifier = Modifier.padding(5.dp).aspectRatio(1f)
                            .fillMaxHeight()
                    )
                    Column(modifier = Modifier.fillMaxHeight()) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(it.point.name)
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    it.tags.forEach { tag ->
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = tag.name,
                            modifier = Modifier.background(
                                color = tag.color,
                                shape = CutCornerShape(5.dp)
                            )
                                .padding(10.dp)
                        )
                    }
                }
            }
        } else {
            Text("地点がありません >_<")
        }
    }
}