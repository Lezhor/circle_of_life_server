package de.htw_berlin.database.control.daos;

import de.htw_berlin.database.models.User;

public interface UserDao extends BaseDao<User> {

    User getByUsername(String username);

    boolean clearUser(User user);
}
