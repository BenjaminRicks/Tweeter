package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements StatusService.PostStatusObserver, FollowService.UnfollowObserver,
        FollowService.FollowObserver, FollowService.FollowersCountObserver, FollowService.FollowingCountObserver,
        UserService.LogoutObserver, FollowService.IsFollowerObserver {

    MainView view;


    public MainPresenter(MainView view) {
        this.view = view;
    }


    @Override
    public void handleUnfollowSuccess() {
        view.updateSelectedUserFollowingAndFollowers();
        view.updateFollowButton(true);
        view.enableFollowButton(true);
    }

    @Override
    public void handleUnfollowFailure(String message) {
        view.displayErrorMessage("Failed to unfollow: " + message);
        view.enableFollowButton(true);

    }

    @Override
    public void handleUnfollowException(Exception ex) {
        view.displayErrorMessage("Failed to unfollow because of exception: " + ex);
        view.enableFollowButton(true);
    }

    @Override
    public void handleGetFollowersCountSuccess(int count) {
        view.setFollowerCount(count);
    }

    @Override
    public void handleGetFollowersCountFailure(String message) {
        view.displayErrorMessage("Failed to get followers count: " + message);
    }

    @Override
    public void handleGetFollowersCountException(Exception ex) {
        view.displayErrorMessage("Failed to get followers count because of exception: " + ex);
    }

    @Override
    public void handleFollowSuccess() {
        view.updateSelectedUserFollowingAndFollowers();
        view.updateFollowButton(false);
        view.enableFollowButton(true);
    }

    @Override
    public void handleFollowFailure(String message) {
        view.displayErrorMessage("Failed to follow: " + message);
        view.enableFollowButton(true);
    }

    @Override
    public void handleFollowException(Exception ex) {
        view.displayErrorMessage("Failed to follow because of exception: " + ex);
        view.enableFollowButton(true);
    }

    @Override
    public void handleGetFollowingCountSuccess(int count) {
        view.setFolloweeCount(count);
    }

    @Override
    public void handleGetFollowingCountFailure(String message) {
        view.displayErrorMessage("Failed to get following count: " + message);
    }

    @Override
    public void handleGetFollowingCountException(Exception ex) {
        view.displayErrorMessage("Failed to get following count because of exception: " + ex);
    }

    @Override
    public void handleLogoutSuccess() {
        view.cancelLogOutToast();
        view.logoutUser();
    }

    @Override
    public void handleLogoutFailure(String message) {
        view.displayErrorMessage("Failed to logout: " + message);
    }

    @Override
    public void handleLogoutException(Exception ex) {
        view.displayErrorMessage("Failed to logout because of exception: " + ex);
    }

    @Override
    public void handleIsFollowerSuccess(boolean isFollower) {
        view.updateFollowerFollowButton(isFollower);

    }

    @Override
    public void handleIsFollowerFailure(String message) {
        view.displayErrorMessage("Failed to determine following relationship: " + message);
    }

    @Override
    public void handleIsFollowerException(Exception ex) {
        view.displayErrorMessage("Failed to determine following relationship because of exception: " + ex);
    }

    public interface MainView {
        void cancelPostToast();
        void setPostToast(String message);

        void displayErrorMessage(String message);
        void displayInfoMessage(String message);

        void updateFollowButton(boolean removed);
        void updateSelectedUserFollowingAndFollowers();
        void enableFollowButton(boolean enable);

        void setFolloweeCount(int count);
        void setFollowerCount(int count);

        void cancelLogOutToast();
        void logoutUser();
        void setLogOutToast(String message);

        void updateFollowerFollowButton(boolean isFollower);


    }

    @Override
    public void handlePostStatusSuccess() {
        view.cancelPostToast();
        view.displayInfoMessage("Successfully Posted!");

    }

    @Override
    public void handlePostStatusFailure(String message) {
        view.displayErrorMessage("Failed to post status: " + message);
    }

    @Override
    public void handlePostStatusException(Exception ex) {
        view.displayErrorMessage("Failed to post status because of exception: " + ex);

    }

    public void unfollow(AuthToken authToken, User user) {
        view.enableFollowButton(false);
        new FollowService().unfollow(authToken, user, this);
        view.displayInfoMessage("Removing " + user.getName() + "...");
    }

    public void follow(AuthToken authToken, User user) {
        view.enableFollowButton(false);
        new FollowService().follow(authToken, user, this);
        view.displayInfoMessage("Adding " + user.getName() + "...");
    }

    public void getPostStatus(AuthToken authToken, Status status) {
        view.setPostToast("Posting status...");
        getPostStatusService().getPostStatus(authToken, status, this);
    }

    public void logout(AuthToken authToken) {
        view.setLogOutToast("Logging out...");
        new UserService().logout(authToken, this);
    }

    public StatusService getPostStatusService() {
        return new StatusService();
    }

    public void getFollowersCount(AuthToken authToken, User user) {
        new FollowService().getFollowersCount(authToken, user, this);
    }

    public void getFollowingCount(AuthToken authToken, User user) {
        new FollowService().getFollowingCount(authToken, user, this);
    }

    public void isFollower(AuthToken authToken, User user, User selectedUser) {
        new FollowService().isFollower(authToken, user, selectedUser, this);
    }



}
