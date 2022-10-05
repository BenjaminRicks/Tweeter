package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends ServiceBase {

    public interface FollowObserver extends ServiceObserver {
        void handleSuccess(boolean updateButton);
    }

    public interface CountObserver extends ServiceObserver {
        void handleSuccess(int count, boolean isFollower);
    }

    public interface IsFollowerObserver extends ServiceObserver {
        void handleSuccess(boolean isFollower);
    }


    public FollowService(){}

    public void getFollowees(AuthToken authToken, User targetUser, int limit, User lastFollowee, PagedObserver observer) {
        GetFollowingTask followingTask = new GetFollowingTask(authToken, targetUser, limit, lastFollowee, new PagedHandler(observer));
        BackgroundTaskUtils.runTask(followingTask);
    }

    public void getFollowers(AuthToken authToken, User targetUser, int limit, User lastFollower, PagedObserver observer) {
        GetFollowersTask followersTask = new GetFollowersTask(authToken, targetUser, limit, lastFollower, new PagedHandler(observer));
        BackgroundTaskUtils.runTask(followersTask);
    }

    public void getUser(AuthToken authToken, String alias, GetUserObserver observer) {
        GetUserTask userTask = new GetUserTask(authToken, alias, new GetUserHandler(observer));
        BackgroundTaskUtils.runTask(userTask);
    }

    public void getFollowersCount(AuthToken authToken, User user, CountObserver observer) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken, user, new CountHandler(observer, true));
        BackgroundTaskUtils.runTask(followersCountTask);
    }

    public void getFollowingCount(AuthToken authToken, User user, CountObserver observer){
        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken, user, new CountHandler(observer, false));
        BackgroundTaskUtils.runTask(followingCountTask);
    }

    public void unfollow(AuthToken authToken, User user, FollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken, user, new FollowHandler(observer, true));
        BackgroundTaskUtils.runTask(unfollowTask);
    }

    public void follow(AuthToken authToken, User user, FollowObserver observer) {
        FollowTask followTask = new FollowTask(authToken, user, new FollowHandler(observer, false));
        BackgroundTaskUtils.runTask(followTask);
    }

    public void isFollower(AuthToken authToken, User user, User selectedUser, IsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, user, selectedUser, new IsFollowerHandler(observer));
        BackgroundTaskUtils.runTask(isFollowerTask);
    }


    private class FollowHandler extends BackgroundTaskHandler<FollowObserver> {
        boolean updateButton;

        public FollowHandler(FollowObserver observer, boolean updateButton) {
            super(observer);
            this.updateButton = updateButton;
        }

        @Override
        protected void handleSuccessMessage(FollowObserver observer, Bundle data) {
            observer.handleSuccess(updateButton);
        }
    }


    private class CountHandler extends BackgroundTaskHandler<CountObserver> {

        boolean isFollower;
        public CountHandler(CountObserver observer, boolean isFollower) {
            super(observer);
            this.isFollower = isFollower;
        }

        @Override
        protected void handleSuccessMessage(CountObserver observer, Bundle data) {
            int count = data.getInt(GetFollowersCountTask.COUNT_KEY);
            observer.handleSuccess(count, isFollower);
        }
    }


    private class IsFollowerHandler extends BackgroundTaskHandler<IsFollowerObserver> {

        public IsFollowerHandler(IsFollowerObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(IsFollowerObserver observer, Bundle data) {
            boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
            observer.handleSuccess(isFollower);
        }
    }

}
