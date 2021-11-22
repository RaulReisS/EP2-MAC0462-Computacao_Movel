package br.com.raulreis.recipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.StringRes

import kotlinx.android.synthetic.main.item_weather.view.*

class InfoAdapter(
    private val ctx: Context,

    

    private val infos: List<Info>) : BaseAdapter() {

    override fun getCount() : Int = infos.size

    override fun getItem(position: Int) = infos[position]

    override fun getItemId(position : Int ) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val info = infos[position]
        

        val row = LayoutInflater.from(ctx).inflate(R.layout.item_weather, parent, false)

        row.txvTemp.text = info.temp
        row.txvLocal.text = info.name
        row.txvData.text = info.data

        return row
    }
}