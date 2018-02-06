package dao;

import models.Category;
import models.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class Sql2oCategoryDaoTest {

  private Sql2oCategoryDao categoryDao;
  private Sql2oTaskDao taskDao;
  private Connection conn;

  @Before
  public void setUp() throws Exception {
    String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
    Sql2o sql2o = new Sql2o(connectionString, "", "");
    categoryDao = new Sql2oCategoryDao(sql2o);
    taskDao = new Sql2oTaskDao(sql2o);
    
    conn = sql2o.open();
  }
  
  @After
  public void tearDown() throws Exception {
    conn.close();
  }

  @Test
  public void addingCourseSetsId() throws Exception {
    Category category = new Category("Chores");
    int originalCategoryId = category.getId();
    categoryDao.add(category);
    assertNotEquals(originalCategoryId, category.getId());
  }

  @Test
  public void existingCategoriesCanBeFoundById() throws Exception {
    Category category = new Category("Chores");
    categoryDao.add(category);
    Category foundCategory = categoryDao.findById(category.getId());
    assertEquals(category, foundCategory);
  }

  @Test
  public void getAllCategories() throws Exception {
    Category category = new Category("Chores");
    Category secondCategory = new Category("misc");
    categoryDao.add(category);
    categoryDao.add(secondCategory);
    assertEquals(2, categoryDao.getAll().size());
  }

  @Test
  public void noCategoriesFound() throws Exception {
    assertEquals(0, categoryDao.getAll().size());
  }

  @Test
  public void updateACategpry() throws Exception {
    Category category = new Category("chores");
    categoryDao.add(category);

    categoryDao.update(category.getId(), "Chores");
    Category updatedCategory = categoryDao.findById(category.getId());
    assertNotEquals(category.getName(), updatedCategory.getName());
  }

  @Test
  public void deleteATask() throws Exception {
    Category category = new Category("chores");
    categoryDao.add(category);

    categoryDao.deleteById(category.getId());
    assertEquals(0, categoryDao.getAll().size());
  }

  @Test
  public void deleteAllCategories() throws Exception {
    Category category = new Category("Chores");
    Category secondCategory = new Category("misc");
    categoryDao.add(category);
    categoryDao.add(secondCategory);

    categoryDao.clearAllCategories();
    assertEquals(0, categoryDao.getAll().size());
  }

  @Test
  public void getAllTasksByCategory() throws Exception {
    Category category = new Category("Chores");
    categoryDao.add(category);
    int categoryId = category.getId();

    Task task = new Task("mow the lawn", categoryId);
    Task secondTask = new Task("wash the dishes", categoryId);
    taskDao.add(task);
    taskDao.add(task);
    assertEquals(2, categoryDao.getAllTasksByCategory(categoryId).size());
    assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(task));
    assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(secondTask));
  }
}
