/*
 * Copyright (C) 2015 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.task.model;

import java.util.Date;

import org.exoplatform.services.security.Identity;
import org.exoplatform.task.domain.TaskFile;
import org.exoplatform.task.domain.Task;
import org.exoplatform.task.util.TaskUtil;

/**
 * @author <a href="mailto:tuyennt@exoplatform.com">Tuyen Nguyen The</a>.
 */
public class TaskFileModel {
  private final TaskFile taskFile;
  private final User author;

  public TaskFileModel(TaskFile taskFile, User author) {
    this.taskFile = taskFile;
    this.author = author;
  }

  public User getAuthor() {
    return this.author;
  }

  public Date getCreatedTime() {
    return taskFile.getCreatedTime();
  }

  public String getFileName() {
    return taskFile.getFileName();
  }

  public long getId() {
    return taskFile.getId();
  }

  public Task getTask() {
    return taskFile.getTask();
  }

  public boolean canEdit(Identity identity) {
    return TaskUtil.canDeleteTaskFile(identity, taskFile);
  }
}
