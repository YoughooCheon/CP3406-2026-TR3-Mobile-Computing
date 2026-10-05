package com.example.cp3406_assessment_younghoocheon

import android.app.Application
import com.example.cp3406_assessment_younghoocheon.di.AppContainer
import com.example.cp3406_assessment_younghoocheon.di.DefaultAppContainer

/** Application class: owns the dependency container so ViewModels can reach the repository. */
class UtilityApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
