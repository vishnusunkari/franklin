package edu.franklin.eac.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;


public class LDAPStaffDAO implements ContactDAO {

	   //private static int count = 0;
	   private LdapTemplate ldapTemplate;	   
	   private Log log = LogFactory.getLog(LDAPStaffDAO.class);
	   
/*	
 * Sample code for different way of ldap retrieval
 *   
 *  private class PersonAttributesMapper implements AttributesMapper {

		@Override
		public Object mapFromAttributes(Attributes attrs) throws NamingException {
			Person person = new Person();
			person.setFullName((String)attrs.get("cn").get());
			person.setTitle((String)attrs.get("title").get());
			//person.setDepartment((String)attrs.get("departmentNumber").get());					
			person.setUid((String)attrs.get("uid").get());
			return person;
		}
		   
	   }
*/	   
	   
	   public void setLdapTemplate(LdapTemplate ldapTemplate) {
	      this.ldapTemplate = ldapTemplate;
	   }
	   
	   /*
	    * Not able to do an ldapTemplate search on all 3 groups together, So
	    * doing an ldapTemplate search on the 3 groups separately ou=Staff,
	    * ou=Faculty and ou=Adjunct.  The same rule is followed while doing 
	    * an ldapTemplate search for supervisorName too.
	    */
	   
	@SuppressWarnings("unchecked")
	public List getAllStaff() {
		   return ldapTemplate.search("ou=Staff", "uid=*", new AbstractContextMapper() {
		     public Object doMapFromContext(DirContextOperations ctx){
		    	DistinguishedName dn = new DistinguishedName(ctx.getStringAttribute("manager"));
		    	//log.info("dn: " + dn);
		    	String supervisorName = null;		    	
		    	//Do an ldap search again for getting the supervisor name
		    	if(dn != null && !dn.isEmpty()) {
			    	try {
			    		//AndFilter andFilter = new AndFilter();
			    		//andFilter.and(new EqualsFilter("objectclass","Staff"));

			    		supervisorName = (String) ldapTemplate.search("", "uid=" + dn.getValue("uid"), new AbstractContextMapper() {
				    		public Object doMapFromContext(DirContextOperations ctx2){	
				    			return ctx2.getStringAttribute("cn");
				    		}
				    	}).get(0);
			    		//count++;
				    	//log.info(supervisorName);
				    	//log.info("Count: " + count);
			    	}catch (Exception e){
			    		log.info("Exception while ldapSearch in getAllStaff: " 
			    					+ e.getMessage() + e.getCause());
			    	}
		    	} /*else {
		    		log.info("dn is null/empty");
		    	}*/

				return new Person(ctx.getStringAttribute("cn"), 
						   				ctx.getStringAttribute("title"),
						   					ctx.getStringAttribute("departmentNumber"),
						   						ctx.getStringAttribute("uid"),
				   									ctx.getStringAttribute("telephoneNumber"),
				   										ctx.getStringAttribute("buildingName"),
				   											ctx.getStringAttribute("roomNumber"),
				   											supervisorName);				   													       
		     }
		   });
	   }
	   
	@SuppressWarnings("unchecked")
	public List getAllFaculty() {
		   return ldapTemplate.search("ou=Faculty", "uid=*", new AbstractContextMapper() {
		     public Object doMapFromContext(DirContextOperations ctx){
		    	 
		    	DistinguishedName dn = new DistinguishedName(ctx.getStringAttribute("manager"));
		    	//log.info("dn: " + dn);
		    	String supervisorName = null;
		    	//Do an ldap search again for getting the supervisor name
		    	if(dn!=null && !dn.isEmpty()){
			    	try {
			    		supervisorName = (String) ldapTemplate.search("", "uid=" + dn.getValue("uid"), new AbstractContextMapper() {
				    		public Object doMapFromContext(DirContextOperations ctx2){	
				    			return ctx2.getStringAttribute("cn");
				    		}
				    	}).get(0);
			    		//count++;
				    	//log.info(supervisorName);
				    	//log.info("Count: " + count);
			    	}catch (Exception e){
			    		log.info("Exception while ldapSearch in getAllFaculty: " 
			    					+ e.getMessage()+ e.getCause());
			    	}
		    	} /*else {
		    		log.info("dn is null/empty");
		    	}*/

				return new Person(ctx.getStringAttribute("cn"), 
						   				ctx.getStringAttribute("title"),
						   					ctx.getStringAttribute("departmentNumber"),
						   						ctx.getStringAttribute("uid"),
				   									ctx.getStringAttribute("telephoneNumber"),
				   										ctx.getStringAttribute("buildingName"),
				   											ctx.getStringAttribute("roomNumber"),
				   											supervisorName);				   													       
		     }
		   }); 
	   }
	   
