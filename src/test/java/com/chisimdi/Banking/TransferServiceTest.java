package com.chisimdi.Banking;

import com.chisimdi.Banking.exceptions.ExistsException;
import com.chisimdi.Banking.exceptions.InsufficientFundsException;
import com.chisimdi.Banking.exceptions.ResourceNotFoundException;
import com.chisimdi.Banking.models.Account;
import com.chisimdi.Banking.models.Transfers;
import com.chisimdi.Banking.models.TransfersDTO;
import com.chisimdi.Banking.models.User;
import com.chisimdi.Banking.repositories.AccountRepository;
import com.chisimdi.Banking.repositories.ScheduledTransferRepository;
import com.chisimdi.Banking.repositories.TransfersRepository;
import com.chisimdi.Banking.services.TransferService;
import com.chisimdi.Banking.services.events.ReverseTransferEvent;
import com.chisimdi.Banking.services.events.TransferEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    TransfersRepository transfersRepository;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;
    @Mock
    ScheduledTransferRepository scheduledTransferRepository;
    @InjectMocks
    TransferService transferService;

    @Test
    void TransferTest(){
        Account sendersAccount=new Account();
        sendersAccount.setBalance(2000);
        User sendingUser=new User();
        sendingUser.setName("chisimdi");
        sendersAccount.setUser(sendingUser);

        Account receivingAccount=new Account();
        receivingAccount.setBalance(2000);
        User receivingUser=new User();
        receivingUser.setName("David");
        receivingAccount.setUser(receivingUser);

        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(sendersAccount);
        when(accountRepository.findById(4)).thenReturn(Optional.of(receivingAccount));

        when(accountRepository.save(sendersAccount)).thenReturn(sendersAccount);
        when(accountRepository.save(receivingAccount)).thenReturn(receivingAccount);

        TransfersDTO transfersDTO= transferService.transfer(2,1,4,200.00);

        assertThat(transfersDTO.getAmount()).isEqualTo(200);
        assertThat(transfersDTO.getTransferringAccountId()).isEqualTo(sendersAccount.getId());
        assertThat(transfersDTO.getTransferringAccountId()).isEqualTo(receivingAccount.getId());
        assertThat(sendersAccount.getBalance()).isEqualTo(1800);
        assertThat(receivingAccount.getBalance()).isEqualTo(2200);

        verify(accountRepository).findByIdAndUserId(1,2);
        verify(accountRepository).findById(4);
        verify(accountRepository).save(sendersAccount);
        verify(accountRepository).save(receivingAccount);
        verify(applicationEventPublisher).publishEvent(any(TransferEvent.class));
    }
    @Test
    void transferTest_ThrowsResourceNotFoundException(){
        Account sendersAccount=new Account();
        sendersAccount.setBalance(2000);
        User sendingUser=new User();
        sendingUser.setName("chisimdi");
        sendersAccount.setUser(sendingUser);

        Account receivingAccount=new Account();
        receivingAccount.setBalance(2000);
        User receivingUser=new User();
        receivingUser.setName("David");
        receivingAccount.setUser(receivingUser);

        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(null);

        assertThatThrownBy(()->transferService.transfer(2,1,4,200.00)).isInstanceOf(ResourceNotFoundException.class);


        verify(accountRepository,never()).save(sendersAccount);
        verify(accountRepository,never()).save(receivingAccount);
        verify(applicationEventPublisher,never()).publishEvent(any(TransferEvent.class));
    }
    @Test
    void transferTest_ResourceNotFoundExceptionForReceiversAccount(){
        Account sendersAccount=new Account();
        sendersAccount.setBalance(2000);
        User sendingUser=new User();
        sendingUser.setName("chisimdi");
        sendersAccount.setUser(sendingUser);

        Account receivingAccount=new Account();
        receivingAccount.setBalance(2000);
        User receivingUser=new User();
        receivingUser.setName("David");
        receivingAccount.setUser(receivingUser);

        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(sendersAccount);
        when(accountRepository.findById(4)).thenReturn(Optional.empty());

        assertThatThrownBy(()->transferService.transfer(2,1,4,200.00)).isInstanceOf(ResourceNotFoundException.class);
        verify(accountRepository,never()).save(sendersAccount);
        verify(accountRepository,never()).save(receivingAccount);
        verify(applicationEventPublisher,never()).publishEvent(any(TransferEvent.class));


    }
    @Test
    void transferTest_InsufficientFundsException(){
        Account sendersAccount=new Account();
        sendersAccount.setBalance(2000);
        User sendingUser=new User();
        sendingUser.setName("chisimdi");
        sendersAccount.setUser(sendingUser);

        Account receivingAccount=new Account();
        receivingAccount.setBalance(2000);
        User receivingUser=new User();
        receivingUser.setName("David");
        receivingAccount.setUser(receivingUser);

        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(sendersAccount);
        when(accountRepository.findById(4)).thenReturn(Optional.of(receivingAccount));



        assertThatThrownBy(()-> transferService.transfer(2,1,4,20000.00)).isInstanceOf(InsufficientFundsException.class);
        verify(accountRepository,never()).save(sendersAccount);
        verify(accountRepository,never()).save(receivingAccount);
        verify(applicationEventPublisher,never()).publishEvent(any(TransferEvent.class));
    }
    @Test
    void reverseTransferTest(){
        Account sendersAccount=new Account();
        sendersAccount.setBalance(2000);
        User sendingUser=new User();
        sendingUser.setName("chisimdi");
        sendersAccount.setUser(sendingUser);
        sendersAccount.setId(1);

        Account receivingAccount=new Account();
        receivingAccount.setBalance(2000);
        User receivingUser=new User();
        receivingUser.setName("David");
        receivingAccount.setUser(receivingUser);
        receivingAccount.setId(2);

        Transfers transfers=new Transfers();
        transfers.setTransferringAccount(sendersAccount);
        transfers.setReceivingAccount(receivingAccount);
        transfers.setAmount(200);
        transfers.setReversed("False");
        when(transfersRepository.findById(1)).thenReturn(Optional.of(transfers));

when(accountRepository.findById(1)).thenReturn(Optional.of(sendersAccount));
when(accountRepository.findById(2)).thenReturn(Optional.of(receivingAccount));

        when(transfersRepository.save(transfers)).thenReturn(transfers);
        when(accountRepository.save(sendersAccount)).thenReturn(sendersAccount);
        when(accountRepository.save(receivingAccount)).thenReturn(receivingAccount);
        transferService.reverseTransfer(1);

        assertThat(receivingAccount.getBalance()).isEqualTo(1800);
        assertThat(sendersAccount.getBalance()).isEqualTo(2200);
        assertThat(transfers.getReversed()).isEqualTo("True");

        verify(transfersRepository).findById(1);
        verify(transfersRepository).save(transfers);
        verify(accountRepository).save(receivingAccount);
        verify(accountRepository).save(sendersAccount);
        verify(applicationEventPublisher).publishEvent(any(ReverseTransferEvent.class));
    }
    @Test
