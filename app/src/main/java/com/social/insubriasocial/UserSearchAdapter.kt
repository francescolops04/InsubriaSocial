package com.social.insubriasocial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class UserSearchAdapter(context: Context, data: ArrayList<String>) : ArrayAdapter<String>(context, R.layout.custom_list_search, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list_search, parent, false)
        }

        val item = getItem(position)

        val textViewSearchUser = convertView!!.findViewById<TextView>(R.id.textViewSearchUser)
        val textViewSearchName = convertView.findViewById<TextView>(R.id.textViewSearchName)

        val parts = item?.split("\n")
        if (parts != null) {
            if (parts.size >= 2) {
                textViewSearchUser.text = parts[0]
                textViewSearchName.text = parts[1]
            }
        }


        return convertView
    }
}
