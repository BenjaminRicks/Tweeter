package edu.byu.cs.tweeter.client.presenter;

public abstract class Presenter {
    public interface View {
        void displayErrorMessage(String message);
        void displayInfoMessage(String message);
    }
    protected View view;

}
