package com.krasobas.task_manager.controllers;

import com.krasobas.task_manager.dao.tasks.TaskDAO;
import com.krasobas.task_manager.models.Task;
import com.krasobas.task_manager.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/tasks")
@SessionAttributes("user")
public class TasksController {
    private TaskDAO taskDao;

    @Autowired
    public TasksController(TaskDAO taskDao) {
        this.taskDao = taskDao;
    }

    @GetMapping()
    public String showAll(HttpSession httpSession, Model model) {
        User user = (User) httpSession.getAttribute("user");
        user.setTasks(taskDao.tasksList(user.getId()));
        model.addAttribute("tasks", user.getTasks());
        return "tasks/main";
    }

    @GetMapping("/{id}")
    public String show(HttpSession httpSession, @PathVariable("id") int id, Model model) {
        User user = (User) httpSession.getAttribute("user");
        Task task = taskDao.showTask(user.getId(), id);
        System.out.println("Controller task " + task);
        model.addAttribute("task", task);
        return "tasks/show";
    }

    @GetMapping("/new")
    public String newTask(@ModelAttribute("task") Task task) {
        return "tasks/new";
    }

    @PostMapping()
    public String create(HttpSession httpSession, @ModelAttribute("task") @Valid Task task,
                         BindingResult bindingResult) {
        User user = (User) httpSession.getAttribute("user");
        if (bindingResult.hasErrors()) {
            return "tasks/new";
        }
        taskDao.saveTask(user.getId(), task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(HttpSession httpSession, @PathVariable("id") int id, Model model) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("task", taskDao.showTask(user.getId(), id));
        return "tasks/edit";
    }

    @PatchMapping("/{id}")
    public String update(HttpSession httpSession, @ModelAttribute("task") @Valid Task task,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        User user = (User) httpSession.getAttribute("user");
        if (bindingResult.hasErrors()) {
            return "tasks/edit";
        }
        taskDao.updateTask(user.getId(), id, task);
        return "redirect:/tasks/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(HttpSession httpSession, @PathVariable("id") int id) {
        User user = (User) httpSession.getAttribute("user");
        taskDao.deleteTask(user.getId(), id);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/previous")
    public String previous(HttpSession httpSession, @PathVariable("id") int id, Model model) {
        User user = (User) httpSession.getAttribute("user");
        Task previous = taskDao.previousTask(user.getId(), id);
        if (previous != null) {
            model.addAttribute("task", previous);
            return "tasks/show";
        } else {
            return "redirect:/tasks/{id}";
        }
    }

    @GetMapping("/{id}/next")
    public String next(HttpSession httpSession, @PathVariable("id") int id, Model model) {
        User user = (User) httpSession.getAttribute("user");
        Task next = taskDao.nextTask(user.getId(), id);
        if (next != null) {
            model.addAttribute("task", next);
            return "tasks/show";
        } else {
            return "redirect:/tasks/{id}";
        }
    }

    @GetMapping("/{id}/done")
    public String done(HttpSession httpSession, @PathVariable("id") int id) {
        User user = (User) httpSession.getAttribute("user");
        taskDao.updateTaskStatus(user.getId(), id);
        return "redirect:/tasks";
    }
}
