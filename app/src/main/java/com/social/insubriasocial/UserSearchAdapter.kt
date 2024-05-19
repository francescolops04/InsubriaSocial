package com.social.insubriasocial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class UserSearchAdapter(context: Context, data: ArrayList<String>) : ArrayAdapter<String>(context, R.layout.custom_list_search, data) {

    //Metodo per ottenere e configurare la vista di un singolo elemento della lista
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        // Se la vista riciclata è null, inflaziona una nuova vista da custom_list_search.xml
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list_search, parent, false)
        }

        // Ottiene l'elemento alla posizione specificata
        val item = getItem(position)

        val textViewSearchUser = convertView!!.findViewById<TextView>(R.id.textViewSearchUser)
        val textViewSearchName = convertView.findViewById<TextView>(R.id.textViewSearchName)

        // Divide l'elemento in parti usando il carattere di nuova riga come delimitatore
        val parts = item?.split("\n")

        // Se ci sono almeno due parti (username e nome completo), aggiorna le TextView
        if (parts != null) {
            if (parts.size >= 2) {
                textViewSearchUser.text = parts[0]
                textViewSearchName.text = parts[1]
            }
        }


        return convertView
    }
}
