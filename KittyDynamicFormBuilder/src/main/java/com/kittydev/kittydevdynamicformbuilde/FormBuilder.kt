package com.kittydev.kittydevdynamicformbuilde

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*


class FormBuilder(
    private var context: Context,
    private var linearLayout: LinearLayout,
) {
    private var formMap: LinkedHashMap<String, FormElements> = LinkedHashMap()
    private var viewMap: LinkedHashMap<String, View> = LinkedHashMap()
    private var selectedEditText: EditText? = null

    fun build(formObjects: List<FormObject?>) {
        for (formObject in formObjects) if (formObject is FormElements) {

            when {
                formObject.attributes_2 != null -> {
                    formObject.attributes_2?.let {
                        formMap[it.tag] = formObject
                        addToLinearLayout(buildElement(formObject), formObject.attributes_2!!.params)
                    }
                }
                formObject.attributes_3 != null -> {
                    formObject.attributes_3?.let {
                        formMap[it.tag] = formObject
                        addToLinearLayout(buildElement(formObject), formObject.attributes_3!!.params)
                    }
                }
                else -> {
                    val tag = formObject.attributes.tag
                    formMap[tag] = formObject
                    addToLinearLayout(buildElement(formObject), formObject.attributes.params)
                }
            }


        } else if (formObject is FormButton) {
            addToLinearLayout(buildButton(formObject), formObject.params)
        }
    }

    private fun buildElement(formElements: FormElements): View? {
        return when (formElements.attributes.type) {
            FormElements.Type.TEXT -> {
                val InputTextView = LayoutInflater.from(context).inflate(R.layout.input, null)
                val InputTextLayout = TextInputLayout(context, null, R.id.input_et_layout)
                val InputTextLayoutHolder: TextInputLayout =
                    InputTextView.findViewById(R.id.input_et_layout)
                val InputTextEditText: TextInputEditText = InputTextView.findViewById(R.id.input_et)
                val InputTextHeading = InputTextView.findViewById<TextView>(R.id.input_et_Heading)
                val InputTextSubHeading =
                    InputTextView.findViewById<TextView>(R.id.input_et_SubHeading)
                val InputTextSpacer = InputTextView.findViewById<View>(R.id.input_et_spacer)
                val RightRefreshBtn = InputTextView.findViewById<View>(R.id.image)
                val InputTextSubHeadingHolder =
                    InputTextView.findViewById<LinearLayout>(R.id.subHeadingHolder)
                formElements.attributes.subHeading?.let {
                    InputTextSpacer.visibility = View.VISIBLE
                    InputTextSubHeadingHolder.visibility = View.VISIBLE
                    InputTextSubHeading.text = formElements.attributes.subHeading
                } ?: run {
                    InputTextSpacer.visibility = View.GONE
                    InputTextSubHeadingHolder.visibility = View.GONE
                }
                formElements.attributes.heading?.let {
                    InputTextHeading.visibility = View.VISIBLE
                    InputTextHeading.text = formElements.attributes.heading
                } ?: run {
                    InputTextHeading.visibility = View.GONE
                }

                formElements.attributes.valueListener?.let { it ->
                    InputTextEditText.doAfterTextChanged { it1 ->
                        if (!it1.isNullOrEmpty()) {
                            it.value = it1.toString()
                        }
                    }
                }

                if (formElements.attributes.isRefreshBtn) {
                    RightRefreshBtn.visibility = View.VISIBLE
                    formElements.attributes.drawable?.let {
                        RightRefreshBtn.setBackgroundResource(it)
                    }
                } else {
                    RightRefreshBtn.visibility = View.GONE
                }

                RightRefreshBtn.setOnClickListener {
                    formElements.attributes.refreshListener?.let {
                        it.value = "${Date().time}"
                    }
                }

                InputTextLayoutHolder.hint = formElements.attributes.hint
                InputTextEditText.isEnabled = formElements.attributes.isEnabled
                formElements.attributes.value?.observeForever {
                    InputTextEditText.setText(it)
                }

                InputTextEditText.inputType = InputType.TYPE_CLASS_TEXT
                viewMap[formElements.attributes.tag] = InputTextEditText
                addViewToView(InputTextLayout, InputTextView)
                InputTextLayout

            }

            FormElements.Type.SELECT -> {
                val DropDownView = LayoutInflater.from(context).inflate(R.layout.input, null)
                val DropDownLayout = TextInputLayout(context, null, R.id.input_et_layout)
                val DropDownLayoutHolder: TextInputLayout =
                    DropDownView.findViewById(R.id.input_et_layout)
                val DropDownEditText: TextInputEditText = DropDownView.findViewById(R.id.input_et)
                val DropDownHeading = DropDownView.findViewById<TextView>(R.id.input_et_Heading)
                val DropDownSubHeading =
                    DropDownView.findViewById<TextView>(R.id.input_et_SubHeading)
                val DropDownSpacer = DropDownView.findViewById<View>(R.id.input_et_spacer)
                val DropDownSubHeadingHolder =
                    DropDownView.findViewById<LinearLayout>(R.id.subHeadingHolder)
                DropDownLayoutHolder.hint = formElements.attributes.hint
                DropDownEditText.isEnabled = formElements.attributes.isEnabled

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
                    DropDownEditText.showSoftInputOnFocus = false
                } else { // API 11-20
                    DropDownEditText.setTextIsSelectable(true)
                }

                DropDownEditText.inputType = InputType.TYPE_CLASS_TEXT
                DropDownEditText.setText(
                    formElements.attributes.selectedOptions.toString().replace("[", "")
                        .replace("]", "")
                )
                DropDownEditText.setOnClickListener {
                    val inputManager =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(DropDownEditText.windowToken, 0)
                    selectDialog(DropDownEditText, formElements)
                }

                DropDownEditText.doAfterTextChanged {
                    if (!it.isNullOrEmpty()) {
                        formElements.attributes.valueListener!!.value = it.toString()
                    }
                }

                formElements.attributes.subHeading?.let {
                    DropDownSpacer.visibility = View.VISIBLE
                    DropDownSubHeadingHolder.visibility = View.VISIBLE
                    DropDownSubHeading.text = formElements.attributes.subHeading
                } ?: run {
                    DropDownSpacer.visibility = View.GONE
                    DropDownSubHeadingHolder.visibility = View.GONE
                }

                formElements.attributes.heading?.let {
                    DropDownHeading.visibility = View.VISIBLE
                    DropDownHeading.text = formElements.attributes.heading
                } ?: run {
                    DropDownHeading.visibility = View.GONE
                }

                viewMap[formElements.attributes.tag] = DropDownEditText
                addViewToView(DropDownLayout, DropDownView)
                DropDownLayout
            }

            FormElements.Type.TWO_INPUT -> {
                val InputTextView = LayoutInflater.from(context).inflate(R.layout.two_input, null)
                val InputTextLayout = TextInputLayout(context, null, R.id.input_et_layout1)
                val InputTextLayoutHolder: TextInputLayout =
                    InputTextView.findViewById(R.id.input_et_layout1)
                val InputTextLayoutHolder2: TextInputLayout =
                    InputTextView.findViewById(R.id.input_et_layout2)
                val InputTextEditText1: TextInputEditText =
                    InputTextView.findViewById(R.id.input_et1)
                val InputTextEditText2: TextInputEditText =
                    InputTextView.findViewById(R.id.input_et2)
                val InputTextHeading = InputTextView.findViewById<TextView>(R.id.input_et_Heading)
                val InputTextSubHeading =
                    InputTextView.findViewById<TextView>(R.id.input_et_SubHeading)
                val InputTextSpacer = InputTextView.findViewById<View>(R.id.input_et_spacer)
                val RightRefreshBtn1 = InputTextView.findViewById<View>(R.id.image1)
                val RightRefreshBtn2 = InputTextView.findViewById<View>(R.id.image2)
                val InputTextSubHeadingHolder =
                    InputTextView.findViewById<LinearLayout>(R.id.subHeadingHolder)

                formElements.attributes.subHeading?.let {
                    InputTextSpacer.visibility = View.VISIBLE
                    InputTextSubHeadingHolder.visibility = View.VISIBLE
                    InputTextSubHeading.text = formElements.attributes.subHeading
                } ?: run {
                    InputTextSpacer.visibility = View.GONE
                    InputTextSubHeadingHolder.visibility = View.GONE
                }

                formElements.attributes_2?.let {
                    it.heading?.let {
                        InputTextHeading.visibility = View.VISIBLE
                        InputTextHeading.text = formElements.attributes_2!!.heading
                    } ?: run {
                        InputTextHeading.visibility = View.GONE
                    }
                }

                formElements.attributes.valueListener?.let { it ->
                    InputTextEditText1.doAfterTextChanged { it1 ->
                        if (!it1.isNullOrEmpty()) {
                            it.value = it1.toString()
                        }
                    }
                }

                formElements.attributes_2?.let { ita ->
                    ita.valueListener?.let { it ->
                        InputTextEditText2.doAfterTextChanged { it1 ->
                            if (!it1.isNullOrEmpty()) {
                                it.value = it1.toString()
                            }
                        }
                    }
                }

                if (formElements.attributes.isRefreshBtn) {
                    RightRefreshBtn1.visibility = View.VISIBLE
                    formElements.attributes.drawable?.let {
                        RightRefreshBtn1.setBackgroundResource(it)
                    }
                } else {
                    RightRefreshBtn1.visibility = View.GONE
                }
                RightRefreshBtn1.setOnClickListener {
                    formElements.attributes.refreshListener?.let {
                        it.value = "${Date().time}"
                    }
                }

                formElements.attributes_2?.let { ita ->
                    if (ita.isRefreshBtn) {
                        RightRefreshBtn2.visibility = View.VISIBLE
                        formElements.attributes_2!!.drawable?.let {
                            RightRefreshBtn2.setBackgroundResource(it)
                        }
                    } else {
                        RightRefreshBtn2.visibility = View.GONE
                    }
                }

                RightRefreshBtn2.setOnClickListener {
                    formElements.attributes_2!!.refreshListener?.let {
                        it.value = "${Date().time}"
                    }
                }


                InputTextLayoutHolder.hint = formElements.attributes.hint

                InputTextEditText1.isEnabled = formElements.attributes.isEnabled


                formElements.attributes.value?.observeForever {
                    InputTextEditText1.setText(it)
                }
                formElements.attributes_2?.let { ita ->
                    InputTextLayoutHolder2.hint = ita.hint
                    InputTextEditText2.isEnabled = ita.isEnabled
                    ita.value?.observeForever {
                        InputTextEditText2.setText(it)
                    }
                    viewMap[ita.tag] = InputTextEditText2
                }
                InputTextEditText1.inputType = InputType.TYPE_CLASS_TEXT
                InputTextEditText2.inputType = InputType.TYPE_CLASS_TEXT
                viewMap[formElements.attributes.tag] = InputTextEditText1
                addViewToView(InputTextLayout, InputTextView)
                InputTextLayout
            }

            FormElements.Type.THREE_INPUT -> {
                val InputTextView = LayoutInflater.from(context).inflate(R.layout.three_input, null)
                val InputTextLayout = TextInputLayout(context, null, R.id.input_et_layout1)
                val InputTextLayoutHolder: TextInputLayout =
                    InputTextView.findViewById(R.id.input_et_layout1)
                val InputTextLayoutHolder2: TextInputLayout =
                    InputTextView.findViewById(R.id.input_et_layout2)
                val InputTextLayoutHolder3: TextInputLayout =
                    InputTextView.findViewById(R.id.input_et_layout3)
                val InputTextEditText1: TextInputEditText =
                    InputTextView.findViewById(R.id.input_et1)
                val InputTextEditText2: TextInputEditText =
                    InputTextView.findViewById(R.id.input_et2)
                val InputTextEditText3: TextInputEditText =
                    InputTextView.findViewById(R.id.input_et3)
                val InputTextHeading = InputTextView.findViewById<TextView>(R.id.input_et_Heading)
                val InputTextSubHeading =
                    InputTextView.findViewById<TextView>(R.id.input_et_SubHeading)
                val InputTextSpacer = InputTextView.findViewById<View>(R.id.input_et_spacer)

                val InputTextSubHeadingHolder =
                    InputTextView.findViewById<LinearLayout>(R.id.subHeadingHolder)

                formElements.attributes.subHeading?.let {
                    InputTextSpacer.visibility = View.VISIBLE
                    InputTextSubHeadingHolder.visibility = View.VISIBLE
                    InputTextSubHeading.text = formElements.attributes.subHeading
                } ?: run {
                    InputTextSpacer.visibility = View.GONE
                    InputTextSubHeadingHolder.visibility = View.GONE
                }

                formElements.attributes_2?.let {
                    it.heading?.let {
                        InputTextHeading.visibility = View.VISIBLE
                        InputTextHeading.text = formElements.attributes_2!!.heading
                    } ?: run {
                        InputTextHeading.visibility = View.GONE
                    }
                }
                formElements.attributes_3?.let {
                    it.heading?.let {
                        InputTextHeading.visibility = View.VISIBLE
                        InputTextHeading.text = formElements.attributes_3!!.heading
                    } ?: run {
                        InputTextHeading.visibility = View.GONE
                    }
                }
                formElements.attributes.valueListener?.let { it ->
                    InputTextEditText1.doAfterTextChanged { it1 ->
                        if (!it1.isNullOrEmpty()) {
                            it.value = it1.toString()
                        }
                    }
                }

                formElements.attributes_2?.let { ita ->
                    ita.valueListener?.let { it ->
                        InputTextEditText2.doAfterTextChanged { it1 ->
                            if (!it1.isNullOrEmpty()) {
                                it.value = it1.toString()
                            }
                        }
                    }
                }
                formElements.attributes_3?.let { ita ->
                    ita.valueListener?.let { it ->
                        InputTextEditText3.doAfterTextChanged { it1 ->
                            if (!it1.isNullOrEmpty()) {
                                it.value = it1.toString()
                            }
                        }
                    }
                }

                InputTextLayoutHolder.hint = formElements.attributes.hint

                InputTextEditText1.isEnabled = formElements.attributes.isEnabled


                formElements.attributes.value?.observeForever {
                    InputTextEditText1.setText(it)
                }

                formElements.attributes_2?.let { ita ->
                    InputTextLayoutHolder2.hint = ita.hint
                    InputTextEditText2.isEnabled = ita.isEnabled
                    ita.value?.observeForever {
                        InputTextEditText2.setText(it)
                    }
                    viewMap[ita.tag] = InputTextEditText2
                }
                formElements.attributes_3?.let { ita ->
                    InputTextLayoutHolder3.hint = ita.hint
                    InputTextEditText3.isEnabled = ita.isEnabled
                    ita.value?.observeForever {
                        InputTextEditText3.setText(it)
                    }
                    viewMap[ita.tag] = InputTextEditText2
                }
                InputTextEditText1.inputType = InputType.TYPE_CLASS_TEXT
                InputTextEditText2.inputType = InputType.TYPE_CLASS_TEXT
                InputTextEditText3.inputType = InputType.TYPE_CLASS_TEXT

                viewMap[formElements.attributes.tag] = InputTextEditText1
                addViewToView(InputTextLayout, InputTextView)
                InputTextLayout
            }

            FormElements.Type.SLIDER -> {
                val SliderView = LayoutInflater.from(context).inflate(R.layout.slider, null)
                val Slider: Slider = SliderView.findViewById(R.id.Slider)
                val SliderLayout = TextInputLayout(context, null, R.id.Slider_Layout)
                val SliderHeading = SliderView.findViewById<TextView>(R.id.Slide_Heading)
                val SliderSubHeading = SliderView.findViewById<TextView>(R.id.Slider_SubHeading)
                val SliderSpacer = SliderView.findViewById<View>(R.id.Slider_Spacer)
                val SliderSubHeadingHolder =
                    SliderView.findViewById<LinearLayout>(R.id.Slider_SubHeading_Holder)
                if (formElements.attributes.subHeading!!.isNotEmpty()) {
                    SliderSpacer.visibility = View.VISIBLE
                    SliderSubHeadingHolder.visibility = View.VISIBLE
                    SliderSubHeading.text = formElements.attributes.subHeading
                } else {
                    SliderSpacer.visibility = View.GONE
                    SliderSubHeadingHolder.visibility = View.GONE
                }
                if (formElements.attributes.heading!!.isNotEmpty()) {
                    SliderHeading.visibility = View.VISIBLE
                    SliderHeading.text = formElements.attributes.heading
                } else {
                    SliderHeading.visibility = View.GONE
                }
                addViewToView(SliderLayout, SliderView)
                viewMap[formElements.attributes.tag] = Slider
                SliderLayout
            }

            FormElements.Type.RATING -> {
                val RatingView = LayoutInflater.from(context).inflate(R.layout.rating, null)
                val Rating = RatingView.findViewById<RatingBar>(R.id.Rating)
                val RatingLayout = TextInputLayout(context, null, R.id.RatingLayout)
                val RatingSubHeading = RatingView.findViewById<TextView>(R.id.Rating_SubHeading)
                val RatingHeading = RatingView.findViewById<TextView>(R.id.Rating_Heading)
                val RatingSpacer = RatingView.findViewById<View>(R.id.Rating_Spacer)
                val RatingSubHeadingHolder =
                    RatingView.findViewById<LinearLayout>(R.id.Rating_SubHeading_Holder)

                formElements.attributes.subHeading?.let {
                    RatingSpacer.visibility = View.VISIBLE
                    RatingSubHeadingHolder.visibility = View.VISIBLE
                    RatingSubHeading.text = formElements.attributes.subHeading
                } ?: run {
                    RatingSpacer.visibility = View.GONE
                    RatingSubHeadingHolder.visibility = View.GONE
                }
                formElements.attributes.heading?.let {
                    RatingHeading.visibility = View.VISIBLE
                    RatingHeading.text = formElements.attributes.heading
                } ?: run {
                    RatingHeading.visibility = View.GONE
                }
                viewMap[formElements.attributes.tag] = Rating
                addViewToView(RatingLayout, RatingView)
                RatingLayout
            }
            else -> null
        }
    }

    private fun buildButton(formButton: FormButton): View {
        val button = Button(context)

        button.text = formButton.title

        if (formButton.backgroundColor != null) {
            button.setBackgroundColor(formButton.backgroundColor!!)
        }
        if (formButton.textColor != null) {
            button.setTextColor(formButton.textColor!!)
        }
        if (formButton.runnable != null) {
            button.setOnClickListener { formButton.runnable!!.run() }
        }
        return button
    }

    private fun addToLinearLayout(view: View?, params: LinearLayout.LayoutParams?) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 8, 8, 8)
        //view!!.layoutParams = params ?: layoutParams
        view?.let {
            it.layoutParams = params ?: layoutParams
            linearLayout.addView(view)
        }

    }

    private fun addViewToView(parent: ViewGroup, child: View) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        child.layoutParams = layoutParams
        parent.addView(child)
    }

    private fun selectDialog(selectedEditText: EditText, selectedFormElements: FormElements) {
        var selectedElements: ArrayList<String> = ArrayList(listOf(""))
        selectedFormElements.attributes.selectedOptions?.let {
            selectedElements = ArrayList(it)
        }

        this.selectedEditText = selectedEditText
        val builder = AlertDialog.Builder(context)

        builder.setTitle(selectedFormElements.attributes.hint)

        builder.setSingleChoiceItems(
            selectedFormElements.attributes.options!!.toTypedArray<CharSequence>(),
            selectedFormElements.checkedValue,
            null
        )

        builder.setPositiveButton("OK") { dialogInterface, _ ->
            dialogInterface.dismiss()
            val selectedPosition = (dialogInterface as AlertDialog).listView.checkedItemPosition
            selectedElements.clear() //We only want one input
            selectedElements.add(selectedFormElements.attributes.options!![selectedPosition])
            selectedFormElements.attributes.selectedOptions = selectedElements
            selectedEditText.setText(
                selectedFormElements.attributes.selectedOptions.toString().replace("[", "")
                    .replace("]", "")
            )
        }
        builder.setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
        builder.show()
    }

    private fun multiSelectDialog(
        selectedEditText: EditText,
        selectedFormElements: FormElements
    ) {

        var selectedElements: ArrayList<String> = ArrayList(listOf(""))

        selectedFormElements.attributes.selectedOptions?.let {
            selectedElements = ArrayList(it)
        }

        this.selectedEditText = selectedEditText
        val builder = AlertDialog.Builder(context)
        builder.setTitle(selectedFormElements.attributes.hint)

        val options = selectedFormElements.attributes.options!!.toTypedArray()

        val arrayChecked = selectedFormElements.attributes.selectedOptions?.let {
            getSelected(
                selectedFormElements.attributes.options!!,
                it
            )
        }

        builder.setMultiChoiceItems(
            options,
            arrayChecked
        ) { dialog, which, isChecked -> arrayChecked?.set(which, isChecked) }


        builder.setPositiveButton("OK") { dialogInterface, _ ->
            dialogInterface.dismiss()
            selectedElements.clear()

            arrayChecked?.forEachIndexed { i, it ->

                Log.e("adad", "$i     $it")

                if (it) {
                    selectedElements.add(selectedFormElements.attributes.options!![i])
                }
            }

            selectedFormElements.attributes.selectedOptions = selectedElements.toList()

            Log.e("akdjah", selectedFormElements.attributes.selectedOptions.toString())

            selectedEditText.setText(


                selectedFormElements.attributes.selectedOptions.toString().replace("[", "")
                    .replace("]", "")
            )
        }

        builder.setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
        builder.show()
    }

    private fun getSelected(options: List<String>, selected: List<String>): BooleanArray {
        val boar: MutableList<Boolean> = mutableListOf()
        options.forEach {
            boar.add(selected.contains(it))
        }
        return boar.toBooleanArray()
    }

    fun getFormValue(Tag: String): String {
        var myvalue = "null"
        for ((_, element) in formMap) {
            val view = viewMap[element.attributes.tag]
            if (element.attributes.tag == Tag) {
                when (element.attributes.type) {
                    FormElements.Type.SLIDER -> {
                        val sl = view as Slider?
                        myvalue = sl!!.value.toString()
                    }
                    FormElements.Type.TEXT -> {
                        val sl = view as EditText?
                        myvalue = sl!!.text.toString()
                    }
                    FormElements.Type.SELECT -> {
                        val sl = view as EditText?
                        myvalue = sl!!.text.toString()
                    }
                    FormElements.Type.RATING -> {
                        val sl = view as RatingBar?
                        myvalue = sl!!.rating.toString()
                    }
                    else -> myvalue = "null"
                }
            }
        }
        return myvalue
    }

    fun reset() {
        linearLayout.removeAllViews()
    }


}

open class FormObject
class FormButton : FormObject() {
    var title: String? = null
    var backgroundColor: Int? = null
    var textColor: Int? = null
    var runnable: Runnable? = null
    var params: LinearLayout.LayoutParams? = null
    fun setTitle(title: String?): FormButton {
        this.title = title
        return this
    }

    fun setBackgroundColor(backgroundColor: Int?): FormButton {
        this.backgroundColor = backgroundColor
        return this
    }

    fun setTextColor(textColor: Int?): FormButton {
        this.textColor = textColor
        return this
    }

    fun setRunnable(runnable: Runnable?): FormButton {
        this.runnable = runnable
        return this
    }
}