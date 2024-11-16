package src;

public class Command {

    private final Action action;
    private String data;


    public Command(Action action, String data) {
        this.data = data;
        this.action = action;
    }

    public Command(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public String getData() {
        return data;
    }
}
