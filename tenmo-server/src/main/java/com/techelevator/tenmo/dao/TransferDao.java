package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface TransferDao {

    void createTransfer(Transfer transfer);

    List<Transfer> listTransfers(int userId);

    Transfer getTransferByTransferId(int transferId);
}
