package com.chisimdi.Banking.services;

import com.chisimdi.Banking.exceptions.ExistsException;
import com.chisimdi.Banking.exceptions.InvalidCredentialsException;
import com.chisimdi.Banking.exceptions.ResourceNotFoundException;
import com.chisimdi.Banking.models.LoginResponse;
import com.chisimdi.Banking.models.User;
import com.chisimdi.Banking.models.UserDTO;
import com.chisimdi.Banking.repositories.UserRepository;
import com.chisimdi.Banking.services.events.UserCreationEvent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;
   private BCryptPasswordEncoder bCryptPasswordEncoder;
   private ApplicationEventPublisher publisher;
   private JwtsUtilService jwtsUtilService;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ApplicationEventPublisher publisher,JwtsUtilService jwtsUtilService){
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.userRepository=userRepository;
        this.publisher=publisher;
        this.jwtsUtilService=jwtsUtilService;

    }

    public UserDTO toUserDTO(User user){
        log.info("Searching for user with Id {}",user.getId());
        UserDTO userDTO=new UserDTO();
        if(user.getName()!=null){
            userDTO.setName(user.getName());
        }
        if(user.getContactInfo()!=null){
            userDTO.setContactInfo(user.getContactInfo());
        }
        userDTO.setId(user.getId());
        if(user.getRoles()!=null){
            userDTO.setRole(user.getRoles());
        }
        if (user.getBranch()!=null) {
            userDTO.setBranchId(user.getBranch().getId());
        }
        return userDTO;
    }
    public List<UserDTO> findAllUsers(int pageNumber,int size){
        log.info("Searching for all users");
        Page<User> users= userRepository.findAll(PageRequest.of(pageNumber, size));

        List<UserDTO>userDTOS=new ArrayList<>();
        for(User u:users){
            userDTOS.add(toUserDTO(u));
        }
        return userDTOS;
    }
    public List<UserDTO>findALlCustomers(int pageNumber,int size){
        log.info("Searching for all customers");
        List<UserDTO>userDTOS=new ArrayList<>();
        Page<User>users=userRepository.findByRoles("Customer",PageRequest.of(pageNumber,size));
        for(User u:users){
        userDTOS.add(toUserDTO(u));
        }
        return userDTOS;
    }
    public List<UserDTO>findAllEmployees(int pageNumber, int size){
        log.info("Searching for all employees");
        List<UserDTO>userDTOS=new ArrayList<>();
        Page<User>users=userRepository.findByRoles("Employee",PageRequest.of(pageNumber,size));
        for(User u:users){
            userDTOS.add(toUserDTO(u));
        }
        return userDTOS;
    }
@Transactional
    public UserDTO RegisterUsers(User user){
        log.info("Registering users");
        if (userRepository.existsByUserName(user.getUserName())){
            throw new ExistsException("User with user name "+user.getUserName()+" already exists");
        }
        log.info("Hashing user password");
        String password=bCryptPasswordEncoder.encode(user.getPassword());
user.setPassword(password);
log.info("User created successfully, publishing new user Created event");
        publisher.publishEvent( new UserCreationEvent(user.getContactInfo(),user.getName()) );
        return toUserDTO(userRepository.save(user));

    }
    public LoginResponse logIn(String userName, String password){
        User user= userRepository.findByUserName(userName);
        if(user==null){
            throw new ResourceNotFoundException("User with user name "+userName+" not found");
        }
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new InvalidCredentialsException("Password for user with user name "+userName+" is incorrect");
        }
        String token=jwtsUtilService.generateToken(user.getId(),userName,user.getRoles());
        return new LoginResponse(jwtsUtilService.extractUserName(token), jwtsUtilService.extractRoles(token), jwtsUtilService.extractUserId(token),token );

    }
    public UserDTO findUserBYId(int id){
        return toUserDTO(userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("user with id "+id+" not found")));
    }



    }


