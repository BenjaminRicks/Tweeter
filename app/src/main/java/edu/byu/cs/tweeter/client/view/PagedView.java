package edu.byu.cs.tweeter.client.view;

import java.util.List;

import edu.byu.cs.tweeter.client.view.View;
import edu.byu.cs.tweeter.model.domain.User;

public interface PagedView<T> extends View {
    void setLoading(boolean isBoolean);
    void addItems(List<T> items);
    void navigateToUser(User user);
}
