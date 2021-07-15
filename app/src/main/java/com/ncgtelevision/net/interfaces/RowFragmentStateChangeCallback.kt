package com.ncgtelevision.net.interfaces

interface RowFragmentStateChangeCallback {
    fun navMenuToggle(rowIndex: Int, columnIndex:Int)
    fun navMenuToggleSelection(rowIndex: Int, columnIndex:Int)

}