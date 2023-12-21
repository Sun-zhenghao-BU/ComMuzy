package com.example.commuzy.ui.me

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

////////////////////
// change the color of the background and text
//////////////////
class CustomGenderSpinnerAdapter(
    context: Context,
    resource: Int,
    objects: List<String>,
    private val textColor: Int,
    private val backgroundColor: Int
) : ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.setTextColor(textColor)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.setTextColor(textColor)
        view.setBackgroundColor(backgroundColor)
        return view
    }
}