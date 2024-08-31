package com.homsdev.account_manager_cli.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private String message;
}
