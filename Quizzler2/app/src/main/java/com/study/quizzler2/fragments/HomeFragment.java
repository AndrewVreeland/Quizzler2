package com.study.quizzler2.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Conversation;
import com.amplifyframework.datastore.generated.model.ConversationTypeEnum;
import com.amplifyframework.datastore.generated.model.Message;
import com.amplifyframework.datastore.generated.model.User;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.study.quizzler2.R;
import com.study.quizzler2.adapters.ConversationAdapter;
import com.study.quizzler2.helpers.DatabaseHelper;
import com.study.quizzler2.helpers.FragmentHelper;
import com.study.quizzler2.helpers.HamburgerMenuHelper;
import com.study.quizzler2.helpers.authentification.AuthHelper;
import com.study.quizzler2.interfaces.updateTriviaTextInterface;
import com.study.quizzler2.managers.UserManager;
import com.study.quizzler2.utils.TopicUtility;
import com.study.quizzler2.helpers.ConversationHelper;
import com.study.quizzler2.utils.ConversationItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements updateTriviaTextInterface.OnTextUpdateListener {

    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;
    private TextView textView;
    private Button learnMoreButton;
    private String currentCategory = "";
    private HamburgerMenuHelper hamburgerMenuHelper;

    private List<Conversation> conversations;
    private ConversationAdapter conversationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        constraintLayout = rootView.findViewById(R.id.constraint_layout);
        CircleMenu circleMenu = rootView.findViewById(R.id.circle_menu);
        textView = rootView.findViewById(R.id.homeFragmentTopTextView);
        progressBar = rootView.findViewById(R.id.progressBar);
        learnMoreButton = rootView.findViewById(R.id.learn_more_btn);
        conversations = new ArrayList<>();
        List<ConversationItem> conversationItems = ConversationHelper.convertToConversationItemList(conversations);

        textView.setVisibility(View.INVISIBLE);
        learnMoreButton.setVisibility(View.INVISIBLE);
        learnMoreButton.setEnabled(false);

        circleMenu.setMainMenu(Color.parseColor("#04378f"), R.mipmap.menu, R.mipmap.cancel)
                .addSubMenu(Color.parseColor("#88bef5"), R.mipmap.home)
                .addSubMenu(Color.parseColor("#83e85a"), R.mipmap.animals)
                .addSubMenu(Color.parseColor("#FF4B32"), R.mipmap.history)
                .addSubMenu(Color.parseColor("#ba53de"), R.mipmap.games)
                .addSubMenu(Color.parseColor("#ff8a5c"), R.mipmap.music)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        learnMoreButton.setVisibility(View.INVISIBLE);
                        learnMoreButton.setEnabled(false);
                        textView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);

                        FragmentHelper.replaceFragmentWithDelay(
                                requireActivity().getSupportFragmentManager(),
                                index,
                                HomeFragment.this,
                                rootView,
                                new FragmentHelper.FragmentReplaceListener() {
                                    @Override
                                    public void onFragmentReplaced() {
                                        updateLearnMoreButtonVisibility();
                                    }
                                }
                        );
                    }
                });



        UserManager userManager = new UserManager(requireContext());
        AuthHelper authHelper = new AuthHelper((FragmentActivity) requireActivity(), userManager);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);

        // Initialize the conversationAdapter here
        conversationAdapter = new ConversationAdapter(conversationItems, null);

        // Initialize the hamburgerMenuHelper
        hamburgerMenuHelper = new HamburgerMenuHelper(activity, drawerLayout, authHelper, conversations, conversationAdapter);

        return rootView;
    }


    @Override
    public void updateText(String newText, String category) {
        this.currentCategory = category;
        textView.setText(newText);
        progressBar.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        updateLearnMoreButtonVisibility();
    }

    private void updateLearnMoreButtonVisibility() {
        ProgressBar progressBar = requireView().findViewById(R.id.progressBar);
        Button learnMoreButton = requireView().findViewById(R.id.learn_more_btn);

        if (learnMoreButton != null && progressBar != null) {
            if (progressBar.getVisibility() == View.VISIBLE) {
                learnMoreButton.setVisibility(View.INVISIBLE);
                learnMoreButton.setEnabled(false);
            } else {
                learnMoreButton.setVisibility(View.VISIBLE);
                learnMoreButton.setEnabled(true);

                learnMoreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, ChatFragment.newInstance("I want to learn more about \"" + textView.getText().toString() + "\"."))
                                .addToBackStack(null)
                                .commit();

                        createNewConversation();
                    }
                });
            }
        }
    }

    private void createNewConversation() {
        Amplify.Auth.getCurrentUser(new com.amplifyframework.core.Consumer<com.amplifyframework.auth.AuthUser>() {
            @Override
            public void accept(com.amplifyframework.auth.AuthUser authUser) {
                User userObj = User.justId(authUser.getUserId());
                ConversationTypeEnum conversationType = TopicUtility.getEnumFromCategory(currentCategory);

                Conversation conversation = Conversation.builder()
                        .user(userObj)
                        .conversationType(conversationType)
                        .build();

                Amplify.API.mutate(
                        ModelMutation.create(conversation),
                        response -> {
                            Log.i("CreateConversation", "Added conversation with id: " + response.getData().getId());
                            String conversationID = response.getData().getId();
                            saveMessageAfterConversation(conversationID, "I want to learn more about \"" + textView.getText().toString() + "\".");

                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hamburgerMenuHelper.addConversation(conversation);
                                }
                            });
                        },
                        error -> Log.e("CreateConversation", "Failed to create conversation.", error)
                );
            }
        }, new com.amplifyframework.core.Consumer<com.amplifyframework.auth.AuthException>() {
            @Override
            public void accept(@NonNull com.amplifyframework.auth.AuthException authException) {
                Log.e("GetCurrentUser", "Failed to retrieve current user.", authException);
            }
        });
    }

    private void saveMessageAfterConversation(String conversationID, String messageText) {
        Conversation conversationReference = Conversation.justId(conversationID);

        Message newMessage = Message.builder()
                .content(messageText)
                .version(1)
                .lastChangedAt(new Temporal.Timestamp(new Date()))
                .createdAt(DatabaseHelper.getCurrentAmplifyDateTime())
                .updatedAt(DatabaseHelper.getCurrentAmplifyDateTime())
                .conversation(conversationReference)
                .build();

        Amplify.API.mutate(
                ModelMutation.create(newMessage),
                response -> {
                    Log.i("SaveMessage", "Added message with id: " + response.getData().getId());
                },
                error -> {
                    Log.e("SaveMessage", "Failed to save message.", error);
                }
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().overridePendingTransition(0, 0);
    }
}