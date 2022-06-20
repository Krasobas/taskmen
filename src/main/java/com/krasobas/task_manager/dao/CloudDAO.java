package com.krasobas.task_manager.dao;

import com.krasobas.task_manager.models.Task;
import com.krasobas.task_manager.models.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

@Component
public class CloudDAO implements DAO {
    private Connection db;
    List<User> users;

    public CloudDAO() {
        init();
    }

    private void init() {
        try (InputStream in = CloudDAO.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("postgresql.driver-class-name"));
            db = DriverManager.getConnection(
                    config.getProperty("postgresql.url"),
                    config.getProperty("postgresql.username"),
                    config.getProperty("postgresql.password"));
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkLogin(String login) {
        boolean rsl = false;
        String sql = "select * from users where login like ?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            rsl = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public boolean checkPassword(String login, String password) {
        boolean rsl = false;
        String sql = "select * from users where login like ? and password like ?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            rsl = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public User getUser(String login, String password) {
        User user = new User(login, password);
        String sql = "select * from users where login like ? and password like ?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User addUser(String login, String password) {
        User user = new User(login, password);
        String sql = "insert into users (login, password) values (?, ?)";
        try (PreparedStatement ps = db.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, login);
            ps.setString(2, password);
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean deleteUser(int userId) {
        boolean rsl = false;
        String sql = "delete from users where id = ?";
        try (PreparedStatement st = db.prepareStatement(sql)) {
            st.setInt(1, userId);
            rsl = st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public List<Task> tasksList(int userId) {
        List<Task> tasks = new LinkedList<>();
        String sql = "select * from tasks where user_id = ?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tasks.add(createTask(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    private Task createTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setTitle(rs.getString("title"));
        task.setContent(rs.getString("content"));
        task.setCreated(rs.getTimestamp("created").toLocalDateTime());
        task.setStatus(rs.getBoolean("done"));
        return task;
    }

    @Override
    public Task showTask(int userId, int taskId) {
        String sql = "select * from tasks where id = ? and user_id = ?";
        return findTask(sql, userId, taskId);
    }

    private Task findTask(String sql, int userId, int taskId) {
        Task task = null;
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, taskId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    task = createTask(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public void saveTask(int userId, Task task) {
        String sql = "insert into tasks(title, content, created, done, user_id) values (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = db.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getContent());
            ps.setTimestamp(3, Timestamp.valueOf(task.getCreated()));
            ps.setBoolean(4, task.isDone());
            ps.setInt(5, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTask(int userId, int taskId, Task task) {
        String sql = "update tasks set title = ?, content = ? where id = ? and user_id = ?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getContent());
            ps.setInt(3, taskId);
            ps.setInt(4, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // это какая-то лажа, нужно в корне поменять логику пагинации
    // это нужно сделать на уровне страницы, чтобы не загружать заново лист задач

    @Override
    public Task previousTask(int userId, int taskId) {
        String sql = "select * from tasks where user_id = ? and id < ? order by desc limit 1";
        return findTask(sql, userId, taskId);
    }

    @Override
    public Task nextTask(int userId, int taskId) {
        String sql = "select * from tasks where user_id = ? and id > ? order by asc limit 1";
        return findTask(sql, userId, taskId);
    }

    @Override
    public boolean updateTaskStatus(int userId, int taskId) {
        boolean rsl = false;
        String sql = "update tasks set done = not done where id = ? and user_id = ?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            ps.setInt(2, userId);
            rsl = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public boolean deleteTask(int userId, int taskId) {
        boolean rsl = false;
        String sql = "delete from tasks where id = ? and user_id = ?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            ps.setInt(2, userId);
            rsl = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }
}
