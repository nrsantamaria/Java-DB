package dao;

import models.Category;
import models.Task;

import java.util.List;

public interface CategoryDao {
  void add(Category category);

  List<Category> getAll();
  List<Task> getAllTasksByCategory(int categoryId);

  Category findById(int id);

  void update(int id, String name);

  void deleteById(int id);
  void clearAllCategories();
}
