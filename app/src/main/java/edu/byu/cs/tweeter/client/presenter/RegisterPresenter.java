package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import edu.byu.cs.tweeter.client.model.service.UserService;

public class RegisterPresenter extends AuthenticatePresenter {

    private Editable firstName;
    private Editable lastName;
    private Editable alias;
    private Editable password;
    private ImageView imageToUpload;

    public RegisterPresenter(RegisterView view, Editable firstName, Editable lastName, Editable alias, Editable password, ImageView imageToUpload) {
        super(view);
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.password = password;
        this.imageToUpload = imageToUpload;
    }
    public interface RegisterView extends AuthenticateView {}

    public void register(String firstName, String lastName, String alias, String password, ImageView imageToUpload){

        String errorMessage = validateRegistration();

        view.clearErrorMessage();
        view.displayInfoMessage("Registering...");

        Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        if(errorMessage == null) {
            new UserService().register(firstName, lastName, alias, password, imageBytes, new AuthenticateObserver());
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

}
