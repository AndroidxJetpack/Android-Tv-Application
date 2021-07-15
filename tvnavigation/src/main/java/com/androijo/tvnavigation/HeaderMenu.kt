package com.androijo.tvnavigation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.androijo.tvnavigation.interfaces.FragmentChangeListener
import com.androijo.tvnavigation.interfaces.NavigationInitializeListener
import com.androijo.tvnavigation.interfaces.NavigationStateListener
import com.androijo.tvnavigation.utils.Constants
import kotlinx.android.synthetic.main.fragment_header_menu.*


class HeaderMenu : Fragment() {
    private lateinit var fragmentChangeListener: FragmentChangeListener
    private lateinit var navigationStateListener: NavigationStateListener
    private lateinit var navigationInitializeListener: NavigationInitializeListener
    private var isCheck = true
    private var TAG = HeaderMenu::class.java.toString()
    private val search = Constants.nav_menu_search
    private val home = Constants.nav_menu_home
    private val school = Constants.nav_menu_school
    private val thematic = Constants.nav_menu_thematic
    private val freeChannel = Constants.nav_menu_free
    private val account = Constants.nav_menu_account
    private val logout = Constants.nav_menu_logout

    private var lastSelectedMenu: String? = home
    private var searchAllowedToGainFocus = false
    private var homeAllowedToGainFocus = false
    private var schoolAllowedToGainFocus = false
    private var thematicAllowedToGainFocus = false
    private var freeAllowedToGainFocus = true
    private var accountAllowedToGainFocus = false
    private var logoutAllowedToGainFocus = false
    private var switchUserAllowedToGainFocus = false

    private var menuTextAnimationDelay = 0//200
    private var selectedSubMenu =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_header_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //by default selection
        setMenuIconFocusView(R.drawable.ic_home_selected, home_IB, home_LL, home_TV, true)

