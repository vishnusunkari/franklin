package edu.franklin.eac.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BuildEmail {
	
    private static Log log = LogFactory.getLog(BuildEmail.class);
    static String appResourceFilename ="application";
	static ResourceBundle appResource = ResourceBundle.getBundle(appResourceFilename);
	//private String [] helpdeskEmail  =  appResource.getString("email.helpdesk").split("[\\s,;]+");
	private String [] termEmail  =  appResource.getString("email.term").split("[\\s,;]+");
	private static String fromAddress = appResource.getString("email.from");

	
	public boolean supervisorReminderEmail(Object [] toAddress, String employeeName,
											StringBuffer macURL, String requestType, 
											String hireTransferTerminationDateStr){
		/*
		* log filename for email messages
		*/
				
		String logFile = appResource.getString("log.email.location")
		+ toAddress[0].toString().substring(0, toAddress[0].toString()
		.indexOf('@')) + "_" + (new Date()).getTime() + ".log";
		
		log.info("log reminder email: " + logFile);
		
		String subject = null;
		String msgBodyReplacementText1 = null;
		String msgBodyReplacementText2 = null;
		
		if(requestType.equals("1")){
			subject = "New Hire";
			msgBodyReplacementText1 = "new hire, ";
			msgBodyReplacementText2 = "start here at Franklin.";
		} else if(requestType.equals("2")){
			subject = "Employee Transfer";
			msgBodyReplacementText1 = ""; 
			msgBodyReplacementText2 = "transition.";
		} 
		
		StringBuilder msgBodyForSupervisor = new StringBuilder();
		if (requestType.equals("1") || requestType.equals("2")){
			msgBodyForSupervisor.append("Congratulations on the addition of ").append(msgBodyReplacementText1).append(employeeName)
				.append(", to your department.\n\n").append("To ensure that all necessary resources are provided, please complete ")
				.append("the Employee Move/Add/Change Request Form ").append("within the next 24 hours. This form provides information ")
				.append("to the responsible departments so that " + employeeName.split(" ")[0]).append(" will have a successful ")
				.append(msgBodyReplacementText2).append("\n\nPlease access the form with the following link (login required):\n")
				.append(macURL.toString().replace(" ", "%20"))
				.append("\n\nThank you,\n")
				.append("Franklin University Department of Human Resources");
		} else if(requestType.equals("3")){
			subject = "Employee Termination";
			msgBodyForSupervisor.append("Please note that Human Resources has submitted a ticket informing ")
				.append("the Service Desk that " + employeeName + "'s last date at the University will be ") 
				.append(hireTransferTerminationDateStr).append(".  To better serve our customers, if you would like ")
				.append("access to ").append(employeeName.split("//s")[0]).append("'s email, share drives, or other ")
				.append("accounts for the next 30 to 90 days, please submit a request indicating the date range ")
				.append("and type of access required to the Service Desk at servicedesk@franklin.edu.\n\n")
				.append("Thank you,\n")
				.append("Franklin University Department of Human Resources");			
		}
		
		StringBuilder emailContent;
		try{
			Email email = new Email();
			email.setSubject(subject);
			emailContent = msgBodyForSupervisor;
			email.setText(emailContent.toString());				
			for(int i=0; i < toAddress.length; i++){
				email.addTo(toAddress[i].toString());
				log.info("toAddress: " + toAddress[i].toString());
			}
			email.setFrom(fromAddress);
			try {
				email.sendEmail();
				for(int i=0; i < toAddress.length; i++){
					log.info("Email sent successfully to Supervisor & CC : " + toAddress[i].toString());
				}
				logEmail(logFile, emailContent.toString());				
			} catch(Exception e){
				log.error("Error sending email: " +e.getClass() + "  " + e.getCause() + "  " + e.getMessage());
				log.error("StackTrace: " + IOUtil.StackTraceToString( e ));
				return false;
			}
			/*
			 * Call the function to create log file for emails
			 */			
		} catch (Exception e) {
			log.error("Error while creating email or with log files: " + e.getClass() +"  " + e.getCause() +"  " + e.getMessage());
			log.error("StackTrace: " + IOUtil.StackTraceToString( e ));
			return false;
		}	
		return true;
	}
	
	public boolean generateTerminationHelpDeskEmail(String subject, String employeeName, String employeeUsername, 
													String buildingName, String roomNumber, String effectiveDate, String comments){
		StringBuilder content = new StringBuilder();
		content.append("Please note that ");
		content.append(employeeName).append("'s (").append(employeeUsername);
		
		/*
		 * Employee Termination notification emails 
		 * to helpdesk@franklin.edu whose subject is "Employee Termination" and  
		 * to Facilities whose subject is "MAC/Facilities"  
		 * should contain the building and room information
		 */
		if(subject.contains("term") || subject.contains("Facilities")){
			if(buildingName != null && roomNumber != null) {
				content.append(", ").append(buildingName).append(" ").append(roomNumber);
			} else {
				log.info("buildingName and roomNumber are null");
			}
		}
		content.append(")")
			   .append(" employment with Franklin University will terminate effective ").append(effectiveDate)
			   .append(".\n\n");
		if(comments!=null && comments.isEmpty() != true){  
			content.append(comments+"\n\n");
		}

		content.append("Thank you,")
			   .append("\nFranklin University Department of Human Resources");
		String logFile = appResource.getString("log.email.location")
		+ "helpdesk@franklin.edu_" + (new Date()).getTime() + ".log";		
		log.info("log reminder email: " + logFile);
		try{
			Email email = new Email();		
			email.setSubject(subject + employeeName);			
			email.setText(content.toString());			
			for(int i=0; i < termEmail.length; i++){
				email.addTo(termEmail[i]);
			}
			email.setFrom(fromAddress);
			try {
				email.sendEmail();
				for(int i=0; i < termEmail.length; i++){
					log.info("HelpDesk Ticket Successfully created to: " + termEmail[i] + " & " + subject);
				}
				logEmail(logFile, content.toString());
			} catch(Exception e){
				//e.printStackTrace();
				log.error("Error sending email: " +e.getClass() + "  " + e.getCause() + "  " + e.getMessage());
				log.error("StackTrace: " + IOUtil.StackTraceToString( e ));
				return false;
			}
			/*
			* Call the function to create log file for emails
			*/			
		} catch (Exception e) {
			log.error("Error while creating email or with log files: " +e.getClass() +
							"  " + e.getCause() + "  " + e.getMessage());
			log.error("StackTrace: " + IOUtil.StackTraceToString( e ));
			return false;
		}
		return true;
	}
	
	
	public boolean generateHelpDeskEmail(String [] toAddress, String fromAddress, String subject, StringBuffer content){
		log.info("Inside generateHelpDeskEmail method");
		String logFile = appResource.getString("log.email.location")
		+ toAddress[0].substring(0, toAddress[0]
		.indexOf('@')) + "_" + (new Date()).getTime() + ".log";
		
		log.info("log reminder email: " + logFile);
		try{
			Email email = new Email();		
			for(int i=0; i < toAddress.length; i++){
				email.addTo(toAddress[i]);
				log.info("toAddress: " + toAddress[i]);
			}
			email.setFrom(fromAddress);
			log.info("fromAddress: " + fromAddress);
			email.setSubject(subject);	
			log.info("subject: " + subject);
			email.setText(content.toString());
			try {
				email.sendEmail();
				for(int i=0; i < toAddress.length; i++){
					log.info("HelpDesk Ticket Successfully created to: " + toAddress[i]);
				}
				logEmail(logFile, content.toString());
			} catch(Exception e){
				e.printStackTrace();
				log.error("Error sending email: " +e.getClass() + "  " + e.getCause() + "  " + e.getMessage());
				log.error("StackTrace: " + IOUtil.StackTraceToString( e ));
				return false;
			}
			/*
			* Call the function to create log file for emails
			*/			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while creating email or with log files: " +e.getClass() + "  " + e.getCause() + "  " + e.getMessage());
			log.error("StackTrace: " + IOUtil.StackTraceToString( e ));
			return false;
		}	
		return true;
	}
	
	/*
	* function to create log file for emails 
	*/
	private static void logEmail(String logFileName, String emailContent) {
		File logFile = new File(logFileName);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(logFile);
			fileWriter.write(emailContent);
		} catch (IOException e) {
			log.error(e.getStackTrace().toString());
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				log.error(e.getStackTrace().toString());
			}
		}
	}

	public boolean oldSupervisorNotificationEmail(String oldSupervisorName, String oldSupervisorEmail, 
			String employeeName, String departmentName, String hireTransferTerminationDateStr) {
		String subject = "Employee Transfer";
		StringBuilder emailContent = new StringBuilder();
		emailContent.append("Please note that Human Resources has submitted a ticket informing the Service Desk that ")
				.append(employeeName).append(" will be transferring to ").append(departmentName).append(" on ") 
				.append(hireTransferTerminationDateStr).append(".  If any account access changes (shared drives, datatel, etc.) ")
				.append("are required as a result of this change, please notify the Service Desk at servicedesk@franklin.edu.\n\n" ) 
				.append("Thank you,\n")
				.append("Franklin University Department of Human Resources");
		try{
			Email email = new Email();
			email.setSubject(subject);			
			email.setText(emailContent.toString());			
			email.addTo(oldSupervisorEmail);
			email.setFrom(fromAddress);
			try {
				email.sendEmail();
				log.info("Email sent successfully to old Supervisor: " + oldSupervisorEmail + " of Employee: " + employeeName);		
			} catch(Exception e){
				System.out.println("ERROR: " + e.getClass() + "  " + e.getCause() + "  " + e.getMessage());
				log.error("Error sending email to old supervisor: " +e.getClass() + "  " + e.getCause() + "  " + e.getMessage());
				log.error("StackTrace: " + IOUtil.StackTraceToString( e ));
				return false;
			}
			/*
			* Call the function to create log file for emails
			*/			
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getClass() + "  " + e.getCause() + "  " + e.getMessage());
			log.error("Error while creating email to old supervisoror with log files: " + 
							e.getClass() +"  " + e.getCause() +"  " + e.getMessage());
			log.error("StackTrace: " + IOUtil.StackTraceToString( e ));
			return false;
		}	
		return true;
	
	}    
}
