package com.example.bankaccounts.services;

import com.example.bankaccounts.dto.input.InputAccount;
import com.example.bankaccounts.dto.input.InputDepositAccount;
import com.example.bankaccounts.dto.input.InputTransferAccount;
import com.example.bankaccounts.dto.output.OutputAccount;
import com.example.bankaccounts.exceptions.*;
import com.example.bankaccounts.mappers.AccountMapper;
import com.example.bankaccounts.models.Account;
import com.example.bankaccounts.repositories.AccountRepository;
import com.example.bankaccounts.utils.AccountDataValidator;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("accountService")
@Repository
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.accountMapper = new AccountMapper();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public OutputAccount deposit(String accountId, InputDepositAccount inputAccount) {
        BigDecimal amount = validateAndGetAmount(inputAccount.getAmount());

        Account account = validateAndGetAccount(accountId, inputAccount.getName(), inputAccount.getPin());
        account.setBalance(account.getBalance().add(amount));
        Account updatedAccount = accountRepository.save(account);
        return accountMapper.accountToDTO(updatedAccount);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public OutputAccount withdraw(String accountId, InputDepositAccount inputAccount) {
        BigDecimal amount = validateAndGetAmount(inputAccount.getAmount());

        Account account = validateAndGetAccount(accountId, inputAccount.getName(), inputAccount.getPin());
        validateAmountForWithdrawal(account.getBalance(), amount);

        account.setBalance(account.getBalance().subtract(amount));
        Account updatedAccount = accountRepository.save(account);
        return accountMapper.accountToDTO(updatedAccount);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public OutputAccount transfer(String accountId, InputTransferAccount inputAccount) {
        BigDecimal amount = validateAndGetAmount(inputAccount.getAmount());

        Account sourceAccount = validateAndGetAccount(accountId, inputAccount.getName(), inputAccount.getPin());
        Account targetAccount = validateAndGetAccount(inputAccount.getTargetAccountId());
        validateAmountForWithdrawal(sourceAccount.getBalance(), amount);

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        Account updatedAccount = accountRepository.save(sourceAccount);

        targetAccount.setBalance(targetAccount.getBalance().add(amount));
        accountRepository.save(sourceAccount);
        return accountMapper.accountToDTO(updatedAccount);
    }

    @Override
    public OutputAccount create(InputAccount createInputAccount) {
        Account account = accountMapper.dtoToAccount(createInputAccount);
        validateUniqueName(createInputAccount.getName());
        return accountMapper.accountToDTO(accountRepository.save(account));
    }

    public List<OutputAccount> findAll() {
        return accountRepository.findAll().stream().map(accountMapper::accountToDTO).collect(Collectors.toList());

    }

    private void validateUniqueName(String accountName) {
        if (accountRepository.findByName(accountName).isPresent()) {
            throw new NotUniqueAccountNameException("not unique account name " + accountName);
        }
    }
    private Account validateAndGetAccount(String accountId, String accountName, String accountPin) {
        long id = validateAndGetAccountId(accountId);
        AccountDataValidator.validateAndGetName(accountName);
        String pin = AccountDataValidator.validateAndGetPin(accountPin);
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isEmpty()) {
            throw new AccountNotFoundException(id);
        }
        if (!accountOptional.get().getPin().equals(pin)) {
            throw new InvalidAccountPinException();
        }
        return accountOptional.get();
    }

    private Account validateAndGetAccount(String accountId) {
        long id = validateAndGetAccountId(accountId);
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isEmpty()) {
            throw new AccountNotFoundException(id);
        }
        return accountOptional.get();
    }

    private Long validateAndGetAccountId(String accountId) {
        long result;
        try {
            result = Long.parseLong(accountId);
        } catch (NumberFormatException e) {
            throw new InvalidAccountIdException(accountId);
        }
        return result;
    }

//    private String validateAndGetAccountName(String accountName) {
//        return AccountDataValidator.validateAndGetName(accountName);
//    }
//
//    private String validateAndGetAccountPin(String accountPin) {
//        return AccountDataValidator.validateAndGetPin(accountPin);
//    }

    private BigDecimal validateAndGetAmount(String amount) {
        return AccountDataValidator.validateAndGetAmount(amount);
    }

    private void validateAmountForWithdrawal(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InvalidAmountException("balance is not enough for withdrawal");
        }
    }
}
