package com.krasobas.task_manager.controllers;

import com.krasobas.task_manager.dao.DAO;
import com.krasobas.task_manager.dao.UserDAO;
import com.krasobas.task_manager.models.Task;
import com.krasobas.task_manager.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/tasks")
@SessionAttributes("user")
public class TasksController {
    @GetMapping()
    public String showAll(@ModelAttribute User user, Model model) {

        model.addAttribute("tasks", user.getTasks().showAll());
        return "tasks/main";
    }

    @GetMapping("/{id}")
    public String show(@ModelAttribute User user, @PathVariable("id") int id, Model model) {
        model.addAttribute("task", user.getTasks().show(id));
        return "tasks/show";
    }

    @GetMapping("/new")
    public String newTask(@ModelAttribute("task") Task task) {
        return "tasks/new";
    }

    @PostMapping()
    public String create(@ModelAttribute User user, @ModelAttribute("task") @Valid Task task,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tasks/new";
        }
        user.getTasks().save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(@ModelAttribute User user, @PathVariable("id") int id, Model model) {
        model.addAttribute("task", user.getTasks().show(id));
        return "tasks/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute User user, @ModelAttribute("task") @Valid Task task,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "tasks/edit";
        }
        user.getTasks().update(id, task);
        return "redirect:/tasks/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@ModelAttribute User user, @PathVariable("id") int id) {
        user.getTasks().delete(id);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/previous")
    public String previous(@ModelAttribute User user, @PathVariable("id") int id, Model model) {
        Task previous = user.getTasks().showPrevious(id);
        if (previous != null) {
            model.addAttribute("task", previous);
            return "tasks/show";
        } else {
            return "redirect:/tasks/{id}";
        }
    }

    @GetMapping("/{id}/next")
    public String next(@ModelAttribute User user, @PathVariable("id") int id, Model model) {
        Task next = user.getTasks().showNext(id);
        if (next != null) {
            model.addAttribute("task", next);
            return "tasks/show";
        } else {
            return "redirect:/tasks/{id}";
        }
    }

    @GetMapping("/{id}/done")
    public String done(@ModelAttribute User user, @PathVariable("id") int id) {
        user.getTasks().updateStatus(id);
        return "redirect:/tasks";
    }
}
