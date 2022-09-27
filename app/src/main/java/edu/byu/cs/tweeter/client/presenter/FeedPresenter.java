package edu.byu.cs.tweeter.client.presenter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.main.feed.FeedFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter implements StatusService.FeedObserver, StatusService.GetUserObserver {

    public static final int PAGE_SIZE = 10;

    private User user;
    private AuthToken authToken;

    private boolean hasMorePages = false;
    private boolean isLoading = false;

    private Status lastStatus;

    public interface FeedView {
        void navigateToUser(User user);
        void displayErrorMessage(String message);
        void displayInfoMessage(String message);

        void setLoading(boolean isLoading);
        void addItems(List<Status> statuses);
    }

    private FeedView view;
    public FeedPresenter(FeedPresenter.FeedView view, User user, AuthToken authToken){
        this.view = view;
        this.user = user;
        this.authToken = authToken;
    }
    @Override
    public void handleUserSuccess(User user) {
        view.navigateToUser(user);
    }

    @Override
    public void handleUserFailure(String message) {
        view.displayInfoMessage("Failed to get user's profile: " + message);
    }

    @Override
    public void handleUserException(Exception ex) {
        view.displayErrorMessage("Failed to get user's profile due to exception: " + ex);
    }

    @Override
    public void handleFeedSuccess(List<Status> statuses, boolean hasMorePages) {
        lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
        setHasMorePages(hasMorePages);

        view.setLoading(false);
        view.addItems(statuses);
        setLoading(false);
    }

    @Override
    public void handleFeedFailure(String message) {
        view.setLoading(false);
        view.displayInfoMessage("Failed to get feed: " + message);
        setLoading(false);
    }

    @Override
    public void handleFeedException(Exception ex) {
        view.setLoading(false);
        view.displayErrorMessage("Failed to get feed because of exception: " + ex);
        setLoading(false);
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    private void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    private void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void loadMoreItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoading(true);
            view.setLoading(true);
            getStatuses(authToken, user, PAGE_SIZE, lastStatus);
        }
    }

    public void getStatuses(AuthToken authToken, User targetUser, int limit, Status lastFollowee) {
        getFeedService().getFeed(authToken, targetUser, limit, lastFollowee, this);
    }

    public StatusService getFeedService() {
        return new StatusService();
    }

    public void getUser(AuthToken authToken, String alias) {
        new StatusService().getUser(authToken, alias, this);
    }
}
