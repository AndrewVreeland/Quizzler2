package com.study.quizzler2.interfaces;

import com.study.quizzler2.helpers.AuthResult;

public interface AuthResultCallback {
    void onResult(AuthResult result);
}