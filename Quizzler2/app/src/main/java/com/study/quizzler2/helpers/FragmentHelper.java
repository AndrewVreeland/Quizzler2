package com.study.quizzler2.helpers;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.study.quizzler2.R;

import com.study.quizzler2.fragments.ChatFragment;
import com.study.quizzler2.interfaces.updateTriviaTextInterface;

public class FragmentHelper {

    public static void replaceFragmentWithDelay(FragmentManager fragmentManager, int index, updateTriviaTextInterface.OnTextUpdateListener listener, View fragmentView) {
        final int DELAY_MILLISECONDS = 0; // Adjust this value as needed (in milliseconds)

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    switch (index) {
                        case 0:
                            // Replace the current fragment with the HomeFragment
                            generateRandomFactAndActivateButton(fragmentManager, "Science", fragmentView, listener);
                            break;
                        case 1:
                            // Replace the current fragment with the AnimalsFragment
                            generateRandomFactAndActivateButton(fragmentManager, "Animals", fragmentView, listener);
                            break;
                        case 2:
                            // Replace the current fragment with the HistoryFragment
                            generateRandomFactAndActivateButton(fragmentManager, "History", fragmentView, listener);
                            break;
                        case 3:
                            // Replace the current fragment with the GamesFragment
                            generateRandomFactAndActivateButton(fragmentManager, "Games", fragmentView, listener);
                            break;
                        case 4:
                            // Replace the current fragment with the MusicFragment
                            generateRandomFactAndActivateButton(fragmentManager, "Music", fragmentView, listener);
                            break;
                    }
                }
            }
        }, DELAY_MILLISECONDS);
    }

    private static void generateRandomFactAndActivateButton(FragmentManager fragmentManager, String topic, View fragmentView, updateTriviaTextInterface.OnTextUpdateListener listener) {
        // Generate a random fact for the provided topic
        ChatGPTRandomFact.generateRandomFact(TopicUtility.getTopicIndex(topic), fragmentView.getContext(), new ChatGPTRandomFact.RandomFactListener() {
            @Override
            public void onRandomFactGenerated(String randomFact) {
                // Print the generated fact to the console
                System.out.println("Generated Random Fact: " + randomFact);

                // Update the text with the random fact on the UI thread
                fragmentView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.updateText(randomFact);
                        }
                        activateButton(fragmentManager, fragmentView, randomFact); // Pass the random fact to the activateButton method
                    }
                });
            }
        });
    }

    private static void activateButton(FragmentManager fragmentManager, View fragmentView, String randomFact) {
        // Update the button visibility and functionality
        Button yourButton = fragmentView.findViewById(R.id.learn_more_btn);
        yourButton.setVisibility(View.VISIBLE); // Make the button visible
        yourButton.setEnabled(true); // Enable the button functionality

        // Set OnClickListener for the button
        yourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the fragment transaction when the button is clicked
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ChatFragment chatFragment = ChatFragment.newInstance("I want to learn more about " + randomFact);
                fragmentTransaction.replace(R.id.fragment_container, chatFragment);
                fragmentTransaction.addToBackStack(null); // Add the transaction to the back stack
                fragmentTransaction.commit();
            }
        });
    }

}