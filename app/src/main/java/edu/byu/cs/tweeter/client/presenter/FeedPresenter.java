package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {

    public interface FeedView extends PagedView<Status>{}

    public FeedPresenter(FeedPresenter.FeedView view, User user, AuthToken authToken){
        this.view = view;
        this.targetUser = user;
        this.authToken = authToken;
    }


    public void getItems(AuthToken authToken, User targetUser, int limit, Status lastItem) {
        getService().getFeed(authToken, targetUser, limit, lastItem, new PagedObserver());
    }

    @Override
    protected String getDescription() {
        return "feed";
    }

    public StatusService getService() {
        return new StatusService();
    }

}
