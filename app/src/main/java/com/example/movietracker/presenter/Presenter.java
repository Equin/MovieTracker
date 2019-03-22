package com.example.movietracker.presenter;

import com.example.movietracker.view.contract.View;

public interface Presenter {
    void resume();
    void pause();
    void destroy();
}