	@SuppressWarnings("unchecked")
	public List getAllAdjunct() {
		   return ldapTemplate.search("ou=Adjunct", "uid=*", new AbstractContextMapper() {
		     public Object doMapFromContext(DirContextOperations ctx){
		    	 
		    	DistinguishedName dn = new DistinguishedName(ctx.getStringAttribute("manager"));
		    	//log.info("dn: " + dn);
		    	String supervisorName = null;
		    	//Do an ldap search again for getting the supervisor name
		    	if(dn!=null && !dn.isEmpty()){
			    	try {
			    		supervisorName = (String) ldapTemplate.search("", "uid=" + dn.getValue("uid"), new AbstractContextMapper() {
				    		public Object doMapFromContext(DirContextOperations ctx2){	
				    			return ctx2.getStringAttribute("cn");
				    		}
				    	}).get(0);
			    		//count++;
			    		//log.info("supervisorName: " + supervisorName);
				    	//log.info("Count: " + count);
			    	}catch (Exception e){
			    		log.info("Exception Message while ldapSearch in getAllAdjunct:: " 
			    					+ e.getMessage()+ e.getCause());
			    	}
		    	} /*else {
		    		log.info("dn is null/empty");
		    	}*/

				return new Person(ctx.getStringAttribute("cn"), 
						   				ctx.getStringAttribute("title"),
						   					ctx.getStringAttribute("departmentNumber"),
						   						ctx.getStringAttribute("uid"),
				   									ctx.getStringAttribute("telephoneNumber"),
				   										ctx.getStringAttribute("buildingName"),
				   											ctx.getStringAttribute("roomNumber"),
				   											supervisorName);				   													       
		     }
		   }); 
	   }
}	   
	   
		/*
		 * Sample code if you want to try it differently 
		 */ 
		 
	/*	
	 * AndFilter andFilter = new AndFilter();
		andFilter.and(new EqualsFilter("objectClass","top"));		   	
		andFilter.and(new EqualsFilter("objectClass","person"));
		andFilter.and(new EqualsFilter("objectClass","organizationalPerson"));
		andFilter.and(new EqualsFilter("objectclass","inetOrgPerson"));
		andFilter.and(new EqualsFilter("objectclass","posixAccount"));				
		andFilter.and(new EqualsFilter("memberOf","cn=Staff"));
		andFilter.and(new EqualsFilter("memberOf","cn=Staff,ou=Groups,dc=Franklin,dc=edu"));
		System.out.println("LDAP Query " + andFilter.encode());
		return ldapTemplate.search("cn=Staff,ou=Groups,dc=franklin,dc=edu", andFilter.encode(),new PersonAttributesMapper());
		return ldapTemplate.search("ou=Staff", "objectClass=person", new PersonAttributesMapper());
   
	   
		public List getAllStaff() {
			return ldapTemplate.search("", "(objectClass=person)",
			return ldapTemplate.search("ou=Staff", "objectClass=person",			
					new PersonAttributesMapper() {
						public Object mapFromAttributes(Attributes attrs)
								throws NamingException {
							return attrs.getAll();
							return attrs.get("cn").get();
						}
					});
		}
	 *	
	 */		

