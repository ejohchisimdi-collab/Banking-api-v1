package com.chisimdi.Banking;

import com.chisimdi.Banking.exceptions.ExistsException;
import com.chisimdi.Banking.exceptions.InvalidCredentialsException;
import com.chisimdi.Banking.exceptions.ResourceNotFoundException;
import com.chisimdi.Banking.models.LoginResponse;
import com.chisimdi.Banking.models.User;
import com.chisimdi.Banking.models.UserDTO;
import com.chisimdi.Banking.repositories.UserRepository;
import com.chisimdi.Banking.services.JwtsUtilService;
import com.chisimdi.Banking.services.UserService;
import com.chisimdi.Banking.services.events.UserCreationEvent;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.xml.transform.Result;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.assertj.core.api.InstanceOfAssertFactories.optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    ApplicationEventPublisher publisher;

    @Mock
    JwtsUtilService jwtsUtilService;

    @InjectMocks
    private UserService userService;


    @Test
    void Register_user_Test(){
        User beforeSavingUser= new User();
        beforeSavingUser.setUserName("Chisimdi");
        beforeSavingUser.setContactInfo("@gmail.com");
        beforeSavingUser.setName("Simdi");

        User afterSavingUser=new User();
        afterSavingUser.setId(1);
        afterSavingUser.setUserName("Chisimdi");
        afterSavingUser.setContactInfo("@gmail.com");
        afterSavingUser.setName("Simdi");

        when(userRepository.save(any(User.class))).thenReturn((afterSavingUser));


        UserDTO result=userService.RegisterUsers(beforeSavingUser);
        assertThat(result.getId()).isEqualTo(afterSavingUser.getId());
        assertThat(result.getName()).isEqualTo(afterSavingUser.getName());
        assertThat (result).isNotNull();

        verify(userRepository).save(any(User.class));
        verify(publisher).publishEvent(any(UserCreationEvent.class));
    }

    @Test
    void registering_UserShouldThrowExistsException(){
        User user=new User();
        user.setUserName("Chisimdi");

        when(userRepository.existsByUserName(user.getUserName())).thenReturn(true);

        assertThatThrownBy(()->userService.RegisterUsers(user)).isInstanceOf(ExistsException.class);

        verify(userRepository,never()).save(any(User.class));
        verify(publisher,never()).publishEvent(any(UserCreationEvent.class));
    }
    @Test
    void loginTest(){
        String userName="Chisimdi";
        String passWord="Ejoh";
        int userId=1;
        User user= new User();
        user.setUserName(userName);
        user.setPassword(passWord);
        user.setRoles("Customer");
        user.setId(1);
        String token= jwtsUtilService.generateToken(userId,userName,user.getRoles());
        when(userRepository.findByUserName("Chisimdi")).thenReturn((user));
        when(bCryptPasswordEncoder.matches(passWord,user.getPassword())).thenReturn(true);
when(jwtsUtilService.generateToken(userId,userName,user.getRoles())).thenReturn(token);
when(jwtsUtilService.extractUserName(token)).thenReturn(userName);
when(jwtsUtilService.extractUserId(token)).thenReturn(userId);
when(jwtsUtilService.extractRoles(token)).thenReturn(user.getRoles());
        LoginResponse loginResponse=userService.logIn(userName,passWord);

assertThat(loginResponse.getToken()).isEqualTo(token);
        assertThat(loginResponse.getUserId()).isEqualTo(user.getId());
        assertThat(loginResponse.getUserName()).isEqualTo(userName);
        assertThat(loginResponse.getRole()).isEqualTo(user.getRoles());

        verify(userRepository).findByUserName(userName);
        verify(bCryptPasswordEncoder).matches(passWord,user.getPassword());
    }
    @Test
    void loginTest_throwsResourceNotFoundException(){
        String userName="chisimdi";
        String password="2020";
        User user=new User();

        when(userRepository.findByUserName(userName)).thenReturn(null);

        assertThatThrownBy(()->userService.logIn(userName,password)).isInstanceOf(ResourceNotFoundException.class);
        verify(bCryptPasswordEncoder,never()).matches(password,user.getPassword());
        verify(jwtsUtilService,never()).generateToken(user.getId(),user.getUserName(),user.getRoles());

    }
    @Test
    void loginTest_throwsInvalidCredentialsException(){
        String userName="chisimdi";
        String password="2020";
        User user=new User();

        when(userRepository.findByUserName(userName)).thenReturn(user);
        when(bCryptPasswordEncoder.matches(password,user.getPassword())).thenReturn(false);

        assertThatThrownBy(()->userService.logIn(userName,password)).isInstanceOf(InvalidCredentialsException.class);

        verify(jwtsUtilService,never()).generateToken(user.getId(),user.getUserName(),user.getRoles());
    }
}
