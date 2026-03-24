package com.akas62083.qm.screens.home.conponent

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.screens.home.HomeEvent
import com.akas62083.qm.screens.home.HomeUiState
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetScreen(
    uiState: HomeUiState,
    clickedMap: (LatLng?) -> Unit,
    changeMapPointName: (String) -> Unit,
    clickedTagToUnSelected: (MapTagEntity) -> Unit,
    clickedTag: (MapTagEntity) -> Unit,
    savePoint: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { clickedMap(null) },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
        modifier = Modifier.fillMaxHeight().fillMaxWidth()
    ) {
        SharedTransitionLayout {
            Column {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.selectedLatLng!!.latitude.toString() + ", " + uiState.selectedLatLng!!.longitude.toString(),
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = uiState.pointName,
                    onValueChange = { changeMapPointName(it) },
                    label = { Text("名前") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                AnimatedContent(
                    targetState = uiState.selectedTags,
                    label = "tag-list"
                ) { tags ->
                    FlowRow(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 30.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(5.dp)
                            )
                    ) {
                        tags.forEach {
                            Card(
                                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                                modifier = Modifier.padding(10.dp)
                                    .sharedElement(
                                        rememberSharedContentState(key = "tag-${it.name}"),
                                        animatedVisibilityScope = this@AnimatedContent,
                                        boundsTransform = { _, _ ->
                                            spring(
                                                dampingRatio = 0.8f,
                                                stiffness = 380f
                                            )
                                        }
                                    )
                                    .clickable { clickedTagToUnSelected(it) }
                            ) {
                                Text(
                                    text = it.name,
                                    modifier = Modifier.background(
                                        it.color,
                                        CutCornerShape(5.dp)
                                    )
                                        .padding(10.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                AnimatedContent(
                    targetState = uiState.notSelectedTags,
                    label = "tag-lists"
                ) { tags ->
                    FlowRow(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 30.dp)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(5.dp)
                            )
                    ) {
                        tags.forEach {
                            Card(
                                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                                modifier = Modifier.padding(10.dp)
                                    .sharedElement(
                                        rememberSharedContentState(key = "tag-${it.name}"),
                                        animatedVisibilityScope = this@AnimatedContent,
                                        boundsTransform = { _, _ ->
                                            spring(
                                                dampingRatio = 0.8f,
                                                stiffness = 380f
                                            )
                                        }
                                    )
                                    .clickable { clickedTag( it ) }
                            ) {
                                Text(
                                    text = it.name,
                                    modifier = Modifier.background(
                                        it.color,
                                        CutCornerShape(5.dp)
                                    )
                                        .padding(10.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = { savePoint() }
                    ) {
                        Text("保存")
                    }
                }
            }
        }
    }
}