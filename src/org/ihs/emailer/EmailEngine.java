package org.ihs.emailer;

import java.util.Properties;

import javax.mail.MessagingException;

/**
 *  donot forget to instantiate EmailEngine by calling instantiateEmailEngine(Properties props)
 *  properties are mendatory required to get smtp host for email server
 *  
 *  mail.transport.protocol=smtp 	(for example)
 *	mail.host=smtp.gmail.com 		(for example)
 *	mail.user.username=my.reminder@gmail.com (for example)
 *	mail.user.password=xxxxxxxx 	(for example)
 *	mail.smtp.auth=true
 *	mail.smtp.port=465				(for example)
 *
 * emailer.admin-email=admin.emailer@abc.org (for example) mandatory if you want to use emailToAdmin component of API
 * 
 * call this method just only once in your application as it will make singleton instance 
 * of EmailEngine and calling it again and again will have no effect
 * 
 * @author maimoonak
 */
public class EmailEngine extends EmailServer{
	private static EmailEngine _instance;

	private EmailEngine(Properties props) throws EmailException {
		super(props);
	}
	
	public static void instantiateEmailEngine(Properties props) throws EmailException{
		if(_instance == null)
		{
			_instance = new EmailEngine(props);
		}
		else
		{
			throw new InstantiationError("An instance of EmailEngine already exists");
		}
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public static EmailEngine getInstance() {
		if(_instance == null){
			throw new InstantiationError("EmailEngine not instantiated");
		}
		return _instance;
	}
	
	public void emailReportToAdmin(String subject,String message) throws MessagingException{
		String[] recipients=new String[]{getProperties().getProperty("emailer.admin-email")};
		postSimpleMail(recipients, subject, message, "emailer.ihs@ihs.org");
	}
	
	public void emailReportToAdminInSeparateThread(String subject,String message){
		final String msg=message;
		final String sub=subject;
		Runnable emailr = new Runnable() {
			@Override
			public void run() {
				try {
					emailReportToAdmin(sub, msg);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(emailr).start();  
	}
}
