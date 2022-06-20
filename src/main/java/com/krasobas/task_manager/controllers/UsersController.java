package com.krasobas.task_manager.controllers;

import com.krasobas.task_manager.dao.users.UserDAO;
import com.krasobas.task_manager.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsersController {
    private UserDAO userDAO;

    @Autowired
    public UsersController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/demo")
    public String demo(HttpSession httpSession) {
        User demo = userDAO.getUser("demo", "demo");
        demo.setStatus(true);
        httpSession.setAttribute("user", demo);
        return "redirect:/tasks";
    }

    @GetMapping()
    public String singIn(@ModelAttribute("user") User user) {
        return "users/singin";
    }

    @PostMapping()
    public String getUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult, HttpSession httpSession, Model model) {
        if (bindingResult.hasErrors()) {
            return "users/singin";
        }
        if (!userDAO.checkLogin(user.getLogin())) {
            model.addAttribute("user_not_found", "There is no any user with such login.");
            return "users/singin";
        } else if (!userDAO.checkPassword(user.getLogin(), user.getPassword())) {
            model.addAttribute("user_not_found", "Wrong password.");
            return "users/singin";
        } else {
            User foundUser = userDAO.getUser(user.getLogin(), user.getPassword());
            foundUser.setStatus(true);
            httpSession.setAttribute("user", foundUser);
            return "redirect:/tasks";
        }
    }

    @GetMapping("/new")
    public String singUp(@ModelAttribute("user") User user) {
        return "users/singup";
    }

    @PostMapping("/new")
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult, HttpSession httpSession, Model model) {
        if (bindingResult.hasErrors()) {
            return "users/singup";
        }
        if (userDAO.checkLogin(user.getLogin())) {
            model.addAttribute("user_login_exist", "This login is already used. Please chose another one.");
            return "users/singup";
        } else {
            User newUser = userDAO.addUser(user.getLogin(), user.getPassword());
            newUser.setStatus(true);
            httpSession.setAttribute("user", newUser);
            return "redirect:/tasks";
        }
    }

    @GetMapping("/exit")
    public String exit(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        user.setStatus(false);
        httpSession.removeAttribute("user");
        return "redirect:/";
    }
}
