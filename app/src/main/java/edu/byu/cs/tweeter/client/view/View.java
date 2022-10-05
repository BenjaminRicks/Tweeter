package edu.byu.cs.tweeter.client.view;

import edu.byu.cs.tweeter.client.presenter.Presenter;

public interface View extends Presenter.View {
    void displayErrorMessage(String message);
    void displayInfoMessage(String message);
}
