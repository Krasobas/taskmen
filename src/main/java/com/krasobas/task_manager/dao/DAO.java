package com.krasobas.task_manager.dao;

import com.krasobas.task_manager.models.Task;
import java.util.List;

public interface DAO {

    void delete(int id);

    List<Task> showAll();

    List<Task> findByName(String key);

    Task show(int id);

    void save(Task task);

    void update(int id, Task task);

    Task showPrevious(int id);

    Task showNext(int id);

    void updateStatus(int id);
}
