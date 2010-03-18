/**
 * The MailClient class provides the user interface and calls the other classes as needed.
 * When you press Send, the MailClient class constructs a Message class object to hold the mail message.
 * The Message object holds the actual message headers and body. Then the MailClient object builds the
 * SMTP envelope using the Envelope class. This class holds the SMTP sender and recipient information,
 * the SMTP server of the recipient's domain, and the Message object. Then the MailClient object creates
 * the SMTPConnection object which opens a connection to the SMTP server and the MailClient object sends
 * the message over the connection. The sending of the mail happens in three phases:
 * The MailClient object creates the SMTPConnection object and opens the connection to the SMTP server.
 * The MailClient object sends the message using the function SMTPConnection.send().
 * The MailClient object closes the SMTP connection.
 */
package edu.gsu.cs.networks;

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

/**
 * A simple mail client with a GUI for sending mail.
 *
 * @author Jussi Kangasharju
 */
public class MailClient extends JFrame
{
	/* The stuff for the GUI. */
    private Button btSend = new Button("Send");
    private Button btClear = new Button("Clear");
    private Button btQuit = new Button("Quit");
    
    private JLabel serverLabel = new JLabel("Local Mailserver:", Label.RIGHT);
    private JTextField serverField = new JTextField("", 40);
    
    private JLabel fromLabel = new JLabel("From:");
    private JTextField fromField = new JTextField("", 40);
    
    private JLabel toLabel = new JLabel("To:"); 
    private JTextField toField = new JTextField("", 40);
    
    /* Cc Label & Text field from GUI */
    private Label ccLabel = new Label("cc:"); 
    private TextField ccField = new TextField("", 40);
    
    // create constants for instantiating input elements
    private final static int TEXTFIELD = 10;
    private final static int TEXTAREA = 20;
    
    // private Vector<JTextComponent> fields = new Vector<JTextComponent>();
    private JTextComponent[] fields;
    
    /* Check box to indicate is you want to Auto Generate the username
     * from the host name and local user id.
     * 
     * CAUTION: This email will not be sent unless your local username
     * is email account username, and you put in the correct mail server
     * name that can identify your username.
     */
    private Checkbox usrName = new Checkbox("Auto Generate?"); 
    private Label subjectLabel = new Label("Subject:");
    private TextField subjectField = new TextField("", 40);
    private Label messageLabel = new Label("Message:");
    private TextArea messageText = new TextArea(10, 40);
    
    /**
     * Create a new MailClient window with fields for entering all
     * the relevant information (From, To, Subject, and message).
     */
    public MailClient(String[] labels, int[] inputs)
    {
		super("Java Mailclient");

		// Setup frame
    	this.setLayout(new BorderLayout());
    	Border emptyBorder = new EmptyBorder(10, 10, 10, 10);
	    this.setBounds(100, 100, 640, 400);
	    this.setBackground(Color.cyan);
	    this.setResizable(false);

	    // Setup panels for labels, and fields
    	JPanel labelPanel = new JPanel(new GridLayout(labels.length, 1));
	    JPanel fieldPanel = new JPanel(new GridLayout(labels.length, 1));
	    add(labelPanel, BorderLayout.WEST);
	    add(fieldPanel, BorderLayout.CENTER);
	    labelPanel.setBorder(emptyBorder);
	    fieldPanel.setBorder(emptyBorder);
	    
	    fields = new JTextComponent[labels.length];
	    
	    // Add localmail server label+field
	    for (int i=0; i < labels.length; i++) {
	    	// add labels
	    	JLabel label = new JLabel(labels[i], Label.RIGHT);
		    label.setVerticalTextPosition(JLabel.CENTER);

		    fields[i] = new JTextField("",40);
		    
		    // fields.add(i, new JTextField("",40));
		  	//label.setLabelFor(fields.get(i).);
		  	
		    labelPanel.add(label);
	    	JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	   	    panel.add(fields[i]);
	   	    fieldPanel.add(panel);
	    }
	//    JTextArea messageField = new JTextArea(10,40);
	 //  fieldPanel.add(messageField);
	    
	    
	    
	    serverLabel.setVerticalTextPosition(JLabel.CENTER);
	    labelPanel.add(serverLabel);
	    JPanel serverPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    serverPanel.add(serverField);
	    fieldPanel.add(serverPanel);
	    
	    // Add from label + field
	    fromLabel.setVerticalTextPosition(JLabel.CENTER);
	    labelPanel.add(fromLabel);
	    JPanel fromPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    fromPanel.add(fromField);
	    fieldPanel.add(fromPanel);

	    // Add o label + field
	    toLabel.setVerticalTextPosition(JLabel.CENTER);
	    labelPanel.add(toLabel);
	    JPanel toPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    toPanel.add(toField);
	    fieldPanel.add(toPanel);

	    
	    /*
	    fields = new JTextField[labels.length];
	    for (int i = 0; i < labels.length; i += 1) {
	    	fields[i] = new JTextField();
	    	fields[i].setColumns(widths[i]);
	   
	 		JLabel lab = new JLabel(labels[i], JLabel.RIGHT);
			lab.setLabelFor(fields[i]);
			labelPanel.add(lab);

			JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
			p.add(fields[i]);
			fieldPanel.add(p);
	    }
		
/*
		// Create panels for holding the fields.
		JPanel serverPanel = new JPanel(new FlowLayout());
		JPanel fromPanel = new JPanel(new FlowLayout());
		JPanel toPanel = new JPanel(new BorderLayout());
		JPanel ccPanel = new JPanel(new BorderLayout());
	    JPanel usrPanel = new JPanel(new BorderLayout()); 	    
	    JPanel subjectPanel = new JPanel(new BorderLayout());
		JPanel messagePanel = new JPanel(new BorderLayout());

		// Add labels to each panel
	//	Dimension dServer = serverLabel.getPreferredSize();  
	//  serverLabel.setPreferredSize(new Dimension(dServer.width+20,dServer.height)); 
		serverPanel.add(serverLabel, BorderLayout.WEST);
		serverPanel.add(serverField, BorderLayout.CENTER);
		
		Dimension dFrom = fromLabel.getPreferredSize();  
	    fromLabel.setPreferredSize(new Dimension(dFrom.width+20,dFrom.height)); 
		fromPanel.add(fromLabel, BorderLayout.WEST);
		fromPanel.add(fromField, BorderLayout.CENTER);
		
		toPanel.add(toLabel, BorderLayout.WEST);
		toPanel.add(toField, BorderLayout.CENTER);
	    
		ccPanel.add(ccLabel, BorderLayout.WEST);
		ccPanel.add(ccField, BorderLayout.CENTER);
	    
		usrPanel.add(usrName, BorderLayout.WEST); // @todo pull email together from local variables
	    
		subjectPanel.add(subjectLabel, BorderLayout.WEST);
		subjectPanel.add(subjectField, BorderLayout.CENTER);

		messagePanel.add(messageLabel, BorderLayout.NORTH);	
		messagePanel.add(messageText, BorderLayout.CENTER);

		// Panel to hold all other panels
		// @todo: add border
		// @todo: add cellspacing
		JPanel fieldPanel = new JPanel(new GridLayout(0, 1, 10, 10));
		Border blackline = BorderFactory.createLineBorder(Color.black);
		Border empty = BorderFactory.createEmptyBorder(20,20,20,20);
		fieldPanel.setBorder(empty);
	
		fieldPanel.add(serverPanel);
		fieldPanel.add(fromPanel);
		fieldPanel.add(toPanel);
	    fieldPanel.add(ccPanel);
	    fieldPanel.add(usrPanel);
	    fieldPanel.add(subjectPanel);
	    fieldPanel.add(messagePanel);
	
		// Create a panel for the buttons and add listeners to the buttons. 
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
		buttonPanel.setBorder(empty);
		btSend.addActionListener(new SendListener());
		btClear.addActionListener(new ClearListener());
		btQuit.addActionListener(new QuitListener());
		buttonPanel.add(btSend);
		buttonPanel.add(btClear);
		buttonPanel.add(btQuit);
		
		// Add, pack, and show.
		this.add(fieldPanel, BorderLayout.NORTH);
		this.add(messagePanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
//		this.add(rootPanel, BorderLayout.NORTH);
 */
	    // Show frame
		this.pack();
		this.setVisible(true);
    }

