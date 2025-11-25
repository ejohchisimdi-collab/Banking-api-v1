package com.chisimdi.Banking.services;

import com.chisimdi.Banking.exceptions.InsufficientFundsException;
import com.chisimdi.Banking.exceptions.ResourceNotFoundException;
import com.chisimdi.Banking.models.Account;
import com.chisimdi.Banking.models.Transactions;
import com.chisimdi.Banking.models.TransactionsDTO;
import com.chisimdi.Banking.models.TransfersDTO;
import com.chisimdi.Banking.repositories.AccountRepository;
import com.chisimdi.Banking.repositories.TransactionRepository;
import com.chisimdi.Banking.services.events.DepositEvent;
import com.chisimdi.Banking.services.events.WithdrawalEvent;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
private ApplicationEventPublisher publisher;
    public TransactionService(AccountRepository accountRepository,TransactionRepository transactionRepository,ApplicationEventPublisher publisher){
        this.accountRepository=accountRepository;
        this.transactionRepository=transactionRepository;
        this.publisher=publisher;
    }

    public TransactionsDTO toTransactionsDTO(Transactions transactions){
        log.info("Converting transactions with ID"+transactions.getId()+" to transactions DTO");
        TransactionsDTO transactionsDTO=new TransactionsDTO();
        if(transactions.getType()!=null){
            transactionsDTO.setType(transactions.getType());
        }
        transactionsDTO.setId(transactions.getId());
        if(transactions.getAccount()!=null){
            transactionsDTO.setAccountId(transactions.getAccount().getId());
        }
        if (transactionsDTO.getLocalDate()!=null){
            transactionsDTO.setLocalDate(transactions.getLocalDate());
        }
        transactionsDTO.setAmount(transactions.getAmount());
        return transactionsDTO;
    }
@Transactional
    public TransactionsDTO Deposit(int userId,int bankId, double amount){
        log.info("Depositing to account with user iD "+userId +"and bankID "+bankId);
        Account account=accountRepository.findByIdAndUserId(bankId,userId);
        if(account==null){
            throw new ResourceNotFoundException("Account with Id "+bankId+
            " and userId "+userId+" not found");
        }
        log.debug("Depositing to account");
        account.setBalance(account.getBalance()+amount);
        accountRepository.save(account);
        Transactions transactions=new Transactions();
        transactions.setAccount(account);
        transactions.setType("Deposit");
        transactions.setLocalDate(LocalDate.now());
        transactions.setAmount(amount);
        transactionRepository.save(transactions);
        log.info("Deposit successful, publishing new deposit event");
        publisher.publishEvent(new DepositEvent(account.getUser().getName(),account.getAccountNumber(),amount,account.getUser().getContactInfo()));
return toTransactionsDTO( transactions);
    }
    @Transactional
    public TransactionsDTO withdraw(int userId,int bankId, double amount){
        log.info("Withdrawing from account with user iD "+userId +"and bankID "+bankId);
        Account account=accountRepository.findByIdAndUserId(bankId,userId);
        if(account==null){
            throw new ResourceNotFoundException("Account with Id "+bankId+
                    " and userId "+userId+" not found");
        }

        if(account.getBalance()<amount){
            throw new InsufficientFundsException("Account with Id "+bankId+
                    " and userId "+userId+" has less money than the withdrawal amount");
        }
        log.info("Processing withdraw");
        account.setBalance(account.getBalance()-amount);
        Transactions transactions=new Transactions();
        transactions.setType("Withdrawal");
        transactions.setAccount(account);
        transactions.setAmount(amount);
        accountRepository.save(account);
        transactionRepository.save(transactions);
        log.info("Withdraw successful, Publishing new Withdrawal Event");
        publisher.publishEvent(new WithdrawalEvent(account.getUser().getName(), account.getAccountNumber(), amount,account.getUser().getContactInfo()));
        return toTransactionsDTO(transactions);

    }
    public List<TransactionsDTO> findByUSers(int userId,int size, int pageNumber){
        log.info("Searching for transactions with userId "+userId);
        Page<Transactions> transactions=transactionRepository.findByAccountUserId(userId,PageRequest.of(pageNumber,size));
        List<TransactionsDTO> transactionsDTOS=new ArrayList<>();
        if(transactions.isEmpty()){
            throw new ResourceNotFoundException("Transactions with user Id "+userId+" not found");
        }
        log.info("Found Transactions");
        for(Transactions t:transactions){
           transactionsDTOS.add(toTransactionsDTO(t));
        }
        return transactionsDTOS;
    }
    public List<TransactionsDTO>findAllTransactions(int pageNumber, int size){
        log.info("Searching for all Transactions");
        Page<Transactions> transactions=transactionRepository.findAll(PageRequest.of(pageNumber,size));
        List<TransactionsDTO>transactionsDTOS=new ArrayList<>();
        for(Transactions t:transactions){
            transactionsDTOS.add(toTransactionsDTO(t));
        }
        return transactionsDTOS;
    }
    public TransactionsDTO findTransactionsById(int userId,int transactionId){
        log.info("Searching for transactions with userId "+userId+" and transaction Id "+ transactionId);
        Transactions transactions=transactionRepository.findByIdAndAccountUserId(transactionId,userId);
        if(transactions==null){
            throw new ResourceNotFoundException("transaction with user ID "+userId+
                    " and transaction Id "+transactionId+" not found");
        }
        log.info("Found transactions");
        return toTransactionsDTO(transactions);


    }

}
