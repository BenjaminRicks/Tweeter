package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.View;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {

    MainView view;


    public MainPresenter(MainView view) {
        this.view = view;
    }

    private class FollowObserver implements FollowService.FollowObserver {

        @Override
        public void handleSuccess(boolean updateButton) {
            view.updateSelectedUserFollowingAndFollowers();
            view.updateFollowButton(updateButton); //false
            view.enableFollowButton(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to follow/unfollow: " + message);
            view.enableFollowButton(true);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to follow/unfollow because of exception: " + ex);
            view.enableFollowButton(true);
        }
    }

    private class CountObserver implements FollowService.CountObserver {
        @Override
        public void handleSuccess(int count, boolean isFollower) {
            if(isFollower) {
                view.setFollowerCount(count);
            }
            else {
                view.setFolloweeCount(count);
            }
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get follow count: " + message);

        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to get follow count because of exception: " + ex);

        }
    }


    private class LogoutObserver implements UserService.LogoutObserver {

        @Override
        public void handleSuccess() {
            view.displayInfoMessage("Logging out...");
            view.cancelLogOutToast();
            view.logoutUser();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to logout: " + message);

        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to logout because of exception: " + ex);

        }
    }


    private class IsFollowerObserver implements FollowService.IsFollowerObserver {

        @Override
        public void handleSuccess(boolean isFollower) {
            view.updateFollowerFollowButton(isFollower);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to determine following relationship: " + message);

        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to determine following relationship because of exception: " + ex);

        }
    }

    public interface MainView extends View {
        void cancelPostToast();
        void setPostToast(String message);

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

    private class PostStatusObserver implements StatusService.PostStatusObserver {

        @Override
        public void handleSuccess() {
            view.cancelPostToast();
            view.displayInfoMessage("Successfully Posted!");
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to post status: " + message);

        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to post status because of exception: " + ex);

        }
    }


    public void unfollow(AuthToken authToken, User user) {
        view.enableFollowButton(false);
        new FollowService().unfollow(authToken, user, new FollowObserver());
        view.displayInfoMessage("Removing " + user.getName() + "...");
    }

    public void follow(AuthToken authToken, User user) {
        view.enableFollowButton(false);
        new FollowService().follow(authToken, user, new FollowObserver());
        view.displayInfoMessage("Adding " + user.getName() + "...");
    }

    public void getPostStatus(AuthToken authToken, Status status) {
        view.setPostToast("Posting status...");
        getPostStatusService().getPostStatus(authToken, status, new PostStatusObserver());
    }

    public void logout(AuthToken authToken) {
        view.setLogOutToast("Logging out...");
        new UserService().logout(authToken, new LogoutObserver());
    }

    public StatusService getPostStatusService() {
        return new StatusService();
    }

    public void getFollowersCount(AuthToken authToken, User user) {
        new FollowService().getFollowersCount(authToken, user, new CountObserver());
    }

    public void getFollowingCount(AuthToken authToken, User user) {
        new FollowService().getFollowingCount(authToken, user, new CountObserver());
    }

    public void isFollower(AuthToken authToken, User user, User selectedUser) {
        new FollowService().isFollower(authToken, user, selectedUser, new IsFollowerObserver());
    }



}
