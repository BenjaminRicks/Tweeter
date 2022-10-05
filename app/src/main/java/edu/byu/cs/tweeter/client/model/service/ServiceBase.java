package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class ServiceBase {

    public interface GetUserObserver extends ServiceObserver {
        void handleSuccess(User user);
    }

    public abstract void getUser(AuthToken authToken, String alias, GetUserObserver observer);

    protected class GetUserHandler extends BackgroundTaskHandler<ServiceBase.GetUserObserver> {

        public GetUserHandler(ServiceBase.GetUserObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(ServiceBase.GetUserObserver observer, Bundle data) {
            User user = (User) data.getSerializable(GetUserTask.USER_KEY);
            observer.handleSuccess(user);
        }
    }

}
