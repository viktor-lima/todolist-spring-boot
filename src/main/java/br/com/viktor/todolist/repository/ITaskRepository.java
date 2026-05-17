package br.com.viktor.todolist.repository;

import java.util.UUID;

import br.com.viktor.todolist.model.Task;

public interface ITaskRepository extends org.springframework.data.jpa.repository.JpaRepository<Task, UUID>{
    
}
