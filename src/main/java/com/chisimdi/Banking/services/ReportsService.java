package com.chisimdi.Banking.services;

import com.chisimdi.Banking.exceptions.ResourceNotFoundException;
import com.chisimdi.Banking.models.*;
import com.chisimdi.Banking.repositories.*;
import com.chisimdi.Banking.services.events.EndOfMonthReportEvent;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class ReportsService {
    private static final Logger log = LoggerFactory.getLogger(ReportsService.class);
    private ApplicationEventPublisher publisher;
    private TransfersRepository transfersRepository;
    private LoansRepository loansRepository;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private MonthlyReportCheckerRepository monthlyReportCheckerRepository;

    public ReportsService(ApplicationEventPublisher publisher, TransfersRepository transfersRepository, LoansRepository loansRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, MonthlyReportCheckerRepository monthlyReportCheckerRepository) {
        this.publisher = publisher;
        this.loansRepository = loansRepository;
        this.transfersRepository = transfersRepository;
        this.accountRepository = accountRepository;
        this.monthlyReportCheckerRepository = monthlyReportCheckerRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public ResponseEntity<InputStreamResource> generateAccountReport(int userId, int accountId, int month, int year) throws Exception {
        log.info("Creating reports for account with userId {} and account Id {}", userId, accountId);
        Account account = accountRepository.findByIdAndUserId(accountId, userId);
        if (account == null) {
            throw new ResourceNotFoundException("account not found");
        }
        LocalDate localDate = LocalDate.of(year, month, 1);
        JSONObject object = new JSONObject();
        object.put("Account number", account.getAccountNumber());
        object.put("Balance", account.getBalance());
        List<Transactions> transactions = transactionRepository.findByAccountUserId(account.getUser().getId());
        JSONArray loanArray = new JSONArray();
        JSONArray transactionsArray = new JSONArray();
        JSONArray transferArray = new JSONArray();

        log.debug("fetching transactions");
        for (Transactions t : transactions) {
            if (t.getLocalDate().getMonthValue() == localDate.getMonthValue() && t.getLocalDate().getYear() == localDate.getYear()) {
                JSONObject transactionsOBJ = new JSONObject();
                transactionsOBJ.put("Amount", t.getAmount());
                transactionsOBJ.put("Type", t.getType());
                transactionsOBJ.put("Date", t.getLocalDate().toString());
                transactionsArray.put(transactionsOBJ);
            }
        }
        object.put("Transactions", transactionsArray);
        List<Transfers> transfers = transfersRepository.findByTransferringAccountUserId(account.getUser().getId());
        log.debug("Searching transfers");
        for (Transfers t : transfers) {
            if (t.getLocalDate().getMonthValue() == localDate.getMonthValue() && t.getLocalDate().getYear() == localDate.getYear()) {
                JSONObject transferOBJ = new JSONObject();
                transferOBJ.put("Receiving Account", t.getReceivingAccount().getId());
                transferOBJ.put("Transferring Account",t.getTransferringAccount().getId());
                transferOBJ.put("Amount", t.getAmount());
                transferOBJ.put("Date", t.getLocalDate().toString());
                transferArray.put(transferOBJ);
            }
        }
        object.put("Transfers", transferArray);
        log.debug("Searching loans");
        List<Loans> loans = loansRepository.findByAccountUserId(account.getUser().getId());
        for (Loans t : loans) {
            JSONObject loansOBJ = new JSONObject();
            loansOBJ.put("Type", t.getType());
            loansOBJ.put("Amount left", t.getAmountWithInterest());
            loansOBJ.put("Status", t.getStatus());
            loanArray.put(loansOBJ);
        }
        object.put("Loans", loanArray);
        log.debug("Creating new file");
        File file = new File("account-" + account.getId() + "-report.json");
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(object.toString(4));
        }
        log.debug("Making file downloadable");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" +
                                file.getName()).contentType(MediaType.APPLICATION_JSON).
                contentLength(file.length()).body(resource);
    }


    @Transactional
    @Scheduled(cron = "0 0 * L * * ")
    public void sendReports() throws Exception {
        LocalDate localDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        if (LocalDate.now().isEqual(localDate)) {
            log.debug("Craeting reports for all accounts");
            List<Account> accounts = accountRepository.findAll();
            for (Account account : accounts) {
                if (monthlyReportCheckerRepository.existsByLocalDateAndAccountId(LocalDate.now(), account.getId())) {
                    continue;
                }

                    JSONObject object = new JSONObject();
                    object.put("Account number", account.getAccountNumber());
                    object.put("Balance", account.getBalance());
                    List<Transactions> transactions = transactionRepository.findByAccountUserId(account.getUser().getId());
                    JSONArray transactionsArray=new JSONArray();
                    JSONArray loansArray=new JSONArray();
                    JSONArray transferArray=new JSONArray();

                    log.debug("fetching transactions");
                    for (Transactions t : transactions) {
                        if (t.getLocalDate().getMonthValue() == localDate.getMonthValue() && t.getLocalDate().getYear() == localDate.getYear()) {
                        JSONObject transactionsOBJ=new JSONObject();
                            transactionsOBJ.put("Amount", t.getAmount());
                            transactionsOBJ.put("Type", t.getType());
                            transactionsOBJ.put("Date", t.getLocalDate());
                            transactionsArray.put(transactionsOBJ);
                        }
                    }
                    object.put("Transactions", transactionsArray);
                    List<Transfers> transfers = transfersRepository.findByTransferringAccountUserId(account.getUser().getId());
                    log.debug("Fetching transfers");
                    for (Transfers t : transfers) {
                        if (t.getLocalDate().getMonthValue() == localDate.getMonthValue() && t.getLocalDate().getYear() == localDate.getYear()) {
                            JSONObject transferOBJ=new JSONObject();
                            transferOBJ.put("Receiving Account", t.getReceivingAccount().getId());
                            transferOBJ.put("Transferring Account",t.getTransferringAccount().getId());
                            transferOBJ.put("Amount", t.getAmount());
                            transferOBJ.put("Date", t.getLocalDate());
                            transferArray.put(transferOBJ);
                        }
                    }
                    object.put("Transfer", transferArray);
                    log.debug("Searching loans");
                    List<Loans> loans = loansRepository.findByAccountUserId(account.getUser().getId());
                    for (Loans t : loans) {
                        JSONObject loansOBJ=new JSONObject();
                        loansOBJ.put("Type", t.getType());
                        loansOBJ.put("Amount left", t.getAmountWithInterest());
                        loansOBJ.put("Status", t.getStatus());
                        loansArray.put(loansOBJ);
                    }
                    object.put("Loans", loansArray);
                    File file = new File("Account -" + account.getId() + "-Summary.json");
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(object.toString(4));
                    }
                    log.debug("Creating end of mont Report event");

                    MonthlyReportChecker monthlyReportChecker = new MonthlyReportChecker();
                    monthlyReportChecker.setLocalDate(localDate);
                    monthlyReportChecker.setAccount(account);
                    monthlyReportCheckerRepository.save(monthlyReportChecker);
                    publisher.publishEvent(new EndOfMonthReportEvent(account.getUser().getName(), account.getUser().getContactInfo(), file, account.getAccountNumber()));
                    file.delete();
                }

            }
        }
    }

