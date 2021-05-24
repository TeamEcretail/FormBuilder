package com.kittydev.dynamicformbuilder

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.kittydev.kittydevdynamicformbuilde.FormBuilder
import com.kittydev.kittydevdynamicformbuilde.FormButton
import com.kittydev.kittydevdynamicformbuilde.FormElements
import com.kittydev.kittydevdynamicformbuilde.FormObject
import com.kittydev.kittydevdynamicformbuilde.Model.attributeDM

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list: ArrayList<String> = ArrayList();
        val sas: ArrayList<FormObject> = ArrayList()
        val mLinearLayout = findViewById<View>(R.id.TableLayout1) as LinearLayout
        val formBuilder: FormBuilder = FormBuilder(this, mLinearLayout)
        val et = MutableLiveData<String>()
        et.value = "hello"
        val _refreshListner = MutableLiveData<String>()

        val sample: ArrayList<String> = ArrayList()
        sample.add("meow")
        list.add("op1");
        list.add("op2");
        list.add("op3");

        val _etListner = MutableLiveData<String>()
        sas.add(
            FormElements().setArguments(
                attributeDM(
                    tag = "et1",
                    type = FormElements.Type.MULTISELECT,
                    hint = "sample multi",
                    heading = "Multi Spinner sample",
                    isRefreshBtn = true,
                    refreshListener = _refreshListner,
                    options = list

                )
            )
        )
        sas.add(
            FormElements().setArguments(
                attributeDM(
                    tag = "et1",
                    type = FormElements.Type.TWO_INPUT,
                    heading = "2 input sample",
                    options = list

                )
            )
        )
        sas.add(
            FormElements().setArguments(
                attributeDM(
                    tag = "et1",
                    type = FormElements.Type.THREE_INPUT,
                    heading = "3 input sample",
                )
            )
        )
        _etListner.observe(this, Observer { it ->
            Log.e("kittydev", "Fromm Main Act : -->  " + it)
        })
        sas.add(
            FormElements().setArguments(
                attributeDM(
                    tag = "et1",
                    type = FormElements.Type.TEXT,
                    valueListener = _etListner,
                    hint = "sample",
                    isRefreshBtn = true,
                    drawable = R.drawable.ic_baseline_refresh_24,
                    refreshListener = _refreshListner,
                    value = et,
                    heading = "Refresh sample"
                )
            )
        )

        sas.add(
            FormElements().setArguments(
                attributeDM(
                    tag = "et1",
                    type = FormElements.Type.TWO_INPUT,
                    valueListener = _etListner,
                    hint = "sample",
                    value = et,
                    heading = "Refresh sample"
                ), attributeDM(
                    tag = "et2",
                    type = FormElements.Type.TWO_INPUT,
                    valueListener = _etListner,
                    hint = "sample2",
                    value = et,
                    heading = "Refresh sample2"
                )
            )
        )

        _refreshListner.observe(this, Observer { it ->
            Log.e("kittydev", "Refreshed")
            et.value = "bye"
        })

        _etListner.observe(this, Observer { it ->
            Log.e("kittydev", "Fromm Main Act : -->  " + it)
        })

        sas.add(
            FormButton().setTitle("submit").setBackgroundColor(Color.parseColor("#1a237e"))
                .setTextColor(Color.WHITE).setRunnable {
                    Log.e("Kittydev", "Clicked")
                })

        val formObjects: List<FormObject> = sas
        formBuilder.build(formObjects)
    }
}