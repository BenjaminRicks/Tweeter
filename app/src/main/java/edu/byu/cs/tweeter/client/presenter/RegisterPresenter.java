package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.text.Editable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter implements UserService.RegisterObserver {

    private RegisterView view;
    private Editable firstName;
    private Editable lastName;
    private Editable alias;
    private Editable password;
    private ImageView imageToUpload;

    public RegisterPresenter(RegisterView view, Editable firstName, Editable lastName, Editable alias, Editable password, ImageView imageToUpload) {
        this.view = view;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.password = password;
        this.imageToUpload = imageToUpload;
    }
    public interface RegisterView {
        void displayErrorMessage(String message);
        void clearErrorMessage();

        void displayInfoMessage(String message);
        void clearInfoMessage();
        void setRegisteringToast();

        void navigateToUser(User user);
    }

    public void register(String firstName, String lastName, String alias, String password, ImageView imageToUpload){

        String errorMessage = validateRegistration();

        view.clearErrorMessage();
        view.displayInfoMessage("Registering...");

        Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        if(errorMessage == null) {

            new UserService().register(firstName, lastName, alias, password, imageBytes, this);
        }
        else{
            view.displayErrorMessage(errorMessage);
        }
    }

    public String validateRegistration() {
        if (firstName.length() == 0) {
            return "First Name cannot be empty.";
        }
        if (lastName.length() == 0) {
            return "Last Name cannot be empty.";
        }
        if (alias.length() == 0) {
            return "Alias cannot be empty.";
        }
        if (alias.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (alias.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }

        if (imageToUpload.getDrawable() == null) {
            return "Profile image must be uploaded.";
        }
        return null;
    }

    @Override
    public void handleRegisterSuccess(User user, AuthToken authToken) {
        view.displayInfoMessage("Hello " + Cache.getInstance().getCurrUser().getName());

        view.navigateToUser(user);

    }

    @Override
    public void handleRegisterFailure(String message) {
        view.displayErrorMessage("Failed to register: " + message);
    }

    @Override
    public void handleRegisterException(Exception ex) {
        view.displayErrorMessage("Failed to register because of exception: " + ex);
    }

}
