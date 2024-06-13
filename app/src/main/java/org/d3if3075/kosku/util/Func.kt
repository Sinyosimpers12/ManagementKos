package org.d3if3075.kosku.util
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
                text = "1. Nama Penghuni dengan nama lengkap.",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "2. Isi Nomor kamar.",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "3. Pastikan kedua kolom sesui sebelum menyimpan.",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "4. Klik Untuk Masuk Ke bagian Detail.",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "5. Silakan Edit Dan Tambah Catatan Jika Ingin." +
                        "" +
                        " ",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "6. Klik Titik Tiga Untuk Melihat Menu Hapus Dan Lainnya" +
                        "" +
                        " " +"" +
                        " " +"" +
                        " " +
                " ",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Copyright ®©  Martin, Samuel, Fuad",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Button(
                onClick = onCloseDialog,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Text("Mengerti")
            }
        }
    }
}





