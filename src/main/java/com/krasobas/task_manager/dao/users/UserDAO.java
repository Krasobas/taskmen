package com.krasobas.task_manager.dao.users;

import com.krasobas.task_manager.models.User;

public interface UserDAO {

    boolean checkLogin(String login);

    boolean checkPassword(String login,String password);

    User getUser(String login, String password);

    User addUser(String login, String password);

    boolean deleteUser(int userId);
}
