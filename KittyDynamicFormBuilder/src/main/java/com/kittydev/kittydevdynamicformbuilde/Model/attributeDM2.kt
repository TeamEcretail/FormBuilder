package com.kittydev.kittydevdynamicformbuilde.Model


import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import com.kittydev.kittydevdynamicformbuilde.FormElements

data class attributeDM2(
    val tag1: String,
    val tag2: String,
    val type1: FormElements.Type,
    val type2: FormElements.Type,
    val hint1: String? = null,
    val hint2: String? = null,
    val value1:  MutableLiveData<String>? = null,
    val value2:  MutableLiveData<String>? = null,
    val heading: String? = null,
    val subHeading: String? = null,
    val options1: List<String>? = listOf(),
    val options2: List<String>? = listOf(),
    var selectedOptions1: List<String>? = listOf(),
    var selectedOptions2: List<String>? = listOf(),
    var isRequired1: Boolean = false,
    var isRequired2: Boolean = false,
    var isRefreshBtn1: Boolean = false,
    var isRefreshBtn2: Boolean = false,
    var isEnabled1: Boolean = true,
    var isEnabled2: Boolean = true,
    val params1: LinearLayout.LayoutParams? = null,
    val params2: LinearLayout.LayoutParams? = null,
    val valueListener1: MutableLiveData<String>? = null,
    val valueListener2: MutableLiveData<String>? = null,
    val refreshListener1: MutableLiveData<String>? = null,
    val refreshListener2: MutableLiveData<String>? = null,
    val drawable1: Int ?=0,
    val drawable2: Int ?=0
)