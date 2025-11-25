package com.chisimdi.Banking;

import com.chisimdi.Banking.exceptions.ResourceNotFoundException;
import com.chisimdi.Banking.models.Branch;
import com.chisimdi.Banking.models.User;
import com.chisimdi.Banking.repositories.BranchRepository;
import com.chisimdi.Banking.repositories.UserRepository;
import com.chisimdi.Banking.services.BranchService;
import com.chisimdi.Banking.services.UserService;
import com.chisimdi.Banking.services.events.BranchAssignmentEvent;
import com.chisimdi.Banking.services.events.BranchRegistrationEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {
    @Mock
    BranchRepository branchRepository;
    @Mock
    ApplicationEventPublisher publisher;
    @Mock
    UserRepository userRepository;
    @Mock
    UserService userService;
    @InjectMocks
    BranchService branchService;

    @Test
    void registerUserToBranch(){
        User user=new User();
        user.setName("Chisimdi");;
        Branch branch=new Branch();
        branch.setCustomers(new ArrayList<>());

        when(userRepository.findByRolesAndId("Customer",1)).thenReturn(user);
        when(branchRepository.findById(2)).thenReturn(Optional.of(branch));
        when(userRepository.save(user)).thenReturn(user);
        when(branchRepository.save(branch)).thenReturn(branch);

        branchService.registerUSerToBranch(1,2);

        assertThat(branch).isNotNull();
        assertThat(user.getBranch()).isEqualTo(branch);

        verify(userRepository).findByRolesAndId("Customer",1);
        verify(branchRepository).findById(2);
        verify(branchRepository).save(branch);
        verify(userRepository).save(user);
        verify(publisher).publishEvent(any(BranchRegistrationEvent.class));
    }
    @Test
    void registerUserToBranch_ThrowsResourceNotFoundExceptionForUser(){
        User user=new User();
        user.setName("Chisimdi");;
        Branch branch=new Branch();
        branch.setCustomers(new ArrayList<>());

        when(userRepository.findByRolesAndId("Customer",1)).thenReturn(null);


        assertThatThrownBy(()->branchService.registerUSerToBranch(1,2)).isInstanceOf(ResourceNotFoundException.class);



        verify(userRepository).findByRolesAndId("Customer",1);
        verify(branchRepository,never()).findById(2);
        verify(branchRepository,never()).save(branch);
        verify(userRepository,never()).save(user);
        verify(publisher,never()).publishEvent(any(BranchRegistrationEvent.class));
    }
    @Test
    void registerUserToBranch_ThrowsResourceNotFoundExceptionForBranch(){
        User user=new User();
        user.setName("Chisimdi");;
        Branch branch=new Branch();
        branch.setCustomers(new ArrayList<>());

        when(userRepository.findByRolesAndId("Customer",1)).thenReturn(user);
        when(branchRepository.findById(2)).thenReturn(Optional.empty());


        assertThatThrownBy(()->branchService.registerUSerToBranch(1,2)).isInstanceOf(ResourceNotFoundException.class);



        verify(userRepository).findByRolesAndId("Customer",1);
        verify(branchRepository).findById(2);
        verify(branchRepository,never()).save(branch);
        verify(userRepository,never()).save(user);
        verify(publisher,never()).publishEvent(any(BranchRegistrationEvent.class));
    }
    @Test
    void assignEmployeesToBranchTest(){
        User user=new User();
        user.setName("Chisimdi");;
        Branch branch=new Branch();
        branch.setEmployees(new ArrayList<>());

        when(userRepository.findByRolesAndId("Employee",1)).thenReturn(user);
        when(branchRepository.findById(2)).thenReturn(Optional.of(branch));
        when(branchRepository.save(branch)).thenReturn(branch);
        when(userRepository.save(user)).thenReturn(user);

        branchService.assignEmployeesToBranch(1,2);

        assertThat(branch).isNotNull();
        assertThat(user.getBranch()).isEqualTo(branch);
        assertThat(branch.getEmployees()).contains(user);

        verify(userRepository).findByRolesAndId("Employee",1);
        verify(branchRepository).findById(2);
        verify(branchRepository).save(branch);
        verify(userRepository).save(user);
        verify(publisher).publishEvent(any(BranchAssignmentEvent.class));

    }
    @Test
    void assignEmployeesToBranchTest_ThrowsResourceNotFoundExceptionForBranch() {
        User user = new User();
        user.setName("Chisimdi");
        ;
        Branch branch = new Branch();
        branch.setEmployees(new ArrayList<>());


        when(branchRepository.findById(2)).thenReturn(Optional.empty());

        assertThatThrownBy(()->branchService.assignEmployeesToBranch(1, 2)).isInstanceOf(ResourceNotFoundException.class);



        verify(userRepository,never()).findByRolesAndId("Employee", 1);
        verify(branchRepository).findById(2);
        verify(branchRepository,never()).save(branch);
        verify(userRepository,never()).save(user);
        verify(publisher,never()).publishEvent(any(BranchAssignmentEvent.class));
    }
    @Test
    void assignEmployeesToBranchTest_ThrowsResourceNotFoundExceptionForUsers() {
        User user = new User();
        user.setName("Chisimdi");
        ;
        Branch branch = new Branch();
        branch.setEmployees(new ArrayList<>());

        when(userRepository.findByRolesAndId("Employee", 1)).thenReturn(null);
        when(branchRepository.findById(2)).thenReturn(Optional.of(branch));


     assertThatThrownBy( ()-> branchService.assignEmployeesToBranch(1, 2)).isInstanceOf(ResourceNotFoundException.class);



        verify(userRepository).findByRolesAndId("Employee", 1);
        verify(branchRepository).findById(2);
        verify(branchRepository,never()).save(branch);
        verify(userRepository,never()).save(user);
        verify(publisher,never()).publishEvent(any(BranchAssignmentEvent.class));


    }
}
