package com.study.quizzler2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult;
import com.google.android.material.navigation.NavigationView;
import com.study.quizzler2.fragments.HomeFragment;
import com.study.quizzler2.fragments.LoginFragment;
import com.study.quizzler2.managers.UserManager;
import com.amplifyframework.core.Amplify;
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        userManager = new UserManager(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Set up the drawer toggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Enable the hamburger icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            if (userManager.isLoggedIn()) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .commit();
            }




        }

        // Set up item click listener for the navigation view
        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle navigation view item clicks here.
            switch (item.getItemId()) {
                case R.id.nav_home:
                    // Do something for home
                    break;
                case R.id.nav_logout:
                    Amplify.Auth.signOut( signOutResult -> {
                        if (signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut
                                || signOutResult instanceof AWSCognitoAuthSignOutResult.PartialSignOut) {

                            // On successful sign out or even a partial sign out, redirect the user to the LoginFragment.
                            runOnUiThread(() -> {
                                userManager.setLoggedIn(false);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new LoginFragment())
                                        .commit();

                                // Depending on the type of sign out, show an appropriate toast.
                                if (signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut) {
                                    Toast.makeText(MainActivity.this, "Signed out successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Partial sign out. Please check for potential issues.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.FailedSignOut) {
                            // Handle the failed sign out.
                            AWSCognitoAuthSignOutResult.FailedSignOut failedSignOutResult =
                                    (AWSCognitoAuthSignOutResult.FailedSignOut) signOutResult;
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Error during logout: " + failedSignOutResult.getException().toString(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                    break;
                // Handle other ids
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}