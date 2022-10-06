package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

    public interface FollowingView extends PagedView<User>{}


    public FollowingPresenter(FollowingView view, User user, AuthToken authToken) {
        this.view = view;
        this.targetUser = user;
        this.authToken = authToken;
    }

    public void getItems(AuthToken authToken, User targetUser, int limit, User lastItem) {
        getService().getFollowees(authToken, targetUser, limit, lastItem, new PagedObserver());
    }

    @Override
    protected String getDescription() {
        return "following";
    }

    public FollowService getService() {
        return new FollowService();
    }


}
