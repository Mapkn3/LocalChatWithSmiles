package Model;

import java.io.Serializable;

public class Message implements Serializable{
    private int authorID;
    private String destination;
    private String message;

    public Message(int authorID, String message, String destination) {
        this.authorID = authorID;
        this.message = message;
        this.destination = destination;
    }

    public String convert() {
        StringBuilder result = new StringBuilder("");
        String[] words = message.split(" ");
        for (String word: words) {
            switch (word) {
                case "*dirol*":
                    result.append("<img src='file:D:\\zzz\\IdeaProjects\\TryLocalChat\\src\\resource\\Kolobok HD Dark skin\\dirol.gif'>");
                    break;
                default:
                    result.append(word);
            }
            result.append(" ");
        }
        return result.toString().trim();
    }

    public int getAuthorID() {
        return authorID;
    }

    public String getDestination() {
        return destination;
    }

    public String getMessage() {
        return message;
    }
}
