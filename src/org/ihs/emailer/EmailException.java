package org.ihs.emailer;

public class EmailException extends Exception{
	
	public static final String NO_MAIL_HOST="No mail host was specified. i.e, mail.host property was not found.";
	public static final String NO_USERNAME="No username for mail host was specified. i.e, mail.user.username property was not found.";
	public static final String NO_PASSWORD="No password for mail host user was specified. i.e, mail.user.password property was not found.";

	private String errorMessage;
	private static final long serialVersionUID = 1L;

	public EmailException(String msg) {
		errorMessage=msg;
	}
	
	@Override
	public void printStackTrace() {
		System.out.println(getMessage());
		super.printStackTrace();
	}
	
	public String getMessage(){
		return errorMessage+super.getMessage()==null?"":("\n"+super.getMessage());
	}
}
