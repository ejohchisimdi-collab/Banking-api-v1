package com.chisimdi.Banking.services;

import com.chisimdi.Banking.exceptions.ExistsException;
import com.chisimdi.Banking.exceptions.InsufficientFundsException;
import com.chisimdi.Banking.exceptions.ResourceNotFoundException;
import com.chisimdi.Banking.models.*;
import com.chisimdi.Banking.repositories.AccountRepository;
import com.chisimdi.Banking.repositories.ScheduledTransferRepository;
import com.chisimdi.Banking.repositories.TransfersRepository;
import com.chisimdi.Banking.services.events.ReverseTransferEvent;
import com.chisimdi.Banking.services.events.TransferEvent;
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
import java.util.Date;
import java.util.List;

@Service
public class TransferService {
    private static final Logger log = LoggerFactory.getLogger(TransferService.class);
    private AccountRepository accountRepository;
    private TransfersRepository transfersRepository;
    private ApplicationEventPublisher publisher;
    private ScheduledTransferRepository scheduledTransferRepository;

    public TransferService(AccountRepository accountRepository, TransfersRepository transfersRepository, ApplicationEventPublisher publisher,ScheduledTransferRepository scheduledTransferRepository) {
        this.accountRepository = accountRepository;
        this.transfersRepository = transfersRepository;
        this.publisher = publisher;
        this.scheduledTransferRepository=scheduledTransferRepository;
    }

    public TransfersDTO toTransfersDTO(Transfers transfers) {
        log.info("Converting Transfer with ID {} to TransferDTO ",transfers.getId());
        TransfersDTO transfersDTO = new TransfersDTO();
        if (transfers.getTransferringAccount() != null) {
            transfersDTO.setTransferringAccountId(transfers.getTransferringAccount().getId());

        }
        if (transfers.getReceivingAccount() != null) {
            transfersDTO.setReceivingAccountId(transfers.getReceivingAccount().getId());
        }
        transfersDTO.setAmount(transfers.getAmount());
        transfersDTO.setId(transfers.getId());
        transfersDTO.setReversed(transfers.getReversed());

        return transfersDTO;
    }
@Transactional
    public TransfersDTO transfer(int userId,int sendersBankId,int receiversBankId, Double amount){
        log.info("processing  transfer for accounts with ids {} and {} ",sendersBankId,receiversBankId);
        Account sendersAccount= accountRepository.findByIdAndUserId(sendersBankId,userId);
        Account receiversBankAccount=accountRepository.findById(receiversBankId).orElse(null);
        if(sendersAccount==null){
            throw new ResourceNotFoundException("Account with ID "+sendersBankId+" not found");

        }
        if(receiversBankAccount==null){
            throw new ResourceNotFoundException("Account with ID "+receiversBankId+" not found");
        }

        if(sendersAccount.getBalance()<amount){
            throw new InsufficientFundsException("Account with Id "+sendersBankId+" has less money than the required amount");
        }
        log.debug("Processing transfer");
        sendersAccount.setBalance(sendersAccount.getBalance()-amount);
        receiversBankAccount.setBalance(receiversBankAccount.getBalance()+amount);
        accountRepository.save(sendersAccount);
        accountRepository.save(receiversBankAccount);

        Transfers transfers=new Transfers();
        transfers.setAmount(amount);
        transfers.setTransferringAccount(sendersAccount);
        transfers.setReceivingAccount(receiversBankAccount);
        transfersRepository.save(transfers);
        log.info("Transfer successful, Publishing new Transfer Event");
        publisher.publishEvent(new TransferEvent(sendersAccount.getUser().getContactInfo(),sendersAccount.getUser().getName(),receiversBankAccount.getAccountNumber(),sendersAccount.getAccountNumber(),amount));
        return toTransfersDTO(transfers);


    }
    @Transactional

