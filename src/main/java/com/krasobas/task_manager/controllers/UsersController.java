package com.krasobas.task_manager.controllers;

import com.krasobas.task_manager.dao.UserDAO;
import com.krasobas.task_manager.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsersController {
    private UserDAO users;

    @Autowired
    public UsersController(UserDAO users) {
        this.users = users;
    }

    @GetMapping()
    public String singIn(@ModelAttribute("user") User user) {
        return "users/singin";
    }

    @PostMapping()
    public String getUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "users/singin";
        }
        if (users.checkUser(user)) {
            httpSession.setAttribute("user", users.findUser(user));
            return "redirect:/tasks";
        } else {
            return "users/notfound";
        }
    }

    @GetMapping("/demo")
    public String demo(HttpSession httpSession) {
        httpSession.setAttribute("user", new User("demo", "demo"));
        return "redirect:/tasks";
    }

    @GetMapping("/exit")
    public String exit(HttpSession httpSession) {
        httpSession.removeAttribute("user");
        return "redirect:/";
    }
}
