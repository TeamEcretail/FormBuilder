package com.kittydev.dynamicformbuilder

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.kittydev.kittydevdynamicformbuilde.KittyBuilder
import com.kittydev.kittydevdynamicformbuilde.KittyButton
import com.kittydev.kittydevdynamicformbuilde.KittyElements
import com.kittydev.kittydevdynamicformbuilde.KittyObject
import com.kittydev.kittydevdynamicformbuilde.Model.attributeDM

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sas: ArrayList<KittyObject> = ArrayList()
        val mLinearLayout = findViewById<View>(R.id.TableLayout1) as LinearLayout
        val kittyBuilder: KittyBuilder = KittyBuilder(this, mLinearLayout)

        val _etListner = MutableLiveData<String>()
        val _refreshListner = MutableLiveData<String>()

        val sample: ArrayList<String> = ArrayList()
        sample.add("meow")
        sample.add("meow1")
        sample.add("meow2")

        sas.add(
            KittyElements().setArguments(
                attributeDM(
                    tag = "et1",
                    type = KittyElements.Type.TEXT,
                    valueListener = _etListner,
                    hint = "sample",
                    heading = "Multi",
                    isRefreshBtn = true,
                    drawable=R.drawable.ic_baseline_refresh_24,
                    refreshListener = _refreshListner
                )
            )
        )


        sas.add(
            KittyElements().setArguments(
                attributeDM(
                    tag = "et2",
                    type = KittyElements.Type.TEXT,
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
            KittyButton().setTitle("submit").setBackgroundColor(Color.parseColor("#1a237e"))
                .setTextColor(Color.WHITE).setRunnable {
                    Log.e("Kittydev", "Clicked")
                })

        val kittyObjects: List<KittyObject> = sas
        kittyBuilder.build(kittyObjects)

    }
}