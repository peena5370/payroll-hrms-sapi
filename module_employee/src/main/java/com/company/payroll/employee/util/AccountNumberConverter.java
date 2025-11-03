package com.company.payroll.employee.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AccountNumberConverter implements AttributeConverter<String, String> {

    private final AccountNumberCipher cipher;

    public AccountNumberConverter(AccountNumberCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public String convertToDatabaseColumn(String plainAccountNumber) {
        if (plainAccountNumber == null) {
            return null;
        }

        return cipher.encrypt(plainAccountNumber);
    }

    @Override
    public String convertToEntityAttribute(String encryptedAccountNumber) {
        if (encryptedAccountNumber == null) {
            return null;
        }

        return cipher.decrypt(encryptedAccountNumber);
    }
}
