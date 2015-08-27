/*
 * Creates two local.js files, one for ECF application and another MAC
*/
package edu.franklin.eac.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileWrite {
	private static Log log = LogFactory.getLog(FileWrite.class);
	static String appResourceFilename ="application";
	static ResourceBundle appResource = ResourceBundle.getBundle(appResourceFilename);
	private static String localjsECFFilePath = appResource.getString("local.js.ecf.filepath");
	private static String localjsMACFilePath = appResource.getString("local.js.mac.filepath");
	private static String localjsDelayedLDAPFilePath = appResource.getString("local.js.delayedldap.filepath");
	public static void write(StringBuffer content){
		try{
			// Create file 
			FileWriter fstreamECF = new FileWriter(localjsECFFilePath);
			BufferedWriter outECF = new BufferedWriter(fstreamECF);
			outECF.write(content.toString());
			outECF.flush();
			//Close the output stream
			outECF.close();
			fstreamECF.close();
			FileWriter fstreamMAC = new FileWriter(localjsMACFilePath);
			BufferedWriter outMAC = new BufferedWriter(fstreamMAC);
			outMAC.write(content.toString());
			outMAC.flush();
			//Close the output stream
			outMAC.close();
			fstreamMAC.close();
			FileWriter fstreamDelayedLDAP = new FileWriter(localjsDelayedLDAPFilePath);
			BufferedWriter outDelayedLDAP = new BufferedWriter(fstreamDelayedLDAP);
			outDelayedLDAP.write(content.toString());
			outDelayedLDAP.flush();
			//Close the output stream
			outDelayedLDAP.close();
			fstreamDelayedLDAP.close();
		}catch (Exception e){
			//Catch exception if any
			log.error("Error: " + e.getMessage());
		}
	}	
}