        //Navigation Menu Options Focus, Key Listeners
        homeListeners()
        searchListner()
        schoolListner()
        thematicListner()
        freeChannelListner()
        accountListner()
        logoutListner()

    }
    private fun setMenuIconFocusView(resource: Int, view: ImageButton, container: LinearLayoutCompat, textView: TextView, inFocus: Boolean) {
        view.setImageResource(resource)
        if(inFocus && isNavigationOpen() ) {
            container.setBackgroundColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.selected_icon_background
                )
            )
            textView.setTextColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.selected_icon_color
                )
            )
        }else{
            container.setBackgroundColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.transparent
                )
            )
            textView.setTextColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.unselected_icon_color
                )
            )
        }
    }

    /**
     * Setting animation when focus is lost
     */
    fun focusOut(v: View, position: Int) {
//        val scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.2f, 1f)
//        val scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.2f, 1f)
//        val set = AnimatorSet()
//        set.play(scaleX).with(scaleY)
//        set.start()
    }

    /**
     * Setting the animation when getting focus
     */
    fun focusIn(v: View, position: Int) {
//        val scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.2f)
//        val scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.2f)
//        val set = AnimatorSet()
//        set.play(scaleX).with(scaleY)
//        set.start()
    }

    private fun enableNavMenuViews(visibility: Int) {
        if (visibility == View.GONE) {
            menuTextAnimationDelay = 0//200 //reset
            home_TV.visibility = visibility
            search_TV.visibility = visibility
            thematic_channel_TV.visibility = visibility
            school_TV.visibility = visibility
            free_channel_TV.visibility = visibility
            account_TV.visibility = visibility
            logout_TV.visibility = visibility
        } else {
            animateMenuNamesEntry(search_TV, visibility, 2)
        }

    }
    private fun animateMenuNamesEntry(view: View, visibility: Int, viewCode: Int) {
        view.postDelayed({
            view.visibility = visibility
            val animate = AnimationUtils.loadAnimation(context, R.anim.slide_in_left_menu_name)
            view.startAnimation(animate)
            menuTextAnimationDelay = 100
            when (viewCode) {
                2 -> {
                    animateMenuNamesEntry(home_TV, visibility, viewCode + 1)
                }
                3 -> {
                    animateMenuNamesEntry(school_TV, visibility, viewCode + 1)
                }
                4 -> {
                    animateMenuNamesEntry(thematic_channel_TV, visibility, viewCode + 1)
                }
                5 -> {
                    animateMenuNamesEntry(free_channel_TV, visibility, viewCode + 1)
                }
                6 -> {
                    animateMenuNamesEntry(account_TV, visibility, viewCode + 1)
                }
                7 -> {
                    animateMenuNamesEntry(logout_TV, visibility, viewCode + 1)
                }
            }
        }, menuTextAnimationDelay.toLong())
    }
    fun openNav() {
        search_TV.visibility=View.VISIBLE
        enableNavMenuViews(View.VISIBLE)
        val lp = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        lp.width = 270
//        open_header_CL.layoutParams = lp
        open_header_SV.layoutParams =lp
        navigationStateListener.onStateChanged(true, lastSelectedMenu)
        highlightMenuSelection(lastSelectedMenu)
        //Setting out of focus views for menu icons, names
        unHighlightMenuSelections(lastSelectedMenu)
        when (lastSelectedMenu) {

            search -> {
                search_LL.requestFocus()
                searchAllowedToGainFocus = true
//                setMenuNameFocusView(shows_TV, true)
            }
            home -> {
                home_LL.requestFocus()
                homeAllowedToGainFocus = true
//                setMenuNameFocusView(movies_TV, true)
            }
            school -> {
                school_channel_ll.requestFocus()
                schoolAllowedToGainFocus = true
//                setMenuNameFocusView(podcasts_TV, true)
            }
            thematic -> {
                thematic_channel_ll.requestFocus()
                thematicAllowedToGainFocus = true
//                setMenuNameFocusView(music_TV, true)
            }
            freeChannel -> {
//                free_channel_ll.requestFocus()
                submenu_ll.visibility = View.VISIBLE
                if(selectedSubMenu != -1 && submenu_ll.childCount > selectedSubMenu){
                    submenu_ll.getChildAt(selectedSubMenu).requestFocus();
                }
                freeAllowedToGainFocus = true
//                setMenuNameFocusView(news_TV, true)
            }
            account -> {
                account_LL.requestFocus()
                accountAllowedToGainFocus = true
//                setMenuNameFocusView(settings_TV, true)
            }
            logout -> {
                logout_LL.requestFocus()
                logoutAllowedToGainFocus = true
//                setMenuNameFocusView(settings_TV, true)
            }

        }

    }

    fun closeNav() {
        enableNavMenuViews(View.GONE)
        val lp = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        lp.width = 100
//        open_header_CL.layoutParams = lp
        open_header_SV.layoutParams =lp

        //highlighting last selected menu icon
        highlightMenuSelection(lastSelectedMenu)

        //Setting out of focus views for menu icons, names
        unHighlightMenuSelections(lastSelectedMenu)

        submenu_ll.visibility = View.GONE
    }

    fun updateNav(updateSelectedMenu:String){
        lastSelectedMenu = updateSelectedMenu
        highlightMenuSelection(lastSelectedMenu)
        //Setting out of focus views for menu icons, names
        unHighlightMenuSelections(lastSelectedMenu)
        when (lastSelectedMenu) {

            search -> {
                search_LL.requestFocus()
                searchAllowedToGainFocus = true
//                setMenuNameFocusView(shows_TV, true)
            }
            home -> {
                home_LL.requestFocus()
                homeAllowedToGainFocus = true
//                setMenuNameFocusView(movies_TV, true)
            }
            school -> {
                school_channel_ll.requestFocus()
                schoolAllowedToGainFocus = true
//                setMenuNameFocusView(podcasts_TV, true)
            }
            thematic -> {
                thematic_channel_ll.requestFocus()
                thematicAllowedToGainFocus = true
//                setMenuNameFocusView(music_TV, true)
            }
            freeChannel -> {
                free_channel_ll.requestFocus()
                freeAllowedToGainFocus = true
//                setMenuNameFocusView(news_TV, true)
            }
            account -> {
                account_LL.requestFocus()
                accountAllowedToGainFocus = true
//                setMenuNameFocusView(settings_TV, true)
            }
            logout -> {
                logout_LL.requestFocus()
                logoutAllowedToGainFocus = true
//                setMenuNameFocusView(settings_TV, true)
            }

        }
    }

    private fun unHighlightMenuSelections(lastSelectedMenu: String?) {
        if (!lastSelectedMenu.equals(search, true)) {
            if(isNavigationOpen()){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setMenuIconFocusView(R.drawable.ic_search_unselected, search_IB, search_LL, search_TV, false)

                }else{
                    setMenuIconFocusView(R.drawable.ic_search_unselected, search_IB, search_LL, search_TV, false)

                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setMenuIconFocusView(R.drawable.ic_search_unselected_close, search_IB, search_LL, search_TV, false)

                }else{
                    setMenuIconFocusView(R.drawable.ic_search_unselected_close, search_IB, search_LL, search_TV, false)
                }
            }
        }

        if (!lastSelectedMenu.equals(home, true)) {
            if(isNavigationOpen()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMenuIconFocusView(R.drawable.ic_home_unselected, home_IB, home_LL, home_TV, false)
                    }
                else{
                    setMenuIconFocusView(R.drawable.ic_home_unselected, home_IB, home_LL, home_TV, false)
                }

            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMenuIconFocusView(R.drawable.ic_home_unselected_close, home_IB, home_LL, home_TV, false)
                    }
                else{
                    setMenuIconFocusView(R.drawable.ic_home_unselected_close, home_IB, home_LL, home_TV, false)
                }
            }
        }
        if (!lastSelectedMenu.equals(school, true)) {
            if(isNavigationOpen()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMenuIconFocusView(R.drawable.ic_school_unselected, school_IB, school_channel_ll, school_TV, false)
                    }
                else{
                    setMenuIconFocusView(R.drawable.ic_school_unselected, school_IB, school_channel_ll, school_TV, false)
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMenuIconFocusView(R.drawable.ic_school_unselected_close, school_IB, school_channel_ll, school_TV, false)
                    }
                else{
                    setMenuIconFocusView(R.drawable.ic_school_unselected_close, school_IB, school_channel_ll, school_TV, false)
                }
            }
        }
        if (!lastSelectedMenu.equals(thematic, true)) {
            if(isNavigationOpen()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMenuIconFocusView(R.drawable.ic_thematic_unselected, thematic_channel_IB, thematic_channel_ll, thematic_channel_TV, false)
                    }
                else{
                    setMenuIconFocusView(R.drawable.ic_thematic_unselected, thematic_channel_IB, thematic_channel_ll, thematic_channel_TV, false)
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMenuIconFocusView(R.drawable.ic_thematic_unselected_close, thematic_channel_IB, thematic_channel_ll, thematic_channel_TV, false)
                    }
                else{
                    setMenuIconFocusView(R.drawable.ic_thematic_unselected_close, thematic_channel_IB, thematic_channel_ll, thematic_channel_TV, false)
                }
            }
        }
        if (!lastSelectedMenu.equals(freeChannel, true)) {
            if(isNavigationOpen()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMenuIconFocusView(R.drawable.ic_free_unselected, free_channel_IB, free_channel_ll, free_channel_TV, false)
                    }else{
                    setMenuIconFocusView(R.drawable.ic_free_unselected, free_channel_IB, free_channel_ll, free_channel_TV, false)
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMenuIconFocusView(R.drawable.ic_free_unselected_close, free_channel_IB, free_channel_ll, free_channel_TV, false)
                    }
                else{
                    setMenuIconFocusView(R.drawable.ic_free_unselected_close, free_channel_IB, free_channel_ll, free_channel_TV, false)
                }
            }
        }
        if (!lastSelectedMenu.equals(account, true)) {
            if(isNavigationOpen()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMenuIconFocusView(R.drawable.ic_account_unselected, account_IB, account_LL, account_TV, false)
                    }
                else{
                    setMenuIconFocusView(R.drawable.ic_account_unselected, account_IB, account_LL, account_TV, false)
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMenuIconFocusView(R.drawable.ic_account_unselected_close, account_IB, account_LL, account_TV, false)
                    }
                else{
                    setMenuIconFocusView(R.drawable.ic_account_unselected_close, account_IB, account_LL, account_TV, false)
                }
            }
        }
        if (!lastSelectedMenu.equals(logout, true)) {
            if(isNavigationOpen()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMenuIconFocusView(R.drawable.ic_logout_unselected, logout_IB, logout_LL, logout_TV, false)
                    }else{
                    setMenuIconFocusView(R.drawable.ic_logout_unselected, logout_IB, logout_LL, logout_TV, false)
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMenuIconFocusView(R.drawable.ic_logout_unselected_close, logout_IB, logout_LL, logout_TV, false)
                    }else{
                    setMenuIconFocusView(R.drawable.ic_logout_unselected_close, logout_IB, logout_LL, logout_TV, false)
                }
            }

        }
    }

    private fun highlightMenuSelection(lastSelectedMenu: String?) {
        when (lastSelectedMenu) {
            search -> {
                setMenuIconFocusView(R.drawable.ic_search_selected, search_IB, search_LL, search_TV, true)
            }
            home -> {
                setMenuIconFocusView(R.drawable.ic_home_selected, home_IB, home_LL, home_TV, true)
            }
            school -> {
                setMenuIconFocusView(R.drawable.ic_school_selected, school_IB, school_channel_ll, school_TV, true)
            }
            thematic -> {
                setMenuIconFocusView(R.drawable.ic_thematic_selected, thematic_channel_IB, thematic_channel_ll, thematic_channel_TV, true)
            }
            freeChannel -> {
                if(!isNavigationOpen()) {
                    setMenuIconFocusView(
                        R.drawable.ic_free_selected,
                        free_channel_IB,
                        free_channel_ll,
                        free_channel_TV,
                        true
                    )
                }else{
                    free_channel_IB.setImageResource( R.drawable.ic_free_unselected)
                }
            }
            account -> {
                setMenuIconFocusView(R.drawable.ic_account_selected, account_IB, account_LL, account_TV, true)
            }
            logout -> {
                setMenuIconFocusView(R.drawable.ic_logout_selected, logout_IB, logout_LL, logout_TV, true)
            }
        }
    }


    private fun isNavigationOpen() = search_TV.visibility == View.VISIBLE

    // Item Listners
    private fun homeListeners() {
//        home_IB.setOnClickListener(View.OnClickListener {
//
//        })
        home_IB.setOnFocusChangeListener { v, hasFocus ->
            Log.d(TAG, "homeListeners: setOnFocusChangeListener ")
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setMenuIconFocusView(R.drawable.ic_home_selected, home_IB, home_LL, home_TV ,hasFocus);
                    focusIn(home_LL, 0)
                }
            } else {
                if (isNavigationOpen()) {
                    setMenuIconFocusView(R.drawable.ic_home_unselected, home_IB, home_LL, home_TV ,hasFocus);
                    focusOut(home_LL, 0)
                }
            }
        }

        home_IB.setOnKeyListener { v, keyCode, event ->
            Log.d(TAG, "homeListeners: setOnKeyListener " + event.action + "  "+ keyCode)
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = home
                        fragmentChangeListener.switchFragment(home)
                        /*lastSelectedMenu = home
                        fragmentChangeListener.switchFragment(home)*/
                        closeNav()
                    }
