package com.study.quizzler2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Conversation;
import com.google.android.material.navigation.NavigationView;
import com.study.quizzler2.adapters.ConversationAdapter;
import com.study.quizzler2.fragments.HomeFragment;
import com.study.quizzler2.fragments.LoginFragment;
import com.study.quizzler2.helpers.ConversationHelper;
import com.study.quizzler2.interfaces.ActionBarVisibility;
import com.study.quizzler2.managers.UserManager;
import com.study.quizzler2.helpers.authentification.AuthHelper;
import com.study.quizzler2.helpers.HamburgerMenuHelper;
import com.study.quizzler2.helpers.DatabaseHelper;
import com.study.quizzler2.utils.ConversationItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ActionBarVisibility {

    private DrawerLayout drawerLayout;
    private UserManager userManager;
    private AuthHelper authHelper;
    private HamburgerMenuHelper hamburgerMenuHelper;
    private ConversationAdapter conversationAdapter;
    private RecyclerView recyclerView;
    private List<Conversation> originalConversations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userManager = new UserManager(this);
        authHelper = new AuthHelper(this, userManager);
        recyclerView = findViewById(R.id.recyclerViewInDrawer);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.recyclerViewInDrawer);


        int desiredRecyclerViewHeight = 9;
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.height = desiredRecyclerViewHeight;
        recyclerView.setLayoutParams(layoutParams);

        // Initialize conversationAdapter with an empty list
        conversationAdapter = new ConversationAdapter(new ArrayList<>(), conversationId -> {
            // Handle conversation item click
        });
        recyclerView.setAdapter(conversationAdapter);

        fetchConversationsFromDynamoDB();

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

    }


    private void fetchConversationsFromDynamoDB() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        int limit = 100;

        DatabaseHelper.getConversations(limit,
                conversations -> {
                    // Store the original list of conversations
                    originalConversations = conversations;

                    runOnUiThread(() -> {
                        List<ConversationItem> conversationItems = ConversationHelper.convertToConversationItemList(conversations);
                        if (conversationAdapter == null) {
                            conversationAdapter = new ConversationAdapter(conversationItems, conversationId -> {
                                // Handle conversation item click
                            });
                            recyclerView.setAdapter(conversationAdapter);
                        }

                        // Initialize or update the conversations in the HamburgerMenuHelper
                        if (hamburgerMenuHelper == null) {
                            hamburgerMenuHelper = new HamburgerMenuHelper(MainActivity.this, drawerLayout, authHelper, originalConversations, conversationAdapter);
                            hamburgerMenuHelper.setupNavigationView(navigationView);
                        } else {
                            hamburgerMenuHelper.updateConversations(conversations);
                        }
                    });
                },
                error -> {
                    Log.e("MainActivity", "Failed to fetch conversations.", error);
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (hamburgerMenuHelper != null) {
            if (hamburgerMenuHelper.handleOptionsItemSelected(item)) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void hideActionBar() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    public void showActionBar() {
        Objects.requireNonNull(getSupportActionBar()).show();
    }

    public HamburgerMenuHelper getHamburgerMenuHelper() {
        return hamburgerMenuHelper;
    }

}