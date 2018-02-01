package dao;

import models.Task;

import java.util.List;

public interface TaskDao {
  //create
  void add(Task task);

  //read
  List<Task> getAll();

  Task findById(int id);
  //update
  //  void update(int id, String content);
  //delete
  //   void deleteTask();

  //   void clearAllTasks();
}
