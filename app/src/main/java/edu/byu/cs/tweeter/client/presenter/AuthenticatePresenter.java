package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticatePresenter extends Presenter {

    public interface AuthenticateView extends View {
        void clearErrorMessage();
        void clearInfoMessage();
        void navigateToUser(User user);
    }
    protected AuthenticateView view;
    public AuthenticatePresenter(AuthenticateView view){
        this.view = view;
    }

    public void login(String username, String password){

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

    protected class AuthenticateObserver implements UserService.AuthenticateObserver {
        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            view.clearInfoMessage();
            view.clearErrorMessage();

            view.displayInfoMessage("Hello " + Cache.getInstance().getCurrUser().getName());
            view.navigateToUser(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage(message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayInfoMessage("Failed to login because of exception: " + ex.getMessage());
        }

    }

    public UserService getService() {
        return new UserService();
    }

}
