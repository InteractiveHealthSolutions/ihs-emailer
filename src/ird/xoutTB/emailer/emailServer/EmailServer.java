package ird.xoutTB.emailer.emailServer;

import ird.xoutTB.emailer.exception.EmailException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class EmailServer {
	
	public enum AttachmentType{
		ZIP("application/zip"),
		PDF("application/pdf"),
		CSV("text/csv"),;
		
		private String type;
		private AttachmentType(String value) {
			type = value;
		}
		
		public String TYPE(){
			return type;
		}
	}
	
	private  String SMTP_HOST_NAME ;
	private  String SMTP_AUTH_USER;
	private  String SMTP_AUTH_PWD;
    private  Authenticator auth =new SMTPAuthenticator();
    private  Properties props = new Properties();

    public Properties getProperties(){
    	return props;
    }
    public Authenticator getAuthenticator(){
    	return auth;
    }
    
public EmailServer(Properties prop) throws EmailException {
	
	String val=prop.getProperty("mail.host");
	if(val==null){
		throw new EmailException(EmailException.NO_MAIL_HOST);
	}
		SMTP_HOST_NAME=val;
		props.setProperty("mail.host", SMTP_HOST_NAME);
		val=prop.getProperty("mail.transport.protocol","smtp");
		props.setProperty("mail.transport.protocol", val);
		val=prop.getProperty("mail.smtp.auth", "true");
		props.put("mail.smtp.auth", val);
		if(val!=null&&val.compareTo("true")==0){
			val=prop.getProperty("mail.user.username");
			if(val==null){
				throw new EmailException(EmailException.NO_USERNAME);
			}
			SMTP_AUTH_USER=val;
			val=prop.getProperty("mail.user.password");
			if(val==null){
				throw new EmailException(EmailException.NO_PASSWORD);
			}
			SMTP_AUTH_PWD=val;
		}
		val=prop.getProperty("mail.smtp.port","465");
		props.put("mail.smtp.port", val);
		props.put("mail.smtp.socketFactory.port", val);
		props.put("mail.smtp.socketFactory.class",
		"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.quitwait", "false");
	}

		public boolean postSimpleMail( String recipients[ ], String subject,
		        String message , String from) throws MessagingException
		{
		boolean debug = false;
		
		Session session = Session.getDefaultInstance(getProperties(), getAuthenticator());
		session.setDebug(debug);
		
		// create a message
		Message msg = new MimeMessage(session);
		
		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);
		
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++)
		{
		addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.BCC, addressTo);
		
		
		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		Transport.send(msg);
		return true;
		}
		public synchronized boolean postHtmlMail(String recipients[ ], String subject,
		        String htmlmsg , String from) throws MessagingException {
			System.out.println("\nSending email to :");
			for (String s : recipients) {
				System.out.print(s+",");
			}
			System.out.println("with subject:"+subject);
			boolean debug = false;
			
			Session session = Session.getDefaultInstance(getProperties(), getAuthenticator());
			session.setDebug(debug);
			 MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));
	 		InternetAddress[] addressTo = new InternetAddress[recipients.length];
	 		for (int i = 0; i < recipients.length; i++)
	 		{
	 		addressTo[i] = new InternetAddress(recipients[i]);
	 		}
	 		message.setRecipients(Message.RecipientType.TO, addressTo);
	         // Set Subject: header field
	         message.setSubject(subject);

	         // Send the actual HTML message, as big as you like
	         message.setContent(htmlmsg,
	                            "text/html" );

	         // Send message
	         Transport.send(message);
	         return true;
		}

		public void postEmailWithAttachment(String recipients[ ], String subject,
		        String htmlmsg , String from,  byte[] bytes, String filename, AttachmentType attachmentType) throws MessagingException, IOException {
			System.out.println("\nSending email to :");
			for (String s : recipients) {
				System.out.print(s+",");
			}
			System.out.println("with subject:"+subject);
			boolean debug = false;
			
			Session session = Session.getDefaultInstance(getProperties(), getAuthenticator());
			session.setDebug(debug);
			 MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));
	 		InternetAddress[] addressTo = new InternetAddress[recipients.length];
	 		for (int i = 0; i < recipients.length; i++)
	 		{
	 		addressTo[i] = new InternetAddress(recipients[i]);
	 		}
	 		message.setRecipients(Message.RecipientType.TO, addressTo);
	         // Set Subject: header field
	         message.setSubject(subject);

	         // Create the message part 
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Fill the message
	         messageBodyPart.setContent(htmlmsg, "text/html" );
	         
	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         DataSource source = new ByteArrayDataSource( bytes, attachmentType.TYPE());
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart );

	         // Send message
	         Transport.send(message);
		}
/**
* SimpleAuthenticator is used to do simple authentication
* when the SMTP server requires it.
*/
private class SMTPAuthenticator extends javax.mail.Authenticator
{

    public PasswordAuthentication getPasswordAuthentication()
    {
        String username = SMTP_AUTH_USER;
        String password = SMTP_AUTH_PWD;
        return new PasswordAuthentication(username, password);
    }
}


}
