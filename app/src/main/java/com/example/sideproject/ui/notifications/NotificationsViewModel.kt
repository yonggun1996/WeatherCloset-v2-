package com.example.sideproject.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {
    var i = 1;

    private val _text = MutableLiveData<String>().apply {
        if (i == 1){
            value = "남"
        }else{
            value = "여"
        }
        //value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}