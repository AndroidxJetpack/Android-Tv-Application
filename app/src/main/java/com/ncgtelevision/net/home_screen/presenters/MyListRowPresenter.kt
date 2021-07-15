package com.ncgtelevision.net.home_screen.presenters

import android.view.ViewGroup
import android.widget.Button
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.RowPresenter
import com.ncgtelevision.net.R

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

    override fun createRowViewHolder(parent: ViewGroup?): RowPresenter.ViewHolder {
//        selectEffectEnabled = false
        shadowEnabled = false
        headerPresenter = MyHeaderPresenter()
        return super.createRowViewHolder(parent)
    }
}













