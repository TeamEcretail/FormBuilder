package com.kittydev.kittydevdynamicformbuilde.Model


import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import com.kittydev.kittydevdynamicformbuilde.FormElements

data class attributeDM(
    val tag: String,
    val type: FormElements.Type,
    val hint: String? = null,
    val value: String? = null,
    val heading: String? = null,
    val subHeading: String? = null,
    val options: List<String>? = listOf(),
    var selectedOptions: List<String>? = listOf(),
    var isRequired: Boolean = false,
    var isRefreshBtn: Boolean = false,
    var isEnabled: Boolean = true,
    val params: LinearLayout.LayoutParams? = null,
    val valueListener: MutableLiveData<String>? = null,
    val refreshListener: MutableLiveData<String>? = null,
    val drawable: Int ?=0
)