import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.Sql2oTaskDao;
import dao.Sql2oCategoryDao;
import models.Category;
import models.Task;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.*;

public class App {
  public static void main(String[] args) { //type “psvm + tab” to autocreate this
    staticFileLocation("/public");

    String connectionString = "jdbc:h2:~/todolist.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
    Sql2o sql2o = new Sql2o(connectionString, "", "");
    Sql2oTaskDao taskDao = new Sql2oTaskDao(sql2o);
    Sql2oCategoryDao categoryDao = new Sql2oCategoryDao(sql2o);

    //get: delete all categories and all tasks
    get("/categories/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      taskDao.clearAllTasks();
      categoryDao.clearAllCategories();

      List<Category> allCategories = categoryDao.getAll();
      model.put("categories", allCategories);

      return new ModelAndView(model, "success.hbs");
    }, new HandlebarsTemplateEngine());

    //get: delete a category and tasks it contains
    get("/categories/:category_id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      int idOfCategoryToDelete = Integer.parseInt(request.params("category_id"));
      categoryDao.deleteById(idOfCategoryToDelete);

      List<Category> allCategories = categoryDao.getAll();
      model.put("categories", allCategories);

      return new ModelAndView(model, "success.hbs");
    }, new HandlebarsTemplateEngine());


    //get: delete all tasks
    get("/tasks/delete", (req, res) -> {
      Map<String, Object> model = new HashMap<>();

      List<Category> allCategories = categoryDao.getAll();
      model.put("categories", allCategories);

      taskDao.clearAllTasks();
      return new ModelAndView(model, "success.hbs");
    }, new HandlebarsTemplateEngine());

    //get: delete an individual task
    get("/tasks/:id/delete", (req, res) -> {
      Map<String, Object> model = new HashMap<>();
      int idOfTaskToDelete = Integer.parseInt(req.params("task_id")); //pull id - must match route segment
      Task deleteTask = taskDao.findById(idOfTaskToDelete);
      taskDao.deleteById(idOfTaskToDelete);
      return new ModelAndView(model, "success.hbs");
    }, new HandlebarsTemplateEngine());

    //get: show a form to create a new category
    get("/categories/new", (req, res) -> {
      Map<String, Object> model = new HashMap<>();
      return new ModelAndView(model, "category-form.hbs");
    }, new HandlebarsTemplateEngine());

    //post: process a form to create a new category
    post("/categories/new", (request, response) -> { //URL to make new task on POST route
      Map<String, Object> model = new HashMap<>();
      String name = request.queryParams("name");
      Category newCategory = new Category(name);
      categoryDao.add(newCategory);

      List<Category> categories = categoryDao.getAll();
      model.put("categories", categories);
      return new ModelAndView(model, "success.hbs");
    }, new HandlebarsTemplateEngine());


    //get: show new task form
    get("/tasks/new", (req, res) -> {
      Map<String, Object> model = new HashMap<>();
      List<Category> categories = categoryDao.getAll();
      model.put("categories", categories);

      return new ModelAndView(model, "task-form.hbs");
    }, new HandlebarsTemplateEngine());

    //task: process new task form
    post("/tasks/new", (request, response) -> { //URL to make new task on POST route
      Map<String, Object> model = new HashMap<>();

      List<Category> categories = categoryDao.getAll();
      model.put("categories", categories);

      int categoryId = Integer.parseInt(request.queryParams("categoryId"));
      Category category = categoryDao.findById(categoryId);
      model.put("category", category);

      String description = request.queryParams("description");
      Task newTask = new Task(description, categoryId);
      taskDao.add(newTask);
      model.put("task", newTask);
      return new ModelAndView(model, "success.hbs");
    }, new HandlebarsTemplateEngine());


    //get: show all tasks
    get("/", (req, res) -> {
      Map<String, Object> model = new HashMap<>();

      List<Category> allCategories = categoryDao.getAll();
      model.put("categories", allCategories);

      List<Task> tasks = taskDao.getAll();
      model.put("tasks", tasks);

      return new ModelAndView(model, "index.hbs");
    }, new HandlebarsTemplateEngine());

    //get: show an individual task
    get("/tasks/:id", (req, res) -> {
      Map<String, Object> model = new HashMap<>();
      int idOfTaskToFind = Integer.parseInt(req.params("id")); //pull id - must match route segment
      Task foundTask = taskDao.findById(idOfTaskToFind); //use it to find task
      model.put("task", foundTask); //add it to model for template to display
      return new ModelAndView(model, "task-detail.hbs"); //individual task page.
    }, new HandlebarsTemplateEngine());

    //get: show all categories
    get("/categories", (req, res) -> {
      Map<String, Object> model = new HashMap<>();

      List<Category> allCategories = categoryDao.getAll();
      model.put("categories", allCategories);

      return new ModelAndView(model, "categories.hbs");
    }, new HandlebarsTemplateEngine());


    //get a specific category (and the tasks it contains)
    get("/categories/:catId", (req, res) -> {
      Map<String, Object> model = new HashMap<>();

      List<Category> allCategories = categoryDao.getAll();
      model.put("categories", allCategories);

      int idofCategoryToFind = Integer.parseInt(req.params("catId"));
      Category category = categoryDao.findById(idofCategoryToFind);
      model.put("category", category);

      List<Task> tasksInCategory = categoryDao.getAllTasksByCategory(idofCategoryToFind);
      model.put("tasksInCategory", tasksInCategory);
      return new ModelAndView(model, "category-detail.hbs");
    }, new HandlebarsTemplateEngine());

    //get: show an individual task that is nested in a category
    get("/categories/:category_id/tasks/:task_id", (req, res) -> {
      Map<String, Object> model = new HashMap<>();

      List<Category> allCategories = categoryDao.getAll();
      model.put("categories", allCategories);

      int idOfTaskToFind = Integer.parseInt(req.params("task_id"));
      Task foundTask = taskDao.findById(idOfTaskToFind);
      model.put("task", foundTask);
      return new ModelAndView(model, "task-detail.hbs");
    }, new HandlebarsTemplateEngine());

    //get: show a form to update a category
    get("/categories/:id/update", (request, response) -> {
      Map<String, Object> model = new HashMap<>();

      model.put("editCategory", true);

      List<Category> allCategories = categoryDao.getAll();
      model.put("categories", allCategories);

      return new ModelAndView(model, "category-form.hbs");
    }, new HandlebarsTemplateEngine());

    //post: process a form to update a category
    post("/categories/update", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      int idOfCategoryToEdit = Integer.parseInt(request.queryParams("editCategoryId"));
      String newName = request.queryParams("newCategoryName");
      categoryDao.update(idOfCategoryToEdit, newName);

      List<Category> allCategories = categoryDao.getAll();
      model.put("categories", allCategories);

      return new ModelAndView(model, "success.hbs");
    }, new HandlebarsTemplateEngine());

    //get: show a form to update a task
    get("/tasks/:id/update", (req, res) -> {
      Map<String, Object> model = new HashMap<>();

      List<Category> allCategories = categoryDao.getAll();
      model.put("categories", allCategories);

      List<Task> allTasks = taskDao.getAll();
      model.put("tasks", allTasks);
      model.put("editTask", true);
      return new ModelAndView(model, "task-form.hbs");
    }, new HandlebarsTemplateEngine());

    //task: process a form to update a task
    post("/tasks/update", (req, res) -> { //URL to make new task on POST route
      Map<String, Object> model = new HashMap<>();

      List<Category> allCategories = categoryDao.getAll();
      model.put("categories", allCategories);

      String newContent = req.queryParams("description");
      int categoryId = Integer.parseInt(req.queryParams("categoryId"));
      int idOfTaskToEdit = Integer.parseInt(req.queryParams("taskToEditId"));
      taskDao.update(idOfTaskToEdit, newContent, categoryId);

      return new ModelAndView(model, "success.hbs");
    }, new HandlebarsTemplateEngine());

  }
}
