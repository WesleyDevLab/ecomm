package ru.ncedu.ecomm.servlets.services;

import ru.ncedu.ecomm.data.models.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.ncedu.ecomm.data.DAOFactory.getDAOFactory;

public class ProfileService {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private User userProfile;

    public ProfileService(String firstName, String lastName, String email, String password,
                          long userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userProfile = getDAOFactory().getUserDAO().getUserById(userId);
    }

    public ProfileService(long userId){
        this.userProfile = getUserProfileById(userId);
        this.firstName = userProfile.getFirstName();
        this.lastName = userProfile.getLastName();
        this.email = userProfile.getEmail();
        this.password = userProfile.getPassword();
    }

    private String getFirstName(String firstName) {
        if (!firstName.trim().isEmpty()) {
            return userProfile.getFirstName() != firstName ?
                    firstName :
                    userProfile.getFirstName();
        } else {
            return userProfile.getFirstName();
        }
    }

    private String getLastName(String lastName) {
        if (!lastName.trim().isEmpty()) {
            return userProfile.getLastName() != lastName ?
                    lastName :
                    userProfile.getLastName();
        } else {
            return userProfile.getLastName();
        }
    }

    private String getEmail(String email) {
        String regPattern = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
        Pattern patternEmailValidation = Pattern.compile(regPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = patternEmailValidation.matcher(email);
        if (matcher.find()) {
            return userProfile.getEmail() != email ?
                    email :
                    userProfile.getEmail();
        } else {
            return userProfile.getEmail();
        }
    }

    private String getPassword(String password) {
        if (!password.trim().isEmpty()) {
            return userProfile.getPassword() != password ?
                    password :
                    userProfile.getPassword();
        } else {
            return userProfile.getPassword();
        }
    }

    public User getUserProfile() {
        return this.userProfile;
    }

    private User getUserProfileById(long userId){
        User user = getDAOFactory().getUserDAO().getUserById(userId);
        return user;
    }

    public User changeProfile(User user){
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setPassword(this.password);

        return user;
    }
}
