package com.androijo.tvnavigation.interfaces

interface FragmentChangeListener {
    fun switchFragment(fragmentName: String?)
    fun switchFragment(fragmentName: String, menu: String)
}