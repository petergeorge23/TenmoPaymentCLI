package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    public final String BASE_URL = "http://localhost:8080/transfer";
    public RestTemplate restTemplate = new RestTemplate();

    private String token = null;

    public void setToken(String token) {
        this.token = token;
    }

    public Transfer [] getAllTransfers() {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(BASE_URL + "s", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;

    }

    public Transfer createTransfer(Transfer newTransfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(newTransfer);
        Transfer returnedTransfer = null;
        try {
            returnedTransfer = restTemplate.postForObject(BASE_URL, entity, Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnedTransfer;
    }

    public Transfer getTransferById(int id) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.GET, makeAuthEntity(), Transfer.class);
           transfer= response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public boolean updateBalance(Transfer transfer){
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        boolean success = false;
        try{
            restTemplate.put("http://localhost:8080/accounts/balance", entity);
            success = true;
        } catch(RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(transfer, headers);
    }
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
