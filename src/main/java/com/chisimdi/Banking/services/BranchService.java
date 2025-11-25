package com.chisimdi.Banking.services;

import com.chisimdi.Banking.exceptions.ResourceNotFoundException;
import com.chisimdi.Banking.models.Branch;
import com.chisimdi.Banking.models.BranchDTO;
import com.chisimdi.Banking.models.User;
import com.chisimdi.Banking.models.UserDTO;
import com.chisimdi.Banking.repositories.BranchRepository;
import com.chisimdi.Banking.repositories.UserRepository;
import com.chisimdi.Banking.services.events.BranchAssignmentEvent;
import com.chisimdi.Banking.services.events.BranchRegistrationEvent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class BranchService {
    private static final Logger log = LoggerFactory.getLogger(BranchService.class);
    private ApplicationEventPublisher publisher;
    private BranchRepository branchRepository;
    private UserRepository userRepository;
    private UserService userService;

    public BranchService(ApplicationEventPublisher publisher,BranchRepository branchRepository, UserRepository userRepository,UserService userService){
        this.branchRepository=branchRepository;
        this.publisher=publisher;
        this.userRepository=userRepository;
        this.userService=userService;
    }

    public BranchDTO toBranchDTO(Branch branch){

        BranchDTO branchDTO=new BranchDTO();
        if(branch.getLocation()!=null){
            branchDTO.setLocation(branch.getLocation());
        }
        if(branch.getContactInfo()!=null){
            branchDTO.setContactInfo(branch.getContactInfo());
        }
        if(branch.getHoursOfOperation()!=null){
            branchDTO.setHoursOfOperation(branch.getHoursOfOperation());
        }
        branchDTO.setId(branch.getId());

                return branchDTO;
    }
    @Transactional
    public BranchDTO createBranch(Branch branch){
        log.info("Creating new Branch");
    return  toBranchDTO(branchRepository.save(branch));
    }

    @Transactional
    public UserDTO assignEmployeesToBranch(int userId, int branchId){
        log.info("Assigning employee with Id "+userId+" to branch with Id "+branchId);
        Branch branch=branchRepository.findById(branchId).orElse(null);
        if(branch==null){
            throw new ResourceNotFoundException("Branch with ID "+branchId+" not found");
        }
        User user=userRepository.findByRolesAndId("Employee", userId);
        if(user==null){
            throw new ResourceNotFoundException("Employee with Id "+ userId +" not found");
        }
        branch.getEmployees().add(user);
        branchRepository.save(branch);
        user.setBranch(branch);
        userRepository.save(user);
        log.info("Employee assigned successfully publishing new Branch Assignment Event ");
        publisher.publishEvent(new BranchAssignmentEvent(branchId, user.getContactInfo(), user.getName()));
        return userService.toUserDTO(user);

    }
    @Transactional
    public UserDTO registerUSerToBranch(int userId,int branchID){
        log.info("Registering user with Id"+userId+" to branch with Id" + branchID);
        User user=userRepository.findByRolesAndId("Customer",userId);
        if(user==null){
            throw new ResourceNotFoundException("customer with Id "+userId+" not found");
        }
        Branch branch=branchRepository.findById(branchID).orElseThrow(()->new ResourceNotFoundException("Branch with Id "+branchID+" not found"));
        branch.getCustomers().add(user);
        user.setBranch(branch);
        branchRepository.save(branch);
        userRepository.save(user);
        log.info("User registered successfully,publishing new Branch Registration Event ");
        publisher.publishEvent(new BranchRegistrationEvent(branchID, user.getContactInfo(), user.getName()));
        return userService.toUserDTO(user);
    }
    public List<BranchDTO> viewAllBranches(int size, int pageNumber){
        log.info("Searching for all branches");
        Page<Branch> branches=branchRepository.findAll(PageRequest.of(pageNumber,size));
        List<BranchDTO>branchDTOS=new ArrayList<>();
        for(Branch b:branches){
            branchDTOS.add(toBranchDTO(b));
        }
        return branchDTOS;
    }


}
