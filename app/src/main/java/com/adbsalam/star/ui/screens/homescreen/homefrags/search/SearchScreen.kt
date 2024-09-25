package com.adbsalam.star.ui.screens.homescreen.homefrags.search

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adbsalam.star.ui.theme.Adb_Salam_StarTheme_Fullscreen_Dark
import com.adbsalam.star.ui.uiutil.AppSearchTextView
import com.adbsalam.star.ui.uiutil.recycleritems.movieSearchItem
import com.adbsalam.star.ui.uiutil.uidatamodels.AppSearchText
import com.adbsalam.star.ui.uiutil.uidatamodels.TextViewModel


var appSearchText = mutableStateOf(AppSearchText())

@Composable
fun SearchScreen() {

    Adb_Salam_StarTheme_Fullscreen_Dark(isBottomBarTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(darkColorScheme().surface)
                .statusBarsPadding()
        ){
            AppSearchTextView()
            List(10){
              movieSearchItem()
            }
        }
    }
}


@Preview
@Composable
fun showPreview(){
    SearchScreen()
}
