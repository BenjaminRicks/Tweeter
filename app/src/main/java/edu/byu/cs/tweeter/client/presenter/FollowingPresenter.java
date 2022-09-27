package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter implements FollowService.FollowingObserver, FollowService.GetUserObserver {

    public static final int PAGE_SIZE = 10;

    private boolean isLoading = false;
    private User user;
    private AuthToken authToken;

    private User lastFollowee;
    private boolean hasMorePages;

    @Override
    public void handleGetUserSuccess(User user) {
        view.navigateToUser(user);
    }

    @Override
    public void handleGetUserFailure(String message) {
        view.displayErrorMessage("Failed to get user's profile: " + message);
    }

    @Override
    public void handleGetUserThrewException(Exception ex) {
        view.displayErrorMessage("Failed to get user's profile because of exception: " + ex);
    }

    @Override
    public void handleGetFollowingSuccess(List<User> followees, boolean hasMorePages) {
        lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
        setHasMorePages(hasMorePages);

        view.setLoading(false);
        view.addItems(followees);
        setLoading(false);
    }

    @Override
    public void handleGetFollowingFailure(String message) {
        view.setLoading(false);
        view.displayErrorMessage("Failed to get following: " + message);
        setLoading(false);
    }

    @Override
    public void handleGetFollowingException(Exception ex) {
        view.setLoading(false);
        view.displayErrorMessage("Failed to get following because of exception: " + ex);
        setLoading(false);
    }

    public interface FollowingView {

        void setLoading(boolean value);
        void addItems(List<User> newUsers);
        void displayErrorMessage(String message);

        void navigateToUser(User user);

    }

    private FollowingPresenter.FollowingView view;

    public FollowingPresenter(FollowingPresenter.FollowingView view, User user, AuthToken authToken) {
        this.view = view;
        this.user = user;
        this.authToken = authToken;
    }

    public User getLastFollowee() {
        return lastFollowee;
    }

    private void setLastFollowee(User lastFollowee) {
        this.lastFollowee = lastFollowee;
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
            getFollowing(authToken, user, PAGE_SIZE, lastFollowee);
        }
    }

    public void getUser(AuthToken authToken, String alias) {
        new FollowService().getUser(authToken, alias, this);

    }
    public void getFollowing(AuthToken authToken, User targetUser, int limit, User lastFollowee) {
        getFollowingService().getFollowees(authToken, targetUser, limit, lastFollowee, this);
    }

    public FollowService getFollowingService() {
        return new FollowService();
    }


}
