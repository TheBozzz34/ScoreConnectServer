package xyz.necrozma.message;

public class Message {
    private int id;
    private int type;
    private String text;

    private String token;

    public Message() {

    }

    // Getter methods (for each field)
    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getToken() { return token; }

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