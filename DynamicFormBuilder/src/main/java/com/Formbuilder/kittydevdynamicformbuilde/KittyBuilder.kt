package com.Formbuilder.kittydevdynamicformbuilde

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.input.view.*
import java.util.*

class KittyBuilder(private var context: Context, private var linearLayout: LinearLayout) {
    private var formMap: LinkedHashMap<String, KittyElements> = LinkedHashMap()
    private var viewMap: LinkedHashMap<String, View> = LinkedHashMap()
    private var selectedEditText: EditText? = null

    fun build(kittyObjects: List<KittyObject?>) {
        for (formObject in kittyObjects) if (formObject is KittyElements) {
            val tag = formObject.attributes.tag
            formMap[tag] = formObject
            addToLinearLayout(buildElement(formObject), formObject.attributes.params)
        } else if (formObject is KittyButton) {
            addToLinearLayout(buildButton(formObject), formObject.params)
        }
    }

    private fun buildElement(kittyElements: KittyElements): View? {
        return when (kittyElements.attributes.type) {
            KittyElements.Type.TEXT -> {

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

                kittyElements.attributes.subHeading?.let {
                    InputTextSpacer.visibility = View.VISIBLE
                    InputTextSubHeadingHolder.visibility = View.VISIBLE
                    InputTextSubHeading.text = kittyElements.attributes.subHeading
                } ?: run {
                    InputTextSpacer.visibility = View.GONE
                    InputTextSubHeadingHolder.visibility = View.GONE
                }
                kittyElements.attributes.heading?.let {
                    InputTextHeading.visibility = View.VISIBLE
                    InputTextHeading.text = kittyElements.attributes.heading
                } ?: run {
                    InputTextHeading.visibility = View.GONE
                }

                kittyElements.attributes.valueListener?.let { it ->
                    InputTextEditText.doAfterTextChanged { it1 ->
                        if (!it1.isNullOrEmpty()) {
                            it.value = it1.toString()
                        }
                    }
                }

                if (kittyElements.attributes.isRefreshBtn) {
                    RightRefreshBtn.visibility = View.VISIBLE
                    kittyElements.attributes.drawable?.let {
                        RightRefreshBtn.setBackgroundResource(it)
                    }
                } else {
                    RightRefreshBtn.visibility = View.GONE
                }

                RightRefreshBtn.setOnClickListener {
                    kittyElements.attributes.refreshListener?.let {
                        it.value = "${Date().time}"
                    }
                }

                InputTextLayoutHolder.hint = kittyElements.attributes.hint

                InputTextEditText.isEnabled = kittyElements.attributes.isEnabled
                InputTextEditText.setText(kittyElements.attributes.value)
                InputTextEditText.inputType = InputType.TYPE_CLASS_TEXT



                viewMap[kittyElements.attributes.tag] = InputTextEditText
                addViewToView(InputTextLayout, InputTextView)
                InputTextLayout
            }

            KittyElements.Type.SELECT -> {
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

                DropDownLayoutHolder.hint = kittyElements.attributes.hint
                DropDownEditText.isEnabled = kittyElements.attributes.isEnabled

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
                    DropDownEditText.showSoftInputOnFocus = false
                } else { // API 11-20
                    DropDownEditText.setTextIsSelectable(true)
                }

                DropDownEditText.inputType = InputType.TYPE_CLASS_TEXT
                DropDownEditText.setText(
                    kittyElements.attributes.selectedOptions.toString().replace("[", "")
                        .replace("]", "")
                )
                DropDownEditText.setOnClickListener {

                    val inputManager =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(DropDownEditText.windowToken, 0)

                    selectDialog(DropDownEditText, kittyElements)
                }

                DropDownEditText.doAfterTextChanged {
                    if (!it.isNullOrEmpty()) {
                        kittyElements.attributes.valueListener!!.value = it.toString()
                    }
                }

                kittyElements.attributes.subHeading?.let {
                    DropDownSpacer.visibility = View.VISIBLE
                    DropDownSubHeadingHolder.visibility = View.VISIBLE
                    DropDownSubHeading.text = kittyElements.attributes.subHeading
                } ?: run {
                    DropDownSpacer.visibility = View.GONE
                    DropDownSubHeadingHolder.visibility = View.GONE
                }

                kittyElements.attributes.heading?.let {
                    DropDownHeading.visibility = View.VISIBLE
                    DropDownHeading.text = kittyElements.attributes.heading
                } ?: run {
                    DropDownHeading.visibility = View.GONE
                }

                viewMap[kittyElements.attributes.tag] = DropDownEditText
                addViewToView(DropDownLayout, DropDownView)
                DropDownLayout
            }


            KittyElements.Type.MULTISELECT -> {
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

                DropDownLayoutHolder.hint = kittyElements.attributes.hint + "C"
                DropDownEditText.isEnabled = kittyElements.attributes.isEnabled

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
                    DropDownEditText.showSoftInputOnFocus = false
                } else { // API 11-20
                    DropDownEditText.setTextIsSelectable(true)
                }

                DropDownEditText.inputType = InputType.TYPE_CLASS_TEXT

                DropDownEditText.setText(
                    kittyElements.attributes.selectedOptions.toString().replace("[", "")
                        .replace("]", "")
                )

                DropDownEditText.setOnClickListener {

                    val inputManager =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(DropDownEditText.windowToken, 0)

                    multiSelectDialog(DropDownEditText, kittyElements)
                }

                DropDownEditText.doAfterTextChanged {
                    if (!it.isNullOrEmpty()) {
                        kittyElements.attributes.valueListener!!.value = it.toString()
                    }
                }

                kittyElements.attributes.subHeading?.let {
                    DropDownSpacer.visibility = View.VISIBLE
                    DropDownSubHeadingHolder.visibility = View.VISIBLE
                    DropDownSubHeading.text = kittyElements.attributes.subHeading
                } ?: run {
                    DropDownSpacer.visibility = View.GONE
                    DropDownSubHeadingHolder.visibility = View.GONE
                }

                kittyElements.attributes.heading?.let {
                    DropDownHeading.visibility = View.VISIBLE
                    DropDownHeading.text = kittyElements.attributes.heading
                } ?: run {
                    DropDownHeading.visibility = View.GONE
                }

                viewMap[kittyElements.attributes.tag] = DropDownEditText
                addViewToView(DropDownLayout, DropDownView)
                DropDownLayout
            }


