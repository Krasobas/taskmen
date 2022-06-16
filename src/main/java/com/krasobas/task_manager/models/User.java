package com.krasobas.task_manager.models;

import com.krasobas.task_manager.dao.DAO;
import com.krasobas.task_manager.dao.MemDAO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User {
    private static int ID_GENERATOR = 0;
    private int id;
    @NotEmpty(message = "Login should not be empty.")
    @Size(min=5, max=10, message = "Login should be between 5 and 10 characters.")
    private String login;
    @NotEmpty(message = "Password should not be empty.")
    @Size(min=5, max=10, message = "Password should be between 5 and 10 characters.")
    private String password;
    private DAO tasks;
    private boolean status = false;

    public User(String login, String password) {
        this.tasks = new MemDAO();
        this.id = ++ID_GENERATOR;
        this.login = login;
        this.password = password;
    }

    public User() {this.id = ++ID_GENERATOR;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DAO getTasks() {
        return tasks;
    }

    public void setTasks(DAO tasks) {
        this.tasks = tasks;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && login.equals(user.login) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }
}
