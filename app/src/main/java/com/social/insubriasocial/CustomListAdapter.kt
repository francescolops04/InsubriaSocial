package com.social.insubriasocial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomListAdapter(context: Context, data: ArrayList<String>) : ArrayAdapter<String>(context, R.layout.custom_list, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list, parent, false)
        }

        val item = getItem(position)

        val textViewUser = convertView!!.findViewById<TextView>(R.id.textViewUser)
        val textViewTitle = convertView.findViewById<TextView>(R.id.textViewTitle)
        val textViewDescription = convertView.findViewById<TextView>(R.id.textViewDescription)

        val parts = item?.split("\n")
        if (parts != null) {
            if (parts.size >= 3) {
                textViewUser.text = parts[0]
                textViewTitle.text = parts[1]
                textViewDescription.text = parts[2]
            }
        }

        return convertView
    }
}