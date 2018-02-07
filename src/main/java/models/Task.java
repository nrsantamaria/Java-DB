package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Task {

  private String description;
  private boolean completed;
  private LocalDateTime createdAt;
  private int id;
  private int categoryId;

  public Task(String description, int categoryId) {
    this.description = description;
    this.completed = false;
    this.categoryId = categoryId;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Task task = (Task) o;

    if (completed != task.completed) return false;
    if (id != task.id) return false;
    if (categoryId != task.categoryId) return false;
    return description.equals(task.description);
  }

  @Override
  public int hashCode() {
    int result = description.hashCode();
    result = 31 * result + (completed ? 1 : 0);
    result = 31 * result + id;
    result = 31 * result + categoryId;
    return result;
  }

  public String getDescription() {
    return this.description;
  }

  public boolean getCompleted() {
    return this.completed;
  }

  public int getId() {
    return this.id;
  }

  public int getCategoryId() {
    return categoryId;
  }
}
