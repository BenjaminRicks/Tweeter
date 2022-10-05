package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends ServiceBase {

    public interface LogoutObserver extends ServiceObserver {
        void handleSuccess();
    }

    public interface AuthenticateObserver extends ServiceObserver {
        void handleSuccess(User user, AuthToken authToken);
    }

    public void register(String firstName, String lastName, String alias, String password, byte[] imageBytesBase64, AuthenticateObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password, imageBytesBase64.toString(), new AuthenticateHandler(observer));
        BackgroundTaskUtils.runTask(registerTask);
    }

    public void login(String username, String password, AuthenticateObserver observer) {
        LoginTask loginTask = new LoginTask(username, password, new AuthenticateHandler(observer));
        BackgroundTaskUtils.runTask(loginTask);
    }

    public void logout(AuthToken authToken, LogoutObserver observer) {
        LogoutTask logoutTask = new LogoutTask(authToken, new LogoutHandler(observer));
        BackgroundTaskUtils.runTask(logoutTask);
    }

    public void getUser(AuthToken authToken, String alias, ServiceBase.GetUserObserver observer) {
        GetUserTask userTask = new GetUserTask(authToken, alias, new UserService.GetUserHandler(observer));
        BackgroundTaskUtils.runTask(userTask);
    }

    private class AuthenticateHandler extends BackgroundTaskHandler<AuthenticateObserver> {

        public AuthenticateHandler(AuthenticateObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(AuthenticateObserver observer, Bundle data) {
            User loggedInUser = (User) data.getSerializable(LoginTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);

            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);


            observer.handleSuccess(loggedInUser, authToken);
        }
    }

    private class LogoutHandler extends BackgroundTaskHandler<LogoutObserver> {

        public LogoutHandler(LogoutObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(LogoutObserver observer, Bundle data) {
            observer.handleSuccess();
        }
    }

}
