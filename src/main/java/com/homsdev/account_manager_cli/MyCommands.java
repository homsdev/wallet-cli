package com.homsdev.account_manager_cli;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class MyCommands {

    @ShellMethod(key = "hello-world")
    public String helloWorld(){
        return "Hello World";
    }

    @ShellMethod(key = "hello-bye")
    public String bye(){
        return "Bye";
    }
}
