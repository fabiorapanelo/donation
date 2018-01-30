package fabiorapanelo.com.donation.activity

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import fabiorapanelo.com.donation.R
import fabiorapanelo.com.donation.fragment.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // tags used to attach the fragments
    var navItemIndex = 0

    private val INDEX_USER_INFORMATION = 0
    private val INDEX_ADD_TICKET = 1
    private val INDEX_CAMPAIGN = 2
    private val INDEX_MANAGE_CAMPAIGN = 3
    private val INDEX_PARTNER = 4
    private val INDEX_MANAGE_PARTNER = 5
    private val INDEX_USER_MANAGEMENT = 6

    private val TAG_USER_INFORMATION = "USER_INFORMATION"
    private val TAG_ADD_TICKET = "ADD_TICKET"
    private val TAG_CAMPAIGN = "CAMPAIGN"
    private val TAG_MANAGE_CAMPAIGN = "MANAGER_CAMPAIGN"
    private val TAG_PARTNER = "PARTNER"
    private val TAG_MANAGE_PARTNER = "MANAGER_PARTNER"
    private val TAG_USER_MANAGEMENT = "USER_MANAGEMENT"

    var CURRENT_TAG = TAG_CAMPAIGN

    private var tabsName: Array<String>? = null
    private var mHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        tabsName = resources.getStringArray(R.array.tabs)

        mHandler = Handler()

        fab.setOnClickListener { view ->
            if(navItemIndex == INDEX_USER_INFORMATION){
                navItemIndex = INDEX_ADD_TICKET
                CURRENT_TAG = TAG_ADD_TICKET
                loadCurrentFragment()
            };

            if(navItemIndex == INDEX_PARTNER){
                navItemIndex = INDEX_MANAGE_PARTNER
                CURRENT_TAG = TAG_MANAGE_PARTNER
                loadCurrentFragment()
            };

            if(navItemIndex == INDEX_CAMPAIGN){
                navItemIndex = INDEX_MANAGE_CAMPAIGN;
                CURRENT_TAG = TAG_MANAGE_CAMPAIGN;
                loadCurrentFragment()
            };
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            navItemIndex = INDEX_USER_INFORMATION;
            CURRENT_TAG = TAG_USER_INFORMATION;
            loadCurrentFragment();
        }
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private fun loadCurrentFragment() {
        // selecting appropriate nav menu item
        nav_view.menu.getItem(navItemIndex).isChecked = true
        supportActionBar?.title = tabsName!![navItemIndex]

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (supportFragmentManager.findFragmentByTag(CURRENT_TAG) != null) {
            drawer_layout.closeDrawers()

            // show or hide the fab button
            toggleFab()
            return
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        val mPendingRunnable = Runnable {
            // update the main content by replacing fragments
            val fragment = getCurrentFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out)
            fragmentTransaction.replace(R.id.frame_layout, fragment, CURRENT_TAG)
            fragmentTransaction.commitAllowingStateLoss()
        }

        // Add to the message queue
        mHandler!!.post(mPendingRunnable)

        // show or hide the fab button
        toggleFab()

        //Closing drawer on item click
        drawer_layout.closeDrawers()

        // refresh toolbar menu
        invalidateOptionsMenu()
    }

    private fun getCurrentFragment(): Fragment {
        // home
        when (navItemIndex) {
            INDEX_USER_INFORMATION -> return UserInformationFragment.newInstance()
            INDEX_ADD_TICKET -> return AddTicketFragment.newInstance()
            INDEX_CAMPAIGN -> return CampaignsFragment.newInstance(500)
            INDEX_MANAGE_CAMPAIGN -> return ManageCampaignFragment.newInstance()
            INDEX_PARTNER -> return PartnersFragment.newInstance(500);
            INDEX_MANAGE_PARTNER -> return ManagePartnerFragment.newInstance();
            INDEX_USER_MANAGEMENT -> return UserManagementFragment.newInstance()
            else -> {
                return UserInformationFragment.newInstance()
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_user_info -> {
                navItemIndex = INDEX_USER_INFORMATION
                CURRENT_TAG = TAG_USER_INFORMATION
            }
            R.id.nav_add_ticket -> {
                navItemIndex = INDEX_ADD_TICKET
                CURRENT_TAG = TAG_ADD_TICKET
            }
            R.id.nav_campaigns -> {
                navItemIndex = INDEX_CAMPAIGN
                CURRENT_TAG = TAG_CAMPAIGN
            }
            R.id.nav_add_campaign -> {
                navItemIndex = INDEX_MANAGE_CAMPAIGN
                CURRENT_TAG = TAG_MANAGE_CAMPAIGN
            }
            R.id.nav_partners -> {
                navItemIndex = INDEX_PARTNER
                CURRENT_TAG = TAG_PARTNER
            }
            R.id.nav_add_partner -> {
                navItemIndex = INDEX_MANAGE_PARTNER
                CURRENT_TAG = TAG_MANAGE_PARTNER
            }
            R.id.nav_manage -> {
                navItemIndex = INDEX_USER_MANAGEMENT
                CURRENT_TAG = TAG_USER_MANAGEMENT
            }
        }

        loadCurrentFragment();
        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }

    // show or hide the fab
    private fun toggleFab() {
        fab.hide();

        when (navItemIndex) {
            INDEX_USER_INFORMATION -> {
                fab.setImageResource(R.drawable.ic_loyalty_white_24dp)
                fab.show();
            }
            INDEX_CAMPAIGN -> {
                fab.setImageResource(R.drawable.ic_add_white_24dp)
                fab.show();
            }
            INDEX_PARTNER -> {
                fab.setImageResource(R.drawable.ic_add_white_24dp)
                fab.show();
            }
        }
    }
}