//                    KeyEvent.KEYCODE_DPAD_UP -> {
//                        if (!home_IB.isFocusable)
//                            home_IB.isFocusable = true
//                        switchUserAllowedToGainFocus = true
//                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = home
                        fragmentChangeListener.switchFragment(Constants.nav_menu_home)
                        closeNav()
                    }
                }
            }
            false
        }
    }

    private fun searchListner() {
//        search_LL.setOnClickListener(View.OnClickListener {
//
//        })
        search_IB.setOnFocusChangeListener { v, hasFocus ->
            Log.d(TAG, "searchListner: "+ hasFocus)
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setMenuIconFocusView(R.drawable.ic_search_selected, search_IB, search_LL, search_TV ,hasFocus);
                    focusIn(search_LL, 0)
                }
            } else {
                if (isNavigationOpen()) {
                    setMenuIconFocusView(R.drawable.ic_search_unselected, search_IB, search_LL, search_TV ,hasFocus);
                    focusOut(search_LL, 0)
                }
            }
        }

        search_IB.setOnKeyListener { v, keyCode, event ->
            Log.d(TAG, "searchListner: "+event.action + " " + keyCode)
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = search
                        fragmentChangeListener.switchFragment(search)

//                        lastSelectedMenu = search
//                        fragmentChangeListener.switchFragment(home)
                        closeNav()
                    }
