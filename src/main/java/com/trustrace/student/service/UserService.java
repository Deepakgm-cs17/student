package com.trustrace.student.service;

import com.trustrace.student.dto.UserPatch;
import com.trustrace.student.dto.UserRequest;
import com.trustrace.student.model.UserModel;
import com.trustrace.student.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public void saveUser(UserModel userDetails){
        userRepository.save(userDetails);
    }

    public Page<UserModel> findByName(Optional<String> username, Optional<Integer> noOfPage) {

        return userRepository.findByName(username.orElse("_"), PageRequest.of(noOfPage.orElse(0), 5));
    }

    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public String updateStudent(UserRequest userRequest, long id) {

        Optional<UserModel> userRequestOptional = userRepository.findById(id);

        if (!userRequestOptional.isPresent()) {
            return "User Id not found";
        }
        else {
            UserModel user = userRequestOptional.get();
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            userRepository.save(user);
            return "User Id updated";
        }
    }

    public String deleteByID(Long id) {
        userRepository.deleteById(id);
        return "User Id Deleted";
    }

    public void partialReplaceUpdate(UserPatch userPatch, Long id) {
        if(userPatch.getAction().equals("replace") || userPatch.getAction().equals("add")){
            if(userPatch.getFieldName().equals("username")) {
                userRepository.findUserReplace(userPatch.getValue(), id);
            }
            else if(userPatch.getFieldName().equals("email")) {
                userRepository.findEmailReplace(userPatch.getValue(), id);
            }
        }
        else if(userPatch.getAction().equals("delete")){
            if(userPatch.getFieldName().equals("username")) {
                userRepository.deleteUsername(id);
            }
            else if(userPatch.getFieldName().equals("email")) {
                userRepository.deleteEmail(id);
            }
        }
    }
}
