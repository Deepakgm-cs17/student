package com.trustrace.student.controller;

import com.trustrace.student.aop.AuthorizationException;
import com.trustrace.student.aop.InvalidException;
import com.trustrace.student.dto.UserPatch;
import com.trustrace.student.dto.UserRequest;
import com.trustrace.student.model.UserModel;
import com.trustrace.student.repository.UserRepository;
import com.trustrace.student.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.status;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity<String> signup(@RequestHeader(value = "Authorization") String authorizationHeader, @RequestBody UserModel userDetails) throws InvalidException, AuthorizationException {
        if (authorizationHeader.equals("123")) {
            if(userDetails.getUsername() == null || userDetails.getEmail() == null) {
                throw new InvalidException("Input is Missing");
            }else{
                userService.saveUser(userDetails);
                return new ResponseEntity<>("Data added.", HttpStatus.OK);
            }
        } else {
            throw new AuthorizationException("Invalid Authorization.");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserModel>> findAll(@RequestHeader(value = "Authorization") String authorizationHeader,@RequestParam Optional<String> username, @RequestParam Optional<Integer> page)throws AuthorizationException {

        if (authorizationHeader.equals("123")) {
            try {
                return status(HttpStatus.OK).body(userService.findByName(username, page));
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
             throw new AuthorizationException("Invalid Authorization.");
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<UserModel>> getById(@RequestHeader(value = "Authorization") String authorizationHeader,@PathVariable Long id) throws AuthorizationException {
        if (authorizationHeader.equals("123")) {
            try {
                return status(HttpStatus.OK).body(userService.getUserById(id));
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new AuthorizationException("Invalid Authorization.");
        }
    }
    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateById(@RequestHeader(value = "Authorization") String authorizationHeader,@RequestBody UserRequest userRequest, @PathVariable Long id)throws AuthorizationException {
        if (authorizationHeader.equals("123")) {
            try {
                return status(HttpStatus.OK).body(userService.updateStudent(userRequest, id));
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
             throw new AuthorizationException("Invalid Authorization.");
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteTutorial(@RequestHeader(value = "Authorization") String authorizationHeader,@PathVariable Long id) throws AuthorizationException{
        if (authorizationHeader.equals("123")) {
            try {
                return status(HttpStatus.OK).body(userService.deleteByID(id));
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new AuthorizationException("Invalid Authorization.");
        }
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<String> partialUpdate(@RequestBody ArrayList<UserPatch> userPatch, @PathVariable Long id) {
        try {
            for (int i = 0; i < userPatch.size(); i++) {
                userService.partialReplaceUpdate(userPatch.get(i), id);
            }
            return status(HttpStatus.OK).body("Patch updated");
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}