package com.krasobas.task_manager.dao.users;

import com.krasobas.task_manager.dao.users.UserDAO;
import com.krasobas.task_manager.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class CloudUserDAO implements UserDAO {
    private Connection db;

    @Autowired
    public CloudUserDAO(Connection db) {
        this.db = db;
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
}
