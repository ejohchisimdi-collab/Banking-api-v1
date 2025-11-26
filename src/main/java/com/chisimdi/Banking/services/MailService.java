package com.chisimdi.Banking.services;

import com.chisimdi.Banking.models.Notifications;
import com.chisimdi.Banking.repositories.NotificationsRepository;
import com.chisimdi.Banking.services.events.*;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class MailService {
    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private JavaMailSender mailSender;
    private NotificationsRepository notificationsRepository;
    @Value("${spring.mail.from}")
    String from;
    public MailService(JavaMailSender mailSender,NotificationsRepository notificationsRepository){
        this.mailSender=mailSender;
        this.notificationsRepository=notificationsRepository;
    }
    @Async
    public void sendSimpleEmail(String to,String subject,String body){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        simpleMailMessage.setFrom(from);
        mailSender.send(simpleMailMessage);
    }
    @Async
    public void sendMailWithAttachment(String to, String subject, String body, File attachment)throws Exception{
        MimeMessage mimeMailMessage=mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMailMessage,true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.addAttachment(attachment.getName(),attachment);
        mimeMessageHelper.setText(body);
        mailSender.send(mimeMailMessage);

    }

    @TransactionalEventListener
    @Async
    public void userCreationEmail(UserCreationEvent userCreationEvent){
        log.info("Sending email to account with contact info {}",userCreationEvent.getContactInfo());
        sendSimpleEmail(userCreationEvent.getContactInfo(),
                "User Creation",
                "Welcome "+userCreationEvent.getName()+" thank you for registering to our bank, we hope you enjoy our services.");
        log.info("Mail sent successfully");
        log.info("creating notification ");
        Notifications notifications=new Notifications();
        notifications.setRecipient(userCreationEvent.getContactInfo());
        notifications.setSubject("User Creation");
        notifications.setLocalDateTime(LocalDateTime.now());
        notificationsRepository.save(notifications);
        log.info("Notification created successfully");
    }

    @TransactionalEventListener
    @Async
    public void accountCreationEmail(AccountCreationEvent accountCreationEvent){
        log.info("Sending email to account with contact info {}",accountCreationEvent.getContactInfo());
        sendSimpleEmail(accountCreationEvent.getContactInfo(),
                "Account Creation",
                "Hello "+accountCreationEvent.getName()+" you have successfully created an account with account number "+accountCreationEvent.getAccountNumber());
        log.info("Mail sent successfully");
        log.info("creating notification ");
        Notifications notifications=new Notifications();
        notifications.setSubject("Account Creation");
        notifications.setRecipient(accountCreationEvent.getContactInfo());
        notifications.setLocalDateTime(LocalDateTime.now());
        notificationsRepository.save(notifications);
        log.info("Notification created successfully");
    }
    @TransactionalEventListener
    @Async
    public void depositEvent(DepositEvent depositEvent){
        log.info("Sending email to account with contact {}",depositEvent.getContactInfo());
        sendSimpleEmail(depositEvent.getContactInfo(), "New Deposit",
                "Hello "+depositEvent.getName()+" you recently made a deposit to your account with account number "+depositEvent.getAccountNumber()+ "amount: "+ depositEvent.getAmount());
        Notifications notifications=new Notifications();
        log.info("Mail sent successfully");
       log.info("creating notification ");
        notifications.setSubject("New Deposit");
        notifications.setRecipient(depositEvent.getContactInfo());
        notificationsRepository.save(notifications);
        log.info("Notification created successfully");


    }
    @TransactionalEventListener
    @Async
    public void withdrawalEvent(WithdrawalEvent withdrawalEvent){
        log.info("Sending mail to account with contact {}",withdrawalEvent.getContactInfo());
        sendSimpleEmail(withdrawalEvent.getContactInfo(), "New Withdrawal",
                "Hello "+withdrawalEvent.getName()+ "you recently made a withdrawal of "+withdrawalEvent.getAmount()+" from account with amount "+withdrawalEvent.getAmount()+" from "+withdrawalEvent.getAccountNumber());
        log.info("Mail sent successfully");
        log.info("Creating notification");
        Notifications notifications=new Notifications();
    notifications.setRecipient(withdrawalEvent.getContactInfo());
    notifications.setSubject("New Withdrawal");
    notificationsRepository.save(notifications);
    log.info("Notification created successfully");
    }
    @TransactionalEventListener
    @Async
    public void transferEvent(TransferEvent transferEvent){
        log.info("Sending mail to account with contact {}",transferEvent.getContactInfo());
        sendSimpleEmail(transferEvent.getContactInfo(),"New Transfer",
                "Hello "+transferEvent.getName()+"you recently transferred money from " +
                        "your account with number  "+transferEvent.getSendersAccountNUmber()+ " to another account with number "+transferEvent.getReceiversAccountNumber()+" amount :"+transferEvent.getAmount());
        log.info("Mail sent successfully");
        log.info("Creating notifications");
        Notifications notifications=new Notifications();
        notifications.setSubject("Transfer");
        notifications.setRecipient(transferEvent.getContactInfo());
        notificationsRepository.save(notifications);
        log.info("notification saved successfully");
    }
    @Async
    @TransactionalEventListener
    public void reverseTransferEvent(ReverseTransferEvent reverseTransferEvent){
       log.info("Sending reverse transfer mail to contact {}",reverseTransferEvent.getContactInfo());
        sendSimpleEmail(reverseTransferEvent.getContactInfo(),"Transfer Reversal","Hello "+reverseTransferEvent.getName()+" Your transfer to account "+reverseTransferEvent.getReceiversAccountNumber()+" from "+reverseTransferEvent.getReceiversAccountNumber()+"was reversed");
        log.info("mail sent successfully");
        log.debug("Creating notifications");
        Notifications notifications=new Notifications();
        notifications.setSubject("Transfer Reversal");
        notifications.setRecipient(reverseTransferEvent.getContactInfo());
        log.info("Notifications saved successfully");
        notificationsRepository.save(notifications);

    }
    @TransactionalEventListener
    @Async
    public void branchCreationEvent(BranchRegistrationEvent branchRegistrationEvent){
       log.info("Sending email to user with contact {}",branchRegistrationEvent.getContactInfo());
        sendSimpleEmail(branchRegistrationEvent.getContactInfo(), "Branch Registration",
                "Hello "+branchRegistrationEvent.getName()+" thank you for registering to our branch with Id "+branchRegistrationEvent.getBranchId()+" we hope you enjoy our services");
        log.info("mail sent successfully creating notification");
        Notifications notifications=new Notifications();
        notifications.setRecipient(branchRegistrationEvent.getContactInfo());
        notifications.setSubject("Branch Registration Event");
    notificationsRepository.save(notifications);
    }
    @TransactionalEventListener
    @Async
    public void branchAssignmentEvent(BranchAssignmentEvent branchAssignmentEvent) {
        log.info("Sending mail to user with contact {}",branchAssignmentEvent.getContactInfo());
        sendSimpleEmail(branchAssignmentEvent.getContactInfo(), "Branch Assignment",
                "Hello " + branchAssignmentEvent.getName() + " you have been assigned to one of or branches with Id " + branchAssignmentEvent.getBranchId() + ". We hope you enjoy working with us");
log.info("Mail sent successfully, creating notifications ");
        Notifications notifications = new Notifications();
        notifications.setSubject("Branch Assignment");
        notifications.setRecipient(branchAssignmentEvent.getContactInfo());
        notificationsRepository.save(notifications);
    }
    public Page<Notifications> findAllNotifications(int pageNumber,int size){
        log.info("searching all notifications");
        return notificationsRepository.findAll(PageRequest.of(pageNumber,size));
    }
    @TransactionalEventListener
    @Async
    public void LoanCreationEvent(LoansCreationEvent loansCreationEvent){

        sendSimpleEmail(loansCreationEvent.getContactInfo(), "Loan Creation",
                "Hello dear customer a loan  of type "+loansCreationEvent.getType()+" with your account number "+loansCreationEvent.getAccountNumber()+" has been created with amount "+loansCreationEvent.getAmount());

  Notifications notifications=new Notifications();
  notifications.setRecipient(loansCreationEvent.getContactInfo());
  notifications.setSubject("Loan creation");
notificationsRepository.save(notifications);
    }


    @TransactionalEventListener
    @Async
    public void loanPaymentEvent(LoanPaymentEvent loanPaymentEvent){
        sendSimpleEmail(loanPaymentEvent.getContactInfo(), "Loan payment",
                "Hello "+loanPaymentEvent.getName()+" You have successfully payed "+loanPaymentEvent.getAmount()+
                        " towards your current loan. YOur current remaining amount to pay is " +loanPaymentEvent.getRemaining());
    Notifications notifications=new Notifications();
    notifications.setRecipient(loanPaymentEvent.getContactInfo());
    notifications.setSubject("Loan Payment");
    notificationsRepository.save(notifications);
    }
@TransactionalEventListener
    @Async
    public void LoanDueDate(DueDateEvent dueDateEvent){
        if(dueDateEvent.getStatus().equalsIgnoreCase("Pending")){
            sendSimpleEmail(dueDateEvent.getContactInfo(), "Loan Still pending",
                    "Hello "+dueDateEvent.getName()+" you loan payment for this month is still pending. Due to this you will be charged an addition late fee. thank you  ");
        }
        if(dueDateEvent.getStatus().equalsIgnoreCase("Paid")){
            sendSimpleEmail(dueDateEvent.getContactInfo(), "Paid Loan","Hello "+dueDateEvent.getName()+" your laon payment for this month has been completed. Thank you for sticking to time ");
        }
        Notifications notifications=new Notifications();
        notifications.setSubject(dueDateEvent.getStatus()+" loan");
        notifications.setRecipient(dueDateEvent.getContactInfo());
        notificationsRepository.save(notifications);
}
@TransactionalEventListener
    @Async
    public void monthlyReport(EndOfMonthReportEvent event)throws Exception{
        sendMailWithAttachment(event.getContactInfo(), "End of month Report","Hello "+event.getName()+" here is your end of month report for your account with number "+event.getAccountNumber(),event.getFile());
Notifications notifications=new Notifications();
notifications.setRecipient(event.getContactInfo());
notifications.setSubject("End of month report");
notificationsRepository.save(notifications);
    }
}
