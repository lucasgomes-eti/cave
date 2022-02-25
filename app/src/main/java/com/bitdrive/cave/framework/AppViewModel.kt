package com.bitdrive.cave.framework

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.bitdrive.cave.App

open class AppViewModel(application: Application, protected val interactors: Interactors) :
    AndroidViewModel(application) {
        protected val application: App = getApplication()
}