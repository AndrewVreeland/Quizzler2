package com.study.quizzler2.helpers;

import android.util.Log;
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
import com.study.quizzler2.fragments.ChatFragment;
import com.study.quizzler2.fragments.LoginFragment;
import com.study.quizzler2.helpers.authentification.AuthHelper;
import com.study.quizzler2.utils.ConversationItem;

import java.util.List;
import java.util.Objects;

public class HamburgerMenuHelper {

    private final AppCompatActivity activity;
    private DrawerLayout drawerLayout;
    private List<Conversation> conversations;
    private ActionBarDrawerToggle toggle;
    private final AuthHelper authHelper;
    private ConversationAdapter conversationAdapter;

    public HamburgerMenuHelper(AppCompatActivity activity, DrawerLayout drawerLayout, AuthHelper authHelper, List<Conversation> conversations, ConversationAdapter adapter) {
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        this.authHelper = authHelper;
        this.conversations = conversations;
        this.conversationAdapter = adapter;
        setupHamburgerIcon();
        setupDrawerToggle();
        setupRecyclerView();
    }





    private void setupHamburgerIcon() {
        activity.runOnUiThread(() -> {
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        });
    }



    private void setupDrawerToggle() {
        toggle = new ActionBarDrawerToggle(activity, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public boolean handleOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
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
                    handleLogout();
                    break;
                // Handle other ids
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void handleLogout() {
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
    }

    private void setupRecyclerView() {
        activity.runOnUiThread(() -> {
            NavigationView navigationView = activity.findViewById(R.id.nav_view);
            RecyclerView recyclerView = navigationView.findViewById(R.id.recyclerViewInDrawer);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));

            List<ConversationItem> conversationItems = ConversationHelper.convertToConversationItemList(conversations);

            conversationAdapter = new ConversationAdapter(conversationItems, conversationID -> {
                ChatFragment chatFragment = ChatFragment.newInstance(null, conversationID);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, chatFragment).commit();
            });

            recyclerView.setAdapter(conversationAdapter);
        });
    }

    public void updateConversations(List<Conversation> updatedConversations) {
        this.conversations = updatedConversations;

        activity.runOnUiThread(() -> {
            Log.d("HamburgerMenuHelper", "Updating conversations...");
            NavigationView navigationView = activity.findViewById(R.id.nav_view);
            RecyclerView recyclerView = navigationView.findViewById(R.id.recyclerViewInDrawer);
            List<ConversationItem> conversationItems = ConversationHelper.convertToConversationItemList(updatedConversations);
            conversationAdapter.updateData(conversationItems);
            Log.d("HamburgerMenuHelper", "Notifying adapter of data change...");
            conversationAdapter.notifyDataSetChanged(); // Notify adapter of the data change
        });
    }

    public void addConversation(Conversation newConversation) {
        Log.d("HamburgerMenuHelper", "Adding new conversation...");

        // Add the new conversation to the local list.
        this.conversations.add(newConversation);

        // Convert all conversations (including the new one) to ConversationItem format
        List<ConversationItem> allConversationItems = ConversationHelper.convertToConversationItemList(this.conversations);

        // Update the RecyclerView to reflect all conversations
        activity.runOnUiThread(() -> {
            conversationAdapter.updateData(allConversationItems); // This should update and notify the adapter.
            NavigationView navigationView = activity.findViewById(R.id.nav_view);
            RecyclerView recyclerView = navigationView.findViewById(R.id.recyclerViewInDrawer);
            recyclerView.smoothScrollToPosition(conversationAdapter.getItemCount() - 1);
            Log.d("HamburgerMenuHelper", "Notifying adapter of data change after adding conversation...");
        });
    }
}