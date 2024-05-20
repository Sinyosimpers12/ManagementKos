@file:Suppress("UNUSED_EXPRESSION")

package org.d3if3075.kosku.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.d3if3075.kosku.model.Kos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KosList(
    kosList: List<Kos>,
    onDeleteClick: (Kos) -> Unit,
    onEditClick: (Kos) -> Unit
) {
    var editedIndex by remember { mutableIntStateOf(-1) }


    LaunchedEffect(kosList) {

    }

    LazyColumn {
        itemsIndexed(kosList) { index, kos ->
            val dismissState = rememberSwipeToDismissBoxState()

            LaunchedEffect(dismissState.currentValue) {
                if (dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd) {
                    onEditClick(kos)
                    editedIndex = index
                } else if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                    onDeleteClick(kos)
                }
            }

            if (editedIndex == index) {
                LaunchedEffect(Unit) {
                    editedIndex = -1 // Reset the edited index
                    dismissState.snapTo(SwipeToDismissBoxValue.Settled)

                }
            }

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    val dismissDirection = dismissState.currentValue
                    val alignment = when (dismissDirection) {
                        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                        else -> Alignment.Center
                    }
                    val icon = when (dismissDirection) {
                        SwipeToDismissBoxValue.StartToEnd -> "Edit"
                        SwipeToDismissBoxValue.EndToStart -> "Delete"
                        else -> ""
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                if (editedIndex == index) Color.Transparent else {
                                    when (dismissDirection) {
                                        SwipeToDismissBoxValue.StartToEnd -> Color.Blue
                                        SwipeToDismissBoxValue.EndToStart -> Color.Red
                                        else -> Color.Transparent
                                    }
                                }
                            )
                            .padding(horizontal = 2.dp),
                        contentAlignment = alignment
                    ) {
                        Text(
                            text = icon,
                            color = Color.Black
                        )
                    }
                }
            ) { KosItem(kos = kos) }
        }
    }
}


@Composable
fun KosItem(
    kos: Kos
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Nomor Kamar: ${kos.roomNumber}", color = Color.Black)
            Text(text = "Nama Penghuni: ${kos.tenantName}", color = Color.Black)
        }
    }
}