package com.study.quizzler2.helpers;

import androidx.fragment.app.FragmentManager;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.study.quizzler2.R;
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
                            listener.updateText("Home Fragment Text");
                            activateButton(fragmentView);
                            break;
                        case 1:
                            // Replace the current fragment with the AnimalsFragment
                            listener.updateText("Animals Fragment Text");
                            activateButton(fragmentView);
                            break;
                        case 2:
                            // Replace the current fragment with the HistoryFragment
                            listener.updateText("History Fragment Text");
                            activateButton(fragmentView);
                            break;
                        case 3:
                            // Replace the current fragment with the GamesFragment
                            listener.updateText("Games Fragment Text");
                            activateButton(fragmentView);
                            break;
                        case 4:
                            // Replace the current fragment with the MusicFragment
                            listener.updateText("Music Fragment Text");
                            activateButton(fragmentView);
                            break;
                    }
                }
            }
        }, DELAY_MILLISECONDS);
    }
    private static void activateButton(View fragmentView){
        // Update the button visibility and functionality
        Button yourButton = fragmentView.findViewById(R.id.learn_more_btn);
        yourButton.setVisibility(View.VISIBLE); // Make the button visible
        yourButton.setEnabled(true); // Enable the button functionality
    }
}