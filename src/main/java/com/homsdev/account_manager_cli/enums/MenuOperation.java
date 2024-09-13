package com.homsdev.account_manager_cli.enums;

import com.homsdev.account_manager_cli.exceptions.InvalidInputException;

public enum MenuOperation {

    READ("Read"),
    DELETE("Delete"),
    UPDATE("Update");

    private final String name;

    MenuOperation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MenuOperation fromStr(String op) {
        for (MenuOperation operation : MenuOperation.values()) {
            if (op.equalsIgnoreCase(operation.getName())) {
                return operation;
            }
        }

        throw new InvalidInputException("Illegal operation selected");
    }

}
