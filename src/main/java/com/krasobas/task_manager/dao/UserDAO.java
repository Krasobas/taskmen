package com.krasobas.task_manager.dao;

import com.krasobas.task_manager.models.Task;
import com.krasobas.task_manager.models.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class UserDAO {
    private static Map<String, User> USERS = new HashMap<>();

    public UserDAO() {
        testInit();
    }

    private void testInit() {
        //create test user 1
        User user1 = new User("krasobas", "password");
        Task old = new Task("четыре", "надо делать");
        old.setCreated(LocalDateTime.now().minusDays(10));
        Task yesterday = new Task("cinq", "надо делать");
        yesterday.setCreated(LocalDateTime.now().minusDays(1));
        List<Task> tasks = new LinkedList<>( List.of(
                new Task("first", "do stuff"),
                new Task("second", "do stuff"),
                new Task("third", "do stuff"),
                old,
                yesterday
        ));

        //create test user 2
        User user2 = new User("artem", "artem");
        List<Task> tasks2 = new LinkedList<>( List.of(
                new Task("приготовить обед", "чтобы было вкусно"),
                new Task("прибухнуть", "не очень жестко")
        ));


        // add test tasks into test user task list
        user1.setTasks(new MemDAO(tasks));
        user2.setTasks(new MemDAO(tasks2));
        USERS.put(user1.getLogin(), user1);
        USERS.put(user2.getLogin(), user2);
    }

    public boolean checkLogin(String login) {
        return USERS.containsKey(login);
    }

    public boolean checkPassword(User user) {
        return USERS.get(user.getLogin())
                .getPassword()
                .equals(user.getPassword());
    }

    public User addUser(User user) {
        User toAdd = new User(
                user.getLogin(),
                user.getPassword()
        );
        USERS.put(
                toAdd.getLogin(),
                toAdd
        );
        return toAdd;
    }

    public User findUser(User user) {
        return USERS.get(user.getLogin());
    }

    public User getUserById(int userId) {
        return USERS.values()
                .stream()
                .filter(user -> user.getId() == userId)
                .findAny().orElse(null);
    }
}
