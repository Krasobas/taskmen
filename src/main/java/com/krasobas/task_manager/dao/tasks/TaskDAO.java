package com.krasobas.task_manager.dao.tasks;

import com.krasobas.task_manager.models.Task;
import com.krasobas.task_manager.models.User;

import java.util.List;

public interface TaskDAO {

    List<Task> tasksList(int userId);

    Task showTask(int userId, int taskId);

    Task saveTask(int userId, Task task);

    void updateTask(int userId, int taskId, Task task);

    Task previousTask(int userId, int taskId);

    Task nextTask(int userId, int taskId);

    boolean updateTaskStatus(int userId, int taskId);

    boolean deleteTask(int userId, int taskId);

}
