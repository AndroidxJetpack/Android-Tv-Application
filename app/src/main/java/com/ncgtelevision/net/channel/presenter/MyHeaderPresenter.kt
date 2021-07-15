package com.ncgtelevision.net.channel.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.RowHeaderPresenter
import com.ncgtelevision.net.R
import com.ncgtelevision.net.utilities.CommonUtility.Companion.isStringEmpty

class MyHeaderPresenter : RowHeaderPresenter() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_header_presenter_channel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any?) {
        super.onBindViewHolder(viewHolder, item)
        (item as? ListRow)?.apply {
            if(!isStringEmpty(this.headerItem?.name)){
                viewHolder.view.findViewById<TextView>(R.id.header_label).text = this.headerItem.name;
            }else{
                val params = LinearLayout.LayoutParams(0, 0)
                viewHolder.view.layoutParams = params
            }
//            when (this.headerItem?.name) {
//                "Actors" -> {
//                    //populate views, etc..
//                }
//                else -> {x
//                    //if you don't want a header for this row
//                    val params = LinearLayout.LayoutParams(0, 0)
//                    viewHolder.view.layoutParams = params
//                }
//            }
        }
    }
}