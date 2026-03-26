package com.akas62083.qm.screens.home.conponent.drawerscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.screens.home.HomeUiState

@Composable
fun Point_Edit(
    uiState: HomeUiState,
    openOrCloseEditPointNameDialog: (MapPointEntity?) -> Unit,
    deleteDialog: (MapPointEntity?) -> Unit,
    openOrCloseBottomSheetOfEditPointsTags: (MapPointEntity?) -> Unit
) {
    Column {
        if (uiState.pointWithTags.isNotEmpty()) {
            uiState.pointWithTags.forEach {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier.height(IntrinsicSize.Min)
                        .padding(7.dp)
                ) {
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                            Text(
                                text = it.point.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.clickable(
                                    interactionSource = null,
                                    indication = null
                                ) {  }
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "edit",
                            modifier = Modifier.aspectRatio(1f)
                                .fillMaxHeight()
                                .padding(10.dp)
                                .clickable { openOrCloseEditPointNameDialog(it.point) }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "delete",
                            modifier = Modifier.aspectRatio(1f)
                                .fillMaxHeight()
                                .padding(10.dp)
                                .clickable { deleteDialog(it.point) }
                        )
                    }
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        Text(
                            text = "タグを編集",
                            modifier = Modifier.background(
                                color = Color.LightGray,
                                shape = CutCornerShape(5.dp)
                            )
                                .padding(10.dp)
                                .clickable(
                                    interactionSource = null,
                                    indication = null
                                ) { openOrCloseBottomSheetOfEditPointsTags(it.point) }
                        )
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
            }
        } else {
            Text("地点がありません >_<")
        }
    }
}