The project is a small pluggable API is to add email capability to any project. This is not intended for use independently and doesnot provide any log or User Interface.

Dependencies:
------------
1- Jdk-1.6+ 

2- JavaMail API (javax.mail)

Installation/Export:
-------------------
For exporting jar using eclipse read https://www.cs.utexas.edu/~scottm/cs307/handouts/Eclipse%20Help/jarInEclipse.htm  .

For exporting jar using commandline read https://docs.oracle.com/javase/7/docs/technotes/tools/windows/jar.html .

You can also use emailer-1.0.0.jar in dist folder to avoid code compiling and exporting steps.
Note: Make sure to remove dist, lib, .settings from export while exporting this project as jar file.

How to use:
----------
Add emailer jar file into your project`s classpath/lib along with required dependencies. 

Instantiate a singleton EmailEngine with properties below 

- mail.transport.protocol = smtp (your mail server protocol)
- mail.host = smtp.gmail.com     (your mail server)
- mail.user.username = my.reminder@gmail.com (you email loginname/username)
- mail.user.password = xxxxxxxx 	(your login passcode)
- mail.smtp.auth = true           (if server requires authentication)
- mail.smtp.port = 465			(your port)
- emailer.admin-email = admin.emailer@abc.org (mandatory if you want to use emailToAdmin component of API)

<b><u>Instantiate (mandatory)</u></b>

EmailEngine.instantiateEmailEngine(prop); 
	
	// where prop is java.util.Properties with above keys. This method must be called once 
	// and only once in code before using Emailer.
	
<b><u>Quick Email to Admin (property: emailer.admin-email)</u></b>
		
	//To use these methods property emailer.admin-email must have been specified at instantiation
	
- EmailEngine.getInstance().emailReportToAdmin("Your subject", "Your email body"); 

- EmailEngine.getInstance().emailReportToAdminInSeparateThread("Your subject", "Your email body"); 

	// emailReportToAdmin method blocks your code until email has been sent. If operation is time critical
	// you can use this method to move forward and leaving the email been sent in background thread.
	
<b><u>Customized Emails to specified recipients</u></b>

- EmailEngine.getInstance().postSimpleMail(new String[]{"me@me.com"}, "Subject", "Body", "abc@me.com");

- EmailEngine.getInstance().postHtmlMail(new String[]{"me@me.com"}, "Subject", "<b>HTML body</b>", "abc@me.com");
	
- EmailEngine.getInstance().postEmailWithAttachment(new String[]{"me@me.com"}, "Subject", "<b>HTML body</b>", "me@me.com", yourByteArrHoldingFile, yourFileName, AttachmentType.CSV);	
