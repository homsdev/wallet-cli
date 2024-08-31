package com.homsdev.account_manager_cli.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ResourceNotCreatedException extends RuntimeException {
    private String message;
}