//                    KeyEvent.KEYCODE_DPAD_UP -> {
//                        if (!search_IB.isFocusable)
//                            search_IB.isFocusable = true
//                        switchUserAllowedToGainFocus = true
//                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = search
                        fragmentChangeListener.switchFragment(search)
//                        fragmentChangeListener.switchFragment(Constants.nav_menu_search)
                        closeNav()
                    }
                }
            }
            false
        }
    }

    private fun schoolListner() {

        school_IB.setOnFocusChangeListener { v, hasFocus ->
            Log.d(TAG, "schoolListner: "+ hasFocus)
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setMenuIconFocusView(R.drawable.ic_school_selected, school_IB, school_channel_ll, school_TV ,hasFocus);
                    focusIn(school_channel_ll, 0)
                }
            } else {
                if (isNavigationOpen()) {
                    setMenuIconFocusView(R.drawable.ic_school_unselected, school_IB, school_channel_ll, school_TV ,hasFocus);
                    focusOut(school_channel_ll, 0)
                }
            }
        }

        school_IB.setOnKeyListener { v, keyCode, event ->
            Log.d(TAG,  "schoolListner: "+ event.action + " " + keyCode)
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = school
                        fragmentChangeListener.switchFragment(school)
                        closeNav()
                    }
//                    KeyEvent.KEYCODE_DPAD_UP -> {
//                        if (!school_IB.isFocusable)
//                            school_IB.isFocusable = true
//                        switchUserAllowedToGainFocus = true
//                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = home
                        fragmentChangeListener.switchFragment(school)
                        closeNav()
                    }
                }
            }
            false
        }
    }

    private fun thematicListner() {

        thematic_channel_IB.setOnFocusChangeListener{ v, hasFocus ->
            Log.d(TAG, "schoolListner: "+ hasFocus)
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setMenuIconFocusView(R.drawable.ic_thematic_selected, thematic_channel_IB, thematic_channel_ll, thematic_channel_TV ,hasFocus);
                    focusIn(school_channel_ll, 0)
                }
            } else {
                if (isNavigationOpen()) {
                    setMenuIconFocusView(R.drawable.ic_thematic_unselected, thematic_channel_IB, thematic_channel_ll, thematic_channel_TV ,hasFocus);
                    focusOut(school_channel_ll, 0)
                }
            }
        }

        thematic_channel_IB.setOnKeyListener { v, keyCode, event ->
            Log.d(TAG,  "schoolListner: "+ event.action + " " + keyCode)
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = thematic
                        fragmentChangeListener.switchFragment(thematic)
                        closeNav()
                    }
