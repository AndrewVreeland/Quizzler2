package com.study.quizzler2.helpers;

import android.view.MenuItem;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Conversation;
import com.google.android.material.navigation.NavigationView;
import com.study.quizzler2.R;
import com.study.quizzler2.adapters.ConversationAdapter;
import com.study.quizzler2.adapters.DrawerRecyclerViewAdapter;
import com.study.quizzler2.fragments.ChatFragment;
import com.study.quizzler2.fragments.LoginFragment;
import com.study.quizzler2.helpers.authentification.AuthHelper;

import java.util.List;

public class HamburgerMenuHelper {

    private AppCompatActivity activity;
    private DrawerLayout drawerLayout;
    private List<Conversation> conversations;
    private ActionBarDrawerToggle toggle;
    private AuthHelper authHelper;
    private DrawerRecyclerViewAdapter yourAdapter; // Assuming you have an adapter named YourRecyclerAdapter

    public HamburgerMenuHelper(AppCompatActivity activity, DrawerLayout drawerLayout, AuthHelper authHelper, List<Conversation> conversations) {
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        this.authHelper = authHelper;
        this.conversations = conversations;
        setupHamburgerIcon();
        setupDrawerToggle();
        setupRecyclerView();
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

    private void setupRecyclerView() {
        NavigationView navigationView = activity.findViewById(R.id.your_navigation_view_id);  // Replace with your NavigationView ID.
        RecyclerView recyclerView = navigationView.getHeaderView(0).findViewById(R.id.recyclerViewInDrawer);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        // Set up the adapter and assign it to the RecyclerView.
        ConversationAdapter adapter = new ConversationAdapter(conversations, new ConversationAdapter.OnConversationClickListener() {
            @Override
            public void onConversationClick(String conversationID) {
                ChatFragment chatFragment = ChatFragment.newInstance(null, conversationID);
                // Display the fragment.
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, chatFragment).commit();
            }
        });

        recyclerView.setAdapter(adapter);
    }
}