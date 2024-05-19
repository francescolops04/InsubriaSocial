package com.social.insubriasocial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class CustomChatAdapter(context: Context, data: ArrayList<String>) : ArrayAdapter<String>(context, R.layout.custom_list_contact, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        // Se convertView è nullo, infla la vista dal layout custom_list_contact
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list_contact, parent, false)
        }

        val item = getItem(position)

        val textViewContactUser = convertView!!.findViewById<TextView>(R.id.textViewUserContact)
        val textViewContactName = convertView.findViewById<TextView>(R.id.textViewNameContact)

        // Suddivide i dati in base alla nuova riga ("\n")
        val parts = item?.split("\n")
        if (parts != null) {
            // Se ci sono almeno due parti (nome utente e nome completo), imposta i testi delle TextView
            if (parts.size >= 2) {
                textViewContactUser.text = parts[0]
                textViewContactName.text = parts[1]
            }
        }


        return convertView
    }
}