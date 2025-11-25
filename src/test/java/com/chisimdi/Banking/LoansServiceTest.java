package com.chisimdi.Banking;

import com.chisimdi.Banking.exceptions.ExistsException;
import com.chisimdi.Banking.exceptions.InsufficientFundsException;
import com.chisimdi.Banking.exceptions.ResourceNotFoundException;
import com.chisimdi.Banking.models.*;
import com.chisimdi.Banking.repositories.AccountRepository;
import com.chisimdi.Banking.repositories.LoansRepository;
import com.chisimdi.Banking.repositories.LoansScheduleRepository;
import com.chisimdi.Banking.services.LoansService;
import com.chisimdi.Banking.services.events.LoanPaymentEvent;
import com.chisimdi.Banking.services.events.LoansCreationEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoansServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    LoansRepository loansRepository;
    @Mock
    LoansScheduleRepository loansScheduleRepository;
    @Mock
    ApplicationEventPublisher publisher;
    @InjectMocks
    LoansService loansService;

    @Test
    void takeMortgageLoanTest(){
        Account account=new Account();
        account.setBalance(2000);
        account.setLoans(new ArrayList<>());
        User user=new User();
        user.setName("Chisimdi");
        account.setUser(user);
        Double amount=2000.00;
        Loans loans=new Loans();

        LoanSchedule loanSchedule=new LoanSchedule();

        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(account);
        when(loansRepository.save(any(Loans.class))).thenAnswer(inv->inv.getArgument(0));
        when(accountRepository.save(account)).thenReturn(account);
        when(loansScheduleRepository.save(any(LoanSchedule.class))).thenReturn(loanSchedule);

        LoansDTO loansDTO=loansService.takeMortgageLoan(2,1,amount);

        assertThat(loansDTO.getType()).isEqualTo("Mortgage");
        assertThat(loansDTO.getAmountWithInterest()).isEqualTo(amount*30*0.064);
        assertThat(loansDTO.getStatus()).isEqualTo("Pending");


        verify(accountRepository).findByIdAndUserId(1,2);
        verify(accountRepository).save(account);
        verify(publisher).publishEvent(any(LoansCreationEvent.class));


    }
    @Test
    void takeMortgageLoan_ThrowsResourceNotFoundException(){
        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(null);

        assertThatThrownBy(()->loansService.takeMortgageLoan(2,1,1000.00)).isInstanceOf(ResourceNotFoundException.class);
        verify(accountRepository,never()).save(any(Account.class));
        verify(publisher,never()).publishEvent(any(LoansCreationEvent.class));

    }
    @Test
    void takeAutoLoanTest(){
        Account account=new Account();
        account.setBalance(2000);
        account.setLoans(new ArrayList<>());
        User user=new User();
        user.setName("Chisimdi");
        account.setUser(user);
        Double amount=2000.00;
        Loans loans=new Loans();

        LoanSchedule loanSchedule=new LoanSchedule();

        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(account);
        when(loansRepository.save(any(Loans.class))).thenAnswer(inv->inv.getArgument(0));
        when(accountRepository.save(account)).thenReturn(account);
        when(loansScheduleRepository.save(any(LoanSchedule.class))).thenReturn(loanSchedule);

        LoansDTO loansDTO=loansService.takeAutoLoan(2,1,amount);

        assertThat(loansDTO.getType()).isEqualTo("Auto");
        assertThat(loansDTO.getAmountWithInterest()).isEqualTo(amount*7*0.068);
        assertThat(loansDTO.getStatus()).isEqualTo("Pending");


        verify(accountRepository).findByIdAndUserId(1,2);
        verify(accountRepository).save(account);
        verify(publisher).publishEvent(any(LoansCreationEvent.class));


    }
    @Test
    void takeAutoLoan_ThrowsResourceNotFoundException(){
        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(null);

        assertThatThrownBy(()->loansService.takeAutoLoan(2,1,1000.00)).isInstanceOf(ResourceNotFoundException.class);
        verify(accountRepository,never()).save(any(Account.class));
        verify(publisher,never()).publishEvent(any(LoansCreationEvent.class));

    }
    @Test
    void takeStudentLoanTest(){
        Account account=new Account();
        account.setBalance(2000);
        account.setLoans(new ArrayList<>());
        User user=new User();
        user.setName("Chisimdi");
        account.setUser(user);
        Double amount=2000.00;
        Loans loans=new Loans();

        LoanSchedule loanSchedule=new LoanSchedule();

        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(account);
        when(loansRepository.save(any(Loans.class))).thenAnswer(inv->inv.getArgument(0));
        when(accountRepository.save(account)).thenReturn(account);
        when(loansScheduleRepository.save(any(LoanSchedule.class))).thenReturn(loanSchedule);

        LoansDTO loansDTO=loansService.takeStudentLoans(2,1,amount);

        assertThat(loansDTO.getType()).isEqualTo("Student");
        assertThat(loansDTO.getAmountWithInterest()).isEqualTo(amount*10*0.0639);
        assertThat(loansDTO.getStatus()).isEqualTo("Pending");


        verify(accountRepository).findByIdAndUserId(1,2);
        verify(accountRepository).save(account);
        verify(publisher).publishEvent(any(LoansCreationEvent.class));


    }
    @Test
    void takeStudentLoan_ThrowsResourceNotFoundException(){
        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(null);

        assertThatThrownBy(()->loansService.takeStudentLoans(2,1,1000.00)).isInstanceOf(ResourceNotFoundException.class);
        verify(accountRepository,never()).save(any(Account.class));
        verify(publisher,never()).publishEvent(any(LoansCreationEvent.class));

    }
    @Test
    void takePersonalLoanTest(){
        Account account=new Account();
        account.setBalance(2000);
        account.setLoans(new ArrayList<>());
        User user=new User();
        user.setName("Chisimdi");
        account.setUser(user);
        Double amount=2000.00;
        Loans loans=new Loans();

        LoanSchedule loanSchedule=new LoanSchedule();

        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(account);
        when(loansRepository.save(any(Loans.class))).thenAnswer(inv->inv.getArgument(0));
        when(accountRepository.save(account)).thenReturn(account);
        when(loansScheduleRepository.save(any(LoanSchedule.class))).thenReturn(loanSchedule);

        LoansDTO loansDTO=loansService.takePersonalLoans(2,1,amount);

        assertThat(loansDTO.getType()).isEqualTo("Personal");
        assertThat(loansDTO.getAmountWithInterest()).isEqualTo(amount*7*0.1374);
        assertThat(loansDTO.getStatus()).isEqualTo("Pending");


        verify(accountRepository).findByIdAndUserId(1,2);
        verify(accountRepository).save(account);
        verify(publisher).publishEvent(any(LoansCreationEvent.class));


    }
    @Test
    void takePersonalLoan_ThrowsResourceNotFoundException(){
        when(accountRepository.findByIdAndUserId(1,2)).thenReturn(null);

        assertThatThrownBy(()->loansService.takePersonalLoans(2,1,1000.00)).isInstanceOf(ResourceNotFoundException.class);
        verify(accountRepository,never()).save(any(Account.class));
        verify(publisher,never()).publishEvent(any(LoansCreationEvent.class));

    }
    @Test
    void payTest(){
      Loans loans=new Loans();
      loans.setAmountWithInterest(200);
      loans.setStatus("Pending");
      Account account=new Account();
      account.setBalance(200);
      loans.setAccount(account);
      account.setLoans(new ArrayList<>());
      account.getLoans().add(loans);
      LoanSchedule loanSchedule=new LoanSchedule();
      loanSchedule.setLoans(loans);
      loanSchedule.setAmountPaid(0);
      loanSchedule.setAmount(200);
      loans.setLoanSchedules(new ArrayList<>());
      loans.getLoanSchedules().add(loanSchedule);
      loanSchedule.setStatus("Pending");
      User user=new User();
      user.setName("Chisimdi");
      account.setUser(user);

      when(loansScheduleRepository.findByIdAndLoansAccountUserId(1,2)).thenReturn(loanSchedule);
      when(loansScheduleRepository.save(loanSchedule)).thenReturn(loanSchedule);
      when(loansRepository.save(loans)).thenReturn(loans);
      when(accountRepository.save(account)).thenReturn(account);

      LoansScheduleDTO loansScheduleDTO=loansService.pay(2,1,200);

      assertThat(loanSchedule.getAmountPaid()).isEqualTo(200);
      assertThat(account.getBalance()).isLessThanOrEqualTo(0);
      assertThat(loanSchedule.getStatus()).isEqualTo("Paid");
      assertThat(loans.getStatus()).isEqualTo("Paid");

verify(loansRepository).save(loans);
verify(accountRepository).save(account);
verify(loansScheduleRepository).save(loanSchedule);
verify(loansScheduleRepository).findByIdAndLoansAccountUserId(1,2);
verify(publisher).publishEvent(any(LoanPaymentEvent.class));
    }

    @Test
    void payTest_ThrowsResourceNOtFoundException(){
        Loans loans=new Loans();
        loans.setAmountWithInterest(200);
        loans.setStatus("Pending");
        Account account=new Account();
        account.setBalance(200);
        loans.setAccount(account);
        account.setLoans(new ArrayList<>());
        account.getLoans().add(loans);
        LoanSchedule loanSchedule=new LoanSchedule();
        loanSchedule.setLoans(loans);
        loanSchedule.setAmountPaid(0);
        loanSchedule.setAmount(200);
        loans.setLoanSchedules(new ArrayList<>());
        loans.getLoanSchedules().add(loanSchedule);
        loanSchedule.setStatus("Pending");
        User user=new User();
        user.setName("Chisimdi");
        account.setUser(user);

        when(loansScheduleRepository.findByIdAndLoansAccountUserId(1,2)).thenReturn(null);


        assertThatThrownBy(()->loansService.pay(2,1,200)).isInstanceOf(ResourceNotFoundException.class);



        verify(loansRepository,never()).save(loans);
        verify(accountRepository,never()).save(account);
        verify(loansScheduleRepository,never()).save(loanSchedule);
        verify(loansScheduleRepository).findByIdAndLoansAccountUserId(1,2);
        verify(publisher,never()).publishEvent(any(LoanPaymentEvent.class));
}
    @Test
    void payTest_ThrowsExistsException(){
        Loans loans=new Loans();
        loans.setAmountWithInterest(200);
        loans.setStatus("Pending");
        Account account=new Account();
        account.setBalance(200);
        loans.setAccount(account);
        account.setLoans(new ArrayList<>());
        account.getLoans().add(loans);
        LoanSchedule loanSchedule=new LoanSchedule();
        loanSchedule.setLoans(loans);
        loanSchedule.setAmountPaid(0);
        loanSchedule.setAmount(200);
        loans.setLoanSchedules(new ArrayList<>());
        loans.getLoanSchedules().add(loanSchedule);
        loanSchedule.setStatus("Paid");
        User user=new User();
        user.setName("Chisimdi");
        account.setUser(user);

        when(loansScheduleRepository.findByIdAndLoansAccountUserId(1,2)).thenReturn(loanSchedule);


        assertThatThrownBy(()->loansService.pay(2,1,200)).isInstanceOf(ExistsException.class);



        verify(loansRepository,never()).save(loans);
        verify(accountRepository,never()).save(account);
        verify(loansScheduleRepository,never()).save(loanSchedule);
        verify(loansScheduleRepository).findByIdAndLoansAccountUserId(1,2);
        verify(publisher,never()).publishEvent(any(LoanPaymentEvent.class));
    }
    @Test
    void payTest_ThrowsInsufficientFundsException(){
        Loans loans=new Loans();
        loans.setAmountWithInterest(200);
        loans.setStatus("Pending");
        Account account=new Account();
        account.setBalance(200);
        loans.setAccount(account);
        account.setLoans(new ArrayList<>());
        account.getLoans().add(loans);
        LoanSchedule loanSchedule=new LoanSchedule();
        loanSchedule.setLoans(loans);
        loanSchedule.setAmountPaid(0);
        loanSchedule.setAmount(300);
        loans.setLoanSchedules(new ArrayList<>());
        loans.getLoanSchedules().add(loanSchedule);
        loanSchedule.setStatus("Pending");
        User user=new User();
        user.setName("Chisimdi");
        account.setUser(user);

        when(loansScheduleRepository.findByIdAndLoansAccountUserId(1,2)).thenReturn(loanSchedule);


        assertThatThrownBy(()->loansService.pay(2,1,300)).isInstanceOf(InsufficientFundsException.class);



        verify(loansRepository).save(loans);
        verify(accountRepository,never()).save(account);
        verify(loansScheduleRepository).save(loanSchedule);
        verify(loansScheduleRepository).findByIdAndLoansAccountUserId(1,2);
        verify(publisher,never()).publishEvent(any(LoanPaymentEvent.class));
    }

    @Test
    void setAutoPayTest(){
        Loans loans=new Loans();
        loans.setAutopay(false);

        when(loansRepository.findByIdAndAccountUserId(1,2)).thenReturn(loans);
        when(loansRepository.save(loans)).thenReturn(loans);
        loansService.setAutoPay(2,1);
        assertThat(loans.isAutopay()).isEqualTo(true);

        verify(loansRepository).save(loans);
        verify(loansRepository).findByIdAndAccountUserId(1,2);
    }
    @Test
    void setAutoPayTest_ThrowsExistsException(){
        Loans loans=new Loans();
        loans.setAutopay(true);

        when(loansRepository.findByIdAndAccountUserId(1,2)).thenReturn(loans);

        assertThatThrownBy(()->loansService.setAutoPay(2,1)).isInstanceOf(ExistsException.class);

        verify(loansRepository,never()).save(loans);
        verify(loansRepository).findByIdAndAccountUserId(1,2);
    }
    @Test
    void setAutoPayTest_ThrowsResourceNotFoudException(){
        Loans loans=new Loans();
        loans.setAutopay(true);

        when(loansRepository.findByIdAndAccountUserId(1,2)).thenReturn(null);

        assertThatThrownBy(()->loansService.setAutoPay(2,1)).isInstanceOf(ResourceNotFoundException.class);

        verify(loansRepository,never()).save(loans);
        verify(loansRepository).findByIdAndAccountUserId(1,2);
    }
}
