package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {

    protected final int PAGE_SIZE = 10;
    protected User targetUser;
    protected AuthToken authToken;
    protected T lastItem;
    protected boolean hasMorePages;
    protected boolean isLoading;
    protected boolean isGettingUser;

    protected void loadMoreItems() {

    }

    protected void getUser(String alias) {
    }

    protected abstract void getItems(AuthToken authToken, User user, int pageSize, T lastItem);
    protected abstract String getDescription();

}
