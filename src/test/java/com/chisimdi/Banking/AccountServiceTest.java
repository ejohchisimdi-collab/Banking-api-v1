package com.chisimdi.Banking;

import com.chisimdi.Banking.exceptions.ExistsException;
import com.chisimdi.Banking.exceptions.ResourceNotFoundException;
import com.chisimdi.Banking.exceptions.WrongRoleException;
import com.chisimdi.Banking.models.Account;
import com.chisimdi.Banking.models.AccountDTO;
import com.chisimdi.Banking.models.User;
import com.chisimdi.Banking.repositories.AccountRepository;
import com.chisimdi.Banking.repositories.UserRepository;
import com.chisimdi.Banking.services.AccountService;
import com.chisimdi.Banking.services.events.AccountCreationEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;
    @Mock
    UserRepository userRepository;
@Mock
    ApplicationEventPublisher publisher;

        @InjectMocks
    AccountService accountService;

    @Test
    void AddAccountTests(){
        String accountNumber="123-456-789";
        Double balance=2000.00;
        String type="Savings";
        int userId=1;

        User user=new User();
        user.setId(1);
        user.setName("Chisimdi");
        user.setRoles("Customer");
        user.setAccounts(new ArrayList<>());


        Account savedAccount=new Account();
        savedAccount.setAccountNumber(accountNumber);
        savedAccount.setBalance(balance);
        savedAccount.setType(type);

        User savedUser= new User();
        user.getAccounts().add(savedAccount);




        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(accountRepository.existsByAccountNumber(accountNumber)).thenReturn(false);


        AccountDTO accountDTO=accountService.addAccount(accountNumber,balance,type,userId);
        assertThat(accountDTO.getType()).isEqualTo(savedAccount.getType());
        assertThat(accountDTO.getBalance()).isEqualTo(savedAccount.getBalance());
        assertThat(accountDTO.getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());

        verify(userRepository).findById(userId);
        verify(accountRepository).existsByAccountNumber(accountNumber);
        verify(publisher).publishEvent(any(AccountCreationEvent.class));




    }
    @Test
    void addAccount_ThrowsResourceNotFoundException(){
        int userId= 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(()->accountService.addAccount("123-456",200.00,"Checking",userId)).isInstanceOf(ResourceNotFoundException.class);

        verify(accountRepository,never()).existsByAccountNumber("123-456");
        verify(publisher,never()).publishEvent(any(AccountCreationEvent.class));
        verify(userRepository,never()).save(any(User.class));

    }
    @Test
    void addAccount_ThrowsWrongRoleException(){
       User user=new User();
       user.setId(1);
       user.setRoles("Employee");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        assertThatThrownBy(()->accountService.addAccount("123-456",1000.00,"Checking",1)).isInstanceOf(WrongRoleException.class);
        verify(accountRepository,never()).existsByAccountNumber("123-456");
        verify(publisher,never()).publishEvent(any(AccountCreationEvent.class));
        verify(userRepository,never()).save(any(User.class));
    }
    @Test
    void addAccount_ThrowsExistsException(){
        User user=new User();
        user.setId(1);
        user.setRoles("Customer");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(accountRepository.existsByAccountNumber("123-456")).thenReturn(true);

        assertThatThrownBy(()->accountService.addAccount("123-456",1000.00,"Checking",1)).isInstanceOf(ExistsException.class);

        verify(publisher,never()).publishEvent(any(AccountCreationEvent.class));
        verify(userRepository,never()).save(any(User.class));
    }
}
