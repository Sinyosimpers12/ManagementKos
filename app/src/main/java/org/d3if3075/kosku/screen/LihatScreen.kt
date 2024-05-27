package org.d3if3075.kosku.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.d3if3075.kosku.theme.KosKuTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3075.kosku.database.CatatanDb
import org.d3if3075.kosku.model.Catatan
import org.d3if3075.kosku.util.ViewModelFactory
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LihatScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = CatatanDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "GoKos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Spacer(modifier = Modifier.height(50.dp)) // Add padding above the LazyColumn
        LazyColumn(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            itemsIndexed(data) { index, catatan ->
                if (index % 2 == 0) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        NomorKamarCard(catatan = catatan)
                        // Check if there's another item in the pair
                        if (index + 1 < data.size) {
                            Spacer(modifier = Modifier.width(10.dp))
                            NomorKamarCard(catatan = data[index + 1])
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun NomorKamarCard(catatan: Catatan) {
    // Hanya menampilkan nomor kamar
    Card(
        modifier = Modifier
            .size(150.dp) // Make the box smaller
            .background(Color.Green)
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Kamar: ${catatan.nomorkamar}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun LihatScreenPreview() {
    KosKuTheme {
        LihatScreen(rememberNavController())
    }
}