    public TransfersDTO reverseTransfer(int transferId){
        log.info("Searching for account with Id "+transferId);
        Transfers transfers=transfersRepository.findById(transferId).orElse(null);
        if(transfers==null){
            throw new ResourceNotFoundException("Transaction with Id "+transferId+" not found");
        }
        if(transfers.getReversed().equalsIgnoreCase("True")){
            throw new ExistsException("Reversed transfer with Id "+transferId+" already exists");
        }
        log.info("Searching for account with Id {}",transfers.getTransferringAccount().getId());
        Account transferingAccount=accountRepository.findById(transfers.getTransferringAccount().getId()).orElse(null);
        log.info("Searching for account with Id {}",transfers.getTransferringAccount().getId());
        Account receivingAccount=accountRepository.findById(transfers.getReceivingAccount().getId()).orElse(null);
log.debug("Processing reversal");
        transferingAccount.setBalance(transferingAccount.getBalance()+transfers.getAmount());
        receivingAccount.setBalance(receivingAccount.getBalance()-transfers.getAmount());
        accountRepository.save(transferingAccount);
        accountRepository.save(receivingAccount);
        transfers.setReversed("True");
        transfersRepository.save(transfers);
        log.info("Reversal successful, publishing new Transfer Reversal Event");
        publisher.publishEvent(new ReverseTransferEvent(transferingAccount.getUser().getContactInfo(),transferingAccount.getUser().getName(),receivingAccount.getAccountNumber(),transferingAccount.getAccountNumber(),transfers.getAmount()));
        return toTransfersDTO(transfers);
    }
    public TransfersDTO findTransferById(int transferId,int userId ){
        log.info("Searching for transaction with Id {} and user Id {}",transferId,userId);
        Transfers transfers=transfersRepository.findByIdAndTransferringAccountUserId(transferId,userId);
        if(transfers==null){
            throw new ResourceNotFoundException("Transfer with ID "+transferId+
                    " and user Id "+userId+" not found");

        }
        log.info("Found transfers");
        return toTransfersDTO(transfers);
    }
    public List<TransfersDTO> findAllReversed(int pageNumber, int size){
        log.info("Searching for all Reversed Transfers");
        Page<Transfers>transfers=transfersRepository.findByReversed("True",PageRequest.of(pageNumber,size));
        List<TransfersDTO>transfersDTOS=new ArrayList<>();
        for(Transfers t:transfers){
            transfersDTOS.add(toTransfersDTO(t));

        }
        return transfersDTOS;
    }
    public List<TransfersDTO>findAllTransfers(int pageNumber,int size) {
        log.info("Searching for all reversed transfers");
        Page<Transfers>transfers=transfersRepository.findAll(PageRequest.of(pageNumber,size));
        List<TransfersDTO>transfersDTOS=new ArrayList<>();
        for(Transfers t:transfers){
           transfersDTOS.add( toTransfersDTO(t));
        }
        return transfersDTOS;
    }
    public List<TransfersDTO>findAllTransactionsByUsers(int userId,int pageNumber, int size){
        log.info("Searching for all transactions for user Id "+userId);
        Page<Transfers> transfers=transfersRepository.findByTransferringAccountUserId(userId, PageRequest.of(pageNumber,size));
        if(transfers.isEmpty()){
            throw new ResourceNotFoundException("Transfers with userId "+userId+" not found");
        }
        List<TransfersDTO>transfersDTOS=new ArrayList<>();
        log.info("Transactions found");
        for(Transfers t:transfers){
            transfersDTOS.add(toTransfersDTO(t));
        }
        return transfersDTOS;
    }
@Transactional
    public TransfersDTO createScheduledTransfer(int year,int month,int day,int userId,int receivingAccountId,int sendingAccountId,double amount){
        LocalDate date=LocalDate.of(year,month,day);
        Account sendingAccount=accountRepository.findByIdAndUserId(sendingAccountId,userId);
        Account receivingAccount=accountRepository.findById(receivingAccountId).orElse(null);
        if(sendingAccount==null){
            return null;
        }
        if(receivingAccount==null){
            return null;
        }
        ScheduledTransfer scheduledTransfer=new ScheduledTransfer();
        scheduledTransfer.setTransferringAccount(sendingAccount);
        scheduledTransfer.setAmount(amount);
        scheduledTransfer.setLocalDate(date);
        scheduledTransfer.setReceivingAccount(receivingAccount);
        scheduledTransferRepository.save(scheduledTransfer);

        Transfers transfers=new Transfers();
        transfers.setReceivingAccount(receivingAccount);
        transfers.setAmount(amount);
        transfers.setTransferringAccount(sendingAccount);
        transfers.setLocalDate(date);
        transfersRepository.save(transfers);
        return toTransfersDTO(transfers);


    }
    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void MakeScheduledTransfer(){
        List<ScheduledTransfer> scheduledTransfers=scheduledTransferRepository.findByCompleted("False");
        for(int x=0;x<scheduledTransfers.size();x++){
            if(scheduledTransfers.get(x).getLocalDate().equals(LocalDate.now())){
                log.info("Processing scheduled transfers");
                Account sendingAccount=accountRepository.findById(scheduledTransfers.get(x).getTransferringAccount().getId()).orElse(null);
                Account receivingAccount=accountRepository.findById(scheduledTransfers.get(x).getReceivingAccount().getId()).orElse(null);
                double amount=scheduledTransfers.get(x).getAmount();
                if(sendingAccount.getBalance()<amount){
                    throw new InsufficientFundsException(" account with Id "+sendingAccount.getId()+" has insufficient funds");
                }
                sendingAccount.setBalance(sendingAccount.getBalance()-amount);
                receivingAccount.setBalance(receivingAccount.getBalance()+amount);
                accountRepository.save(sendingAccount);
                accountRepository.save(receivingAccount);
                scheduledTransfers.get(x).setCompleted("True");
                scheduledTransferRepository.save(scheduledTransfers.get(x));
                log.info("Publishing new Transfer Event");
                publisher.publishEvent(new TransferEvent(sendingAccount.getUser().getContactInfo(),sendingAccount.getUser().getName(),receivingAccount.getAccountNumber(),sendingAccount.getAccountNumber(),amount));
            }

        }
    }


}
