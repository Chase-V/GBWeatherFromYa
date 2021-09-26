package com.tashev.gbweatherfromya.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showActionSnack(text:String, actionText:String, action: View.OnClickListener){
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).setAction(actionText,action).show()
}
fun View.showSnack(text:String){
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}