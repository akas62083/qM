package com.akas62083.qm.screens.home.conponent.dialogs_bottomsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.akas62083.qm.screens.home.AddOrEditEntity
import com.akas62083.qm.screens.home.HomeUiState
import com.akas62083.qm.screens.home.SelectedColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTagDialog(
    uiState: HomeUiState,
    onValueChange: (String) -> Unit,
    onColorPickerBottomSheetToggle: () -> Unit,
    cancel: () -> Unit,
    onColorClicked: (SelectedColor) -> Unit,
    confirm: () -> Unit
) {
    val state = uiState.addOrEditEntity as AddOrEditEntity.AddTag
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "タグを追加") },
        text = {
            Column {
                Row {
                    TextField(
                        modifier = Modifier.weight(8f),
                        value = state.text,
                        onValueChange = { onValueChange(it) },
                        label = { Text(text = "タグ名") },
                        singleLine = true
                    )
                    Box(
                        modifier = Modifier.weight(2f)
                            .aspectRatio(1f)
                            .fillMaxSize()
                            .padding(4.dp)
                            .background(
                                color = when (state.selectedColor) {
                                    is SelectedColor.Red -> Color(0xffff5252)
                                    is SelectedColor.Orange -> Color(0xffff9800)
                                    is SelectedColor.Yellow -> Color(0xfffdd835)
                                    is SelectedColor.Green -> Color(0xff4caf50)
                                    is SelectedColor.Blue -> Color(0xff2196f3)
                                    is SelectedColor.Purple -> Color(0xff9c27b0)
                                    is SelectedColor.Pink -> Color(0xffe91e63)
                                    is SelectedColor.Custom -> state.selectedColor.color
                                },
                                shape = RoundedCornerShape(3.dp)
                            )
                            .clickable { onColorPickerBottomSheetToggle() }
                    ) {}
                }
            }
        },
        confirmButton = {
            Button(
                shape = RoundedCornerShape(5.dp),
                onClick = {
                    confirm()
                }
            ) {
                Text("追加")
            }
        },
        dismissButton = {
            Button(
                shape = RoundedCornerShape(5.dp),
                onClick = {
                    cancel()
                }
            ) {
                Text("キャンセル")
            }
        },
        shape = RoundedCornerShape(5.dp)
    )


    if(state.colorPicker) {
        var redSliderValue by remember { mutableFloatStateOf(0f) }
        var greenSliderValue by remember { mutableFloatStateOf(0f) }
        var blueSliderValue by remember { mutableFloatStateOf(0f) }

        ModalBottomSheet(
            onDismissRequest = { onColorPickerBottomSheetToggle() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
            modifier = Modifier.fillMaxHeight().fillMaxWidth()
        ) {
            Column {
                Row {
                    Box(modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .fillMaxSize()
                        .padding(5.dp)
                        .background(Color(0xffff5252), shape = RoundedCornerShape(5.dp))
                        .border(width = if(state.selectedColor is SelectedColor.Red) 2.dp else 0.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                        .clickable { onColorClicked(SelectedColor.Red)}
                    ) {}
                    Box(modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .fillMaxSize()
                        .padding(5.dp)
                        .background(Color(0xffff9800), shape = RoundedCornerShape(5.dp))
                        .border(width = if(state.selectedColor is SelectedColor.Orange) 2.dp else 0.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                        .clickable { onColorClicked(SelectedColor.Orange)}
                    ) {}
                    Box(modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .fillMaxSize()
                        .padding(5.dp)
                        .background(Color(0xfffdd835), shape = RoundedCornerShape(5.dp))
                        .border(width = if(state.selectedColor is SelectedColor.Yellow) 2.dp else 0.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                        .clickable { onColorClicked(SelectedColor.Yellow)}
                    ) {}
                    Box(modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .fillMaxSize()
                        .padding(5.dp)
                        .background(Color(0xff4caf50), shape = RoundedCornerShape(5.dp))
                        .border(width = if(state.selectedColor is SelectedColor.Green) 2.dp else 0.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                        .clickable { onColorClicked(SelectedColor.Green)}
                    ) {}
                }
                Row {
                    Box(modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .fillMaxSize()
                        .padding(5.dp)
                        .background(Color(0xff2196f3), shape = RoundedCornerShape(5.dp))
                        .border(width = if(state.selectedColor is SelectedColor.Blue) 2.dp else 0.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                        .clickable { onColorClicked(SelectedColor.Blue)}
                    ) {}
                    Box(modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .fillMaxSize()
                        .padding(5.dp)
                        .background(Color(0xff9c27b0), shape = RoundedCornerShape(5.dp))
                        .border(width = if(state.selectedColor is SelectedColor.Purple) 2.dp else 0.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                        .clickable { onColorClicked(SelectedColor.Purple)}
                    ) {}
                    Box(modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .fillMaxSize()
                        .padding(5.dp)
                        .background(Color(0xffe91e63), shape = RoundedCornerShape(5.dp))
                        .border(width = if(state.selectedColor is SelectedColor.Pink) 2.dp else 0.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                        .clickable { onColorClicked(SelectedColor.Pink)}
                    ) {}
                    Box(modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .fillMaxSize()
                        .padding(5.dp)
                        .background(Color(red = redSliderValue.toInt(), green = greenSliderValue.toInt(), blue = blueSliderValue.toInt()), shape = RoundedCornerShape(5.dp))
                        .border(width = if(state.selectedColor is SelectedColor.Custom) 2.dp else 0.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                        .clickable { onColorClicked(SelectedColor.Custom(Color(redSliderValue.toInt(), greenSliderValue.toInt(), blueSliderValue.toInt())))}
                    ) {}
                }
                Text(modifier = Modifier.padding(7.5.dp), text = "カスタムカラー", textAlign = TextAlign.Center)
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    Box(modifier = Modifier.weight(1f)
                            .fillMaxSize()
                            .padding(5.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("Red: ${redSliderValue.toInt()}")
                    }
                    Slider(
                        modifier = Modifier.weight(3f).fillMaxSize(),
                        value = redSliderValue,
                        onValueChange = { redSliderValue = it },
                        valueRange = 0f..255f,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    Box(modifier = Modifier.weight(1f)
                        .fillMaxSize()
                        .padding(5.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("Green: ${greenSliderValue.toInt()}")
                    }
                    Slider(
                        modifier = Modifier.weight(3f).fillMaxSize(),
                        value = greenSliderValue,
                        onValueChange = { greenSliderValue = it },
                        valueRange = 0f..255f,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    Box(modifier = Modifier.weight(1f)
                        .fillMaxSize()
                        .padding(5.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("Blue: ${blueSliderValue.toInt()}")
                    }
                    Slider(
                        modifier = Modifier.weight(3f).fillMaxSize(),
                        value = blueSliderValue,
                        onValueChange = { blueSliderValue = it },
                        valueRange = 0f..255f,
                    )
                }
            }
        }
    }
}