            KittyElements.Type.SLIDER -> {
                val SliderView = LayoutInflater.from(context).inflate(R.layout.slider, null)
                val Slider: Slider = SliderView.findViewById(R.id.Slider)
                val SliderLayout = TextInputLayout(context, null, R.id.Slider_Layout)
                val SliderHeading = SliderView.findViewById<TextView>(R.id.Slide_Heading)
                val SliderSubHeading =
                    SliderView.findViewById<TextView>(R.id.Slider_SubHeading)
                val SliderSpacer = SliderView.findViewById<View>(R.id.Slider_Spacer)
                val SliderSubHeadingHolder =
                    SliderView.findViewById<LinearLayout>(R.id.Slider_SubHeading_Holder)
                if (kittyElements.attributes.subHeading!!.isNotEmpty()) {
                    SliderSpacer.visibility = View.VISIBLE
                    SliderSubHeadingHolder.visibility = View.VISIBLE
                    SliderSubHeading.text = kittyElements.attributes.subHeading
                } else {
                    SliderSpacer.visibility = View.GONE
                    SliderSubHeadingHolder.visibility = View.GONE
                }
                if (kittyElements.attributes.heading!!.isNotEmpty()) {
                    SliderHeading.visibility = View.VISIBLE
                    SliderHeading.text = kittyElements.attributes.heading
                } else {
                    SliderHeading.visibility = View.GONE
                }
                addViewToView(SliderLayout, SliderView)
                viewMap[kittyElements.attributes.tag] = Slider
                SliderLayout
            }

