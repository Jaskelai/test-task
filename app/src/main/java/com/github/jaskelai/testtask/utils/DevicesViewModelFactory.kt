package com.github.jaskelai.testtask.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.jaskelai.testtask.domain.DevicesInteractor
import com.github.jaskelai.testtask.presentation.DevicesViewModel

class DevicesViewModelFactory(private val devicesInteractor: DevicesInteractor) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DevicesViewModel(devicesInteractor) as T
    }
}
