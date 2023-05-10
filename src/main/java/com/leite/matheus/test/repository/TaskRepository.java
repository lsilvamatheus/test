package com.leite.matheus.test.repository;

import com.leite.matheus.test.domain.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
