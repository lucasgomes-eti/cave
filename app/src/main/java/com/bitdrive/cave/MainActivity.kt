package com.bitdrive.cave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.bitdrive.cave.framework.AppViewModelFactory
import com.bitdrive.cave.ui.view.MainPageScaffold
import com.bitdrive.cave.ui.viewmodel.AlarmsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainPageScaffold() }
    }
}

