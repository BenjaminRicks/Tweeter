package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class ServiceBase<T> {

    public interface PagedObserver extends ServiceObserver {
        void handleSuccess(List<?> items, boolean hasMorePages);
    }

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

    protected class PagedHandler extends BackgroundTaskHandler<PagedObserver> {

        public PagedHandler(PagedObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(PagedObserver observer, Bundle data) {
            List<T> items = (List<T>) data.getSerializable(GetFeedTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);
            observer.handleSuccess(items, hasMorePages);
        }
    }

}
