package com.study.quizzler2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.amplifyframework.core.Amplify;
import com.study.quizzler2.R;
import com.study.quizzler2.managers.UserManager;
import com.study.quizzler2.utils.HandlerUtil;

public class LoginFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private UserManager userManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        userManager = new UserManager(getContext());

        usernameEditText = rootView.findViewById(R.id.usernameEditText);
        passwordEditText = rootView.findViewById(R.id.passwordEditText);
        loginButton = rootView.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                v.clearFocus();
                attemptLogin();
            }
        });

        return rootView;
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void attemptLogin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        Amplify.Auth.signIn(
                username,
                password,
                result -> {
                    HandlerUtil.runOnMainThread(() -> {
                        if (result.isSignedIn()) {
                            Log.d("LoginFragment", "SignIn Callback executed. Result: " + result.isSignedIn());

                            userManager.setLoggedIn(true);
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new HomeFragment())
                                    .commit();
                            Toast.makeText(getContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle cases like MultiFactorAuth or other additional sign-in steps
                            Toast.makeText(getContext(), "Sign in not complete. Check your credentials or MFA.", Toast.LENGTH_SHORT).show();
                        }
                    });
                },
                error -> {
                    // Handle the error
                    HandlerUtil.runOnMainThread(() -> {
                        Log.e("LoginFragment", "Error Callback executed. Error: " + error.toString());
                        Toast.makeText(getContext(), "Error logging in: " + error.toString(), Toast.LENGTH_SHORT).show();
                    });
                }
        );
    }
}