            KittyElements.Type.RATING -> {
                val RatingView = LayoutInflater.from(context).inflate(R.layout.rating, null)
                val Rating = RatingView.findViewById<RatingBar>(R.id.Rating)
                val RatingLayout = TextInputLayout(context, null, R.id.RatingLayout)
                val RatingSubHeading =
                    RatingView.findViewById<TextView>(R.id.Rating_SubHeading)
                val RatingHeading = RatingView.findViewById<TextView>(R.id.Rating_Heading)
                val RatingSpacer = RatingView.findViewById<View>(R.id.Rating_Spacer)
                val RatingSubHeadingHolder =
                    RatingView.findViewById<LinearLayout>(R.id.Rating_SubHeading_Holder)

                kittyElements.attributes.subHeading?.let {
                    RatingSpacer.visibility = View.VISIBLE
                    RatingSubHeadingHolder.visibility = View.VISIBLE
                    RatingSubHeading.text = kittyElements.attributes.subHeading
                } ?: run {
                    RatingSpacer.visibility = View.GONE
                    RatingSubHeadingHolder.visibility = View.GONE
                }
                kittyElements.attributes.heading?.let {
                    RatingHeading.visibility = View.VISIBLE
                    RatingHeading.text = kittyElements.attributes.heading
                } ?: run {
                    RatingHeading.visibility = View.GONE
                }
                viewMap[kittyElements.attributes.tag] = Rating
                addViewToView(RatingLayout, RatingView)
                RatingLayout
            }
            else -> null
        }
    }

    private fun buildButton(formButton: KittyButton): View {
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
        view!!.layoutParams = params ?: layoutParams
        linearLayout.addView(view)
    }

    private fun addViewToView(parent: ViewGroup, child: View) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        child.layoutParams = layoutParams
        parent.addView(child)
    }

    private fun selectDialog(selectedEditText: EditText, selectedKittyElements: KittyElements) {
        val selectedElements: MutableList<String> =
            ArrayList(selectedKittyElements.attributes.selectedOptions)
        this.selectedEditText = selectedEditText
        val builder = AlertDialog.Builder(context)

        builder.setTitle(selectedKittyElements.attributes.hint + " Single Select")

        builder.setSingleChoiceItems(
            selectedKittyElements.attributes.options!!.toTypedArray<CharSequence>(),
            selectedKittyElements.checkedValue,
            null
        )

        builder.setPositiveButton("OK") { dialogInterface, _ ->
            dialogInterface.dismiss()
            val selectedPosition = (dialogInterface as AlertDialog).listView.checkedItemPosition
            selectedElements.clear() //We only want one input
            selectedElements.add(selectedKittyElements.attributes.options!![selectedPosition])
            selectedKittyElements.attributes.selectedOptions = selectedElements
            selectedEditText.setText(
                selectedKittyElements.attributes.selectedOptions.toString().replace("[", "")
                    .replace("]", "")
            )
        }
        builder.setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
        builder.show()
    }

    private fun multiSelectDialog(
        selectedEditText: EditText,
        selectedKittyElements: KittyElements
    ) {
        val selectedElements = ArrayList(selectedKittyElements.attributes.selectedOptions)
        this.selectedEditText = selectedEditText
        val builder = AlertDialog.Builder(context)
        builder.setTitle(selectedKittyElements.attributes.hint + " Multi Select")

        val options = selectedKittyElements.attributes.options!!.toTypedArray()
        val arrayChecked = selectedKittyElements.attributes.selectedOptions?.let {
            getSelected(
                selectedKittyElements.attributes.options!!,
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
                if (it) {
                    selectedElements.add(selectedKittyElements.attributes.options!![i])
                }
            }

            selectedKittyElements.attributes.selectedOptions = selectedElements.toList()

            selectedEditText.setText(
                selectedKittyElements.attributes.selectedOptions.toString().replace("[", "")
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
                    KittyElements.Type.SLIDER -> {
                        val sl = view as Slider?
                        myvalue = sl!!.value.toString()
                    }
                    KittyElements.Type.TEXT -> {
                        val sl = view as EditText?
                        myvalue = sl!!.text.toString()
                    }
                    KittyElements.Type.SELECT -> {
                        val sl = view as EditText?
                        myvalue = sl!!.text.toString()
                    }
                    KittyElements.Type.RATING -> {
                        val sl = view as RatingBar?
                        myvalue = sl!!.rating.toString()
                    }
                    else -> myvalue = "null"
                }
            }
        }
        return myvalue
    }
   fun reset(){
        linearLayout.removeAllViews()
    }

}

open class KittyObject
class KittyButton : KittyObject() {
    var title: String? = null
    var backgroundColor: Int? = null
    var textColor: Int? = null
    var runnable: Runnable? = null
    var params: LinearLayout.LayoutParams? = null
    fun setTitle(title: String?): KittyButton {
        this.title = title
        return this
    }

    fun setBackgroundColor(backgroundColor: Int?): KittyButton {
        this.backgroundColor = backgroundColor
        return this
    }

    fun setTextColor(textColor: Int?): KittyButton {
        this.textColor = textColor
        return this
    }

    fun setRunnable(runnable: Runnable?): KittyButton {
        this.runnable = runnable
        return this
    }
}