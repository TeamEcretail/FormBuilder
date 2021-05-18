# Kittydev Dynamic FormBulider

| [![GitHub release](https://img.shields.io/github/release/KittyDev18/KittyDev-FormBuilder)](https://GitHub.com/KittyDev18/KittyDev-FormBuilder/releases/) | [![GitHub license](https://img.shields.io/github/license/KittyDev18/KittyDev-FormBuilder)](https://github.com/KittyDev18/KittyDev-FormBuilder/blob/master/LICENSE) |
|--|--|




**Attention**: This library is Currently in Initial Stage with Limited elements.

 * Build `Dynamic Forms` Easily
 * Material Design Elements

Implementation
--------
```groovy
//In build.gradle(project)
allprojects {  
  repositories {  
        maven { url 'https://jitpack.io' }  
 }}


//In build.gradle(app)
dependencies {
 	implementation 'com.github.KittyDev18:KittyDev-FormBuilder:ver'
	implementation 'com.google.android.material:material:ver'
}

//In Style.xml Add any Material Design Theme
parent="Theme.MaterialComponents.Light.NoActionBar.Bridge"
```
Current Support
--------
* Text Field (only text)
* DropDown / Single Select
* Slider
* Rating (Star) 
* Button

Usage
--------
**XML**

```xml
<?xml version="1.0" encoding="utf-8"?>  
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"  
  xmlns:app="http://schemas.android.com/apk/res-auto"  
  xmlns:tools="http://schemas.android.com/tools"  
  android:layout_width="match_parent"  
  android:layout_height="match_parent"  
  tools:context=".MainActivity">  
  
 <LinearLayout  android:id="@+id/kittylayout"  
  android:layout_width="match_parent"  
  android:layout_height="0dp"  
  android:orientation="vertical"  
  app:layout_constraintBottom_toBottomOf="parent"  
  app:layout_constraintEnd_toEndOf="parent"  
  app:layout_constraintStart_toStartOf="parent"  
  app:layout_constraintTop_toTopOf="parent" />  
  
</androidx.constraintlayout.widget.ConstraintLayout>
```
**Kotlin**

```kotlin
val FormArray: ArrayList<KittyObject> = ArrayList()  
val mLinearLayout = findViewById<View>(R.id.kittylayout) as LinearLayout  
val kittyBuilder: KittyBuilder = KittyBuilder(this, mLinearLayout)

FormArray.add(KittyElements().setArguments(
                attributeDM(
                    tag = "et1",
                    type = KittyElements.Type.TEXT,
                    valueListener = _etListner,
                    hint = "sample",
                    heading = "Multi",
                    isRefreshBtn = true,
                    refreshListener = _refreshListner
                )
            ))

val kittyObjects: List<KittyObject> = FormArray  
kittyBuilder.build(kittyObjects)
```

**Java**
``` java	
  List < KittyObject > FormArray = new ArrayList < KittyObject > ();
  LinearLayout mLinearLayout = findViewById(R.id.kittylayout);
  FormBuilder KittyBuilder = new FormBuilder(this, mLinearLayout);

 
  FormArray.add(new FormElement().setArguments(
                attributeDM(
                    tag = "et1",
                    type = KittyElements.Type.TEXT,
                    valueListener = _etListner,
                    hint = "sample",
                    heading = "Multi",
                    isRefreshBtn = true,
                    refreshListener = _refreshListner
                )
            ));


  KittyBuilder.build(FormArray);

```

License
-------

    Copyright 2020 KittyDev

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