//                    KeyEvent.KEYCODE_DPAD_UP -> {
//                        if (!school_IB.isFocusable)
//                            school_IB.isFocusable = true
//                        switchUserAllowedToGainFocus = true
//                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = thematic
                        fragmentChangeListener.switchFragment(thematic)
                        closeNav()
                    }
                }
            }
            false
        }
    }

    private fun freeChannelListner() {

        free_channel_IB.setOnFocusChangeListener { v, hasFocus ->
            Log.d(TAG, "schoolListner: "+ hasFocus)
            if (hasFocus) {
                setMenuIconFocusView(R.drawable.ic_free_selected, free_channel_IB, free_channel_ll, free_channel_TV ,hasFocus);
                if (isNavigationOpen()) {
                    submenu_ll.visibility = View.VISIBLE;
                    setMenuIconFocusView(R.drawable.ic_free_selected, free_channel_IB, free_channel_ll, free_channel_TV ,hasFocus);
                    focusIn(school_channel_ll, 0)
                }
            }
            else {
                if (isNavigationOpen()) {
                    setMenuIconFocusView(R.drawable.ic_free_unselected, free_channel_IB, free_channel_ll, free_channel_TV ,hasFocus);
                    focusOut(school_channel_ll, 0)
                }
            }
        }

//        free_channel_IB.setOnClickListener {
//           // Log.d(TAG, "schoolListner: "+ hasFocus)
//                if (isNavigationOpen()) {
//                    submenu_ll.visibility = View.VISIBLE;
//                    setMenuIconFocusView(R.drawable.ic_free_selected, free_channel_IB, free_channel_ll, free_channel_TV , true);
//                    focusIn(school_channel_ll, 0)
//                }
//             else {
//                if (isNavigationOpen()) {
//                    setMenuIconFocusView(R.drawable.ic_free_unselected, free_channel_IB, free_channel_ll, free_channel_TV ,true);
//                    focusOut(school_channel_ll, 0)
//                }
//            }
//        }

        free_channel_IB.setOnKeyListener { v, keyCode, event ->
            Log.d(TAG,  "schoolListner: "+ event.action + " " + keyCode)
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = freeChannel
                        fragmentChangeListener.switchFragment(freeChannel)
                        closeNav()
                     //   setMenuIconFocusView(R.drawable.ic_free_unselected, free_channel_IB, free_channel_ll, free_channel_TV ,true);
//                        if(isCheck)
//                        {
//                            submenu_ll.visibility = View.VISIBLE;
//                           isCheck=!isCheck
//                        }
//                        else
//                        {
//                            submenu_ll.visibility = View.GONE;
//                            isCheck=!isCheck
//                        }
                    }
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        if (submenu_ll.visibility == View.VISIBLE && submenu_ll.getChildAt(0)!= null) {
                            submenu_ll.requestFocus()
                            return@setOnKeyListener true;
                        }
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = freeChannel
                        fragmentChangeListener.switchFragment(freeChannel)
                        closeNav()
                    }
                }
            }
            false
        }
    }

    private fun accountListner() {

        account_IB.setOnFocusChangeListener { v, hasFocus ->
            Log.d(TAG, "schoolListner: "+ hasFocus)
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setMenuIconFocusView(R.drawable.ic_account_selected, account_IB, account_LL, account_TV ,hasFocus);
                    focusIn(school_channel_ll, 0)
                }
            } else {
                if (isNavigationOpen()) {
                    setMenuIconFocusView(R.drawable.ic_account_unselected, account_IB, account_LL, account_TV ,hasFocus);
                    focusOut(school_channel_ll, 0)
                }
            }
        }

        account_IB.setOnKeyListener { v, keyCode, event ->
            Log.d(TAG,  "schoolListner: "+ event.action + " " + keyCode)
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = account
                        fragmentChangeListener.switchFragment(account)
                        closeNav()
                    }
