package com.homsdev.account_manager_cli.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.component.SingleItemSelector;
import org.springframework.shell.component.StringInput;
import org.springframework.shell.component.SingleItemSelector.SingleItemSelectorContext;
import org.springframework.shell.component.StringInput.StringInputContext;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Component
public class ShellTerminalComponent extends AbstractShellComponent {

    private final Terminal terminal;

    private final String SUCCESS_MESSAGE = "\033[32m";
    private final String ERROR_MESSAGE = "\033[31m";
    private final String RESET = "\033[0m";

    public ShellTerminalComponent(Terminal terminal) {
        this.terminal = terminal;
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

    public void printInfoMessage(String text) {
        StringBuilder msg = new StringBuilder();
        msg.append(text);

        terminal.writer().println(msg);
        terminal.flush();
    }

    public void printErrorMessage(String text) {
        StringBuilder msg = new StringBuilder();
        msg.append(ERROR_MESSAGE);
        msg.append(text);
        msg.append(RESET);
        terminal.writer().println(text);
        terminal.flush();
    }

    public String readSimpleTextInput(String instruction, String defaultValue) {

        StringInput simpleInput = new StringInput(terminal, instruction, defaultValue);

        simpleInput.setResourceLoader(getResourceLoader());
        simpleInput.setTemplateExecutor(getTemplateExecutor());

        StringInputContext context = simpleInput.run(StringInputContext.empty());

        String result = Optional
                .ofNullable(context.getResultValue())
                .map(input -> input.trim())
                .filter(input -> !input.isEmpty())
                .orElseGet(() -> context.getDefaultValue());

        return result;
    }

    public String readMultipleSelectionInput(String instruction, LinkedHashMap<String, String> items) {

        List<SelectorItem<String>> selectionItems = new ArrayList<>();

        items.entrySet().stream().forEach(item -> {
            selectionItems.add(SelectorItem.of(item.getValue(), item.getKey()));
        });

        SingleItemSelector<String, SelectorItem<String>> singleItemSelectorComponent = new SingleItemSelector<>(
                terminal, selectionItems, instruction, null);

        singleItemSelectorComponent.setResourceLoader(getResourceLoader());
        singleItemSelectorComponent.setTemplateExecutor(getTemplateExecutor());

        SingleItemSelectorContext<String, SelectorItem<String>> context = singleItemSelectorComponent
                .run(SingleItemSelectorContext.empty());

        String selectedValue = context.getResultItem()
                .map(SelectorItem::getItem)
                .orElseGet(() -> "");

        return selectedValue;
    }

    public Optional<LocalDate> readDateTypeInput(String instruction) {
        LocalDate now = LocalDate.now();
        String year = readSimpleTextInput("Year:", String.valueOf(now.getYear()));
        LinkedHashMap<String, String> months = new LinkedHashMap<>();
        for (int index = 0; index < Month.values().length; index++) {
            months.put(String.valueOf(index + 1), Month.values()[index].toString());
        }
        String month = readMultipleSelectionInput("Select month", months);

        String day = readSimpleTextInput("Day", "01");

        LocalDate inputDate = null;

        try {
            Integer y = Integer.valueOf(year);
            Integer m = Integer.valueOf(month);
            Integer d = Integer.valueOf(day);
            inputDate = LocalDate.of(y, m, d);
        } catch (DateTimeException e) {
            printErrorMessage("Invalid date");
        }

        return Optional.ofNullable(inputDate);
    }

}
