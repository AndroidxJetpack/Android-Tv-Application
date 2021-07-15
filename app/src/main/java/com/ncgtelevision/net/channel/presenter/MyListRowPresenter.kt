package com.ncgtelevision.net.channel.presenter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.RowPresenter

class MyListRowPresenter: ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL) {
    override fun isUsingDefaultListSelectEffect(): Boolean {
        return false
    }

    override fun onRowViewSelected(holder: RowPresenter.ViewHolder?, selected: Boolean) {
         super.onRowViewSelected(holder, selected)

        // effects the views shifting up and down when navigating to and from the next row
        // for this to take effect, the call to super.onRowViewSelected has to be removed
//        setSelectLevel(holder, 0f)

        if (selected) {
//            holder?.view?.findViewById<Button>(R.id.button_start_watching)?.apply {
//                requestFocus()
//            }
        }
    }
    override fun onSelectLevelChanged(holder: RowPresenter.ViewHolder) {
        super.onSelectLevelChanged(holder)
//        val vh = holder as ViewHolder
//        Log.d("TAG", "onSelectLevelChanged:selectedPosition->> " +  vh.selectedPosition)
//        Log.d("TAG", "onSelectLevelChanged:childCount->> " +  vh.gridView.childCount)
//        Log.d("TAG", "onSelectLevelChanged: isEnabled=>>" +  vh.gridView.isEnabled)
//        Log.d("TAG", "onSelectLevelChanged:nextFocusUpId->> " +  vh.listRowPresenter)


//        if (vh.isSelected) {
////            vh.gridView.visibility = View.VISIBLE
////            vh.selectedPosition
//        } else {
////            vh.gridView.visibility = View.GONE
//        }
    }

    override fun createRowViewHolder(parent: ViewGroup?): RowPresenter.ViewHolder {
//        selectEffectEnabled = false
        shadowEnabled = false
        headerPresenter = MyHeaderPresenter()
        return super.createRowViewHolder(parent)
    }
}













