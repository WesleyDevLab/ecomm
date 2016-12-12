package ru.ncedu.ecomm.data.accessobjects;

import ru.ncedu.ecomm.data.models.User;

import java.util.List;

/**
 * Created by Andrey on 12/8/2016.
 */
public interface UserDAO {
    List<User> getUser();
    User getUserById(long id);
    List<User> getUserByRoleId(long roleId);
    User addUser(User user);
    User updateUser(User user);
    void deleteUser(User user);
}
