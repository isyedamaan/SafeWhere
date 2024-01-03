package com.cyk29.safewhere.sosmodule;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    public void sendEmail(String toEmail, String subject, String message) {
        try {

            final String stringSenderEmail = "safe.where.app@gmail.com";
            final String stringReceiverEmail = toEmail;
            final String stringPasswordSenderEmail = "lbez tsbc skta mkvf";

            final String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.setProperty("mail.transport.protocol", "smtp");
            properties.setProperty("mail.host", stringHost);
            properties.setProperty("mail.smtp.host", stringHost);
            properties.setProperty("mail.smtp.port", "465");
            properties.setProperty("mail.smtp.socketFactory.fallback", "false");
            properties.setProperty("mail.smtp.quitwait", "false");
            properties.setProperty("mail.smtp.socketFactory.port", "465");
            properties.setProperty("mail.smtp.starttls.enable", "true");
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.ssl.enable", "true");
            properties.setProperty("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            stringSenderEmail,
                            stringPasswordSenderEmail
                    );
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            Thread thread = new Thread(() -> {
                try {
                    Transport.send(mimeMessage);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
