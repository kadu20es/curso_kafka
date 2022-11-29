package br.com.alura.ecommerce;

public class Email {
    final String subject, body;

    public Email(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    /*
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Email.class)
                .add("Subject: ", subject)
                .add("Body: ", body)
                .toString();
    }
    */

}
