package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    public StoryPresenter(StoryView view, User user, AuthToken authToken) {
        this.view = view;
        this.targetUser = user;
        this.authToken = authToken;
    }

    public interface StoryView extends PagedView<Status> {}

    @Override
    protected String getDescription() {
        return "story";
    }

    public void getItems(AuthToken authToken, User user, int limit, Status lastItem) {
        getService().getStory(authToken, user, limit, lastItem, new PagedObserver());
    }

    public StatusService getService() {
        return new StatusService();
    }

}
