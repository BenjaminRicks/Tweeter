package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {

    public interface FollowersView extends PagedView<User> {}


    public FollowersPresenter(FollowersView view, User user, AuthToken authToken) {
        this.view = view;
        this.targetUser = user;
        this.authToken = authToken;
    }


    @Override
    protected void getItems(AuthToken authToken, User user, int pageSize, User lastItem) {
        getService().getFollowers(authToken, user, pageSize, lastItem, new PagedObserver());
    }

    @Override
    protected String getDescription() {
        return "followers";
    }


    @Override
    protected FollowService getService() {
        return new FollowService();
    }

}
