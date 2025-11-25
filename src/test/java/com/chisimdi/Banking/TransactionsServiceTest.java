package com.chisimdi.Banking;

import com.chisimdi.Banking.exceptions.InsufficientFundsException;
import com.chisimdi.Banking.exceptions.ResourceNotFoundException;
import com.chisimdi.Banking.models.Account;
import com.chisimdi.Banking.models.Transactions;
import com.chisimdi.Banking.models.TransactionsDTO;
import com.chisimdi.Banking.models.User;
import com.chisimdi.Banking.repositories.AccountRepository;
import com.chisimdi.Banking.repositories.TransactionRepository;
import com.chisimdi.Banking.services.TransactionService;
import com.chisimdi.Banking.services.events.DepositEvent;
import com.chisimdi.Banking.services.events.WithdrawalEvent;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionsServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    ApplicationEventPublisher publisher;
    @InjectMocks
    TransactionService transactionService;

    @Test
    void depositTest(){
        User user=new User();
        user.setName("Chisimdi");
        user.setId(2);
        Account account=new Account();
        account.setBalance(500);
        account.setUser(user);


        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        TransactionsDTO transactions=transactionService.Deposit(2,1,200);
        assertThat(transactions.getAmount()).isEqualTo(200);

        assertThat(transactions.getType()).isEqualTo("Deposit");
assertThat(account.getBalance()).isEqualTo(700);
        verify(accountRepository).findByIdAndUserId(1,2);
        verify(accountRepository).save(account);
        verify(publisher).publishEvent(any(DepositEvent.class));

    }
    @Test
    void depositTest_ThrowResourceNotFoundException(){
        User user=new User();
        user.setName("Chisimdi");
        user.setId(2);
        Account account=new Account();
        account.setBalance(500);

        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(null);
        assertThatThrownBy(()->transactionService.Deposit(2,1,500)).isInstanceOf(ResourceNotFoundException.class);

        verify(accountRepository,never()).save(account);
        verify(publisher,never()).publishEvent(any(DepositEvent.class));
    }

    @Test
    void withdrawalTest(){
        Account account=new Account();
        User user=new User();
        user.setName("Chisimdi");
        account.setUser(user);
        account.setBalance(2000);

        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
       TransactionsDTO transactionsDTO= transactionService.withdraw(2,1,200);

        verify(accountRepository).findByIdAndUserId(1,2);
        verify(accountRepository).save(account);
        verify(publisher).publishEvent(any(WithdrawalEvent.class));
        assertThat(account.getBalance()).isEqualTo(1800);
        assertThat(transactionsDTO.getType()).isEqualTo("Withdrawal");
        assertThat(transactionsDTO.getAmount()).isEqualTo(200);

    }
@Test
    void withdrawalTest_ThrowsResourceNotFoundException(){
    Account account=new Account();
    User user=new User();
    user.setName("Chisimdi");
    account.setUser(user);
    account.setBalance(2000);
    when(accountRepository.findByIdAndUserId(1,2)).thenReturn(null);

    assertThatThrownBy(()->transactionService.withdraw(2,1,200)).isInstanceOf(ResourceNotFoundException.class);
    verify(accountRepository,never()).save(account);
    verify(publisher,never()).publishEvent(any(WithdrawalEvent.class));
}
@Test
    void withdrawalTest_ThrowsInsufficientFundsException(){
    Account account=new Account();
    User user=new User();
    user.setName("Chisimdi");
    account.setUser(user);
    account.setBalance(2000);

    when(accountRepository.findByIdAndUserId(1,2)).thenReturn(account);
    assertThatThrownBy(()->transactionService.withdraw(2,1,20000)).isInstanceOf(InsufficientFundsException.class);
    verify(accountRepository,never()).save(account);
    verify(publisher,never()).publishEvent(any(WithdrawalEvent.class));
}
}