//                    KeyEvent.KEYCODE_DPAD_UP -> {
//                        if (!school_IB.isFocusable)
//                            school_IB.isFocusable = true
//                        switchUserAllowedToGainFocus = true
//                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = account
                        fragmentChangeListener.switchFragment(account)
                        closeNav()
                    }
                }
            }
            false
        }
    }

    private fun logoutListner() {

        logout_IB.setOnFocusChangeListener { v, hasFocus ->
            Log.d(TAG, "schoolListner: "+ hasFocus)
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setMenuIconFocusView(R.drawable.ic_logout_selected, logout_IB, logout_LL, logout_TV ,hasFocus);
                    focusIn(logout_LL, 0)
                }
            } else {
                if (isNavigationOpen()) {
                    setMenuIconFocusView(R.drawable.ic_logout_unselected, logout_IB, logout_LL, logout_TV ,hasFocus);
                    focusOut(logout_TV, 0)
                }
            }
        }

        logout_IB.setOnKeyListener { v, keyCode, event ->
            Log.d(TAG,  "schoolListner: "+ event.action + " " + keyCode)
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = logout
                        fragmentChangeListener.switchFragment(logout)
                    }
//                    KeyEvent.KEYCODE_DPAD_UP -> {
//                        if (!school_IB.isFocusable)
//                            school_IB.isFocusable = true
//                        switchUserAllowedToGainFocus = true
//                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = logout
                        fragmentChangeListener.switchFragment(logout)
