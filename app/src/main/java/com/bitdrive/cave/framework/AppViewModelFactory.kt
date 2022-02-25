package com.bitdrive.cave.framework

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object AppViewModelFactory : ViewModelProvider.Factory {

    lateinit var application: Application
    lateinit var dependencies: Interactors

    fun inject(application: Application, dependencies: Interactors) {
        AppViewModelFactory.application = application
        AppViewModelFactory.dependencies = dependencies
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (AppViewModel::class.java.isAssignableFrom(modelClass)) {
            modelClass.getConstructor(Application::class.java, Interactors::class.java)
                .newInstance(application, dependencies)
        } else {
            throw IllegalStateException("ViewModel must extend AppViewModel")
        }
    }
}