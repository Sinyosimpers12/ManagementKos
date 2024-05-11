package org.d3if3075.kosku.ui// org.d3if3075.kosku.ui.KosItem.kt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if3075.kosku.model.Kos

@Composable
fun KosItem(kos: Kos) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),

        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Nomor Kamar: ${kos.roomNumber}", color = Color.Black)
            Text(text = "Nama Penghuni: ${kos.tenantName}", color = Color.Black)
        }
    }
}


@Composable
fun AddKosDialog2(
    kosViewModel: KosViewModel,
    kos: Kos,
    onCloseDialog: () -> Unit
) {

    var roomNumber by remember { mutableStateOf(kos.roomNumber) } // Populate with current roomNumber
    var tenantName by remember { mutableStateOf(kos.tenantName) } // Populate with current tenantName
    var errorMessage by remember { mutableStateOf("") }

    val saveKos: () -> Unit = {
        if (roomNumber.isNotBlank() && tenantName.isNotBlank()) {
            kosViewModel.viewModelScope.launch {
                val newKos = kos.copy(roomNumber = roomNumber, tenantName = tenantName)
                kosViewModel.edit(newKos)
                onCloseDialog()
            }
        } else {
            errorMessage = "Nomor kamar dan nama penghuni harus diisi."
        }
    }

    AlertDialog(
        onDismissRequest = onCloseDialog,
        title = { Text(text = "Update Data Kos") },
        text = {
            // Menggunakan Column untuk mengelompokkan kedua TextField
            Column {
                TextField(
                    value = roomNumber,
                    onValueChange = { roomNumber = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text("Nomor Kamar: $roomNumber") } // Display current roomNumber
                )
                TextField(
                    value = tenantName,
                    onValueChange = { tenantName = it },
                    label = { Text("Nama Penghuni: $tenantName") } // Display current tenantName
                )
                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = saveKos
            ) {
                Text(text = "Simpan")
            }
        },
        dismissButton = {
            Button(
                onClick = onCloseDialog
            ) {
                Text(text = "Batal")
            }
        }
    )
}


