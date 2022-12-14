package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends ServiceBase {

    public interface PostStatusObserver extends ServiceObserver {
        void handleSuccess();
    }

    public void getUser(AuthToken authToken, String alias, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(authToken, alias, new GetUserHandler(observer));
        BackgroundTaskUtils.runTask(getUserTask);
    }

    public void getFeed(AuthToken authToken, User user, int limit, Status status, PagedObserver observer) {
        GetFeedTask getFeedTask = new GetFeedTask(authToken, user, limit, status, new PagedHandler(observer));
        BackgroundTaskUtils.runTask(getFeedTask);
    }

    public void getStory(AuthToken authToken, User user, int limit, Status status, PagedObserver observer) {
        GetStoryTask getStoryTask = new GetStoryTask(authToken, user, limit, status, new PagedHandler(observer));
        BackgroundTaskUtils.runTask(getStoryTask);
    }

    public void getPostStatus(AuthToken authToken, Status status, PostStatusObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(authToken, status, new PostStatusHandler(observer));
        BackgroundTaskUtils.runTask(statusTask);
    }


    private class PostStatusHandler extends BackgroundTaskHandler<PostStatusObserver> {

        public PostStatusHandler(PostStatusObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(PostStatusObserver observer, Bundle data) {
            observer.handleSuccess();
        }
    }
}
