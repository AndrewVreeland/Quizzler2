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
import android.widget.TextView;
import android.widget.Toast;
import com.study.quizzler2.interfaces.AuthResultCallback;

import androidx.fragment.app.Fragment;

import com.amplifyframework.core.Amplify;
import com.study.quizzler2.R;
import com.study.quizzler2.helpers.authentification.AuthHelper;
import com.study.quizzler2.helpers.authentification.AuthResult;
import com.study.quizzler2.interfaces.ActionBarVisibility;
import com.study.quizzler2.managers.UserManager;
import com.study.quizzler2.utils.HandlerUtility;

public class LoginFragment extends Fragment {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private UserManager userManager;
    private Context fragmentContext;
    private AuthHelper authHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        fragmentContext = getContext();
        userManager = new UserManager(getContext());
        AuthHelper authHelper = new AuthHelper(getActivity(), userManager);
        usernameEditText = rootView.findViewById(R.id.usernameEditText);
        passwordEditText = rootView.findViewById(R.id.passwordEditText);
        loginButton = rootView.findViewById(R.id.loginButton);
        TextView signUpTextView = rootView.findViewById(R.id.signUpTextView);

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SignUpFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignUpFragment())
                        .addToBackStack(null)  // This will allow you to go back to the LoginFragment by pressing the back button
                        .commit();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                v.clearFocus();
                attemptLogin();

                // Implement the AuthResultCallback interface here
                AuthResultCallback authResultCallback = new AuthResultCallback() {
                    @Override
                    public void onResult(AuthResult authResult) {
                        if (authResult.isSuccess()) {
                            // User attributes fetched successfully
                            Log.d("LoginFragment", "User attributes: " + authResult.getMessage());
                        } else {
                            // Error fetching user attributes
                            Log.e("LoginFragment", "Error fetching user attributes: " + authResult.getMessage());
                        }
                    }
                };

                authHelper.fetchAndLogUsername(authResultCallback);
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
                    HandlerUtility.runOnMainThread(() -> {
                        if (result.isSignedIn()) {
                            Log.d("LoginFragment", "SignIn Callback executed. Result: " + result.isSignedIn());

                            // Fetch the logged-in user's details
                            Amplify.Auth.getCurrentUser(
                                    user -> {
                                        String loggedInUsername = user.getUsername();
                                        userManager.saveUsername(loggedInUsername);  // Save the logged-in username

                                        // Move to the next fragment only after the username is saved
                                        userManager.setLoggedIn(true);
                                        getParentFragmentManager().beginTransaction()
                                                .replace(R.id.fragment_container, new HomeFragment())
                                                .commit();
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (getContext() != null) {
                                                    Toast.makeText(getContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    },
                                    error -> Log.e("LoginFragment", "Error fetching current user: " + error.toString(), error)
                            );
                        } else {
                            // Handle cases like MultiFactorAuth or other additional sign-in steps
                            Toast.makeText(getContext(), "Sign in not complete. Check your credentials or MFA.", Toast.LENGTH_SHORT).show();
                        }
                    });
                },
                error -> {
                    // Handle the error
                    HandlerUtility.runOnMainThread(() -> {
                        Log.e("LoginFragment", "Error Callback executed. Error: " + error.toString());
                        Toast.makeText(getContext(), "Error logging in: " + error.toString(), Toast.LENGTH_SHORT).show();
                    });
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof ActionBarVisibility) {
            // This will hide the ActionBar when the fragment is visible
            ((ActionBarVisibility) getActivity()).hideActionBar();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof ActionBarVisibility) {
            // This will show the ActionBar again when the fragment is no longer visible
            ((ActionBarVisibility) getActivity()).showActionBar();
        }
    }
}