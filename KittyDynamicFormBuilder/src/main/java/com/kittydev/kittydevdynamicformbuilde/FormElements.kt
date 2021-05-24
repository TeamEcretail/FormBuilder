package com.kittydev.kittydevdynamicformbuilde

import com.kittydev.kittydevdynamicformbuilde.Model.attributeDM
import com.kittydev.kittydevdynamicformbuilde.Model.attributeDM2

/**
 * Created by PadmaDev on 07/10/2020
 */
class FormElements : FormObject() {
    lateinit var attributes: attributeDM
    lateinit var attributes_2input: attributeDM2
    fun setArguments(arr: attributeDM): FormElements {
        this.attributes = arr
        return this
    }

    fun set2Arguments(arr: attributeDM2): FormElements {
        this.attributes_2input = arr
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