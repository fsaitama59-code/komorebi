package com.komorebi.reader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.komorebi.reader.ui.components.GlassComponents.GlassNavigationBar
import com.komorebi.reader.ui.navigation.BottomNavItems
import com.komorebi.reader.ui.navigation.KomorebiNavHost
import com.komorebi.reader.ui.theme.KomorebiTheme
import com.komorebi.reader.ui.theme.komorebiColors
import com.komorebi.reader.ui.theme.komorebiSpacing
import com.komorebi.reader.ui.theme.komorebiShapes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KomorebiTheme {
                KomorebiApp()
            }
        }
    }
}

@Composable
fun KomorebiApp() {
    var selectedIndex by remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = komorebiColors().surfaceBase
    ) {
        androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize()) {
            // Main navigation content
            KomorebiNavHost()

            // Floating bottom navigation bar
            GlassNavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = komorebiSpacing().ScreenPadding + 20.dp, horizontal = komorebiSpacing().ScreenPadding)
                    .align(Alignment.BottomCenter),
                items = BottomNavItems,
                selectedIndex = selectedIndex,
                onItemClick = { selectedIndex = it }
            )
        }
    }
}