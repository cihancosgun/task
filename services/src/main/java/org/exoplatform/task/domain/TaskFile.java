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

package org.exoplatform.task.domain;

import org.exoplatform.commons.api.persistence.ExoEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author <a href="mailto:cihancosgun55@hotmail.com">Cihan Coşgun</a>.
 */
@Entity(name = "TaskFile")
@ExoEntity
@Table(name = "TASK_FILES")
@NamedQueries({
    @NamedQuery(name = "TaskFile.countFileOfTask",
        query = "SELECT count(c) FROM TaskFile c WHERE c.task.id = :taskId"),
    @NamedQuery(name = "TaskFile.findFilesOfTask",
        query = "SELECT c FROM TaskFile c WHERE c.task.id = :taskId ORDER BY c.createdTime DESC"),
    @NamedQuery(name = "TaskFile.deleteFileOfTask",
        query = "DELETE FROM TaskFile c WHERE c.task.id = :taskId")
})
public class TaskFile {
  @Id
  @SequenceGenerator(name="SEQ_TASK_FILES_FILE_ID", sequenceName="SEQ_TASK_FILES_FILE_ID")
  @GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ_TASK_FILES_FILE_ID")
  @Column(name = "FILE_ID")
  private long id;

  private String author;

  @Column(name = "FILE_NAME")
  private String fileName;
  
  @Column(name = "FILE_TYPE")
  private String fileType;
  
  @Column(name = "FILE_SIZE")
  private Long fileSize;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TASK_ID")
  private Task task;

  public TaskFile() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  
  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }
  
  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public Task getTask() {
    return task;
  }

  public void setTask(Task task) {
    this.task = task;
  }

  @Override
  public TaskFile clone() {
    TaskFile c = new TaskFile();
    c.setId(getId());
    c.setAuthor(getAuthor());
    c.setFileName(getFileName());
    c.setFileType(getFileType());
    c.setFileSize(getFileSize());
    c.setCreatedTime(getCreatedTime());
    c.setTask(getTask().clone());
    return c;
  }
}
