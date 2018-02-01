package dao;

import models.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Sql2oTaskDaoTest {

  private Sql2oTaskDao taskDao; //ignore me for now. We'll create this soon.
  private Connection conn; //must be sql2o class conn

  @Before
  public void setUp() throws Exception {
    String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
    Sql2o sql2o = new Sql2o(connectionString, "", "");
    taskDao = new Sql2oTaskDao(sql2o); //ignore me for now

    //keep connection open through entire test so it does not get erased.
    conn = sql2o.open();
  }

  @After
  public void tearDown() throws Exception {
    conn.close();
  }

  @Test
  public void addingCourseSetsId() throws Exception {
    Task task = new Task("mow the lawn");
    int originalTaskId = task.getId();
    taskDao.add(task);
    assertNotEquals(originalTaskId, task.getId());
  }

  @Test
  public void existingTasksCanBeFoundById() throws Exception {
    Task task = new Task("mow the lawn");
    taskDao.add(task);
    Task foundTask = taskDao.findById(task.getId());
    assertEquals(task, foundTask);
  }

  @Test
  public void getAllTasks() throws Exception {
    Task task = new Task("mow the lawn");
    Task secondTask = new Task("wash the dishes");
    taskDao.add(task);
    taskDao.add(secondTask);
    assertEquals(2, taskDao.getAll().size());
  }

  @Test
  public void noTasksFound() throws Exception {
    assertEquals(0, taskDao.getAll().size());
  }
}