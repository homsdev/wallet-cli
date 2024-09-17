package com.homsdev.account_manager_cli.command.ui;

import org.jline.terminal.Terminal;
import org.springframework.shell.component.ConfirmationInput;
import org.springframework.shell.component.ConfirmationInput.ConfirmationInputContext;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConfirmationInputComponent extends AbstractShellComponent {

    private final Terminal terminal;

    public Boolean readUserConfirmation(Boolean defaultValue, String question) {
        ConfirmationInput component = new ConfirmationInput(terminal, question, defaultValue);
        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());
        ConfirmationInputContext context = component.run(ConfirmationInputContext.empty());
        return context.getResultValue();
    }
}
