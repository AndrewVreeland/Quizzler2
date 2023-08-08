package com.study.quizzler2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.study.quizzler2.R;
import com.study.quizzler2.utils.HandlerUtil;

public class SignUpFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private Button signUpButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        usernameEditText = rootView.findViewById(R.id.usernameSignUpEditText);
        passwordEditText = rootView.findViewById(R.id.passwordSignUpEditText);
        emailEditText = rootView.findViewById(R.id.emailSignUpEditText);
        signUpButton = rootView.findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(v -> attemptSignUp());

        return rootView;
    }

    private void attemptSignUp() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build();

        Amplify.Auth.signUp(
                username,
                password,
                options,
                result -> HandlerUtil.runOnMainThread(() ->
                        Toast.makeText(getContext(), "Sign Up was successful!", Toast.LENGTH_SHORT).show()
                ),
                error -> HandlerUtil.runOnMainThread(() ->
                        Toast.makeText(getContext(), "Sign Up failed: " + error.toString(), Toast.LENGTH_SHORT).show()
                )
        );
    }
}