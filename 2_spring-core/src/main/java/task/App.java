package task;

import task.config.AppConfig;
import task.console.ConsoleListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            var consoleListener = context.getBean(ConsoleListener.class);
            consoleListener.run();
        }
    }
}