package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
   int findAccountIdByUserId(int userId);
   int findUserIdByAccountId(int accountId);
   BigDecimal getBalance(int userId);
   void updateBalance(int accountId, BigDecimal currentBalance);


}
