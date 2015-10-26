package br.unisc.pdm.caronauniscapp.chat;

/**
 * @author greg
 * @since 6/21/13
 */
public class Chat {

    private String message;
    private String author;
    private String quando;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Chat() {
    }

    Chat(String message, String author, String quando) {
        this.message = message;
        this.author = author;
        this.quando = quando;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
    public String getQuando() {
        return quando;
    }
    public void setMessage(String msg) {
        message = msg;
    }

    public void setAuthor(String aut) {
        author = aut;
    }
    public void setQuando(String qd) {
        quando = qd;
    }
}