void reverseTransferTest_ThrowsResourceNOtFoundException(){
    Account sendersAccount=new Account();
    sendersAccount.setBalance(2000);
    User sendingUser=new User();
    sendingUser.setName("chisimdi");
    sendersAccount.setUser(sendingUser);
    sendersAccount.setId(1);

    Account receivingAccount=new Account();
    receivingAccount.setBalance(2000);
    User receivingUser=new User();
    receivingUser.setName("David");
    receivingAccount.setUser(receivingUser);
    receivingAccount.setId(2);

    Transfers transfers=new Transfers();
    transfers.setTransferringAccount(sendersAccount);
    transfers.setReceivingAccount(receivingAccount);
    transfers.setAmount(200);
    transfers.setReversed("False");
    when(transfersRepository.findById(1)).thenReturn(Optional.empty());

    assertThatThrownBy(()->transferService.reverseTransfer(1)).isInstanceOf(ResourceNotFoundException.class);
    verify(transfersRepository,never()).save(transfers);
    verify(accountRepository,never()).save(receivingAccount);
    verify(accountRepository,never()).save(sendersAccount);
    verify(applicationEventPublisher,never()).publishEvent(any(ReverseTransferEvent.class));
}

@Test
    void reverseTransferTest_ThrowsExistsException(){
    Account sendersAccount=new Account();
    sendersAccount.setBalance(2000);
    User sendingUser=new User();
    sendingUser.setName("chisimdi");
    sendersAccount.setUser(sendingUser);
    sendersAccount.setId(1);

    Account receivingAccount=new Account();
    receivingAccount.setBalance(2000);
    User receivingUser=new User();
    receivingUser.setName("David");
    receivingAccount.setUser(receivingUser);
    receivingAccount.setId(2);

    Transfers transfers=new Transfers();
    transfers.setTransferringAccount(sendersAccount);
    transfers.setReceivingAccount(receivingAccount);
    transfers.setAmount(200);
    transfers.setReversed("True");
    when(transfersRepository.findById(1)).thenReturn(Optional.of(transfers));

    assertThatThrownBy(()->transferService.reverseTransfer(1)).isInstanceOf(ExistsException.class);
    verify(transfersRepository,never()).save(transfers);
    verify(accountRepository,never()).save(receivingAccount);
    verify(accountRepository,never()).save(sendersAccount);
    verify(applicationEventPublisher,never()).publishEvent(any(ReverseTransferEvent.class));
}
}
