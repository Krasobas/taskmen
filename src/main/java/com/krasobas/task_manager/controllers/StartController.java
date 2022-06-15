package com.krasobas.task_manager.controllers;

import com.krasobas.task_manager.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class StartController {
    @GetMapping("/")
    String main(HttpSession httpSession) {
        if (httpSession.getAttribute("user") != null) {
            return "redirect:/tasks";
        }
        return "index";
    }
}
