package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.ServiceBase;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {

    protected final int PAGE_SIZE = 10;
    protected User targetUser;
    protected AuthToken authToken;
    protected T lastItem;
    protected boolean hasMorePages;
    protected boolean isLoading;
    protected boolean isGettingUser;
    protected PagedView<T> view;

    public interface PagedView<T> extends View {
        void setLoading(boolean isBoolean);
        void addItems(List<T> items);
        void navigateToUser(User user);
    }

    protected class PagedObserver implements ServiceBase.PagedObserver {

        @Override
        public void handleFailure(String message) {
            view.setLoading(false);
            displayFailMessage(message);
            setLoading(false);

        }

        @Override
        public void handleException(Exception ex) {
            view.setLoading(false);
            displayErrorMessage(ex);
            setLoading(false);
        }

        @Override
        public void handleSuccess(List<?> items, boolean hasMorePages) {
            lastItem = (items.size() > 0) ? (T) items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);

            view.setLoading(false);
            view.addItems((List<T>)items);
            setLoading(false);
        }
    }

//    public abstract void setData(List<T> items);


    public void displayFailMessage(String message) {
        view.displayErrorMessage("Failed to get " + getDescription() + ": " + message);

    }

    public void displayErrorMessage(Exception ex) {
        view.displayErrorMessage("Failed to get " + getDescription() + " because of exception: " + ex);
    }

    private class GetUserObserver implements ServiceBase.GetUserObserver {

        @Override
        public void handleSuccess(User user) {
            view.navigateToUser(user);

        }

        @Override
        public void handleFailure(String message) {
            view.displayInfoMessage("Failed to get user's profile: " + message);

        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to get user's profile due to exception: " + ex);

        }
    }

    protected void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    public void loadMoreItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoading(true);
            view.setLoading(true);
            getItems(authToken, targetUser, PAGE_SIZE, lastItem);
        }
    }

    public void getUser(AuthToken authToken, String alias) {
        view.displayInfoMessage("Getting user's profile...");
        getService().getUser(authToken, alias, new GetUserObserver());
    }

    protected void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean isLoading() {
        return isLoading;
    }

    protected abstract ServiceBase getService();

    protected abstract void getItems(AuthToken authToken, User user, int pageSize, T lastItem);
    protected abstract String getDescription();
    protected T getLastItem() {
        return lastItem;
    }

    protected void setLastItem(T lastItem) {
        this.lastItem = lastItem;
    }

}
