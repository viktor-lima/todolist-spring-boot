package br.com.viktor.todolist.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.viktor.todolist.model.Task;
import br.com.viktor.todolist.repository.ITaskRepository;
import br.com.viktor.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody Task task, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        task.setIdUser((UUID) idUser);

        LocalDateTime currentDate = LocalDateTime.now();
        if (currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de inicio/Final deve ser maior que a data atual");
        }

        if (task.getStartAt().isAfter(task.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A Data de inicio deve ser anterior a data final");
        }

        Task taskResponse = this.taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(taskResponse);

    }

    @GetMapping("/list")
    public List<Task> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        List<Task> tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity Update(@RequestBody Task task, HttpServletRequest request, @PathVariable UUID id) {
        System.out.println("cheguei começo do controler");
        var taskExist = this.taskRepository.findById((UUID) id).orElse(null);
        if (taskExist == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tarefa Não encontrada");
        }
        System.out.println("passou diferente de null");

        var idUser = request.getAttribute("idUser");
        System.out.println(idUser);
        System.out.println(task.getIdUser());

        if (!taskExist.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuário não tem permissão para alterar esta tarefa");
        }
        System.out.println("usuario tem parmissão");
        Utils.copyNonNullProperties(task, taskExist);
        var taskUpdated = this.taskRepository.save(taskExist);
        return ResponseEntity.ok().body(taskUpdated);
    }
}
