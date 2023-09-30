package com.example.bankaccounts.services;

import com.example.bankaccounts.dto.input.InputAccount;
import com.example.bankaccounts.dto.input.InputDepositAccount;
import com.example.bankaccounts.dto.input.InputTransferAccount;
import com.example.bankaccounts.dto.output.OutputAccount;

import java.util.List;

public interface AccountService {
    OutputAccount deposit(String accountId, InputDepositAccount inputAccount);

    OutputAccount withdraw(String accountId, InputDepositAccount inputAccount);

    OutputAccount transfer(String accountId, InputTransferAccount inputAccount);

    OutputAccount create(InputAccount createAccount);

    List<OutputAccount> findAll();
}
