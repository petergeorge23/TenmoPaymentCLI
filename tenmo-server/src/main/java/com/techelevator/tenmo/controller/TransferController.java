package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;
    private Principal principal;

    public TransferController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public void createTransfer(@RequestBody Transfer transfer, Principal principal) {

        if (transfer.getAccountFrom() == transfer.getAccountTo()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You cannot transfer money to yourself.");
        }
        else if (transfer.getAmount().compareTo(accountDao.getBalance(transfer.getAccountFrom())) > 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "There is insufficient funds.");
        } else {
            transferDao.createTransfer(transfer);
        }
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> listTransfers(Principal principal){
        return transferDao.listTransfers(accountDao.findAccountIdByUserId(userDao.findIdByUsername(principal.getName())));
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id){
        return transferDao.getTransferByTransferId(id);
    }
}
