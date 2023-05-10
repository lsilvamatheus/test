package com.leite.matheus.test.domain;

import com.leite.matheus.test.dto.TaskDto;
import com.leite.matheus.test.enums.TaskStatus;

import javax.persistence.*;

@Entity
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.CREATED;

    public Task(String title) {
        this.title = title;
    }

    private Task() {}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskStatus(TaskStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public TaskDto toDto() {
        return new TaskDto(String.valueOf(id), title, description, status.name());
    }
}