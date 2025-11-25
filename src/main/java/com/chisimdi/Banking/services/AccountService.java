package com.chisimdi.Banking.services;

import com.chisimdi.Banking.exceptions.ExistsException;
import com.chisimdi.Banking.exceptions.ResourceNotFoundException;
import com.chisimdi.Banking.exceptions.WrongRoleException;
import com.chisimdi.Banking.models.Account;
import com.chisimdi.Banking.models.AccountDTO;
import com.chisimdi.Banking.models.User;
import com.chisimdi.Banking.repositories.AccountRepository;
import com.chisimdi.Banking.repositories.UserRepository;
import com.chisimdi.Banking.services.events.AccountCreationEvent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private ApplicationEventPublisher publisher;

    public AccountService(AccountRepository accountRepository,UserRepository userRepository,ApplicationEventPublisher publisher){
        this.publisher=publisher;
        this.accountRepository=accountRepository;
        this.userRepository=userRepository;
    }

    public AccountDTO toAccountDTO(Account account){
        log.info("converting account with ID {} to accountDTO",account.getId());
        AccountDTO accountDTO=new AccountDTO();

        accountDTO.setId(account.getId());
        if(account.getAccountNumber()!=null) {
            accountDTO.setAccountNumber(account.getAccountNumber());
        }
        accountDTO.setBalance(account.getBalance());
        if(account.getType()!=null) {
            accountDTO.setType(account.getType());
        }
        return accountDTO;
    }

    public List<AccountDTO> getAccountsByUser(int userID,int size,int pageNumber){
        log.info("Searching for all accounts belonging to user With ID {}",userID);
       Page<Account> accounts= accountRepository.findByUserId(userID, PageRequest.of(pageNumber,size));
       List<AccountDTO>accountDTOS=new ArrayList<>();
        if(accounts==null){
            throw new ResourceNotFoundException("Accounts belonging to user with ID "+userID+" not found");
        }
        for(Account a:accounts){
            accountDTOS.add(toAccountDTO(a));
        }
        log.info("found all accounts");
        return accountDTOS;

    }
    @Transactional
    public AccountDTO addAccount(String accountNumber,Double balance,String type,int userId){
        log.info("Creating new account for user with Id "+userId);
        User user=userRepository.findById(userId).orElse(null);
        if(user==null){
            throw new ResourceNotFoundException("user with id "+userId + "not found");
        }
        if(!user.getRoles().equalsIgnoreCase("Customer")){
            throw new WrongRoleException("User with Id "+userId+" is not a customer");
        }
        if(accountRepository.existsByAccountNumber(accountNumber)){
            throw new ExistsException("Account with accountNumber "+accountNumber+ "already exists");
        }
        log.debug("Creating account");
        Account account=new Account();
        account.setType(type);
        account.setAccountNumber(accountNumber);
        account.setBalance(balance);
        account.setUser(user);
        user.getAccounts().add(account);
        log.info("Account created successfully, publishing new Account Creation Event");
        publisher.publishEvent(new AccountCreationEvent(account.getUser().getId(),account.getUser().getContactInfo(),account.getAccountNumber(),account.getUser().getName()));
        userRepository.save(user);
        log.info("account created");
        return toAccountDTO(account);
    }
    public List<AccountDTO> findAllAccounts(int pageNumber,int size){
        log.info("searching for all accounts");
        List<AccountDTO>accountDTOS=new ArrayList<>();

        Page<Account>accounts=accountRepository.findAll(PageRequest.of(pageNumber,size));
        for(Account a:accounts){
            accountDTOS.add(toAccountDTO(a));

        }
        return accountDTOS;
    }
    public List<AccountDTO>findAccountsByUser(int userId,int pageNumber,int size){
        log.info("Searching for accounts belonging to user with Id "+userId);
        List<AccountDTO>accountDTOS=new ArrayList<>();
        Page<Account>accounts=accountRepository.findByUserId(userId,PageRequest.of(pageNumber,size));
        if(accounts.isEmpty()){
            throw new ResourceNotFoundException("Accounts not found");
        }
        for(Account a:accounts){
            accountDTOS.add(toAccountDTO(a));

        }
        log.info("accounts found");
        return accountDTOS;
    }
    public AccountDTO findAccountById(int bankId,int userId){
        log.info("Searching for accounts with user Id "+userId+" and bank Id "+bankId);
        Account account=accountRepository.findByIdAndUserId(bankId,userId);
        if(account==null){
            throw new ResourceNotFoundException("Account with Id "+bankId+ " and userId "+userId +"not found");
        }
        log.info("account found");
        return toAccountDTO(account);
    }

}
