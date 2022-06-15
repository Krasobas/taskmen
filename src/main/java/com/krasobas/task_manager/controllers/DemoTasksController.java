package com.krasobas.task_manager.controllers;
//
//import com.krasobas.task_manager.dao.DAO;
//import com.krasobas.task_manager.models.Task;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//@Controller
//@RequestMapping("demo/tasks")
//public class DemoTasksController {
//    private DAO dao;
//    @Autowired
//    public DemoTasksController(DAO dao) {
//        this.dao = dao;
//    }
//
//    @GetMapping()
//    public String showAll(Model model) {
//        model.addAttribute("tasks", dao.showAll());
//        return "tasks/main";
//    }
//
//    @GetMapping("/{id}")
//    public String show(@PathVariable("id") int id, Model model) {
//        model.addAttribute("task", dao.show(id));
//        return "tasks/show";
//    }
//
//    @GetMapping("/new")
//    public String newTask(@ModelAttribute("task") Task task) {
//        return "tasks/new";
//    }
//
//    @PostMapping()
//    public String create(@ModelAttribute("task") @Valid Task task,
//                         BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "tasks/new";
//        }
//        dao.save(task);
//        return "redirect:/demo/tasks";
//    }
//
//    @GetMapping("/{id}/edit")
//    public String edit(@PathVariable("id") int id, Model model) {
//        model.addAttribute("task", dao.show(id));
//        return "tasks/edit";
//    }
//
//    @PatchMapping("/{id}")
//    public String update(@ModelAttribute("task") @Valid Task task,
//                         BindingResult bindingResult,
//                         @PathVariable("id") int id) {
//        if (bindingResult.hasErrors()) {
//            return "tasks/edit";
//        }
//        dao.update(id, task);
//        return "redirect:/demo/tasks/{id}";
//    }
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") int id) {
//        dao.delete(id);
//        return "redirect:/demo/tasks";
//    }
//
//    @GetMapping("/{id}/previous")
//    public String previous(@PathVariable("id") int id, Model model) {
//        Task previous = dao.showPrevious(id);
//        if (previous != null) {
//            model.addAttribute("task", previous);
//            return "tasks/show";
//        } else {
//            return "redirect:/demo/tasks/{id}";
//        }
//    }
//
//    @GetMapping("/{id}/next")
//    public String next(@PathVariable("id") int id, Model model) {
//        Task next = dao.showNext(id);
//        if (next != null) {
//            model.addAttribute("task", next);
//            return "tasks/show";
//        } else {
//            return "redirect:/demo/tasks/{id}";
//        }
//    }
//
//    @GetMapping("/{id}/done")
//    public String done(@PathVariable("id") int id) {
//        dao.updateStatus(id);
//        return "redirect:/demo/tasks";
//    }
//}
