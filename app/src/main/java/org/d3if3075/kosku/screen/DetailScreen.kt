package org.d3if3075.kosku.screen

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import org.d3if3075.kosku.R
import org.d3if3075.kosku.theme.KosKuTheme
import org.d3if3075.kosku.database.CatatanDb
import org.d3if3075.kosku.model.CatatanImage
import org.d3if3075.kosku.util.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

const val KEY_ID_CATATAN = "idCatatan"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = CatatanDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var judul by remember { mutableStateOf("") }
    var catatan by remember { mutableStateOf("") }
    var nomorkamar by remember { mutableIntStateOf(0) }
    var imageUrl by remember { mutableStateOf("") }
    var jenisKelamin by remember { mutableStateOf("Pria") } // Default to "Pria"
    var tanggalKeluar by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(id) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getCatatan(id) ?: return@LaunchedEffect
        judul = data.judul
        catatan = data.catatan
        nomorkamar = data.nomorkamar
        jenisKelamin = data.jenisKelamin
        tanggalKeluar = data.tanggalKeluar
        val images = viewModel.getCatatanImages(id)
        if (images.isNotEmpty()) {
            imageUrl = images.first().imageUrl
        }

        Log.d("ImageData", "Images: $images")
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedUri ->

            coroutineScope.launch {
                val catatanImage = CatatanImage(catatanId = id ?: 0, imageUrl = selectedUri.toString())
                viewModel.insertCatatanImage(catatanImage)

                imageUrl = selectedUri.toString()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_catatan))
                    else
                        Text(text = stringResource(id = R.string.edit_catatan))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        if (id == null) {
                            // Validation checks only for insert operation
                            if (judul.isBlank() || catatan.isBlank() || nomorkamar == 0) {
                                Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                                return@IconButton
                            }
                            coroutineScope.launch {
                                if (!viewModel.isRoomNumberValid(nomorkamar)) {
                                    Toast.makeText(context, R.string.invalid_room_number, Toast.LENGTH_LONG).show()
                                    return@launch
                                }
                                viewModel.insert(judul, catatan, nomorkamar, jenisKelamin, tanggalKeluar)
                                navController.popBackStack()
                            }
                        } else {
                            // No validation checks for update operation
                            coroutineScope.launch {
                                viewModel.update(id, judul, catatan, nomorkamar, jenisKelamin, tanggalKeluar)
                                navController.popBackStack()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (id != null) {
                        DeleteAction(
                            delete = {
                                viewModel.delete(id)
                                navController.popBackStack()
                            },
                            addPhoto = {
                                imagePickerLauncher.launch("image/*")
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FormCatatan(
                    title = judul,
                    onTitleChange = { judul = it },
                    desc = catatan,
                    onDescChange = { catatan = it },
                    nomorKamar = nomorkamar.toString(),
                    onNomorKamarChange = { nomorkamar = it.toIntOrNull() ?: 0 },
                    imageUrl = imageUrl,
                    onImageClick = { imagePickerLauncher.launch("image/*") },
                    jenisKelamin = jenisKelamin,
                    onJenisKelaminChange = { jenisKelamin = it },
                    tanggalKeluar = tanggalKeluar,
                    onTanggalKeluarChange = { tanggalKeluar = it }
                )
            }
        }
    }
}


@Composable
fun FormCatatan(
    title: String,
    onTitleChange: (String) -> Unit,
    desc: String,
    onDescChange: (String) -> Unit,
    nomorKamar: String,
    onNomorKamarChange: (String) -> Unit,
    imageUrl: String,
    onImageClick: () -> Unit,
    jenisKelamin: String,
    onJenisKelaminChange: (String) -> Unit,
    tanggalKeluar: String,
    onTanggalKeluarChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(R.string.judul)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = nomorKamar,
            onValueChange = { onNomorKamarChange(it) },
            label = { Text(text = stringResource(R.string.nomor_kamar)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = desc,
            onValueChange = { onDescChange(it) },
            label = { Text(text = stringResource(R.string.isi_catatan)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth().height(150.dp)
        )

        // Radio buttons for Jenis Kelamin
        val jenisKelaminOptions = listOf("Pria", "Wanita")
        RadioGroup(
            options = jenisKelaminOptions,
            selectedOption = jenisKelamin,
            onSelectedOptionChange = { onJenisKelaminChange(it) }
        )

        // Date picker for Tanggal Keluar
        DatePicker(
            selectedDate = tanggalKeluar,
            onDateChange = { onTanggalKeluarChange(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LoadImageFromLocalUri(imageUrl)

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun RadioGroup(
    options: List<String>,
    selectedOption: String,
    onSelectedOptionChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        options.forEach { text ->
            Row(
                modifier = Modifier
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onSelectedOptionChange(text) }
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onSelectedOptionChange(text) },
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = text)
            }
        }
    }
}


@Composable
fun DatePicker(
    selectedDate: String,
    onDateChange: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            Text(text = "Pilih Tanggal Keluar")
        }

        if (showDialog) {
            DateSelector(selectedDate = selectedDate) { newDate ->
                onDateChange(newDate)
                showDialog = false
            }
        }
    }
}

@Composable
fun DateSelector(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val today = Calendar.getInstance()
    val dates = mutableListOf<Calendar>()

    // Populate the list with dates for the next 30 days
    repeat(30) {
        dates.add(today.clone() as Calendar)
        today.add(Calendar.DAY_OF_YEAR, 1)
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth().height(200.dp) // Added height to avoid infinite height issue
    ) {
        itemsIndexed(dates) { index, calendar ->
            val dateFormatted = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)
            DateItem(
                date = dateFormatted,
                isSelected = dateFormatted == selectedDate,
                onClick = {
                    onDateSelected(dateFormatted)
                }
            )
        }
    }
}

@Composable
fun DateItem(
    date: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = onClick) // Added clickable modifier
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
            .height(56.dp)
    ) {
        Text(
            text = date,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            ),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun LoadImageFromLocalUri(imageUrl: String) {
    if (imageUrl.isNotEmpty()) {
        Image(
            painter = rememberImagePainter(data = imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp)
        )
    }
}

@Composable
fun DeleteAction(delete: () -> Unit, addPhoto: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.hapus))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.tambah_foto))
                },
                onClick = {
                    expanded = false
                    addPhoto()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    KosKuTheme {
        DetailScreen(rememberNavController())
    }
}
