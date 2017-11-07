/* 
* Copyright (C) 2003-2015 eXo Platform SAS.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see http://www.gnu.org/licenses/ .
*/

package org.exoplatform.task.dao;

import java.util.Date;
import java.util.List;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.task.util.ListUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.task.domain.TaskFile;
import org.exoplatform.task.domain.Task;
import org.exoplatform.task.AbstractTest;

/**
 * @author <a href="mailto:tuyennt@exoplatform.com">Tuyen Nguyen The</a>.
 */
public class TestTaskFileDAO extends AbstractTest {

  private TaskFileHandler taskFileDAO;
  private TaskHandler taskDAO;

  private final String username = "root";
  private DAOHandler daoHandler;

  @Before
  public void initDAOs() {
    PortalContainer container = PortalContainer.getInstance();
    
    daoHandler = (DAOHandler) container.getComponentInstanceOfType(DAOHandler.class);
    taskDAO = daoHandler.getTaskHandler();
    taskFileDAO = daoHandler.getTaskFileHandler();
    
    Assert.assertNotNull(taskFileDAO);
  }

  @After
  public void cleanData() {
    taskFileDAO.deleteAll();
    taskDAO.deleteAll();
  }

  @Test
  public void testCreateTaskFile() {
    Task task = newDefaultSimpleTask();
    taskDAO.create(task);

    List<Task> tasks = taskDAO.findAll();
    task = tasks.get(0);

    TaskFile taskFile = newDefaultSimpleTaskFile(task);
    taskFileDAO.create(taskFile);

    ListAccess<TaskFile> listTaskFiles = taskFileDAO.findTaskFiles(task.getId());
    TaskFile[] taskFiles = ListUtil.load(listTaskFiles, 0, -1);
    Assert.assertEquals(1, taskFiles.length);
    taskFile = taskFiles[0];
    Assert.assertEquals(username, taskFile.getAuthor());

    //Assert.assertEquals(task.getId(), taskFile.getTask().getId());
  }

  @Test
  public void testUpdateTaskFile() {
    // Create Task
    Task task = newDefaultSimpleTask();
    taskDAO.create(task);

    List<Task> tasks = taskDAO.findAll();
    task = tasks.get(0);

    // Create TaskFile
    TaskFile taskFile = newDefaultSimpleTaskFile(task);
    taskFileDAO.create(taskFile);

    // Load taskFile of task
    ListAccess<TaskFile> listTaskFiles = taskFileDAO.findTaskFiles(task.getId());
    TaskFile[] taskFiles = ListUtil.load(listTaskFiles, 0, -1);
    Assert.assertEquals(1, taskFiles.length);
    taskFile = taskFiles[0];

    // Update taskFile
    long id = taskFile.getId();
    taskFile.setFileName("New taskFile content");
    taskFile.setFileType("");
    taskFile.setFileSize(0L);
    taskFileDAO.update(taskFile);

    taskFile = taskFileDAO.find(id);
    Assert.assertEquals("New taskFile content", taskFile.getFileName());
  }

  @Test
  public void testDeleteTaskFile() {
    // Create Task
    Task task = newDefaultSimpleTask();
    taskDAO.create(task);

    List<Task> tasks = taskDAO.findAll();
    task = tasks.get(0);

    // Create TaskFile
    TaskFile taskFile = newDefaultSimpleTaskFile(task);
    taskFileDAO.create(taskFile);

    // Load taskFile of task
    ListAccess<TaskFile> listTaskFiles = taskFileDAO.findTaskFiles(task.getId());
    TaskFile[] taskFiles = ListUtil.load(listTaskFiles, 0, -1);
    Assert.assertEquals(1, taskFiles.length);
    taskFile = taskFiles[0];

    // Delete taskFile
    long id = taskFile.getId();
    taskFileDAO.delete(taskFile);

    taskFile = taskFileDAO.find(id);
    Assert.assertNull(taskFile);
  }

  @Test
  public void testCountTaskFiles() {
    // Create Task
    Task task = newDefaultSimpleTask();
    taskDAO.create(task);

    List<Task> tasks = taskDAO.findAll();
    task = tasks.get(0);

    // Create TaskFile
    final int number = 10;
    for(int i = 0; i < number; i++) {
      TaskFile taskFile = newDefaultSimpleTaskFile(task);
      taskFile.setFileName("TaskFile number " + i);
      taskFile.setFileType("");
      taskFile.setFileSize(0L);
      taskFileDAO.create(taskFile);
    }

    // Assure have 10 taskFile in task
    ListAccess<TaskFile> listTaskFiles = taskFileDAO.findTaskFiles(task.getId());
    long count = ListUtil.getSize(listTaskFiles);

    Assert.assertEquals(number, count);
  }

  @Test
  public void testLoadTaskFileWithLimit() {
    // Create Task
    Task task = newDefaultSimpleTask();
    taskDAO.create(task);

    List<Task> tasks = taskDAO.findAll();
    task = tasks.get(0);

    // Create TaskFile
    final int number = 10;
    for(int i = 0; i < number; i++) {
      TaskFile taskFile = newDefaultSimpleTaskFile(task);
      taskFile.setFileName("TaskFile number " + i);
      taskFile.setFileType("");
      taskFile.setFileSize(0L);
      taskFileDAO.create(taskFile);
    }

    ListAccess<TaskFile> listTaskFiles = taskFileDAO.findTaskFiles(task.getId());

    TaskFile[] taskFiles = ListUtil.load(listTaskFiles, 0, 5); //taskFileDAO.findTaskFilesOfTask(task, 0, 5);
    Assert.assertEquals(5, taskFiles.length);

    taskFiles = ListUtil.load(listTaskFiles, 2, 5); //taskFileDAO.findTaskFilesOfTask(task, 2, 5);
    Assert.assertEquals(5, taskFiles.length);

    //. If does not pass limit number, start will be ignored
    taskFiles = ListUtil.load(listTaskFiles, 2, -1); //taskFileDAO.findTaskFilesOfTask(task, 2, 0);
    Assert.assertEquals(10, taskFiles.length);

    taskFiles = ListUtil.load(listTaskFiles, 8, 5); //taskFileDAO.findTaskFilesOfTask(task, 8, 5);
    Assert.assertEquals(2, taskFiles.length);
  }

  private Task newDefaultSimpleTask() {
    Task task = new Task();
    task.setTitle("Default task");
    task.setAssignee("root");
    task.setCreatedBy("root");
    task.setCreatedTime(new Date());
    return task;
  }

  private TaskFile newDefaultSimpleTaskFile(Task task) {
    TaskFile taskFile = new TaskFile();
    taskFile.setAuthor(username);
    taskFile.setFileName("Default taskFile");
    taskFile.setFileType("");
    taskFile.setFileSize(0L);
    taskFile.setCreatedTime(new Date());
    taskFile.setTask(task);
    return taskFile;
  }
}
