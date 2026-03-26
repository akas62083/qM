package com.akas62083.qm.screens.home.conponent

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.unit.dp
import com.akas62083.qm.db.mappoint.MapPointEntity
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.db.tagandpoint.PointWithTags
import com.akas62083.qm.db.tagandpoint.TagWithPoints
import com.akas62083.qm.screens.home.HomeUiState
import com.akas62083.qm.screens.home.conponent.drawerscreen.Point
import com.akas62083.qm.screens.home.conponent.drawerscreen.Point_Edit
import com.akas62083.qm.screens.home.conponent.drawerscreen.Tag
import com.akas62083.qm.screens.home.conponent.drawerscreen.Tag_Edit

@Composable
fun DrawerScreen(
    uiState: HomeUiState,
    // point not-edit
    pointMapClicked: (PointWithTags) -> Unit,
    // tag not-edit
    tagMapClicked: (TagWithPoints) -> Unit,
    addTagDialogToggle: () -> Unit,
    // point edit
    editPointSTagsBottomSheetToggle: (MapPointEntity?) -> Unit,
    editPointNameDialogToggle: (MapPointEntity?) -> Unit,
    deletePointDialogToggle: (MapPointEntity?) -> Unit,
    // tag edit
    deleteTagDialogToggle: (MapTagEntity?) -> Unit,
    editTagNameDialogToggle: (MapTagEntity?) -> Unit,
    //
    drawerMenuChanged: (Boolean) -> Unit,

) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
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
                        drawerMenuChanged(true)
                        dropDownMenuExpanded = false
                    },
                    modifier = Modifier.background(if(uiState.dropDownMenuLocationDisplay) Color.LightGray else Color.Transparent)
                )
                DropdownMenuItem(
                    text = {
                        Text("タグ一覧")
                    },
                    onClick = {
                        drawerMenuChanged(false)
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
                                Point_Edit(
                                    uiState = uiState,
                                    editPointNameDialogToggle = { editPointNameDialogToggle(it) },
                                    deletePointDialogToggle = { deletePointDialogToggle(it) },
                                    editPointSTagsBottomSheetToggle = { editPointSTagsBottomSheetToggle(it) }
                                )
                            }
                            false -> {
                                Tag_Edit(
                                    uiState = uiState,
                                    addTagDialogToggle = { addTagDialogToggle() },
                                    deleteTagDialogToggle = { deleteTagDialogToggle(it) },
                                    editTagNameDialogToggle = { editTagNameDialogToggle(it) }
                                )
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
                                Point(
                                    uiState = uiState,
                                    onPointClicked = { pointMapClicked(it) }
                                )
                            }
                            false -> {
                                Tag(
                                    uiState = uiState,
                                    addTagDialogToggle = { addTagDialogToggle() },
                                    onTagClicked = { tagMapClicked(it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}