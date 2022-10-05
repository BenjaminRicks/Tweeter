package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

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

    private class StoryObserver implements StatusService.StatusObserver {

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            setData(statuses);

            view.setLoading(false);
            view.addItems(statuses);
            setLoading(false);
        }

        @Override
        public void handleFailure(String message) {
            view.setLoading(false);
            displayFailMessage(message);
            setLoading(false);

        }

        @Override
        public void handleException(Exception ex) {
            view.setLoading(false);
            displayErrorMessage(ex);
            setLoading(false);
        }
    }

    public void setData(List<Status> statuses) {
        lastItem = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
        setHasMorePages(hasMorePages);
    }

    @Override
    protected String getDescription() {
        return "story";
    }

    public void getItems(AuthToken authToken, User user, int limit, Status lastItem) {
        getService().getStory(authToken, user, limit, lastItem, new StoryObserver());
    }

    public StatusService getService() {
        return new StatusService();
    }

}
