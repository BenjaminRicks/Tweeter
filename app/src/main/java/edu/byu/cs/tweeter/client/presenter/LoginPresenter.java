package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;

public class LoginPresenter extends AuthenticatePresenter {

    public interface LoginView extends AuthenticateView {}

    public LoginPresenter(LoginView view) {
        super(view);
    }

    public void login(String username, String password) {

        String errorMessage = validateLogin(username, password);

        if(errorMessage == null) {
            view.clearErrorMessage();
            view.displayInfoMessage("Logging In...");
            new UserService().login(username, password, new AuthenticateObserver());
        }
        else{
            view.displayErrorMessage(errorMessage);
        }
    }


    public String validateLogin(String alias, String password) {
        if (alias.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (alias.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }
        return null;
    }


}
