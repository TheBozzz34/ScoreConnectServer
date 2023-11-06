package xyz.necrozma.message;

public class Message {
    private int id;
    private int type;
    private String text;

    public Message() {
        // no-args constructor
    }

    // Getter methods (for each field) can be added here if you want to access the parsed values.
    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setId(int NewId) {
        id = NewId;
    }

    public void setType(int NewType) {
        type = NewType;
    }

    public void setText(String NewText) {
        text = NewText;
    }
}