package com.chisimdi.Banking.services;

import com.chisimdi.Banking.exceptions.ExistsException;
import com.chisimdi.Banking.exceptions.InsufficientFundsException;
import com.chisimdi.Banking.exceptions.ResourceNotFoundException;
import com.chisimdi.Banking.models.*;
import com.chisimdi.Banking.repositories.AccountRepository;
import com.chisimdi.Banking.repositories.LoansRepository;
import com.chisimdi.Banking.repositories.LoansScheduleRepository;
import com.chisimdi.Banking.services.events.DueDateEvent;
import com.chisimdi.Banking.services.events.LoanPaymentEvent;
import com.chisimdi.Banking.services.events.LoansCreationEvent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoansService {
    private static final Logger log = LoggerFactory.getLogger(LoansService.class);
    private AccountRepository accountRepository;
    private LoansRepository loansRepository;
    private LoansScheduleRepository loansScheduleRepository;
    private ApplicationEventPublisher publisher;

    public LoansService(AccountRepository accountRepository,LoansRepository loansRepository,LoansScheduleRepository loansScheduleRepository,ApplicationEventPublisher publisher){
        this.accountRepository=accountRepository;
        this.loansRepository=loansRepository;
        this.loansScheduleRepository=loansScheduleRepository;
        this.publisher=publisher;
    }
    public LoansDTO toLoansDTO(Loans loans){
        LoansDTO loansDTO=new LoansDTO();
        loansDTO.setId(loans.getId());
        if(loans.getType()!=null){
            loansDTO.setType(loans.getType());
        }
        loansDTO.setAmountWithInterest(loans.getAmountWithInterest());
        if(loans.getStatus()!=null){
            loansDTO.setStatus(loans.getStatus());
        }
        loansDTO.setAutoPay(loans.isAutopay());
        return loansDTO;


    }

    public LoansScheduleDTO toLoansScheduleDTO(LoanSchedule loanSchedule){

        LoansScheduleDTO scheduleDTO=new LoansScheduleDTO();
        if(loanSchedule.getStatus()!=null){
            scheduleDTO.setStatus(loanSchedule.getStatus());
        }
        scheduleDTO.setAmount(loanSchedule.getAmount());
        scheduleDTO.setAmountPaid(loanSchedule.getAmountPaid());
        scheduleDTO.setId(loanSchedule.getId());
        if(loanSchedule.getDueDate()!=null){
            scheduleDTO.setDueDate(loanSchedule.getDueDate());
        }
        return scheduleDTO;
    }
@Transactional
    public LoansDTO takeMortgageLoan(int userId, int accountId,Double amount){
        log.info("Taking mortgage loan with account Id {}, user Id {} and amount {}",accountId,userId,amount);
        Account account= accountRepository.findByIdAndUserId(accountId,userId);
        if(account==null){
            throw new ResourceNotFoundException("Account with userId "+userId+" and bank Id "+accountId+" not found");

        }
log.debug("Creating loans");
        Loans loans=new Loans();
        loans.setAccount(account);
        loans.setType("Mortgage");
        loans.setStatus("Pending");
        loans.setAmountWithInterest(amount*30*0.064);
        loans.setTotalPayments(360);
        loansRepository.save(loans);
        account.getLoans().add(loans);
        accountRepository.save(account);
        double scheduledAmount =amount*(0.0053*(Math.pow(1+0.0053,360))/(Math.pow(1+0.0053,360)-1));
log.debug("Creating loan schedules");
        for(int x=0;x<loans.getTotalPayments();x++){
            LoanSchedule loanSchedule=new LoanSchedule();
            loanSchedule.setLoans(loans);
            loanSchedule.setAmount(scheduledAmount);
            loanSchedule.setDueDate(LocalDate.now().plusMonths(x));
            loans.getLoanSchedules().add(loanSchedule);
            loansRepository.save(loans);
            loanSchedule.setStatus("Pending");
            loansScheduleRepository.save(loanSchedule);
            log.info("loan created sucessfully");
        }
        log.debug("Publishing new loans creation event");
        publisher.publishEvent(new LoansCreationEvent(account.getUser().getContactInfo(),amount, loans.getType(), account.getAccountNumber()));
        return toLoansDTO(loans);

    }
    @Transactional
    public LoansDTO takeAutoLoan(int userId, int accountId,Double amount){
        log.info("Taking auto loan with account Id {}, user Id {} and amount {}",accountId,userId,amount);
        Account account= accountRepository.findByIdAndUserId(accountId,userId);
        if(account==null){
            throw new ResourceNotFoundException("Account with userId "+userId+" and bank Id "+accountId+" not found");

        }
log.debug("creating new loan");
        Loans loans=new Loans();
        loans.setAccount(account);
        loans.setType("Auto");
        loans.setStatus("Pending");
        loans.setAmountWithInterest(amount*7*0.068);
        loans.setTotalPayments(84);
        loansRepository.save(loans);
        account.getLoans().add(loans);
        accountRepository.save(account);
        double scheduledAmount =amount*(0.00057*(Math.pow(1+0.00057,84))/(Math.pow(1+0.00057,84)-1));
log.debug("Creating loan schedules");
        for(int x=0;x<loans.getTotalPayments();x++){
            LoanSchedule loanSchedule=new LoanSchedule();
            loanSchedule.setLoans(loans);
            loanSchedule.setAmount(scheduledAmount);
            loanSchedule.setDueDate(LocalDate.now().plusMonths(x));
            loanSchedule.setStatus("Pending");
            loans.getLoanSchedules().add(loanSchedule);
            loansRepository.save(loans);
            loansScheduleRepository.save(loanSchedule);
        }
        log.debug("Publishing new loan creation event");
        log.info("loan created");
        publisher.publishEvent(new LoansCreationEvent(account.getUser().getContactInfo(),amount, loans.getType(), account.getAccountNumber()));
        return toLoansDTO(loans);

    }
    @Transactional
    public LoansDTO takeStudentLoans(int userId, int accountId,Double amount){
        log.info("Taking student loan with account Id {}, user Id {} and amount {}",accountId,userId,amount);
        Account account= accountRepository.findByIdAndUserId(accountId,userId);
        if(account==null){
            throw new ResourceNotFoundException("Account with userId "+userId+" and bank Id "+accountId+" not found");

        }
log.debug("Creating new loans");
        Loans loans=new Loans();
        loans.setAccount(account);
        loans.setType("Student");
        loans.setStatus("Pending");
        loans.setAmountWithInterest(amount*10*0.0639);
        loans.setTotalPayments(120);
        loansRepository.save(loans);
        account.getLoans().add(loans);
        accountRepository.save(account);
        double scheduledAmount =amount*(0.0005325*(Math.pow(1+0.005325,120))/(Math.pow(1+0.005325,120)-1));
log.debug("Creating new loan schedules");
        for(int x=0;x<loans.getTotalPayments();x++){
            LoanSchedule loanSchedule=new LoanSchedule();
            loanSchedule.setLoans(loans);
            loanSchedule.setAmount(scheduledAmount);
            loanSchedule.setDueDate(LocalDate.now().plusMonths(x));
            loanSchedule.setStatus("Pending");
            loans.getLoanSchedules().add(loanSchedule);
            loansRepository.save(loans);
            loansScheduleRepository.save(loanSchedule);
        }
        log.debug("Publishing new loan created event");
        log.info("Loan created successfully");
        publisher.publishEvent(new LoansCreationEvent(account.getUser().getContactInfo(),amount, loans.getType(), account.getAccountNumber()));
        return toLoansDTO(loans);

    }
@Transactional
    public LoansDTO takePersonalLoans(int userId, int accountId,Double amount){
    log.info("Taking personal loan with account Id {}, user Id {} and amount {}",accountId,userId,amount);
        Account account= accountRepository.findByIdAndUserId(accountId,userId);
        if(account==null){
            throw new ResourceNotFoundException("Account with userId "+userId+" and bank Id "+accountId+" not found");

        }
log.debug("creating new loans");
        Loans loans=new Loans();
        loans.setAccount(account);
        loans.setType("Personal");
        loans.setStatus("Pending");
        loans.setAmountWithInterest(amount*7*0.1374);
        loans.setTotalPayments(84);
        loansRepository.save(loans);
        account.getLoans().add(loans);
        accountRepository.save(account);
        double scheduledAmount =amount*(0.01145*(Math.pow(1+0.01145,84))/(Math.pow(1+0.01145,84)-1));
log.debug("Creating loan schedules");
        for(int x=0;x<loans.getTotalPayments();x++){
            LoanSchedule loanSchedule=new LoanSchedule();
            loanSchedule.setAmount(scheduledAmount);
            loanSchedule.setDueDate(LocalDate.now().plusMonths(x));
            loans.getLoanSchedules().add(loanSchedule);
            loanSchedule.setStatus("Pending");
            loanSchedule.setLoans(loans);
            loansScheduleRepository.save(loanSchedule);
            loansRepository.save(loans);
        }
        log.debug("Publishing new loan creation event");
        log.info("Loan created successfully");
        publisher.publishEvent(new LoansCreationEvent(account.getUser().getContactInfo(),amount, loans.getType(), account.getAccountNumber()));
        return toLoansDTO(loans);

    }

    public List<LoansScheduleDTO> findAllSchedulesPerLoan(int loanId, int userId){
        log.info("Searching for schedules with loan Id "+loanId+" and user Id "+userId);
        List<LoanSchedule>loanSchedules=loansScheduleRepository.findByLoansIdAndLoansAccountUserId(loanId,userId);
        List<LoansScheduleDTO>loansScheduleDTOS=new ArrayList<>();
        if(loanSchedules.isEmpty()){
            throw new ResourceNotFoundException("Loan Schedules with loan Id "+loanId+" and user Id "+userId+" not found");
        }
        for(LoanSchedule l:loanSchedules){
            loansScheduleDTOS.add(toLoansScheduleDTO(l));
        }
        log.info("Loans found successfully");
        return loansScheduleDTOS;

    }
    public LoansDTO findSpecificLoans(int loansId,int userId){
        log.info("Searching for loans with Id {} and user Id {}",loansId,userId);
        Loans loans= loansRepository.findByIdAndAccountUserId(loansId,userId);
        if(loans==null){
            throw new ResourceNotFoundException("Loans with id "+loansId+" and user Id"+userId+" not found");
        }
        log.info("Loans found successfully");
        return toLoansDTO(loans);
    }
    public List<LoansDTO> findAllLoans(int size, int pageNumber){
        Page<Loans> loans=loansRepository.findAll(PageRequest.of(pageNumber,size));
        List<LoansDTO>loansDTOS=new ArrayList<>();
        for(Loans l:loans){
            loansDTOS.add(toLoansDTO(l));
        }
        return loansDTOS;
    }
    public List<LoansDTO> findAllLoansByUser(int userID,int pageNumber, int size){
        log.info("Searching for loans with user Id {}",userID);
        Page<Loans>loans=loansRepository.findByAccountUserId(userID,PageRequest.of(pageNumber,size));
        List<LoansDTO>loansDTOS=new ArrayList<>();
        if(loans.isEmpty()){
            throw new ResourceNotFoundException("Loans with user Id "+userID+" not found");
        }
        for(Loans l:loans){
            loansDTOS.add(toLoansDTO(l));
        }
        log.info("Loans found successfully");
        return loansDTOS;
    }

    @Transactional
    public LoansScheduleDTO pay(int userId, int scheduleId, double amount){
        log.info("Paying for loan with schedule id {} and userId {}",scheduleId,userId);
    LoanSchedule loanSchedule=loansScheduleRepository.findByIdAndLoansAccountUserId(scheduleId,userId);

    if(loanSchedule==null){
        throw new ResourceNotFoundException("Loans Schedule with user Id "+userId+" and Id "+scheduleId+" not found");
    }
        if(loanSchedule.getStatus().equalsIgnoreCase("Paid")){
            throw new ExistsException("A payment with Id "+scheduleId+" already exists and is paid for completely");
        }
        log.debug("Paying for loan");
    loanSchedule.setAmountPaid(loanSchedule.getAmountPaid()+amount);
    if(loanSchedule.getAmountPaid()>=loanSchedule.getAmount()){
        loanSchedule.setStatus("Paid");
    }
    loansScheduleRepository.save(loanSchedule);
    Loans loans=loanSchedule.getLoans();
    loans.setAmountWithInterest(loans.getAmountWithInterest()-amount);
    if(loans.getAmountWithInterest()<=0){
        loans.setStatus("Paid");
    }
    loansRepository.save(loans);
    Account account=loans.getAccount();
        if(account.getBalance()<amount){
            throw new InsufficientFundsException("account with id "+account.getId()+" does not have enough money");
        }
    account.setBalance(account.getBalance()-amount);
    accountRepository.save(account);

    log.info("Loan payed for successfully");
    log.debug("Publishing new loan payment event");
    publisher.publishEvent(new LoanPaymentEvent(account.getUser().getContactInfo(),loans.getId(),account.getUser().getName(),amount,loans.getAmountWithInterest()));
    return  toLoansScheduleDTO(loanSchedule);
    }

    @Transactional
    public LoansDTO setAutoPay(int userId, int loanId){
        log.info("Searching for loan with user Id {} and loan Id {}",userId,loanId);
        Loans loans= loansRepository.findByIdAndAccountUserId(loanId,userId);
        if(loans==null){
            throw new ResourceNotFoundException("Loan with user Id "+userId+" and loan Id "+loanId+" not found");

        }
        if(loans.isAutopay()==true){
  throw new ExistsException("loan with autopay id "+ loanId+" already exists");
        }
        log.debug("Setting auto pay");
        loans.setAutopay(true);
        loansRepository.save(loans);
        log.info("Auto pay set successfully");
        return toLoansDTO(loans);

    }
    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void pay(){
        log.debug("Searching for loans by auto pay");
       List<Loans>loans= loansRepository.findByAutoPay(true);
       for(int x=0;x<loans.size();x++){
           for(int y=0;y<loans.get(x).getLoanSchedules().size();y++){
               Account account=loans.get(x).getAccount();
               log.debug("Searching for months that matches loans schedule due date");
               if(loans.get(x).getLoanSchedules().get(y).getDueDate().equals(LocalDate.now())&&!loans.get(x).getLoanSchedules().get(y).getStatus().equalsIgnoreCase("Paid")) {
                   if (account.getBalance() < loans.get(x).getLoanSchedules().get(y).getAmount()) {
                       throw new InsufficientFundsException("Account with Id " + account.getId() + " does not have enough money");

                   }
                   log.debug("Paying for loan");
                   account.setBalance(account.getBalance() - loans.get(x).getLoanSchedules().get(y).getAmount());
                   loans.get(x).getLoanSchedules().get(y).setStatus("Paid");
                   double amount=loans.get(x).getLoanSchedules().get(y).getAmount();
                   loans.get(x).getLoanSchedules().get(y).setAmountPaid( loans.get(x).getLoanSchedules().get(y).getAmount());
                   loans.get(x).setAmountWithInterest(loans.get(x).getAmountWithInterest()-loans.get(x).getLoanSchedules().get(y).getAmount());
                   if(loans.get(x).getAmountWithInterest()<=0){
                       loans.get(x).setStatus("Paid");
                   }
                   log.debug("Loan payed for successfully, publishing new loan payment event");
                   publisher.publishEvent(new LoanPaymentEvent(account.getUser().getContactInfo(),loans.get(x).getId(),account.getUser().getName(),loans.get(x).getLoanSchedules().get(y).getAmount(),loans.get(x).getAmountWithInterest()));
                   loansRepository.save(loans.get(x));
                   loansScheduleRepository.save(loans.get(x).getLoanSchedules().get(y));
                   accountRepository.save(account);

               }
               }
       }
    }
@Transactional
    @Scheduled(cron = "0 0 * L * *")
    public void endOfMonthCheckForLoans(){
        log.debug("Searching for loans with status pending");
        List<Loans>loans=loansRepository.findByStatus("Pending");
        log.debug("Searching for loans that are paid and unpaid");
        for(int x=0;x<loans.size();x++){
            for (int y=0;y<loans.get(x).getLoanSchedules().size();y++){
                LoanSchedule loanSchedule=loans.get(x).getLoanSchedules().get(y);
                if(loanSchedule.getDueDate().isBefore(LocalDate.now())&&loanSchedule.getStatus().equalsIgnoreCase("Pending")&&loanSchedule.isNotified()==false){
                    log.debug("Loan unpaid, publishing new due date event with status unpaid");
                    loans.get(x).setAmountWithInterest(loans.get(x).getAmountWithInterest()+(loans.get(x).getLoanSchedules().get(y).getAmount()-loans.get(x).getLoanSchedules().get(y).getAmountPaid()));
                    loanSchedule.setNotified(true);
                    publisher.publishEvent(new DueDateEvent(loans.get(x).getAccount().getUser().getName(),loans.get(x).getAccount().getUser().getContactInfo(), loanSchedule.getStatus(), loans.get(x).getId()));
                }
                if (loanSchedule.getDueDate().isBefore(LocalDate.now())&&loanSchedule.getStatus().equalsIgnoreCase("Paid")&&loanSchedule.isNotified()==false){
                    log.debug("Loan paid, publishing new due date event with status paid");
                    loanSchedule.setNotified(true);
                    publisher.publishEvent(new DueDateEvent(loans.get(x).getAccount().getUser().getName(),loans.get(x).getAccount().getUser().getContactInfo(), loanSchedule.getStatus(),loans.get(x).getId()));
                }

            }
        }
    }



}
