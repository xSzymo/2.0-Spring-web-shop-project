package com.shop.others.email;

import com.shop.configuration.ApplicationProperties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;


/*
 * Author : Cheok Yan Cheng
 * source : http://stackoverflow.com/questions/14939607/java-to-send-an-email-via-gmail
 */
public class EmailActions {
    public static Session authorizeWebShopEmail() throws MessagingException {
        Session session = null;
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
        final String username = ApplicationProperties.SHOP_EMAIL;
        final String password = ApplicationProperties.SHOP_EMAIL_PASSWORD;
        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        return session;
    }
}
