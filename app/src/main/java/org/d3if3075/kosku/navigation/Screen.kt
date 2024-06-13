package org.d3if3075.kosku.navigation

import org.d3if3075.kosku.screen.KEY_ID_CATATAN


sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object Lihat: Screen("lihatScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_CATATAN}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}