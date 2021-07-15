package com.ncgtelevision.net.home_screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.androijo.tvnavigation.HeaderMenu
import com.androijo.tvnavigation.interfaces.FragmentChangeListener
import com.androijo.tvnavigation.interfaces.NavigationInitializeListener
import com.androijo.tvnavigation.interfaces.NavigationStateListener
import com.androijo.tvnavigation.utils.Constants
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ncgtelevision.net.R
import com.ncgtelevision.net.account.MembershipFragment
import com.ncgtelevision.net.channel.ChannelFragment
import com.ncgtelevision.net.interfaces.NavigationMenuCallback
import com.ncgtelevision.net.local_storage.TokenStorage
import com.ncgtelevision.net.search_screen.SearchFragment
import com.ncgtelevision.net.signin.LogoutFragment
import com.ncgtelevision.net.signin.SignInActivity
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext


class HomeActivity : FragmentActivity(), NavigationStateListener, FragmentChangeListener,
    NavigationMenuCallback, NavigationInitializeListener {
    var TAG="HomeActivity"
    private lateinit var mCurrentFragment: String
    private lateinit var main_FL: FrameLayout
    private lateinit var nav_fragment: FrameLayout
    private lateinit var headerMenu: HeaderMenu
    private lateinit var newHomeFragment: NewHomeFragment
    private lateinit var membershipFragment: MembershipFragment
    private lateinit var channelFragment: ChannelFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var logoutFragment: LogoutFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.i(TAG, "onCreate: token"+TokenStorage.readSharedToken(this@HomeActivity))
        headerMenu = HeaderMenu()
        fragmentReplacer3(R.id.nav_fragment, headerMenu)
        nav_fragment = findViewById<FrameLayout>(R.id.nav_fragment);
        main_FL = findViewById<FrameLayout>(R.id.main_FL)
        newHomeFragment = NewHomeFragment()
        membershipFragment = MembershipFragment()
        mCurrentFragment = Constants.nav_menu_home
        fragmentReplacer(R.id.main_FL, newHomeFragment)
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log("main")
        crashlytics.log("E/TAG: main")
        navMenuToggle(false)
        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext: SSLContext
            sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
    }

    private fun fragmentReplacer3(containerId: Int, headerMenu: HeaderMenu) {
        supportFragmentManager.beginTransaction()
            .replace(containerId, headerMenu)
            //.addToBackStack("amit")
            .commit()
    }

    private fun fragmentReplacer(containerId: Int, fragment: Fragment) {
        if(supportFragmentManager.backStackEntryCount > 0){
            for (i in 0..supportFragmentManager.backStackEntryCount) {
               //  supportFragmentManager.popBackStack("amit", FragmentManager.POP_BACK_STACK_INCLUSIVE)
               // supportFragmentManager.popBackStack()
            }
//            for(fragments in supportFragmentManager.getFragments()){
//                if(fragments is HeaderMenu){ // .tag.equals("amit")
//                    Log.d("TAG", "fragmentReplacer: HeaderMenu")
//                  continue;
//                }else{
//                    supportFragmentManager.beginTransaction().remove(fragment).commit()
//                }
//            }
                supportFragmentManager.beginTransaction()
                    .replace(containerId, fragment)
                    .addToBackStack("amit")
                    .commit()
           // }
        }

        else
        {
            supportFragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack("amit")
                .commit()
        }
    }
    private fun fragmentReplacer2(containerId: Int, fragment: LogoutFragment) {
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack("amit")
            .commit()
    }

    override fun onStateChanged(expanded: Boolean, lastSelected: String?) {
        if (!expanded) {
//            nav_fragment.setBackgroundResource(R.drawable.ic_nav_bg_closed)
//            nav_fragment.clearFocus()
            navMenuToggle(false)
            when(mCurrentFragment){
                Constants.nav_menu_search ->{
                    mCurrentFragment = Constants.nav_menu_search
                    searchFragment?.intializeFirstFocus()
                }
                Constants.nav_menu_home ->{
                    mCurrentFragment = Constants.nav_menu_home
                    newHomeFragment?.intializeFirstFocus()
                }
                Constants.nav_menu_account ->{
                    mCurrentFragment = Constants.nav_menu_account
                    membershipFragment?.intializeFirstFocus()
                }
                Constants.nav_menu_school ->{
                    mCurrentFragment = Constants.nav_menu_school
                    channelFragment?.intializeFirstFocus()
                }
                Constants.nav_menu_thematic ->{
                    mCurrentFragment = Constants.nav_menu_thematic
                    channelFragment?.intializeFirstFocus()
                }
                Constants.nav_menu_logout ->{
                    mCurrentFragment = Constants.nav_menu_logout
                    logoutFragment?.intializeFirstFocus()
                }
            }
        }
    }

    override fun switchFragment(fragmentName: String?) {
//        nav_fragment.setBackgroundResource(R.drawable.ic_nav_bg_closed)
        when(fragmentName){
            Constants.nav_menu_search ->{
                navMenuToggle(false)
                searchFragment = SearchFragment()
                mCurrentFragment = Constants.nav_menu_search
                fragmentReplacer(R.id.main_FL, searchFragment)
            }
            Constants.nav_menu_home ->{
                navMenuToggle(false)
                newHomeFragment = NewHomeFragment()
                mCurrentFragment = Constants.nav_menu_home
                fragmentReplacer(R.id.main_FL, newHomeFragment)
            }
            Constants.nav_menu_thematic ->{
                navMenuToggle(false)
                channelFragment = ChannelFragment.newInstance("Thematic Channel", "Thematic Channel")
                mCurrentFragment = Constants.nav_menu_thematic
                fragmentReplacer(R.id.main_FL, channelFragment)
            }
            Constants.nav_menu_school ->{
                navMenuToggle(false)
                channelFragment = ChannelFragment.newInstance("School Channel", "School Channel")
                mCurrentFragment = Constants.nav_menu_school
                fragmentReplacer(R.id.main_FL, channelFragment)
            }
            Constants.nav_menu_free ->{
                navMenuToggle(false)
                channelFragment = ChannelFragment.newInstance("NCG - Free Channel", "")
                mCurrentFragment = Constants.nav_menu_free
                fragmentReplacer(R.id.main_FL, channelFragment)
            }
            Constants.nav_menu_account ->{
                navMenuToggle(false)
                membershipFragment = MembershipFragment();
                mCurrentFragment = Constants.nav_menu_account
                fragmentReplacer(R.id.main_FL, membershipFragment)
            }
            Constants.nav_menu_logout ->{
//                showLogoutDialog()
                navMenuToggle(false)
                logoutFragment = LogoutFragment.newInstance(mCurrentFragment, "");
                mCurrentFragment = Constants.nav_menu_logout
                fragmentReplacer2(R.id.main_FL, logoutFragment)
            }

        }
    }

    override fun switchFragment(fragmentName: String, menu: String) {
//        nav_fragment.setBackgroundResource(R.drawable.ic_nav_bg_closed)
        navMenuToggle(false)
        when(fragmentName){
            Constants.nav_menu_thematic ->{
                navMenuToggle(false)
                channelFragment = ChannelFragment.newInstance(menu, "")
                mCurrentFragment = Constants.nav_menu_thematic
                fragmentReplacer(R.id.main_FL, channelFragment)
            }
            Constants.nav_menu_school ->{
                navMenuToggle(false)
                channelFragment = ChannelFragment.newInstance(menu, "")
                mCurrentFragment = Constants.nav_menu_school
                fragmentReplacer(R.id.main_FL, channelFragment)
            }
            Constants.nav_menu_free ->{
                navMenuToggle(false)
                channelFragment = ChannelFragment.newInstance(menu, "")
                mCurrentFragment = Constants.nav_menu_free
                fragmentReplacer(R.id.main_FL, channelFragment)
            }
        }
    }
    override fun navMenuToggle(toShow: Boolean) {
        try {
            if (toShow)
            {
                nav_fragment.setBackgroundResource(R.drawable.ic_nav_bg_open)
                main_FL.clearFocus()
                nav_fragment.requestFocus()
                navEnterAnimation()
                val parms = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//                parms.setMargins(240,0,0,0 )
                parms.width = 270
                nav_fragment.layoutParams = parms
//                main_FL.layoutParams = parms;
                headerMenu.openNav()
            }
            else
            {
                nav_fragment.setBackgroundResource(R.drawable.ic_nav_bg_closed)
                main_FL.layoutParams
                nav_fragment.clearFocus()
                main_FL.requestFocus()
                val parms = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                parms.width = 100
                nav_fragment.layoutParams = parms
//                parms.setMargins(60,0,0,0 )
//                main_FL.layoutParams = parms;
                headerMenu.closeNav()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun selectedOption(objects: Any) {
        if(objects is String){
            headerMenu.updateNav(objects)
            mCurrentFragment = objects
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        when(fragment) {
            is SearchFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is NewHomeFragment -> {
                fragment.setNavigationMenuCallback(this)
                fragment.setNavigationInitializeListener(this)
            }
            is MembershipFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is ChannelFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is LogoutFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is HeaderMenu -> {
                fragment.setFragmentChangeListener(this)
                fragment.setNavigationStateListener(this)
            }
        }
    }
    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
        //set title for alert dialog
        builder.setTitle("Alert!!!")
        //set message for alert dialog
        builder.setMessage("Do you want to logout?")
        builder.setIcon(R.drawable.splash_logo)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            startActivity(Intent(this@HomeActivity, SignInActivity::class.java))
            TokenStorage.clearSharedToken(this@HomeActivity);
            finish()
        }
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
        }
// Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun navEnterAnimation() {
        val animate = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        nav_fragment.startAnimation(animate)
    }

    override fun initializeSubMenu(subMenu: Array<String>?) {
        headerMenu?.initializeSubMenu(subMenu)
    }

    override fun changeMenu(menu: String, parentMenu: String) {
        switchFragment(parentMenu, menu);
        selectedOption(parentMenu);
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(supportFragmentManager.backStackEntryCount <=1) {
//                finish()
                showBackKeyDialog()
                return true;
            }
        }
        return super.onKeyDown(keyCode, event)
    }
    private fun showBackKeyDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
        //set title for alert dialog
        builder.setTitle("Alert!!!")
        //set message for alert dialog
        builder.setMessage("Do you want to exit from the App?")
        builder.setIcon(R.drawable.splash_logo)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            finish()
        }
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
        }
// Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}