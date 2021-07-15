package com.androijo.tvnavigation.interfaces

interface NavigationInitializeListener {
    fun initializeSubMenu(subMenu: Array<String>?)
    fun changeMenu(menu: String, parentMenu: String)
}