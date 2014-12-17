package org.ihs.emailer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.MessagingException;

import org.ihs.emailer.EmailEngineTest.EmailSender1Test;
import org.ihs.emailer.EmailEngineTest.EmailSender2Test;
import org.ihs.emailer.EmailEngineTest.EmailSender3Test;
import org.ihs.emailer.EmailEngineTest.Emailer1Test;
import org.ihs.emailer.EmailEngineTest.Emailer2Test;
import org.ihs.emailer.EmailEngineTest.Emailer3Test;
import org.ihs.emailer.EmailServer.AttachmentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	Emailer1Test.class,
	Emailer2Test.class,
	Emailer3Test.class,
	EmailSender1Test.class,
	EmailSender2Test.class,
	EmailSender3Test.class,
})
public class EmailEngineTest {
	public static class Emailer1Test{
		public Emailer1Test() {}
		
		@Test (expected=NullPointerException.class)
		public void emailerIntantiateTest() throws EmailException, IOException {
			System.out.println("Emailer 1");

			EmailEngine.instantiateEmailEngine(null);
		}
	}
	public static class Emailer2Test{
		public Emailer2Test() {}
		
		@Test (expected=EmailException.class)
		public void emailerIntantiateTest() throws EmailException, IOException {
			System.out.println("Emailer 2");

			EmailEngine.instantiateEmailEngine(new Properties());
		}
	}
	
	public static class Emailer3Test{
		public Emailer3Test() {}
		
		@Test (expected=InstantiationError.class)
		public void emailerIntantiateTest() throws EmailException, IOException {
			System.out.println("Emailer 3");

			System.out.println(">>>>LOADING SYSTEM PROPERTIES...");
			InputStream f = Thread.currentThread().getContextClassLoader().getResourceAsStream("emailer.properties");
			Properties prop = new Properties();
			prop.load(f);
			System.out.println("......PROPERTIES LOADED SUCCESSFULLY......");
			
			EmailEngine.instantiateEmailEngine(prop);
			System.out.println("......EMAIL ENGINE LOADED SUCCESSFULLY......");
			
			// Instantianting again would throw exception
			EmailEngine.instantiateEmailEngine(prop);
		}
	}
	
	public static class EmailSender1Test{
		public EmailSender1Test() {}
		
		@Test
		public void sendEmailTest() throws MessagingException{
			EmailEngine.getInstance().emailReportToAdminInSeparateThread("ATEST", "TEST");
			EmailEngine.getInstance().emailReportToAdminInSeparateThread("ATEST2", "TEST2");
			EmailEngine.getInstance().emailReportToAdminInSeparateThread("ATEST3", "TEST3");
		}
	}
	
	public static class EmailSender2Test{
		public EmailSender2Test() {}
		
		@Test
		public void sendEmailTest() throws MessagingException{
			EmailEngine.getInstance().emailReportToAdmin("TEST", "TEST");
			EmailEngine.getInstance().emailReportToAdmin("TEST2", "TEST2");
			EmailEngine.getInstance().emailReportToAdmin("TEST3", "TEST3");
		}
	}
	
	public static class EmailSender3Test{
		public EmailSender3Test() {}
		
		@Test
		public void sendEmailTest() throws MessagingException, IOException{
			EmailEngine.getInstance().postSimpleMail(new String[]{"testird@gmail.com"}, "STEST", "STEST", "me@me.com");
			EmailEngine.getInstance().postHtmlMail(new String[]{"testird@gmail.com"}, "STESThtml", "<b>STEST</b>", "me@me.com");
			EmailEngine.getInstance().postEmailWithAttachment(new String[]{"testird@gmail.com"}, "STEST-att", "STEST", "me@me.com", new byte[]{0,1,2,13,3,2,3,4,5,6,7,8,9}, "myfile.txt", AttachmentType.CSV);
		}
	}
}
