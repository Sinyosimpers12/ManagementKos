package org.d3if3075.kosku.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

import org.d3if3075.kosku.model.Kos

@Composable
fun KosList(kosList: List<Kos>) {
    LazyColumn {
        items(kosList) { kos ->
            KosItem(kos = kos)
        }
    }
}


