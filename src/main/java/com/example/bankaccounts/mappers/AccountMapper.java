package com.example.bankaccounts.mappers;

import com.example.bankaccounts.dto.input.InputAccount;
import com.example.bankaccounts.dto.output.OutputAccount;
import com.example.bankaccounts.models.Account;
import com.example.bankaccounts.utils.AccountDataValidator;

public class AccountMapper {
    public Account dtoToAccount(InputAccount inputAccount) {
        Account result = new Account();
        result.setName(AccountDataValidator.validateAndGetName(inputAccount.getName()));
        result.setPin(AccountDataValidator.validateAndGetPin(inputAccount.getPin()));
        return result;
    }

    public OutputAccount accountToDTO(Account account) {
        OutputAccount result = new OutputAccount();
        if (account != null) {
            if (account.getId() != null) {
                result.setId(account.getId().toString());
            }
            result.setName(account.getName());
            if (account.getBalance() != null) {
                result.setBalance(account.getBalance().toString());
            }
        }
        return result;
    }
}
