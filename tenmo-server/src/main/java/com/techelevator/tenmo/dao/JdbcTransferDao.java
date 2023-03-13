package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;
    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    private AccountDao accountDao;

    @Override
    public void createTransfer(Transfer transfer) {

            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                    "VALUES(?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(),
                    transfer.getAccountTo(), transfer.getAmount());
    }

    @Override
    public List<Transfer> listTransfers(int accountId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql =  "SELECT transfer_id, transfer_status.transfer_status_id, transfer_status_desc, transfer_type.transfer_type_id, transfer_type_desc, account_from, account_to, amount" +
                " FROM transfer JOIN transfer_status ON transfer_status.transfer_status_id = transfer.transfer_status_id" +
                " JOIN transfer_type ON transfer_type.transfer_type_id = transfer.transfer_type_id" +
                " WHERE account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);

        while(results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transferList.add(transfer);
        }
        return transferList;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_status.transfer_status_id, transfer_status_desc, transfer_type.transfer_type_id, transfer_type_desc, account_from, account_to, amount" +
                " FROM transfer JOIN transfer_status ON transfer_status.transfer_status_id = transfer.transfer_status_id" +
                " JOIN transfer_type ON transfer_type.transfer_type_id = transfer.transfer_type_id" +
                " WHERE transfer.transfer_id = ?";

                SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
                if(results.next()){
                    transfer = mapRowToTransfer(results);
                }
        return transfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs){
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferTypeDesc(rs.getString("transfer_type_desc"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setTransferStatusDesc(rs.getString("transfer_status_desc"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
