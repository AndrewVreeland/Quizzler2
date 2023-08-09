package com.study.quizzler2.helpers;

import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.study.quizzler2.R;
import com.study.quizzler2.fragments.LoginFragment;
import com.study.quizzler2.helpers.authentification.AuthHelper;

public class HamburgerMenuHelper {

    private AppCompatActivity activity;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private AuthHelper authHelper;

    public HamburgerMenuHelper(AppCompatActivity activity, DrawerLayout drawerLayout, AuthHelper authHelper) {
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        this.authHelper = authHelper;
        setupHamburgerIcon();
        setupDrawerToggle();
    }

    private void setupHamburgerIcon() {
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupDrawerToggle() {
        toggle = new ActionBarDrawerToggle(activity, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    public void setupNavigationView(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    // Do something for home
                    break;
                case R.id.nav_logout:
                    authHelper.handleSignOut(result -> {
                        // This lambda will be called after the sign-out process has been completed.
                        activity.runOnUiThread(() -> {
                            if (result.isSuccess()) {
                                activity.getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new LoginFragment())
                                        .commit();
                            }
                        });
                    });
                    break;
                // Handle other ids
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}