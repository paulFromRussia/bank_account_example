package com.example.bankaccounts.utils;

import com.example.bankaccounts.exceptions.InvalidAccountNameException;
import com.example.bankaccounts.exceptions.InvalidAccountPinException;
import com.example.bankaccounts.exceptions.InvalidAmountException;

import java.math.BigDecimal;

public class AccountDataValidator {
    public static String validateAndGetPin(String inputPin) {
        if (inputPin == null || inputPin.length() != 4) {
            throw new InvalidAccountPinException();
        }
        return inputPin;
    }

    public static String validateAndGetName(String inputName) {
        if (inputName == null || inputName.length() == 0) {
            throw new InvalidAccountNameException("account name is empty");
        }
        return inputName;
    }

    public static BigDecimal validateAndGetAmount(String inputAmount) {
        try {
            BigDecimal result = new BigDecimal(inputAmount);
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                throw new InvalidAmountException("amount value is incorrect");
            }
            return result;
        } catch (Exception e) {
            throw new InvalidAmountException("amount value is incorrect");
        }
    }
}
