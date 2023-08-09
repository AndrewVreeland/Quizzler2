package com.study.quizzler2.interfaces;

import com.study.quizzler2.helpers.authentification.AuthResult;

public interface AuthResultCallback {
    void onResult(AuthResult result);
}