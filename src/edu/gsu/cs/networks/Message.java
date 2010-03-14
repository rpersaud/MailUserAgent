/**
 * 
 */
package edu.gsu.cs.networks;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

/**
 * Mail message.
 *
 * @author Jussi Kangasharju
 */
public class Message
{
    /* The headers and the body of the message. */
    public String Headers;
    public String Body;

    /* Sender and recipient. With these, we don't need to extract them
       from the headers. */
    private String From;
    private String To;
    private String Cc;
    
    // These variables hold the hostname & local user id to form 
    // fromField email address if usrName checkbox is checked.
    private String hostSvr,username;

    /* To make it look nicer */
    private static final String CRLF = "\r\n";

    /* Create the message object by inserting the required headers from
       RFC 822 (From, To, Date). */
    public Message(String from, String to, String cc, String subject, String text, boolean state, String localsvr) throws IOException
    {
    	/* Remove whitespace */
        // Get host name and local user name
        InetAddress inet;
        inet = InetAddress.getByName(localsvr);
        hostSvr = inet.getHostName();
        username = System.getProperty("user.name");
                
        if (state) { // if usrName checkbox is check, generate email address
            From = username + "@" + hostSvr;            
        }
        else  {// otherwise, use the one typed in the text field
        	From = from.trim();
        }
        
        To = to.trim();
        Cc = cc.trim();
	    Headers = "From: " + From + CRLF;
	    Headers += "To: " + To + CRLF;
        Headers += "Cc: " + Cc + CRLF;
        Headers += "Subject: " + subject.trim() + CRLF;

        /* A close approximation of the required format. Unfortunately only GMT. */
        SimpleDateFormat format = 
        	new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        String dateString = format.format(new Date());
        Headers += "Date: " + dateString + CRLF;
        Body = text;
    }

    /* Two functions to access the sender and recipient. */
    public String getFrom()
    {
    	return From;
    }

    public String getTo()
    {
    	return To;
    }

    // Create Accessor for Cc field
    public String getCc()
    {
    	return Cc;
    }
    
    /* Check whether the message is valid. In other words, check that
       both sender and recipient contain only one @-sign. */
    public boolean isValid()
    {
		int fromat = From.indexOf('@');
		int toat = To.indexOf('@');
	    int ccat;
	    if (!Cc.equals("")) {
	            ccat = Cc.indexOf('@');
	    } else {
	    	ccat = 0; // make it 0 to indicate invalid
	    }
	    // @ is not 1st char and at least 1 char after @
		if (fromat < 1 || (From.length() - fromat) <= 1) { 
		    System.out.println("Sender address is invalid");
		    return false;
		}
		if (toat < 1 || (To.length() - toat) <= 1) {
		    System.out.println("Recipient address is invalid");
		    return false;
		}
	    if ( (!Cc.equals("")) &&  (ccat < 1 || (Cc.length() - ccat) <= 1) ) {
		    System.out.println("CC: address is invalid");
		    return false;
		}
	    // Not more than one @
        if (fromat != From.lastIndexOf('@')) {
		    System.out.println("Sender address is invalid");
		    return false;
		}
		if (toat != To.lastIndexOf('@')) {
		    System.out.println("Recipient address is invalid");
		    return false;
		}	
		if ( (!Cc.equals("")) && ccat != Cc.lastIndexOf('@')) {
		    System.out.println("CC: address is invalid");
		    return false;
		}
	    return true;
    }
    
    /* For printing the message. */
    public String toString()
    {
		String res;
		res = Headers + CRLF;
		res += Body;
		return res;
    }
}