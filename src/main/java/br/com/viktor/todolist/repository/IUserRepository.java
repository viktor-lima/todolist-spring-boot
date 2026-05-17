package br.com.viktor.todolist.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.viktor.todolist.model.User;

public interface IUserRepository extends JpaRepository<User, UUID>{

    User findByUserName(String userName);
    
}
