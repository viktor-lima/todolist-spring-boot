package br.com.viktor.todolist.repository;

import java.util.UUID;

import br.com.viktor.todolist.model.Task;
import java.util.List;


public interface ITaskRepository extends org.springframework.data.jpa.repository.JpaRepository<Task, UUID>{

    List<Task> findByIdUser(UUID idUser);
    
}
