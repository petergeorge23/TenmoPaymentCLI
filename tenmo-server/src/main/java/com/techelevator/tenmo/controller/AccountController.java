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
public class AccountController {
    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;
    private Principal principal;

    private static final String BASE_URL = "/accounts";

    public AccountController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = BASE_URL + "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        return accountDao.getBalance(accountDao.findAccountIdByUserId(userDao.findIdByUsername(principal.getName())));
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @RequestMapping(path = BASE_URL +  "/balance", method = RequestMethod.PUT)
    public Account updateBalance(Principal principal, @RequestBody Transfer transfer){
        Account account = new Account();
        User user = userDao.getUserById(accountDao.findUserIdByAccountId(transfer.getAccountTo()));

        //updates account of sending user
            BigDecimal currentBalance = accountDao.getBalance((accountDao.findAccountIdByUserId(userDao.findIdByUsername(principal.getName()))));
            if(user != null) {

                currentBalance = currentBalance.subtract(transfer.getAmount());
                account.setBalance(currentBalance);
                accountDao.updateBalance(accountDao.findAccountIdByUserId(userDao.findIdByUsername(principal.getName())), currentBalance);
                //updates account of receiving user
                currentBalance = accountDao.getBalance(transfer.getAccountTo());
                currentBalance = currentBalance.add(transfer.getAmount());
                accountDao.updateBalance(transfer.getAccountTo(), currentBalance);
            }else{
                System.out.println("Invalid user. Please try again");
            }

        return account;
    }



}
