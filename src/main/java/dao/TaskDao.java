package dao;

import models.Task;

import java.util.List;

public interface TaskDao {
  //create
  void add(Task task);

  //read
  List<Task> getAll();

  Task findById(int id);

  void update(int id, String newDescription, int categoryId);

  void deleteById(int id);

  void clearAllTasks();
}
