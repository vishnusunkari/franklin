package edu.franklin.eac.ldap;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;

import edu.franklin.eac.dao.ContactDAO;
import edu.franklin.eac.dao.LDAPStaffDAO;
import edu.franklin.eac.dao.Person;
import edu.franklin.eac.util.IOUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject; 

public class LDAPClient extends TimerTask implements ServletContextListener  {
		

	private static ApplicationContext factory = new ClassPathXmlApplicationContext("spring-ldap.xml");
	private  Log log = LogFactory.getLog(LDAPClient.class);
	final int initialDelay = 30000;          // start after 30 seconds
	final int repeatPeriod = 3*60*60*1000;   // repeat every 3 hours
	
	JSONObject json;
	
	private TimerTask timerTask = null;
	private Timer timer = null;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if(timerTask != null){
			timerTask.cancel();
			log.info("LDAPThread Timer task cancelled");
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {		
		timerTask = new LDAPClient();
		timer = new Timer();
		timer.scheduleAtFixedRate(timerTask, initialDelay, repeatPeriod);		
	}	
	
	public void run() {
		try {
			//Resource resource = new ClassPathResource("edu/franklin/eac/spring-ldap.xml");
			//BeanFactory factory = new XmlBeanFactory(resource);
			
	    	log.info("LDAPThread Started");
	    	
			ContactDAO ldapContact = (LDAPStaffDAO)factory.getBean("ldapContact");
			List <Person> contactList = (List <Person>) ldapContact.getAllStaff();
			contactList.addAll((List <Person>) ldapContact.getAllFaculty());
			contactList.addAll((List <Person>) ldapContact.getAllAdjunct());
			//JSONArray json = new JSONArray();
			JSONObject json = new JSONObject();  
			StringBuffer content = new StringBuffer(); 
			content.append("var employees = [\n");
			log.info("Contact List Size " + contactList.size());
			for( int i = 0 ; i < contactList.size(); i++){
				Person p = new Person();
				p = (Person) contactList.get(i);
				content.append("\t");
				json.put("u", p.getUid());	
				json.put("d", p.getDepartment());				
				json.put("t", p.getTitle());
				json.put("n", p.getFullName());
				json.put("e", p.getExtension());
				json.put("b", p.getBuilding());
				json.put("r", p.getRoom());
				json.put("s", p.getSupervisor());
				content.append(json.toString()).append(",\n");
				//log.info(json.toString());
			}
			log.info("Content Length: " + content.length());
			//Remove the last comma before new line character
			content.deleteCharAt(content.length()-2);
			content.append("]");
			IOUtil.fileWrite(content);
		} catch (DataAccessException e) {
			log.info("Error occured " + e.getCause());
		}
	}

	/*public void setJSONObject(){
		try {
			/Resource resource = new ClassPathResource("edu/franklin/eac/spring-ldap.xml");
			/BeanFactory factory = new XmlBeanFactory(resource);
			
			ContactDAO ldapContact = (LDAPStaffDAO)factory.getBean("ldapContact");
			List contactList = (List) ldapContact.getAllStaff();
			contactList.addAll((List) ldapContact.getAllFaculty());
			contactList.addAll((List) ldapContact.getAllAdjunct());
			/JSONArray json = new JSONArray();
			json = new JSONObject();  
			StringBuffer content = new StringBuffer(); 
			content.append("var employees = [\n");
			log.info("Contact List Size " + contactList.size());
			for( int i = 0 ; i < contactList.size(); i++){
				Person p = new Person();
				p = (Person) contactList.get(i);
				content.append("\t");
				json.put("u", p.getUid());	
				json.put("d", p.getDepartment());				
				json.put("t", p.getTitle());
				json.put("n", p.getFullName());
				json.put("e", p.getExtension());
				json.put("b", p.getBuilding());
				json.put("r", p.getRoom());
				json.put("s", p.getSupervisor());
				content.append(json.toString()).append(",\n");
				log.info(json.toString());
			}
			log.info("Content Length: " + content.length());
			log.info("Content : " + content.toString());
			/Remove the last comma before new line character
			content.deleteCharAt(content.length()-2);
			content.append("]");
			FileWrite file = new FileWrite(content);
			
		} catch (DataAccessException e) {
			log.info("Error occured " + e.getCause());
		}
	}
	
	public JSONObject getJSONObject(){
		return json;
	}
	*/	
	
	
}