//                        closeNav()
                    }
                }
            }
            false
        }
    }


    fun setFragmentChangeListener(callback: FragmentChangeListener) {
        this.fragmentChangeListener = callback
    }

    fun setNavigationStateListener(callback: NavigationStateListener) {
        this.navigationStateListener = callback
    }
    fun setNavigationInitializeListener(callback: NavigationInitializeListener) {
        this.navigationInitializeListener = callback
    }

    fun initializeSubMenu(subMenu: Array<String>?) {
        try {
            submenu_ll.removeAllViews()
        if (subMenu != null) {
            var i: Int = 0;
            for (menu in subMenu) {
                val index = i;
                i = i + 1;
                val inflater = LayoutInflater.from(context);
                val view: Button =
                    inflater.inflate(R.layout.item_sub_menu, submenu_ll, false) as Button;
                view.setText(menu)

                view.setOnKeyListener { v, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN) {
                        when (keyCode) {
                            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                                closeNav()
                                navigationStateListener.onStateChanged(false, lastSelectedMenu)
                            }
                            KeyEvent.KEYCODE_DPAD_DOWN -> {
                                if (index == subMenu.size - 1) {
                                    account_IB.requestFocus()
                                    return@setOnKeyListener true
                                } else {
                                    view.requestFocus()
                                }
                            }
                            KeyEvent.KEYCODE_DPAD_UP -> {
                                if (index == 0) {
                                    free_channel_IB.requestFocus()
                                    return@setOnKeyListener true
                                } else {
                                    view.requestFocus()
                                }
                            }
                            KeyEvent.KEYCODE_DPAD_CENTER -> {
                                lastSelectedMenu = freeChannel
                                selectedSubMenu = index
                                fragmentChangeListener.switchFragment(freeChannel, menu)
                                closeNav()
                            }
                            KeyEvent.KEYCODE_ENTER -> {
                                lastSelectedMenu = freeChannel
                                selectedSubMenu = index
                              try{
                                  fragmentChangeListener.switchFragment(freeChannel, menu)
                              }catch (e:Exception){
                                  e.printStackTrace()
                              }
                                closeNav()
                            }
                        }
                    }
                    false;
                }
                submenu_ll.addView(view);
            }
        }
    }
    catch (e:Exception){
            Log.d("main", "initializeSubMenu: ${e.message}")
        }
    }
//    fun setSelectedMenu(navMenuName: String) {
//        when (navMenuName) {
//            shows -> {
//                lastSelectedMenu = shows
//            }
//            movies -> {
//                lastSelectedMenu = movies
//            }
//        }
//
//        highlightMenuSelection(lastSelectedMenu)
//        unHighlightMenuSelections(lastSelectedMenu)
//
//    }

}