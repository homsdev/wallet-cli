package com.homsdev.account_manager_cli.command.ui;

import org.jline.terminal.Terminal;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageDisplayComponent {
    
    private final String SUCCESS_MESSAGE = "\033[32m";
    private final String ERROR_MESSAGE = "\033[0;31m";
    private final String BOLD = "\033[1m";
    private final String RESET = "\033[0m";

    private final Terminal terminal;
    
    public void printErrorMessage(String text) {
        StringBuilder msg = new StringBuilder();
        msg.append(BOLD);
        msg.append(ERROR_MESSAGE);
        msg.append("Error: ");
        msg.append(text);
        msg.append(RESET);
        terminal.writer().println(msg);
        terminal.flush();
    }

    public void printInfoMessage(String text) {
        StringBuilder msg = new StringBuilder();
        msg.append(text);
        terminal.writer().println(msg);
        terminal.flush();
    }

    public void printSuccessMessage(String text) {
        StringBuilder msg = new StringBuilder();
        msg.append(SUCCESS_MESSAGE);
        msg.append("Successful operation!: ");
        msg.append(text);
        msg.append(RESET);

        terminal.writer().println(msg);
        terminal.flush();
    }
}
