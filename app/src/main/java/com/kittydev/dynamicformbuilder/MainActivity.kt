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

        //val _etListner = MutableLiveData<String>()
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
                    valueListener = _etListner,
                    hint = "sample multi",
                    heading = "Multi",
                    isRefreshBtn = true,
                    refreshListener = _refreshListner,
                    options = list

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
                    heading = "Multi",
                    isRefreshBtn = true,
                    drawable = R.drawable.ic_baseline_refresh_24,
                    refreshListener = _refreshListner
                )
            )
        )


        sas.add(
            FormElements().setArguments(
                attributeDM(
                    tag = "et2",
                    type = FormElements.Type.TEXT,
                    hint = "sample 2",
                )
            )
        )


        _refreshListner.observe(this, Observer { it ->
            Log.e("kittydev", "Refreshed")
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