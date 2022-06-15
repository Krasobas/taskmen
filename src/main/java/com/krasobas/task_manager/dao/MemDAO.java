package com.krasobas.task_manager.dao;

import com.krasobas.task_manager.models.Task;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MemDAO implements DAO {
    private List<Task> tasks;

    public MemDAO(List<Task> tasks) {
        this.tasks = tasks;
    }

    public MemDAO() {
        tasks = new LinkedList<>();
    }



    @Override
    public List<Task> showAll() {
        return tasks;
    }

    @Override
    public Task show(int id) {
        return tasks.stream().filter(task -> task.getId() == id).findAny().orElse(null);
    }

    @Override
    public Task showPrevious(int id) {
        Task current = show(id);
        Task previous = null;
        ListIterator<Task> iterator = tasks.listIterator();
        while (iterator.hasNext()) {
            if (current.equals(iterator.next())) {
                iterator.previous();
                if (iterator.hasPrevious()) {
                    previous = iterator.previous();
                }
                break;
            }
        }
        return previous;
    }

    @Override
    public Task showNext(int id) {
        Task current = show(id);
        Task next = null;
        ListIterator<Task> iterator = tasks.listIterator();
        while (iterator.hasNext()) {
            if (current.equals(iterator.next())) {
                if (iterator.hasNext()) {
                    next = iterator.next();
                }
                break;
            }
        }
        return next;
    }

    @Override
    public List<Task> findByName(String key) {
        return null;
    }

    @Override
    public void save(Task task) {
        tasks.add(task);
    }

    @Override
    public void update(int id, Task task) {
        Task toBeUpdated = show(id);
        toBeUpdated.setTitle(task.getTitle());
        toBeUpdated.setContent(task.getContent());
    }

    @Override
    public void updateStatus(int id) {
        show(id).setStatus();
    }

    @Override
    public void delete(int id) {
        tasks.removeIf(task -> task.getId() == id);
    }
}
