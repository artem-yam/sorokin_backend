package task.model.command;

public interface ExecutableCommand {

    void execute();

    CommandType getType();
}
