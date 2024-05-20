package org.d3if3075.kosku.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    var showDialog1 by remember { mutableStateOf(false) }
    var showDialog3 by remember { mutableStateOf(false) }
    val kosList by kosViewModel.kosList.collectAsState()
    var selectedKos by remember { mutableStateOf<Kos?>(null) }
    var editCompleted by remember { mutableStateOf(false) }


    KosKuTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "GoKos")},
                    navigationIcon = {
                        IconButton(onClick = { showDialog3 = true }) {
                            Icon(Icons.Default.Home, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = navigateToAturActivity) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDialog = true }, // Menampilkan dialog saat tombol ditekan
                    content = {
                        Text(text = "+")
                    }
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
                    KosList(
                        kosList = kosList,
                        onDeleteClick = { kos ->
                            kosViewModel.deleteKos(kos)
                        },
                        onEditClick = { kos ->
                            selectedKos = kos
                            showDialog1 = true
                        }
                    )
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
    if (showDialog1) {
        selectedKos?.let { kos ->
            AddKosDialog2(
                kosViewModel = kosViewModel,
                kos = kos,
                onCloseDialog = {
                    showDialog1 = false
                    editCompleted = true
                }
            )
        }
    }
    if (showDialog3) {
        AddKosDialogGuide(
            onCloseDialog = { showDialog3 = false }
        )
    }

}
