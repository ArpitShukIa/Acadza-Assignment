package com.arpit.acadzaassignment.util

import android.app.Activity
import androidx.fragment.app.Fragment
import com.arpit.acadzaassignment.Application
import com.arpit.acadzaassignment.viewmodels.ViewModelFactory

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val application = (requireActivity().application as Application)
    return ViewModelFactory(application)
}

fun Activity.getViewModelFactory(): ViewModelFactory {
    val application = application as Application
    return ViewModelFactory(application)
}