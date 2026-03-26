package com.akas62083.qm.screens.home.conponent.dialogs_bottomsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akas62083.qm.db.maptag.MapTagEntity
import com.akas62083.qm.screens.home.AddOrEditEntity
import com.akas62083.qm.screens.home.HomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPointSTagsBottomSheet(
    uiState: HomeUiState,
    cancel: () -> Unit,
    removeTag: (MapTagEntity) -> Unit,
    addTag: (MapTagEntity) -> Unit
) {
    val state = uiState.addOrEditEntity as AddOrEditEntity.EditPointSTags
    val editPoint = state.point

    val selectedTags = remember(uiState.pointWithTags, editPoint) {
        uiState.pointWithTags.find { it.point.id == editPoint.id }?.tags ?: emptyList()
    }

    val unselectedTags = remember(uiState.tagWithPoints, selectedTags) {
        uiState.tagWithPoints
            .map { it.tag }
            .filter { tag -> selectedTags.none { it.id == tag.id } }
    }

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { cancel() },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            items(
                items = selectedTags,
                key = { "tag-${it.id}" }
            ) { tag ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = true,
                        onCheckedChange = { removeTag(tag) }
                    )
                    Text(
                        text = tag.name,
                        modifier = Modifier.background(
                            tag.color,
                            CutCornerShape(5.dp)
                        )
                            .padding(10.dp)
                    )
                }
            }

            items(
                items = unselectedTags,
                key = { "tag-${it.id}" }
            ) { tag ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = false,
                        onCheckedChange = { addTag(tag) }
                    )

                    Text(
                        text = tag.name,
                        modifier = Modifier.background(
                            tag.color,
                            CutCornerShape(5.dp)
                        )
                            .padding(10.dp)
                    )
                }
            }
        }
    }
}
