package com.finartz.intern.campaignlogic.service;

import com.finartz.intern.campaignlogic.commons.Converters;
import com.finartz.intern.campaignlogic.model.entity.AccountEntity;
import com.finartz.intern.campaignlogic.security.JwtTokenProvider;
import com.finartz.intern.campaignlogic.model.request.LoginRequest;
import com.finartz.intern.campaignlogic.model.request.RegisterRequest;
import com.finartz.intern.campaignlogic.model.response.LoginResponse;
import com.finartz.intern.campaignlogic.model.response.RegisterResponse;
import com.finartz.intern.campaignlogic.model.value.Role;
import com.finartz.intern.campaignlogic.repository.AccountRepository;
import com.finartz.intern.campaignlogic.security.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("accountService")
public class AccountServiceImpl implements  AccountService, UserDetailsService {
  private final JwtTokenProvider jwtTokenProvider;
  private final AccountRepository accountRepository;

  @Autowired
  public AccountServiceImpl(AccountRepository accountRepository, JwtTokenProvider jwtTokenProvider) {
    this.accountRepository = accountRepository;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public RegisterResponse addUser(RegisterRequest request) {
    if (accountRepository.existsByEmail(request.getEmail())){
      throw new RuntimeException("User Account Already Exists");
    }

    AccountEntity accountEntity = accountRepository.save(Converters.registerRequestToUserEntity(request));

    //TODO create cart
    String cartId = "null-cart-id";

    return RegisterResponse.builder()
        .id(accountEntity.getAccountId().toString())
        .name(accountEntity.getName())
        .lastName(accountEntity.getLastName())
        .token(generateToken(accountEntity.getAccountId().toString(), cartId, Role.USER))
        .build();
  }

  @Override
  public LoginResponse loginUser(LoginRequest request) {
    Optional<AccountEntity> optionalUserEntity = accountRepository.findByEmailAndPassword(request.getEmail(), Utils.encrypt(request.getPassword()));

    if (!optionalUserEntity.isPresent()){
      throw new ApplicationContextException("User Not Found");
    }

    //TODO create cart
    String cartId = "null-cart-id";

    LoginResponse loginResponse = Converters.accountToLoginResponse(optionalUserEntity.get());
    loginResponse.setToken(generateToken(optionalUserEntity.get().getAccountId().toString(), cartId, optionalUserEntity.get().getRole()));

    return loginResponse;
  }

  @Override
  public RegisterResponse addSellerAccount(RegisterRequest request) {
    if (accountRepository.existsByEmail(request.getEmail())){
      throw new RuntimeException("Seller Account Already Exists");
    }

    AccountEntity accountEntity = accountRepository.save(Converters.registerSellerRequestToUserEntity(request));

    //TODO create cart
    String cartId = "null-cart-id";

    return RegisterResponse.builder()
        .id(accountEntity.getAccountId().toString())
        .name(accountEntity.getName())
        .lastName(accountEntity.getLastName())
        .token(generateToken(accountEntity.getAccountId().toString(), cartId, Role.SELLER))
        .build();
  }


  private String generateToken(String userId, String cartId, Role role) {
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
    //authorities.add(new SimpleGrantedAuthority(role.name()));
    //roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));
    return jwtTokenProvider.generateToken(userId, cartId, authority);
  }

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    //AccountEntity userEntity = userRepository.findByEmail(email);
    Optional<AccountEntity> userEntity = accountRepository.findById(userId);
    if (!userEntity.isPresent()) {
      try {
        throw new Exception("AccountEntity Not Found");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return new org.springframework.security.core.userdetails.User(userEntity.get().getEmail(), userEntity.get().getPassword(), getAuthority(userEntity.get()));
  }

  private Set getAuthority(AccountEntity accountEntity) {
    Set authorities = new HashSet<>();
    //accountEntity.getRole().getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));
    authorities.add(accountEntity.getRole());
    return authorities;
  }
}
