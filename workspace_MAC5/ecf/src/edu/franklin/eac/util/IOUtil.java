package edu.franklin.eac.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IOUtil {
	private static Log log = LogFactory.getLog(IOUtil.class);
	static String appResourceFilename ="application";
	static ResourceBundle appResource = ResourceBundle.getBundle(appResourceFilename);
	private static String localjsECFFilePath = appResource.getString("local.js.ecf.filepath");
	private static String localjsMACFilePath = appResource.getString("local.js.mac.filepath");
	private static String localjsDelayedLDAPFilePath = appResource.getString("local.js.delayedldap.filepath");
	
	//printing the stack trace to logs
	public static String StackTraceToString( Exception e )
	{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		PrintStream p = new PrintStream( b );
		e.printStackTrace( p );
		p.flush();
		return b.toString();
	}
	
	//FileWrite (local.js in 3 places ecf/mac/delayedLDAP)
	public static void fileWrite(StringBuffer content){
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
