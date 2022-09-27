package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter implements StatusService.StoryObserver, StatusService.GetUserObserver {

    private final int PAGE_SIZE = 10;

    private StoryView view;
    private User user;
    private AuthToken authToken;

    private boolean isLoading = false;
    private boolean hasMorePages;
    private Status lastStatus;


    public StoryPresenter(StoryView view, User user, AuthToken authToken) {
        this.view = view;
        this.user = user;
        this.authToken = authToken;
    }
    public interface StoryView {
        void navigateToUser(User user);
        void displayErrorMessage(String message);
        void displayInfoMessage(String message);

        void setLoading(boolean isLoading);
        void addItems(List<Status> statuses);
    }

    @Override
    public void handleStorySuccess(List<Status> statuses, boolean hasMorePages) {
        lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
        setHasMorePages(hasMorePages);

        view.setLoading(false);
        view.addItems(statuses);
        setLoading(false);

    }

    @Override
    public void handleStatusFailure(String message) {
        view.displayErrorMessage("Failed to get story: " + message);
    }

    @Override
    public void handleStatusException(Exception ex) {
        view.displayErrorMessage("Failed to get story because of exception: " + ex);
    }

    @Override
    public void handleUserSuccess(User user) {
        view.navigateToUser(user);
        view.displayInfoMessage("Getting user's profile...");
    }

    @Override
    public void handleUserFailure(String message) {
        view.displayErrorMessage("Failed to get user's profile: " + message);
    }

    @Override
    public void handleUserException(Exception ex) {
        view.displayErrorMessage("Failed to get user's profile because of exception: " + ex);

    }

    public void loadMoreItems(){
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoading(true);
            view.setLoading(true);
            getStory(authToken, user, PAGE_SIZE, lastStatus);
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public void getStory(AuthToken authToken, User user, int limit, Status lastStatus) {
        getStoryService().getStory(authToken, user, limit, lastStatus, this);
    }

    public StatusService getStoryService() {
        return new StatusService();
    }

    public void getUser(AuthToken authToken, String alias) {
        new StatusService().getUser(authToken, alias, this);
    }


}
