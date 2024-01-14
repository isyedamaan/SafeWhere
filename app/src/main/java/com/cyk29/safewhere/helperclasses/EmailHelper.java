package com.cyk29.safewhere.helperclasses;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Helper class for sending emails.
 */
public class EmailHelper {

    /**
     * Sends an email with the specified recipient, subject, and HTML content.
     *
     * @param toEmail The recipient of the email.
     * @param subject The subject of the email.
     * @param htmlContent The HTML content of the email.
     */
    public void sendEmail(String toEmail, String subject, String htmlContent) {
        try {

            final String stringSenderEmail = "safe.where.app@gmail.com";
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
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            mimeMessage.setFrom(new InternetAddress(stringSenderEmail, "SafeWhere"));
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(htmlContent, "text/html");

            Thread thread = new Thread(() -> {
                try {
                    Transport.send(mimeMessage);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
