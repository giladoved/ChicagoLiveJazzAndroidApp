package giladoved.chicagolivejazz.Activities;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.SubMenu;

import com.google.firebase.analytics.FirebaseAnalytics;

import giladoved.chicagolivejazz.Fragments.*;
import giladoved.chicagolivejazz.R;

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    Class fragmentClass;
    Fragment fragment;

    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);

        analytics = FirebaseAnalytics.getInstance(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentClass = Splash.class;
        fragmentManager = getSupportFragmentManager();
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragmentPage, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        toolbar.setTitle("Chicago Live Jazz");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            MenuItem menuItem = navigationView.getMenu().getItem(i);
            if (menuItem.hasSubMenu()) {
                SubMenu subMenu = menuItem.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    subMenuItem.setChecked(false);
                }
            } else {
                menuItem.setChecked(false);
            }
        }

        String screen = "";
        int id = item.getItemId();
        if (id == R.id.green_mill) {
            fragmentClass = GreenMill.class;
            screen = "greenmill";
        } else if (id == R.id.jazz_showcase) {
            fragmentClass = JazzShowcase.class;
            screen = "jazzshowcase";
        } else if (id == R.id.requestclub) {
            fragmentClass = RequestClub.class;
            screen = "requestclub";
        } else if (id == R.id.about) {
            fragmentClass = About.class;
            screen = "about";
        } else {
            screen = "splash";
            fragmentClass = Splash.class;
        }

        Bundle bundle = new Bundle();
        bundle.putString("screen", screen);
        analytics.logEvent("select_screen", bundle);

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        fragmentManager.beginTransaction().replace(R.id.fragmentPage, fragment).commit();

        item.setChecked(true);
        toolbar.setTitle(item.getTitle());

        return true;
    }
}
