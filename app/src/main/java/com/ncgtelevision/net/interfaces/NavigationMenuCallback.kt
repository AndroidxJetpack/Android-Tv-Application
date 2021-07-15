package com.ncgtelevision.net.interfaces

interface NavigationMenuCallback {
    fun navMenuToggle(toShow: Boolean)
    fun selectedOption(any: Any)
}