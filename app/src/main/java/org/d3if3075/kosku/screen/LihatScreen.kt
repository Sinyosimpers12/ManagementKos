package org.d3if3075.kosku.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.d3if3075.kosku.database.CatatanDb
import org.d3if3075.kosku.database.CatatanRepository
import org.d3if3075.kosku.model.Catatan
import org.d3if3075.kosku.util.LihatViewModelFactory

@Composable
fun LihatScreen(navController: NavHostController) {
    val context = LocalContext.current
    val catatanDao = CatatanDb.getInstance(context).dao
    val repository = CatatanRepository(catatanDao)
    val viewModelFactory = LihatViewModelFactory(repository)
    val viewModel: LihatViewModel = viewModel(factory = viewModelFactory)

    val catatanList by viewModel.catatanList.collectAsState(initial = emptyList())

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(catatanList) { catatan ->
            CatatanListItem(catatan = catatan)
        }
    }
}

@Composable
fun CatatanListItem(catatan: Catatan) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Judul: ${catatan.judul}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Catatan: ${catatan.catatan}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Nomor Kamar: ${catatan.nomorkamar}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Tanggal Masuk: ${catatan.tanggal}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Jenis Kelamin: ${catatan.jenisKelamin}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Tanggal Keluar: ${catatan.tanggalKeluar}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
