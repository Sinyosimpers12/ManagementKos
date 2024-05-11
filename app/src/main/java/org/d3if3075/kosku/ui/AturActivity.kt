package org.d3if3075.kosku.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.d3if3075.kosku.database.KosDao
import org.d3if3075.kosku.database.KosDatabase
import org.d3if3075.kosku.database.KosRepository
import org.d3if3075.kosku.model.Kos
import org.d3if3075.kosku.theme.KosKuTheme

class AturActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = applicationContext
        val kosDao: KosDao = KosDatabase.getDatabase(context).kosDao()
        val kosRepository = KosRepository(kosDao)
        val kosViewModel = KosViewModel(kosRepository)

        kosViewModel.getAllKos()

        setContent {
            KosKuApp1(
                kosViewModel = kosViewModel,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun KosKuApp1(
    kosViewModel: KosViewModel,
) {
    val context = LocalContext.current
    var showDialog2 by remember { mutableStateOf(false) }
    var selectedKos by remember { mutableStateOf<Kos?>(null) }
    var editCompleted by remember { mutableStateOf(false) } // State variable to track edit completion

    val kosList by kosViewModel.kosList.collectAsState()
    val deletedKos by kosViewModel.deletedKos.collectAsState()

    LaunchedEffect(deletedKos, editCompleted) { // Trigger recomposition on changes in editCompleted
        if (deletedKos != null || editCompleted) {
            kosViewModel.clearDeletedKos()
            // Perform any necessary actions to refresh the activity
            // For example, you can re-fetch the data or trigger a navigation to refresh the screen
            kosViewModel.getAllKos() // Fetch the data again
            editCompleted = false // Reset the editCompleted status
        }
    }

    KosKuTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "GoKos") },
                    navigationIcon = {
                        IconButton(onClick = {
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) {
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
                    KosList1(
                        kosList = kosList,
                        onDeleteClick = { kos ->
                            kosViewModel.deleteKos(kos)
                        },
                        onEditClick = { kos ->
                            selectedKos = kos
                            showDialog2 = true
                        }
                    )
                }
            }
        }
    }

    if (showDialog2) {
        selectedKos?.let { kos ->
            AddKosDialog2(
                kosViewModel = kosViewModel,
                kos = kos,
                onCloseDialog = {
                    showDialog2 = false
                    editCompleted = true // Update editCompleted when the edit operation is completed
                }
            )
        }
    }
}






@Composable
fun KosList1(
    kosList: List<Kos>,
    onDeleteClick: (Kos) -> Unit,
    onEditClick: (Kos) -> Unit
) {
    LazyColumn {
        items(kosList) { kos ->
            KosItem1(kos = kos, onDeleteClick = onDeleteClick, onEditClick = onEditClick)
        }
    }
}


@Composable
fun KosItem1(
    kos: Kos,
    onDeleteClick: (Kos) -> Unit,
    onEditClick: (Kos) -> Unit
) {
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

            // Edit Button
            Button(
                onClick = { onEditClick(kos) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue)
            ) {
                Text(text = "Edit")
            }

            // Delete Button
            Button(
                onClick = { onDeleteClick(kos) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text(text = "Delete")
            }
        }
    }
}
