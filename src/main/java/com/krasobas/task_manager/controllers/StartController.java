package com.krasobas.task_manager.controllers;

import com.krasobas.task_manager.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class StartController {
    @GetMapping("/")
    String main(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user != null && user.isStatus()) {
            return "redirect:/tasks";
        }
        return "index";
    }
}