    static public void main(String argv[])
    {
    	String[] labels = {"local mailserver:", "from:", "to:", "cc:", "subject:"};
    	int[] inputs = {TEXTFIELD, TEXTFIELD, TEXTFIELD, TEXTFIELD, TEXTFIELD};
    	new MailClient(labels, inputs);
    }

    /* Handler for the Send-button. */
    class SendListener implements ActionListener
    {
		public void actionPerformed(ActionEvent event)
		{ 
			System.out.println("Sending mail");
		    Envelope envelope;  // TRY IT HERE 
		    
		    /* Check that we have the local mailserver */
		    //if ((serverField.getText()).equals("")) {
		    if (fields[0].getText().equals("")) {
		    	System.out.println("Need name of local mailserver!");
		    	return;
		    }
	
		    /* Check that we have the sender and recipient.
		     * Also, still send if fromField is blank IFF usrName checkbox
	         * is checked.
	         */ 
	        if ((fromField.getText()).equals("") && !usrName.getState()) {
	        	System.out.println("Need sender!");
	        	return;
		    }
		    if ((toField.getText()).equals("")) {
		    	System.out.println("Need recipient!");
		    	return;
		    }
		    if (usrName.getState()) {
	               System.out.println("Checked.");
		    }
		    /* Create the message */
           // Modified the message format to include Extra fields
		    try {
		    	Message mailMessage = new Message(fromField.getText(), 
		    			toField.getText(),
		    			ccField.getText(),
		    			subjectField.getText(), 
		    			messageText.getText(),
		    			usrName.getState(),
		    			fields[0].getText());//serverField.getText());
	           
		    	/* Check that the message is valid, i.e., sender and recipient addresses look ok. */
		    	if (!mailMessage.isValid()) {
		    		return;
		    	}
	
		    	/* Create the envelope, open the connection and try to send the message. */
		    	try {
		    		envelope = new Envelope(mailMessage, fields[0].getText());//serverField.getText());
		    	} catch (UnknownHostException e) {
		    		/* If there is an error, do not go further */
		    		return;
		    	}
		    } catch (IOException e) {
		    	return;
		    }
			try {
				SMTPConnection connection = new SMTPConnection(envelope);
				connection.send(envelope);
				connection.close();
			} catch (IOException error) {
				System.out.println("Sending failed: " + error);
				return;
			}
			System.out.println("Mail sent succesfully!");
		}
    }

    /* Clear the fields in the GUI. */
    class ClearListener implements ActionListener
    {
		public void actionPerformed(ActionEvent e)
		{
		    System.out.println("Clearing fields");
		    fromField.setText("");
		    toField.setText("");
		    subjectField.setText("");
		    messageText.setText("");
		}
    }

    /* Quit. */
    class QuitListener implements ActionListener
    {
		public void actionPerformed(ActionEvent e)
		{
		    System.exit(0);
		}
    }   
}        