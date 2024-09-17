package com.homsdev.account_manager_cli.command.ui;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.jline.terminal.Terminal;
import org.springframework.shell.component.SingleItemSelector;
import org.springframework.shell.component.SingleItemSelector.SingleItemSelectorContext;
import org.springframework.shell.component.StringInput;
import org.springframework.shell.component.StringInput.StringInputContext;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class InputReadComponent extends AbstractShellComponent {

    private final Terminal terminal;
    private final MessageDisplayComponent messageDisplayComponent;

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
            messageDisplayComponent.printErrorMessage("Invalid date");
        }

        return Optional.ofNullable(inputDate);
    }

    private String readMultipleSelectionInput(String instruction, LinkedHashMap<String, String> items) {

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

}
