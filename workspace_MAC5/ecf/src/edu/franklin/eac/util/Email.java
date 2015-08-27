package edu.franklin.eac.util;


import java.util.*;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email
{
	static String appResourceFilename = "application";
	static ResourceBundle appResource = ResourceBundle.getBundle(appResourceFilename);
	private static String mailSmtpHost = appResource.getString("mail.smtp.host");
    private Properties props;
    private ArrayList <InternetAddress> toList;
    private ArrayList <InternetAddress> ccList;
    private ArrayList <InternetAddress> bccList;
    private MimeMessage msg;
    public Email()
    {
        props = new Properties();
        toList = new ArrayList<InternetAddress>();
        ccList = new ArrayList<InternetAddress>();
        bccList = new ArrayList<InternetAddress>();
        props.put("mail.smtp.host", mailSmtpHost);
        //props.put("mail.smtp.host", "mcadlx01.servers.mc.franklin.edu");
        props.put("mail.user", "Automated_Process_DO_NOT_REPLY@www.franklin.edu");
        Session session = Session.getInstance(props, null);
        msg = new MimeMessage(session);
        
        
    }

    public void setFrom(String from)
        throws MessagingException
    {
        msg.setFrom(new InternetAddress(from));
    }

    public void setText(String textMsg)
        throws MessagingException
    {
        msg.setText(textMsg);
    }

    public void setHTML(String htmlMsg)
        throws MessagingException
    {
        msg.setDataHandler(new DataHandler(new ByteArrayDataSource(htmlMsg, "text/html")));
    }

    public void setSubject(String subject)
        throws MessagingException
    {
        msg.setSubject(subject);
    }
    
    public void addTo(String to)
        throws MessagingException
    {
    	toList.add(new InternetAddress(to));
    }

    public void addCc(String cc)
        throws MessagingException
    {
       	ccList.add(new InternetAddress(cc));
    }

    public void addBcc(String bcc)
        throws MessagingException
    {
    	bccList.add(new InternetAddress(bcc));
    }

    public void setReplyTo(String replyTo)
        throws MessagingException
    {
        msg.setReplyTo(InternetAddress.parse(replyTo, false));
    }    
           
    public void sendEmail()
        throws MessagingException
    {
        if(msg.getFrom() == null)
            msg.setFrom(new InternetAddress("Automated_Process_DO_NOT_REPLY@www.franklin.edu"));
        msg.setHeader("X-mailer", "msgsend");
        msg.setRecipients(Message.RecipientType.TO, (Address[])toList.toArray(new Address[0]));
        msg.setRecipients(Message.RecipientType.CC, (Address[])ccList.toArray(new Address[0]));
        msg.setRecipients(Message.RecipientType.BCC,(Address[])bccList.toArray(new Address[0]));
        msg.setSentDate(new Date());

        if(!toList.isEmpty())
        	Transport.send(msg);
    }
}