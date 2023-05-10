package com.leite.matheus.test.controller;

import com.leite.matheus.test.domain.Task;
import com.leite.matheus.test.dto.TaskDto;
import com.leite.matheus.test.enums.TaskStatus;
import com.leite.matheus.test.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping
    public ResponseEntity<Object> createOne(@RequestBody TaskDto taskDto) {
        Task task = new Task(taskDto.getDescription());
        task.setTitle(taskDto.getTitle());
        task.setTaskStatus(TaskStatus.valueOf(taskDto.getStatus()));
        task.setDescription(taskDto.getDescription());

        task = taskRepository.save(task);
        return ResponseEntity.ok(task.toDto().getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> readOne(@PathVariable("id") Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            return ResponseEntity.ok(task.toDto());
        } else {
            return ResponseEntity.ok("Task not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteoOne(@PathVariable("id") Long id) {
        if (taskRepository.findById(id).isPresent()) {
            taskRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping
    public ResponseEntity<Object> updateOne(@PathVariable("id") Long id, @RequestBody TaskDto taskDto) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (Arrays.stream(TaskStatus.values()).noneMatch(status -> status.name().equals(taskDto.getStatus()))) {
            return ResponseEntity.ok("Available statuses are: CREATED, APPROVED, REJECTED, BLOCKED, DONE.");
        } else if (taskOptional.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Task taskUpdate = taskOptional.get();
        taskUpdate.setTitle(taskDto.getDescription());
        taskUpdate.setDescription(taskDto.getDescription());
        taskUpdate.setTaskStatus(TaskStatus.valueOf(taskDto.getStatus()));
        return ResponseEntity.ok(taskRepository.save(taskUpdate).toDto());
    }

    @GetMapping("/describe/{id}")
    public ResponseEntity<Object> decribeOne(@PathVariable("id") Long id) throws IllegalAccessException {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(taskOptional.isEmpty()) {
            return ResponseEntity.ok("Task with id = " + id + " does not exist");
        }
        return ResponseEntity.ok(describedTask(taskOptional.get()));
    }

    @GetMapping("/describe")
    public ResponseEntity<Object> describeAll() {
        List<String> result = new ArrayList<>();
        Iterable<Task> tasks = taskRepository.findAll();
        tasks.forEach(task -> {
            result.add(describedTask(task));
        });
        return ResponseEntity.ok(result);
    }

    private String describedTask(Task task) {
        TaskDto taskDto = task.toDto();
        return "Task [NAME] : " + taskDto.getId() + "description is" + taskDto.getDescription();
    }
}
