package com.kittydev.kittydevdynamicformbuilde

import com.kittydev.kittydevdynamicformbuilde.Model.attributeDM

/**
 * Created by PadmaDev on 07/10/2020
 */
class KittyElements : KittyObject() {
    lateinit var attributes: attributeDM
    fun setArguments(arr: attributeDM): KittyElements {
        this.attributes = arr
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
        TEXT, SELECT, SLIDER, RATING, MULTISELECT
    }
}