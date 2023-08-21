package com.study.quizzler2.helpers.authentification;

import static com.amplifyframework.core.Amplify.Auth;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult;
import com.amplifyframework.core.Amplify;
import com.study.quizzler2.fragments.LoginFragment;
import com.study.quizzler2.R;
import com.study.quizzler2.helpers.authentification.AuthResult;
import com.study.quizzler2.interfaces.AuthResultCallback;
import com.study.quizzler2.managers.UserManager;

public class AuthHelper {

    private final FragmentManager fragmentManager;
    private final UserManager userManager;
    private final Activity activity;

    public AuthHelper(FragmentActivity activity, UserManager userManager) {
        this.fragmentManager = activity.getSupportFragmentManager();
        this.userManager = userManager;
        this.activity = activity;
    }

    public void signOut() {
        // Your Amplify sign-out logic
        Auth.signOut(signOutResult -> {
            if (signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut
                    || signOutResult instanceof AWSCognitoAuthSignOutResult.PartialSignOut) {

                activity.runOnUiThread(() -> {
                    userManager.setLoggedIn(false);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new LoginFragment())
                            .commit();

                    if (signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut) {
                        Toast.makeText(activity, "Signed out successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Partial sign out. Please check for potential issues.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.FailedSignOut) {
                AWSCognitoAuthSignOutResult.FailedSignOut failedSignOutResult =
                        (AWSCognitoAuthSignOutResult.FailedSignOut) signOutResult;
                activity.runOnUiThread(() -> {
                    Toast.makeText(activity, "Error during logout: " + failedSignOutResult.getException().toString(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    public void handleSignOut(AuthResultCallback callback) {
        Auth.signOut(signOutResult -> {
            if (signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut) {
                callback.onResult(new AuthResult(true, "Signed out successfully."));
            } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.PartialSignOut) {
                callback.onResult(new AuthResult(true, "Partial sign out. Please check for potential issues."));
            } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.FailedSignOut) {
                AWSCognitoAuthSignOutResult.FailedSignOut failedSignOutResult =
                        (AWSCognitoAuthSignOutResult.FailedSignOut) signOutResult;
                callback.onResult(new AuthResult(false, "Error during logout: " + failedSignOutResult.getException().toString()));
            }
        });
    }

    public void fetchAndLogUsername() {
        Amplify.Auth.fetchUserAttributes(
                attributes -> Log.i("AuthDemo", "User attributes = " + attributes.toString()),
                error -> Log.e("LoginFragment", "Error fetching current user: " + error.toString(), error));
    }

}