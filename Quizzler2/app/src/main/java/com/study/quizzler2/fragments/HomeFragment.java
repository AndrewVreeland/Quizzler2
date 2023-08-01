package com.study.quizzler2.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.study.quizzler2.R;
import com.study.quizzler2.helpers.FragmentHelper;
import com.study.quizzler2.interfaces.updateTriviaTextInterface;

import java.util.Objects;

public class HomeFragment extends Fragment implements updateTriviaTextInterface.OnTextUpdateListener {

    private CircleMenu circleMenu;
    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;
    private TextView textView;
    private Button learnMoreButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        constraintLayout = rootView.findViewById(R.id.constraint_layout);
        circleMenu = rootView.findViewById(R.id.circle_menu);
        textView = rootView.findViewById(R.id.homeFragmentTopTextView);
        progressBar = rootView.findViewById(R.id.progressBar);
        learnMoreButton = rootView.findViewById(R.id.learn_more_btn);

        // Initially disable the textView and Learn More button
        textView.setVisibility(View.INVISIBLE);
        learnMoreButton.setVisibility(View.INVISIBLE);
        learnMoreButton.setEnabled(false);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.menu, R.mipmap.cancel)
                .addSubMenu(Color.parseColor("#88bef5"), R.mipmap.home)
                .addSubMenu(Color.parseColor("#83e85a"), R.mipmap.animals)
                .addSubMenu(Color.parseColor("#FF4B32"), R.mipmap.history)
                .addSubMenu(Color.parseColor("#ba53de"), R.mipmap.games)
                .addSubMenu(Color.parseColor("#ff8a5c"), R.mipmap.music)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        // Hide the "Learn More" button when the menu is selected
                        learnMoreButton.setVisibility(View.INVISIBLE);
                        learnMoreButton.setEnabled(false);

                        // Show the progress bar while loading
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
                                        // After the fragment has been replaced, set the "Learn More" button visibility
                                        updateLearnMoreButtonVisibility();
                                    }
                                }
                        );
                    }
                });

        return rootView;
    }

    @Override
    public void updateText(String newText) {
        textView.setText(newText);
        progressBar.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);

        // Update the visibility of the "Learn More" button based on the progress bar
        updateLearnMoreButtonVisibility();
    }

    private void updateLearnMoreButtonVisibility() {
        ProgressBar progressBar = requireView().findViewById(R.id.progressBar);
        Button learnMoreButton = requireView().findViewById(R.id.learn_more_btn);

        if (learnMoreButton != null && progressBar != null) {
            if (progressBar.getVisibility() == View.VISIBLE) {
                // If the progress bar is visible, hide and disable the button
                learnMoreButton.setVisibility(View.INVISIBLE);
                learnMoreButton.setEnabled(false);
            } else {
                // If the progress bar is not visible, make the button visible and enable it
                learnMoreButton.setVisibility(View.VISIBLE);
                learnMoreButton.setEnabled(true);

                // Set OnClickListener for the button
                learnMoreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle the fragment transaction when the button is clicked
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, ChatFragment.newInstance("I want to learn more about \"" + textView.getText().toString() + "\"."))
                                .addToBackStack(null) // Add the transaction to the back stack
                                .commit();
                    }
                });
            }
        }
    }
}