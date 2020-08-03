/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.util;

/**
 *
 * @author paola.vargas
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailService {

    private final Properties properties = new Properties();

    private String password;
    InputStream inputStream;
    private Session session;
    private String usuario;
    private String contrasena;
    private String email;

    private void init() throws Exception {
        try {
            getPropValues();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", 587);
            properties.put("mail.smtp.mail.sender", email);
            properties.put("mail.smtp.user", usuario);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.debug", "true");
            session = Session.getDefaultInstance(properties);
        } catch (Exception ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

    }

    public boolean enviarEmail(String mailReceptor, String asunto, String mensaje) {

        try {

            init();
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailReceptor));
            message.setSubject(asunto);
            message.setText(mensaje);
            Transport t = session.getTransport("smtp");
            t.connect((String) properties.get("mail.smtp.user"), contrasena);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            return true;
        } catch (Exception me) {
            System.out.print(me);
            return false;
        }

    }

    public void getPropValues() throws IOException {

        try {
            properties.load(new BufferedReader(new FileReader("config.properties")));
            usuario = properties.getProperty("usuario");
            contrasena = properties.getProperty("contrasena");
            email = properties.getProperty("destinatariomail");

        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }

    }

}
