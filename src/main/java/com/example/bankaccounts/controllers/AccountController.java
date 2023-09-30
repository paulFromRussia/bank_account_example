package com.example.bankaccounts.controllers;

import com.example.bankaccounts.dto.input.InputAccount;
import com.example.bankaccounts.dto.input.InputDepositAccount;
import com.example.bankaccounts.dto.input.InputTransferAccount;
import com.example.bankaccounts.dto.output.OutputAccount;
import com.example.bankaccounts.mappers.AccountMapper;
import com.example.bankaccounts.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<OutputAccount>> getAccounts() {
        return new ResponseEntity<>(accountService.findAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/deposit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OutputAccount> deposit(@PathVariable("id") String accountId, @RequestBody InputDepositAccount inputAccount) {
        return new ResponseEntity<>(accountService.deposit(accountId, inputAccount), HttpStatus.OK);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<OutputAccount> withdraw(@PathVariable("id") String accountId, @RequestBody InputDepositAccount inputAccount) {
        return new ResponseEntity<>(accountService.withdraw(accountId, inputAccount), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<OutputAccount> transfer(@PathVariable("id") String accountId, @RequestBody InputTransferAccount inputAccount) {
        return new ResponseEntity<>(accountService.transfer(accountId, inputAccount), HttpStatus.OK);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OutputAccount> create(@RequestBody InputAccount inputAccount) {
        OutputAccount outputAccount = accountService.create(inputAccount);
        return new ResponseEntity<>(outputAccount, HttpStatus.CREATED);
    }
}
