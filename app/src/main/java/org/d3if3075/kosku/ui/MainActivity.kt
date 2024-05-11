package org.d3if3075.kosku.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.d3if3075.kosku.database.KosDao
import org.d3if3075.kosku.database.KosDatabase
import org.d3if3075.kosku.database.KosRepository
import org.d3if3075.kosku.model.Kos
import org.d3if3075.kosku.theme.KosKuTheme

fun navigateToAturActivity(activity: AppCompatActivity) {
    val intent = Intent(activity, AturActivity::class.java)
    activity.startActivity(intent)
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = applicationContext
        val kosDao: KosDao = KosDatabase.getDatabase(context).kosDao()
        val kosRepository = KosRepository(kosDao)
        val kosViewModel = KosViewModel(kosRepository)

        kosViewModel.getAllKos()

        setContent {
            KosKuApp(
                kosViewModel = kosViewModel,
                navigateToAturActivity = { navigateToAturActivity(this) }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun KosKuApp(
    kosViewModel: KosViewModel,
    navigateToAturActivity: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) } // State untuk menampilkan dialog
    val kosList by kosViewModel.kosList.collectAsState()

    KosKuTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "GoKos")},
                    navigationIcon = {
                        IconButton(onClick = {  }) {
                            Icon(Icons.Default.Home, contentDescription = "Back")
                        }
                    },
                    actions = {
                        // Add a button to navigate to org.d3if3075.kosku.ui.AturActivity
                        IconButton(onClick = navigateToAturActivity) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDialog = true }, // Menampilkan dialog saat tombol ditekan
                    content = { Text(text = "+") }
                )
            }

        ) {
            // Content of Scaffold goes here
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (kosList.isEmpty()) {
                    Text(
                        text = "Belum ada data kos",
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    KosList(kosList = kosList)
                }
            }
        }
    }

    // Menampilkan dialog jika showDialog bernilai true
    if (showDialog) {
        AddKosDialog(
            kosViewModel = kosViewModel,
            onCloseDialog = { showDialog = false }
        )
    }
}

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
        title = { Text(text = "Tambah Data Kos") },
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


