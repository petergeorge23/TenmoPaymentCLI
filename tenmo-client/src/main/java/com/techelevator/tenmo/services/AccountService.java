package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    public final String BASE_URL = "http://localhost:8080/";
    public RestTemplate restTemplate = new RestTemplate();


    private String token = null;

    public void setToken(String token) {
        this.token = token;
    }

    public BigDecimal getBalance() {
        BigDecimal balance = new BigDecimal(0);
        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(BASE_URL + "accounts/balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }
    public User[] getAllUsers(){
        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(BASE_URL+ "users", HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

//    public boolean updateBalance(Transfer transfer){
//        HttpEntity<Transfer> entity =
//    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(account, headers);
    }
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
