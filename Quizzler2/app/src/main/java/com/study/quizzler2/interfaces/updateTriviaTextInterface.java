package com.study.quizzler2.interfaces;

public interface
updateTriviaTextInterface {
    void updateText(String newText);

    public interface OnTextUpdateListener {
        void updateText(String newText, String category);
    }
}
