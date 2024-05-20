package org.d3if3075.kosku.ui// org.d3if3075.kosku.ui.Func.kt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if3075.kosku.model.Kos

@Composable
fun AddKosDialog(
    kosViewModel: KosViewModel,
    onCloseDialog: () -> Unit
) {

    var roomNumber by remember { mutableStateOf("") }
    var tenantName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val saveKos: () -> Unit = {
        if (roomNumber.isNotBlank() && tenantName.isNotBlank()) {
            kosViewModel.viewModelScope.launch {
                val isRoomNumberExists = kosViewModel.isRoomNumberExists(roomNumber)
                if (!isRoomNumberExists) {
                    val newKos = Kos(roomNumber = roomNumber, tenantName = tenantName)
                    kosViewModel.insertKos(newKos)
                    onCloseDialog()
                } else {
                    errorMessage = "Nomor kamar sudah terdaftar. Harap masukkan nomor kamar lain."
                }
            }
        } else {
            errorMessage = "Nomor kamar dan nama penghuni harus diisi."
        }
    }

    AlertDialog(
        onDismissRequest = onCloseDialog,
        title = { Text(text = "Tambah Penghuni Kos") },
        text = {
            // Menggunakan Column untuk mengelompokkan kedua TextField
            Column {
                TextField(
                    value = roomNumber,
                    onValueChange = { roomNumber = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text("Nomor Kamar") }
                )
                TextField(
                    value = tenantName,
                    onValueChange = { tenantName = it },
                    label = { Text("Nama Penghuni") }
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
@Composable
fun AddKosDialog2(
    kosViewModel: KosViewModel,
    kos: Kos,
    onCloseDialog: () -> Unit
) {

    var roomNumber by remember { mutableStateOf(kos.roomNumber) }
    var tenantName by remember { mutableStateOf(kos.tenantName) }
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
            Column {
                TextField(
                    value = roomNumber,
                    onValueChange = { roomNumber = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text("Nomor Kamar: $roomNumber") }
                )
                TextField(
                    value = tenantName,
                    onValueChange = { tenantName = it },
                    label = { Text("Nama Penghuni: $tenantName") }
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

@Composable
fun AddKosDialogGuide(onCloseDialog: () -> Unit) {
    Card(
        modifier = Modifier.padding(16.dp),

    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Panduan Pengisian Data Kos",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "1. Isi Nomor Kamar.",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "2. Isi Nama Penghuni dengan nama lengkap.",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "3. Pastikan kedua kolom sesui sebelum menyimpan.",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "4. Geser Ke Kiri sampai habis dan background akan berwarna merah otomatis menghapus data.",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "5. Geser Ke Kanan sampai habis dan background berwarna biru untuk meng edit data." +
                        "" +
                        " ",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Copyright amba- ®©  Martin,Samuel,Fuad.",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Button(
                onClick = onCloseDialog,
                modifier = Modifier.align(Alignment.End) ,
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Text("Mengerti")
            }
        }
    }
}





