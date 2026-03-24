package com.akas62083.qm.screens.home.conponent

import android.R.attr.tag
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.contentValuesOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.db.tagandpoint.TagWithPoints
import com.akas62083.qm.screens.home.HomeEvent
import com.akas62083.qm.screens.home.HomeUiState
import com.akas62083.qm.screens.home.dialogs.DeletePointDialog
import com.akas62083.qm.screens.home.dialogs.EditPointNameDialog

@Composable
fun DrawerScreen(
    uiState: HomeUiState,
    dropDownMenuDisplayChange: (Boolean) -> Unit,
    openOrCloseAddTagDialog: () -> Unit,
    clickedDownMenuPoint: (PointWithTags) -> Unit,
    clickedDownMenuTag: (TagWithPoints) -> Unit,
    openOrCloseEditPointNameDialog: (MapPointEntity?) -> Unit,
    editPointName: () -> Unit,
    changePointName: (String) -> Unit,
    deleteDialog: (MapPointEntity?) -> Unit,
    deletePoint: () -> Unit
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) } //地点一覧表示かタグ一覧表示かを指定する四角い小さいやつ
    var isEditMode by remember { mutableStateOf(false) }
    val animatedDegree: Float by animateFloatAsState(
        if(dropDownMenuExpanded) 0f else -90f,
        label = "degree"
    )
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(12.dp))
        Box {
            Row(
                modifier = Modifier.height(IntrinsicSize.Min)
                    .clickable(
                        interactionSource = null,
                        indication = null
                    ) { dropDownMenuExpanded = !dropDownMenuExpanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedContent(
                    targetState = uiState.dropDownMenuLocationDisplay,
                    transitionSpec = {
                        if(targetState) {
                            slideInVertically { -it } + fadeIn() togetherWith slideOutVertically { it } + fadeOut()
                        } else {
                            slideInVertically { it } + fadeIn() togetherWith slideOutVertically { -it } + fadeOut()
                        }
                    },
                    label = "location-display"
                ) { target ->
                    when(target) {
                        true -> {
                            Text(
                                text = "地点一覧",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        false -> {
                            Text(
                                text = "タグ一覧",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Box(modifier = Modifier.aspectRatio(1f).padding(15.dp).fillMaxSize()) {
                    Icon(
                        modifier = Modifier.fillMaxSize().rotate(animatedDegree),
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }
            DropdownMenu(
                expanded = dropDownMenuExpanded,
                onDismissRequest = { dropDownMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text("地点一覧")
                    },
                    onClick = {
                        dropDownMenuDisplayChange(true)
                        dropDownMenuExpanded = false
                    },
                    modifier = Modifier.background(if(uiState.dropDownMenuLocationDisplay) Color.LightGray else Color.Transparent)
                )
                DropdownMenuItem(
                    text = {
                        Text("タグ一覧")
                    },
                    onClick = {
                        dropDownMenuDisplayChange(false)
                        dropDownMenuExpanded = false
                    },
                    modifier = Modifier.background(if(!uiState.dropDownMenuLocationDisplay) Color.LightGray else Color.Transparent)
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Text(
                text = if(isEditMode) "編集を終了する" else "編集する",
                color = Color(0xff4444ff),
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null
                ) { isEditMode = !isEditMode }
            )
        }
        AnimatedContent(
            targetState = isEditMode,
            label = "isEditMode"
        ) { targetState ->
            when(targetState) {
                true -> {
                    AnimatedContent(
                        targetState = uiState.dropDownMenuLocationDisplay,
                        label = "tag-list"
                    ) { target ->
                        when(target) {
                            true -> {
                                Column {
                                    if (uiState.pointWithTags.isNotEmpty()) {
                                        uiState.pointWithTags.forEach {
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Column(
                                                modifier = Modifier.height(IntrinsicSize.Min)
                                                    .padding(7.dp)
                                            ) {
                                                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                                                    Icon(
                                                        imageVector = Icons.Default.ChevronRight,
                                                        contentDescription = null,
                                                        modifier = Modifier.aspectRatio(1f)
                                                            .fillMaxHeight()
                                                            .padding(7.5.dp)
                                                            .clickable {}
                                                    )
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
                                                    Spacer(modifier = Modifier.width(50.dp))
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
                                                            ) {  }
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
                            false -> {
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
                                                        .clickable { clickedDownMenuTag(it) }
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
                                            onClick = { openOrCloseAddTagDialog() },
                                            shape = CutCornerShape(5.dp)
                                        ) {
                                            Text(text = "タグを追加")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                false -> {
                    AnimatedContent(
                        targetState = uiState.dropDownMenuLocationDisplay,
                        label = "tag-list"
                    ) { target ->
                        when(target) {
                            true -> {
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
                            false -> {
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
                                                        .clickable { clickedDownMenuTag(it) }
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
                                            onClick = { openOrCloseAddTagDialog() },
                                            shape = CutCornerShape(5.dp)
                                        ) {
                                            Text(text = "タグを追加")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if(uiState.editPointName != null) {
        EditPointNameDialog(
            uiState = uiState,
            cancel = { openOrCloseEditPointNameDialog(null) },
            edit = { editPointName() },
            changePointName = { changePointName(it) }
        )
    }
    if(uiState.deletePoint != null) {
        DeletePointDialog(
            uiState = uiState,
            cancel = { deleteDialog(null) },
            delete = { deletePoint() },
        )
    }
}