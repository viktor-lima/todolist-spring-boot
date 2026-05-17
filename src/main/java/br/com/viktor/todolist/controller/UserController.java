package br.com.viktor.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.viktor.todolist.model.User;
import br.com.viktor.todolist.repository.IUserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    
    @PostMapping("/create")
    public ResponseEntity create(@RequestBody User user){
        User userResponse = this.userRepository.findByUserName(user.getUserName());
        if(userResponse != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        var passwordHashered =  BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(passwordHashered);
        User userCreated =  this.userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
