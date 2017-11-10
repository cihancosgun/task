/*
* JBoss, a division of Red Hat
* Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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

package org.exoplatform.task.dao.jpa;

import javax.persistence.TypedQuery;

import org.exoplatform.commons.persistence.impl.GenericDAOJPAImpl;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.task.dao.TaskFileHandler;
import org.exoplatform.task.domain.TaskFile;

/**
 * @author <a href="mailto:tuyennt@exoplatform.com">Tuyen Nguyen The</a>.
 */
public class TaskFileDAOImpl extends CommonJPADAO<TaskFile, Long> implements TaskFileHandler {

  @Override
  public ListAccess<TaskFile> findTaskFiles(long taskId) {
    TypedQuery<TaskFile> query = getEntityManager().createNamedQuery("File.findFilesOfTask", TaskFile.class);
    TypedQuery<Long> count = getEntityManager().createNamedQuery("File.countFileOfTask", Long.class);

    query.setParameter("taskId", taskId);
    count.setParameter("taskId", taskId);

    return new JPAQueryListAccess<TaskFile>(TaskFile.class, count, query);
  }
}