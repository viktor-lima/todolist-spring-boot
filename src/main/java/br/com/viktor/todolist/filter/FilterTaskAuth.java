package br.com.viktor.todolist.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import br.com.viktor.todolist.model.User;
import br.com.viktor.todolist.repository.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (servletPath.equals("/tasks/create")) {
            // Primeiro temos que pegar a autenticação do usuário (user and password)
            String authorizationHeader = request.getHeader("Authorization");
            // identificar o tipo de autenticação (Basic)
            String authEndoded = authorizationHeader.substring("Basic ".length()).trim();
            byte[] authDecode = Base64.getDecoder().decode(authEndoded);
            String[] credentials = new String(authDecode).split(":");
            String userName = credentials[0];
            String password = credentials[1];
            // Validar user
            User user = this.userRepository.findByUserName(userName);
            if (user == null) {
                response.sendError(401);
            } else {
                // Valida senha
                Result passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }

            }
        } else {
            filterChain.doFilter(request, response);
        }

    }

}
