package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

    private class FollowingObserver implements FollowService.FollowsObserver {

        @Override
        public void handleSuccess(List<User> followPeople, boolean hasMorePages) {
            setData(followPeople);

            view.setLoading(false);
            view.addItems(followPeople);
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

    public void setData(List<User> followees) {
        lastItem = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
        setHasMorePages(hasMorePages);
    }



    public interface FollowingView extends PagedView<User>{}


    public FollowingPresenter(FollowingView view, User user, AuthToken authToken) {
        this.view = view;
        this.targetUser = user;
        this.authToken = authToken;
    }

    public void getItems(AuthToken authToken, User targetUser, int limit, User lastItem) {
        getService().getFollowees(authToken, targetUser, limit, lastItem, new FollowingObserver());
    }

    @Override
    protected String getDescription() {
        return "following";
    }

    public FollowService getService() {
        return new FollowService();
    }


}
