package com.sooraj.aitaskapi.repository;

import com.sooraj.aitaskapi.entity.Task;
import com.sooraj.aitaskapi.entity.TaskPriority;
import com.sooraj.aitaskapi.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {


}

