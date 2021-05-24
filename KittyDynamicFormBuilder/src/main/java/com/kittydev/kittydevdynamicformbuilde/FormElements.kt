package com.kittydev.kittydevdynamicformbuilde

import com.kittydev.kittydevdynamicformbuilde.Model.attributeDM

/**
 * Created by PadmaDev on 07/10/2020
 */
class FormElements : FormObject() {
    lateinit var attributes: attributeDM
    var attributes_2: attributeDM? = null
    var attributes_3: attributeDM? = null

    fun setArguments(a1: attributeDM, a2: attributeDM? = null,a3: attributeDM?=null): FormElements {
        this.attributes = a1
        this.attributes_2 = a2
        this.attributes_3= a3
        return this
    }


    val checkedValue: Int
        get() {
            attributes.selectedOptions.let {
                val element = it?.get(0)
                if (attributes.options!!.contains(element)) {
                    return attributes.options!!.indexOf(element)
                }
            }
            return 0
        }

    enum class Type {
        TEXT, SELECT, SLIDER, RATING, MULTISELECT, TWO_INPUT, THREE_INPUT
    }
}