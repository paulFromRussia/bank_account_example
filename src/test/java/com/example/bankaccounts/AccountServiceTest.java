package com.example.bankaccounts;

import com.example.bankaccounts.dto.input.InputAccount;
import com.example.bankaccounts.dto.input.InputDepositAccount;
import com.example.bankaccounts.dto.input.InputTransferAccount;
import com.example.bankaccounts.dto.output.OutputAccount;
import com.example.bankaccounts.exceptions.*;
import com.example.bankaccounts.models.Account;
import com.example.bankaccounts.repositories.AccountRepository;
import com.example.bankaccounts.services.AccountService;
import com.example.bankaccounts.services.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;


@DataJpaTest
public class AccountServiceTest {
    @Mock
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    public void withdraw() {
        InputAccount inputAccount = new InputAccount();
        inputAccount.setPin("1111");
        inputAccount.setName("test");
        OutputAccount outputAccount = accountService.create(inputAccount);

        InputDepositAccount inputDepositAccount = new InputDepositAccount();
        inputDepositAccount.setName("test");
        inputDepositAccount.setPin("1111");

        inputDepositAccount.setAmount("-1");
        try {
            accountService.withdraw("1", inputDepositAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAmountException);
        }

        inputDepositAccount.setAmount("xxxx");
        try {
            accountService.withdraw("1", inputDepositAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAmountException);
        }

        inputDepositAccount.setAmount("100");
        inputDepositAccount.setPin("111");
        try {
            accountService.withdraw(outputAccount.getId(), inputDepositAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAccountPinException);
        }

        inputDepositAccount.setAmount("101");
        inputDepositAccount.setPin("1111");
        try {
            accountService.withdraw(outputAccount.getId(), inputDepositAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAmountException);
        }

        inputDepositAccount.setAmount("100");
        Account account = accountRepository.findByName("test").get();
        Assertions.assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    public void transfer() {
        InputAccount createInputSourceAccount = new InputAccount();
        createInputSourceAccount.setPin("1111");
        createInputSourceAccount.setName("source_account");
        OutputAccount outputSourceAccount = accountService.create(createInputSourceAccount);

        InputDepositAccount inputAccount = new InputDepositAccount();
        inputAccount.setName("test");
        inputAccount.setPin("1111");
        inputAccount.setAmount("100");
        accountService.deposit(outputSourceAccount.getId(), inputAccount);

        createInputSourceAccount = new InputAccount();
        createInputSourceAccount.setPin("1111");
        createInputSourceAccount.setName("target_account");
        OutputAccount outputTargetAccount = accountService.create(createInputSourceAccount);

        InputTransferAccount inputTransferAccount = new InputTransferAccount();
        inputTransferAccount.setAmount("-1");
        try {
            accountService.transfer(outputSourceAccount.getId(), inputTransferAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAmountException);
        }

        inputTransferAccount.setAmount("10s");
        try {
            accountService.transfer(outputSourceAccount.getId(), inputTransferAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAmountException);
        }

        inputTransferAccount.setName(outputSourceAccount.getName());
        inputTransferAccount.setPin("1111");
        inputTransferAccount.setAmount("100");
        try {
            accountService.transfer(outputSourceAccount.getId(), inputTransferAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAccountIdException);
        }

        inputTransferAccount.setName(outputSourceAccount.getName());
        inputTransferAccount.setPin("1111");
        inputTransferAccount.setAmount("100");
        inputTransferAccount.setTargetAccountId("0");
        try {
            accountService.transfer(outputSourceAccount.getId(), inputTransferAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof AccountNotFoundException);
        }

        inputTransferAccount.setTargetAccountId(outputTargetAccount.getId());
        inputTransferAccount.setAmount("101");
        try {
            accountService.transfer(outputSourceAccount.getId(), inputTransferAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAmountException);
        }

        inputTransferAccount.setAmount("100");
        accountService.transfer(outputSourceAccount.getId(), inputTransferAccount);
        Account sourceAccount = accountRepository.findByName("source_account").get();
        Account targetAccount = accountRepository.findByName("target_account").get();
        Assertions.assertEquals(BigDecimal.ZERO, sourceAccount.getBalance());
        Assertions.assertEquals(new BigDecimal(100), targetAccount.getBalance());
    }

    @Test
    public void deposit() {
        InputAccount inputAccount = new InputAccount();
        inputAccount.setPin("1111");
        inputAccount.setName("test");
        OutputAccount outputAccount = accountService.create(inputAccount);

        InputDepositAccount inputDepositAccount = new InputDepositAccount();
        inputDepositAccount.setName("test");
        inputDepositAccount.setPin("1111");

        inputDepositAccount.setAmount("-1");
        try {
            accountService.deposit("1", inputDepositAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAmountException);
        }

        inputDepositAccount.setAmount("xxxx");
        try {
            accountService.deposit("1", inputDepositAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAmountException);
        }

        inputDepositAccount.setAmount("100");
        accountService.deposit(outputAccount.getId(), inputDepositAccount);
        Assertions.assertEquals(new BigDecimal(100), accountRepository.findByName("test").get().getBalance());
    }

    @Test
    public void create() {
        InputAccount inputAccount = new InputAccount();
        inputAccount.setPin("1111");
        inputAccount.setName("");
        try {
            accountService.create(inputAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAccountNameException);
        }

        inputAccount.setName(null);
        try {
            accountService.create(inputAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAccountNameException);
        }

        inputAccount.setName("test");
        inputAccount.setPin(null);
        try {
            accountService.create(inputAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAccountPinException);
        }

        inputAccount.setPin("");
        try {
            accountService.create(inputAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAccountPinException);
        }

        inputAccount.setPin("999");
        try {
            accountService.create(inputAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAccountPinException);
        }

        inputAccount.setPin("10000");
        try {
            accountService.create(inputAccount);
            Assertions.fail("exception expected");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidAccountPinException);
        }

        inputAccount.setPin("100f");
        OutputAccount account = accountService.create(inputAccount);
        Assertions.assertNotNull(account);
        Assertions.assertEquals("test", account.getName());
        Assertions.assertEquals("0", account.getBalance());
        Assertions.assertTrue(accountRepository.findByName("test").isPresent());
    }

    @Test
    void getAccounts() {
        List<OutputAccount> outputAccountList = accountService.findAll();
        Assertions.assertTrue(outputAccountList.isEmpty());

        InputAccount inputAccount = new InputAccount();
        inputAccount.setPin("1111");
        inputAccount.setName("account1");
        OutputAccount outputAccount1 = accountService.create(inputAccount);

        inputAccount = new InputAccount();
        inputAccount.setPin("1111");
        inputAccount.setName("account2");
        OutputAccount outputAccount2 = accountService.create(inputAccount);

        outputAccountList = accountService.findAll();
        Assertions.assertEquals(2, outputAccountList.size());
        Assertions.assertEquals("account1", outputAccount1.getName());
        Assertions.assertEquals("account2", outputAccount2.getName());
    }
}
