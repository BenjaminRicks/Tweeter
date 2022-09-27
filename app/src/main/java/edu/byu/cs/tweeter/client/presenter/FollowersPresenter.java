package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter implements FollowService.FollowersObserver, FollowService.GetUserObserver {

    private final int PAGE_SIZE = 10;
    private boolean isLoading = false;
    private boolean hasMorePages;
    private User user;
    private AuthToken authToken;

    private User lastFollower;

    @Override
    public void handleGetUserSuccess(User user) {
        view.navigateToUser(user);
        view.displayInfoMessage("Getting user's profile");
    }

    @Override
    public void handleGetUserFailure(String message) {
        view.displayErrorMessage("Failed to get user's profile: " + message);
    }

    @Override
    public void handleGetUserThrewException(Exception ex) {
        view.displayErrorMessage("Failed to get user's profile because of exception: " + ex);
    }

    public interface FollowersView {
        void displayErrorMessage(String message);
        void displayInfoMessage(String message);

        void navigateToUser(User user);

        void setLoading(boolean isLoading);
        void addItems(List<User> followers);
    }

    private FollowersView view;

    public FollowersPresenter(FollowersView view, User user, AuthToken authToken) {
        this.view = view;
        this.user = user;
        this.authToken = authToken;
    }

    @Override
    public void handleGetFollowersSuccess(List<User> followers, boolean hasMorePages) {
        lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
        setHasMorePages(hasMorePages);

        view.setLoading(false);
        view.addItems(followers);
        setLoading(false);
    }

    @Override
    public void handleGetFollowersFailure(String message) {
        view.setLoading(false);
        view.displayErrorMessage("Failed to get followers: " + message);
        setLoading(false);

    }

    @Override
    public void handleGetFollowersException(Exception ex) {
        view.setLoading(false);
        view.displayErrorMessage("Failed to get followers because of exception: " + ex);
        setLoading(false);
    }


    private void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean isLoading() {
        return isLoading;
    }

    private void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    public User getLastFollower() {
        return lastFollower;
    }

    public void loadMoreItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoading(true);
            view.setLoading(true);
            getFollowers(authToken, user, PAGE_SIZE, lastFollower);
        }
    }

    public void getUser(AuthToken authToken, String alias) {
        new FollowService().getUser(authToken, alias, this);
    }


    public void getFollowers(AuthToken authToken, User user, int limit, User lastFollower) {
        getFollowerService().getFollowers(authToken, user, limit, lastFollower, this);
    }

    public FollowService getFollowerService() {
        return new FollowService();
    }
}
