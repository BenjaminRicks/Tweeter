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

//    private class FollowersObserver implements FollowService.FollowsObserver {
//
//        @Override
//        public void handleSuccess(List<User> followPeople, boolean hasMorePages) {
//            setData(followPeople);
//
//            view.setLoading(false);
//            view.addItems(followPeople);
//            setLoading(false);
//        }
//
//        @Override
//        public void handleFailure(String message) {
//            view.setLoading(false);
//            displayFailMessage(message);
//            setLoading(false);
//
//        }
//
//        @Override
//        public void handleException(Exception ex) {
//            view.setLoading(false);
//            displayErrorMessage(ex);
//            setLoading(false);
//        }
//    }

    @Override
    protected void getItems(AuthToken authToken, User user, int pageSize, User lastItem) {
        getService().getFollowers(authToken, user, pageSize, lastItem, new PagedObserver());
    }

    @Override
    protected String getDescription() {
        return "followers";
    }


//    public void setData(List<User> followers) {
//        lastItem = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
//        setHasMorePages(hasMorePages);
//    }

    @Override
    protected FollowService getService() {
        return new FollowService();
    }

}
