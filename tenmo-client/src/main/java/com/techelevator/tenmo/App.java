package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();
    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        accountService.setToken(currentUser.getToken());
        transferService.setToken(currentUser.getToken());
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        BigDecimal account = accountService.getBalance();
        System.out.println("Your current account balance is : $" + account);
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
        Transfer[] transfers = transferService.getAllTransfers();
        printTransfersOrError(transfers);

        int id = consoleService.promptForInt("Please enter ID of transfer you would like to see. ");
        printTransferByIdOrError(id);


	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        User[] users = accountService.getAllUsers();
        printUsersOrError(users);

        Transfer newTransfer = new Transfer();
        newTransfer.setAccountFrom(currentUser.getUser().getId());
        newTransfer.setTransferStatusId(2);
        newTransfer.setTransferStatusDesc("Approved");
        newTransfer.setTransferTypeId(2);
        newTransfer.setTransferTypeDesc("Send");


        int id = consoleService.promptForInt("Please enter the id of the user you would like to transfer to: ");
        BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount you would like to transfer: ");

        newTransfer.setAccountTo(id);
        newTransfer.setAmount(amount);

        transferService.createTransfer(newTransfer);

        transferService.updateBalance(newTransfer);


	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

    private void printUsersOrError(User[] users){
        if(users!= null){
            consoleService.printUsers(users);
        }else{
            consoleService.printErrorMessage();
        }
    }

    private void printTransfersOrError(Transfer[] transfers){
        if(transfers!= null){
            consoleService.printTransfers(transfers);
        }else{
            consoleService.printErrorMessage();
        }
    }

    private void printTransferByIdOrError(int id){
        Transfer transfer = transferService.getTransferById(id);
        System.out.println("Transfer ID: " + transfer.getTransferId() + ", Account From: " + transfer.getAccountFrom()
                + ", Account To: " + transfer.getAccountTo() + ", Status: " + transfer.getTransferStatusDesc()
                + ", Type: " + transfer.getTransferTypeDesc() + ", Amount: $" + transfer.getAmount());
        }

}
