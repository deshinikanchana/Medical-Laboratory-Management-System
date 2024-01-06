package lk.ijse.laboratory.Util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EmailController {
    public static void SendEmail(String user,String recipient, String subject,String body,int num) throws MessagingException, IOException {
            boolean ok = false;
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.transport.protocol", "smtp");

        String userName = "your email";
        String password = "Your password";
        String msg1 = "VERIFY IT'S YOU";
        String msg2 = "Enter The OTP Code Below Into The Suwasahana Medical Laboratory Management System To Reset Your System User Password.";
        String alert = "SECURITY ALERT !!!";
        String Msg1 = "Someone Tryies To Log Into The Suwasahana Medical Laboratory Management System As A New User.\n\nWe Have Informed You Because You Are The Current Admin Of This System.";
        String Msg2 ="Enter The OTP Code Below Into The Suwasahana Medical Laboratory Management System To Continue Registration Process.";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName,password);
            }
        });

        Message message = new MimeMessage(session);
        message.setSubject(subject);

        Address addressTo = new InternetAddress(recipient);
        message.setRecipient(Message.RecipientType.TO, addressTo);
        MimeBodyPart messageBodyPart = new MimeBodyPart();

        MimeBodyPart attachment = new MimeBodyPart();
        MimeMultipart multipart = new MimeMultipart();
        if(num == 1) {
            messageBodyPart.setText("Hi " + user + ",\n\n\t\t\t\t\t\t\t" + msg1 + "\n\n" + Msg2 + "\n\n\t\t\t\t\t\t\t\t" + body + "\n\nThanks,\nSuwasahana MediLab");
        }else if(num ==2){
            messageBodyPart.setText("Hi " + user + ",\n\n\t\t\t\t\t\t\t" + alert + "\n\n" + Msg1 + "\n\n\t\t\t\t\t\t\t\t" + body + "\n\nThanks,\nSuwasahana MediLab");
        }else if (num == 3) {
            attachment.attachFile(new File(body));
            messageBodyPart.setText("Hi  " + user + ", Here is Your Report");
            multipart.addBodyPart(attachment);

        }else{
            messageBodyPart.setText("Hi " + user + ",\n\n\t\t\t\t\t\t\t" + msg1 + "\n\n" + msg2 + "\n\n\t\t\t\t\t\t\t\t" + body + "\n\nThanks,\nSuwasahana MediLab");
        }
        multipart.addBodyPart(messageBodyPart);


        message.setContent(multipart);

        Transport.send(message);
    }
}
