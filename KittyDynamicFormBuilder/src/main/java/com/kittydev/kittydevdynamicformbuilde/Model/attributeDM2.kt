package com.kittydev.kittydevdynamicformbuilde.Model


import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import com.kittydev.kittydevdynamicformbuilde.FormElements

data class attributeDM2(
    val tag: String,
    val tag2: String,
    val type: FormElements.Type,
    val type2: FormElements.Type,
    val hint: String? = null,
    val hint2: String? = null,
    val value:  MutableLiveData<String>? = null,
    val value2:  MutableLiveData<String>? = null,
    val heading: String? = null,
    val subHeading: String? = null,
    val options: List<String>? = listOf(),
    val options2: List<String>? = listOf(),
    var selectedOptions: List<String>? = listOf(),
    var selectedOptions2: List<String>? = listOf(),
    var isRequired: Boolean = false,
    var isRequired2: Boolean = false,
    var isRefreshBtn: Boolean = false,
    var isRefreshBtn2: Boolean = false,
    var isEnabled: Boolean = true,
    var isEnabled2: Boolean = true,
    val params: LinearLayout.LayoutParams? = null,
    val params2: LinearLayout.LayoutParams? = null,
    val valueListener: MutableLiveData<String>? = null,
    val valueListener2: MutableLiveData<String>? = null,
    val refreshListener: MutableLiveData<String>? = null,
    val refreshListener2: MutableLiveData<String>? = null,
    val drawable: Int ?=0,
    val drawable2: Int ?=0
)