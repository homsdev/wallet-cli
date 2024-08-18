package com.homsdev.account_manager_cli.utils;

import com.homsdev.account_manager_cli.model.TRANSACTION_TYPE;

public final class TransactionUtils {

    public static TRANSACTION_TYPE transactionTypeParse(String transactionType) {
        for (TRANSACTION_TYPE type : TRANSACTION_TYPE.values()) {
            if (type.name().equalsIgnoreCase(transactionType)) {
                return type;
            }
        }
        System.out.println("No enum constant");
        //throw new IllegalArgumentException("No enum constant " + name);
        return null;
    }

    ;
}
