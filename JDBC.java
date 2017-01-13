import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class JDBC{
	//====================Components needed for GUI Design======================//
	 public static JPanel	   topP;		   // Top panel
	 public static JPanel	   bottomP;		   // Bottom panel
	 public static JPanel	   sideP_1;		   // Side panel_1
	 public static JPanel	   sideP_1T;       // Side panel_1 Top part
	 public static JPanel	   sideP_1M;	   // Side panel_1 Middle part
	 public static JPanel	   sideP_1B;	   // Side panel_1 Bottom part
	 public static JPanel	   sideP_2;		   // Side panel_2 
	 public static JPanel	   sideP_2C;	   // Side panel_2 for column label and field
     public static JTextArea   inputArea;      // Text area for input
     public static JTextArea   outputArea;     // Text area for output
     public static JLabel      DBLabel;		   // Label for DB
     public static JTextField  DBField;        // Text field for DB input
     public static JLabel	   ColumnLabel;    // Label for column
     public static JTextField  ColumnField;    // Text field for column input
     public static JButton     selectButton;   // Button to select DB
     public static JButton 	   execButton;	   // Button to execute query
     public static JButton     avgButton;  	   // Button to calculate average
     public static JTextField  avgField;	   // Field for average
     public static JButton     minButton;	   // Button to calculate min
     public static JTextField  minField;	   // Field for min
     public static JButton	   maxButton;	   // Button to calculate max
     public static JTextField  maxField;	   // Field for max
     public static JButton 	   medianButton;   // Button to calculate median
     public static JTextField  medianField;	   // Field for median
     public static JScrollPane inputScroll;	   // scroll input area
     public static JScrollPane outputScroll;   // scroll output area
     public static JFrame 	   f;			   // Main frame
     public static Font ss_font = new Font("SansSarif",Font.BOLD,16) ;
     public static Font ms_font = new Font("Monospaced", Font.BOLD,16);
    //=======================Data Field Needed for JDBC============================//
	 public static final String url = "jdbc:mysql://holland.mathcs.emory.edu:3306/";
	 public static final String userName = "cs377";
	 public static final String password = "abc123";
	 public static Connection conn; 
     public static Statement stmt; 
     public static ResultSet rset=null;
     	 //=========Data Field for statements========//
	     public static String name;
	     public static String s;  //SQL query
	     public static int NCols; //Number of Columns
	     public static int c;     //column number
	     public static int count; //count for tuples
	     public static ResultSetMetaData meta; //meta data
	     	//===Allow 100 fields by default===//
		     public static int[] length = new int[100]; //store length of each field
		     public static int[] type = new int[100];   //store type of data of each field
		     	//======integer code for data type======//
		     		public static final int integer = 4;
		     		public static final int string = 1;
		     		public static final int decimal = 3;
	 //=======================Data Field Needed for functional button===================//
	  public static int valid;
	  public static double sum;
	  public static double avg;
	  public static String max;
	  public static String min;
	  public static String median;
	  public static PriorityQueue pq; //use a priority queue to obtain median
    //===================================================================================================//
     

  
     public static void main(String[] args){  	 
    //=========================================================//
    	 //===============GUI DESIGN==============//
    	 /*------Set up components--------*/
    	 Border fieldBorder = BorderFactory.createCompoundBorder(
    			 				BorderFactory.createLineBorder(Color.GRAY), 
    			 				BorderFactory.createEmptyBorder(0,7,0,0)); //create an empty border for fields
    	 /*input area--editable;scrollPane*/
    	 
    	 inputArea = new JTextArea();             
    	 inputArea.setEditable(true);           // Input Editable
    	 inputArea.setFont(ss_font);
    	 inputArea.setBorder(BorderFactory.createEmptyBorder(10,30,10,10));
    	 inputScroll = new JScrollPane(inputArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
   				 JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); //Set scroll pane
    	 
    	 
    	 /*output area--nonEditable;scrollPane*/
    	 
        outputArea = new JTextArea();
        outputArea.setEditable(false);   //output not editable
        outputArea.setFont(ms_font);
        outputArea.setBorder(BorderFactory.createEmptyBorder(10,30,10,10));
        outputScroll = new JScrollPane(outputArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
  				 JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); //set scroll pane
        
        
        /*DB Label*/
        
        DBLabel = new JLabel("  Database:");
        DBLabel.setFont(ss_font);
        DBLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        DBLabel.setOpaque(true);
        DBLabel.setBackground(Color.white);
        
        /*DB field ---editable*/
        
        DBField = new JTextField();
        DBField.setEditable(true);
        DBField.setBorder(fieldBorder);
        DBField.setFont(ss_font);
        
        /*Column Label*/
        
        ColumnLabel= new JLabel("Column: ");
        ColumnLabel.setFont(ss_font);
        
        /*Column field--editable*/
        
        ColumnField = new JTextField();
        ColumnField.setEditable(true);
        ColumnField.setBorder(fieldBorder);
        ColumnField.setFont(ss_font);
        
        /*Select Button*/
        
        selectButton = new JButton("SELECT");
        selectButton.setFont(ss_font);
        
        /*Execute Button*/
        
        execButton = new JButton("EXECUTE");
        execButton.setFont(ss_font);
        
        /*avgButton*/
        
        avgButton = new JButton("AVERAGE");
        avgButton.setFont(ss_font);
        
        /*avgField--non editable*/
        
        avgField = new JTextField();
        avgField.setEditable(false);
        avgField.setFont(ss_font);
        avgField.setBorder(fieldBorder);
        avgField.setBackground(Color.WHITE);
        /*minButton*/
        
        minButton = new JButton("MINIMUM");
        minButton.setFont(ss_font);
        
        /*minField--non editable*/
        
        minField = new JTextField();
        minField.setEditable(false);
        minField.setFont(ss_font);
        minField.setBorder(fieldBorder);
        minField.setBackground(Color.WHITE);
        
        /*maxButton*/
        
        maxButton = new JButton("MAXIMUM");
        maxButton.setFont(ss_font);
        
        /*maxField--non editable*/
        
        maxField = new JTextField();
        maxField.setEditable(false);
        maxField.setFont(ss_font);
        maxField.setBorder(fieldBorder);
        maxField.setBackground(Color.WHITE);
        
        /*medianButton*/
        
        medianButton = new JButton("MEDIAN");
        medianButton.setFont(ss_font);
        
        /*medianField--non editable*/
        
        medianField = new JTextField();
        medianField.setEditable(false);
        medianField.setFont(ss_font);
        medianField.setBorder(fieldBorder);
        medianField.setBackground(Color.WHITE);
        
  
  
        /*-----Add components to Panel------*/
        /*--Top Panel--*/
        topP = new JPanel();
        topP.setLayout(new BorderLayout(30,0)); //set up layout manager
        topP.add(inputScroll,"Center"); // add scroll to panel
        	/*--Top Side Panel--*/
        	sideP_1= new JPanel();
        	sideP_1.setLayout(new GridLayout(3,1,0,10));
        	
        		/*--Top part--*/
        		sideP_1T= new JPanel();
        		sideP_1T.setLayout(new GridLayout(2,1,0,10));
        		sideP_1T.add(DBLabel);
        		sideP_1T.add(DBField);
        		/*--Middle part--*/
        		sideP_1M= new JPanel();
        		sideP_1M.setLayout(new GridLayout(2,1,0,10));
        		sideP_1M.add(selectButton);
        		/*--Bottom part--*/
        		sideP_1B= new JPanel();
        		sideP_1B.setLayout(new GridLayout(2,1));
        		sideP_1B.add(new JLabel());
        		sideP_1B.add(execButton);
        	selectButton.setPreferredSize(new Dimension(140,20));
        	sideP_1.add(sideP_1T);
        	sideP_1.add(sideP_1M);
        	sideP_1.add(sideP_1B);
        topP.add(sideP_1,"East");
        topP.setBorder(BorderFactory.createEmptyBorder(30, 10, 0, 50));
        
        
        
        /*--Bottom Panel--*/
        bottomP = new JPanel();
        bottomP.setLayout(new BorderLayout(20,0));
        bottomP.setSize(900,400);
        bottomP.add(outputScroll);
        	/*--Bottom Side Panel--*/
        	sideP_2= new JPanel();
        	sideP_2.setLayout(new GridLayout(9,1,0,10));
        		sideP_2C= new JPanel();
        		sideP_2C.setLayout(new GridLayout(1,2,5,0));
        		sideP_2C.add(ColumnLabel);
        		sideP_2C.add(ColumnField);
        	sideP_2.add(sideP_2C);
        	sideP_2.add(avgButton);
        	sideP_2.add(avgField);
        	sideP_2.add(minButton);
        	sideP_2.add(minField);
        	sideP_2.add(maxButton);
        	sideP_2.add(maxField);
        	sideP_2.add(medianButton);
        	sideP_2.add(medianField);
        	medianButton.setPreferredSize(new Dimension(80,20));
        bottomP.add(sideP_2, "East");
        bottomP.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 50));
        
        
        
        /*--Add to main frame--*/
        f = new JFrame("Gabriel's SQL");
        f.getContentPane().setLayout( new GridLayout(2,1,0,30));
        f.getContentPane().add(topP);
        f.getContentPane().add(bottomP);
       
     //================END OF GUI===============//
     //==========================Register Action Listener============================//
        selectButton.addActionListener(new SelectButtonListener());
        execButton.addActionListener(new ExecButtonListener());
        avgButton.addActionListener(new AvgButtonListener());
        minButton.addActionListener(new MinButtonListener());
        maxButton.addActionListener(new MaxButtonListener());
        medianButton.addActionListener(new MedianButtonListener());
        f.setSize(1100, 800);
        f.setVisible(true);
     //=======Test Connection=======//



     }
    public static class SelectButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
		      conn=null;
		      stmt=null;
		      String dbName=DBField.getText();
		      try {
		    	  Class.forName("com.mysql.jdbc.Driver");
		      } 
		      catch (Exception x){
		         outputArea.setText("Failed to load JDBC driver.");
		         return;
		      }
		      try{
		         conn = DriverManager.getConnection(url+dbName,userName,password);
		         stmt = conn.createStatement ();
		         outputArea.setText("connected to "+dbName);
		      } 
		      catch (Exception x){
		         outputArea.setText("problems connecting to " + url+dbName);
		      }
		}
     }
     public static class ExecButtonListener implements ActionListener{
    	 public void actionPerformed(ActionEvent e){
    		 count=0;
        	 s=inputArea.getText();
        	 if(s.equalsIgnoreCase("exit")){
        		 try{
        			 outputArea.setText("EXIT");
        			 conn.close();
            		 stmt.close();
            		 return;
            	 }
	            	 catch (Exception x){
	            		 outputArea.setText(x.getMessage());
	            	 }   
    		 }           
        	 try{
        		 if(rset!=null) rset.close();
        		       
        		 rset=stmt.executeQuery(s);
        		 meta = rset.getMetaData();
        		 outputArea.setText("");;
        		 NCols =meta.getColumnCount();
        		//==Print the column names==//
    		 	for(int i=1;i<=NCols;i++){
    		 		String name;
    		 		name = meta.getColumnName(i);
    		 		type[i] = meta.getColumnType(i);
  	    		 	/*if(meta.getColumnDisplaySize(i)<=10) length[i] = Math.max(6,meta.getColumnDisplaySize(i)); //set the size of each column takes up
  	    		 	else length[i] =10;
  	    		 	outputArea.append(name.substring(0, length[i]));*/
    		 		outputArea.append(name);
    		 		length[i]= Math.max(6,meta.getColumnDisplaySize(i))+2;
    		 		for(int j=name.length();j<length[i];j++){
    		 			outputArea.append(" ");
    		 		}
    		 		outputArea.append(" ");
    		 	}
    		 	outputArea.append("\n");
    		 	//==Print the division==//
    		 	for(int i=1;i<=NCols;i++){
    		 		for(int j=0;j<length[i];j++){
    		 			outputArea.append("-");
    		 		}
    		 		outputArea.append(" ");
    		 	}
    		 	outputArea.append("\n");
    		 	//==Print the row of data==//
    		 	while(rset.next()){
    		 		count++;
    		 		for(int i=1;i<=NCols;i++){
    		 			String nextItem;
    		 			nextItem=rset.getString(i);
  	    		 		if(type[i]==string){
  	    		 			if(nextItem==null){
  	    		 				outputArea.append("NULL");
  	    		 				for(int j=4;j<length[i];j++){
  	    		 					outputArea.append(" ");
  	    		 				}
  	    		 			}
  	    		 			else{
	  	    		 			outputArea.append(nextItem);
	  			         	  	for(int j=nextItem.length();j<length[i];j++){
	  	    		 				outputArea.append(" ");
	  			         	  	}
  	    		 			}
  	    		 		}
  	    		 		if(type[i]==integer){
  	    		 			if(nextItem==null){
  	    		 				for(int j=4;j<length[i];j++){
  	    		 					outputArea.append(" ");
  	    		 				}
  	    		 				outputArea.append("NULL");
  	    		 			}
  	    		 			else{
	  	    		 			for(int j=nextItem.length();j<length[i];j++){
	  	    		 				outputArea.append(" ");
	  	    		 			}
	  	    		 			outputArea.append(nextItem);
  	    		 			}
  			         	  	
  	    		 		}
  	    		 		if(type[i]==decimal){
  	    		 			
  	    		 			if(nextItem==null){
  	    		 				for(int j=4;j<length[i];j++){
  	    		 					outputArea.append(" ");
  	    		 				}
  	    		 				outputArea.append("NULL");
  	    		 			}
  	    		 			else{
	  	    		 			for(int j=nextItem.length();j<length[i];j++){
	  	    		 				outputArea.append(" ");
	  	    		 			}
	  	    		 			outputArea.append(nextItem);
  	    		 			}
  			         	  	
  	    		 		}
		         	  	outputArea.append(" ");
    		 		}
		 				outputArea.append("\n");
    		 	}
        	 }
        	 catch(Exception x){
        		 outputArea.setText(x.getMessage());
 		 	}
    	 }
     }
     public static class AvgButtonListener implements ActionListener{
    	 public void actionPerformed(ActionEvent e){
			 try {
				 c=Integer.parseInt(ColumnField.getText());
	    		 if(type[c]==string){
	    			 avgField.setText("Invalid");
	    			 return;
	    		 }
	    		 DecimalFormat df = new DecimalFormat();
	        	 df.setMaximumFractionDigits(meta.getScale(c));
	    		 sum=0;
	    		 avg=0;
	    		 valid=count;
	    		 rset.beforeFirst();
				 while(rset.next()){
					 if(rset.getString(c)==null) valid--;
					 sum+=rset.getDouble(c);
				 }
				 avg=(double)sum/valid;
				 avgField.setText(""+df.format(avg));
			} 
			 catch (Exception x ) {
				outputArea.append("\n"+x.getMessage());
			} 
    	 }
     }
     public static class MinButtonListener implements ActionListener{
    	 public void actionPerformed(ActionEvent e){
    		 try{
    			 c=Integer.parseInt(ColumnField.getText());
    			 rset.beforeFirst();
    			 min="{";
    			 while(rset.next()){
    				 if(rset.getString(c)!=null && rset.getString(c).compareToIgnoreCase(min)<=0){
    					 min=rset.getString(c);
    				 }
    			 }
    			 minField.setText(min);
    			 
    		 }
    		 catch(Exception x){
    			 outputArea.append("\n"+x.getMessage());
    		 }
    	 }
     }
     public static class MaxButtonListener implements ActionListener{
    	 public void actionPerformed(ActionEvent e){
    		 try{
    			 c=Integer.parseInt(ColumnField.getText());
    			 rset.beforeFirst();
    			 max="";
    			 while(rset.next()){
    				 if(rset.getString(c)!=null && rset.getString(c).compareToIgnoreCase(max)>0){
    					 max=rset.getString(c);
    				 }
    			 }
    			 maxField.setText(max);
    			 
    		 }
    		 catch(Exception x){
    			 outputArea.append("\n"+x.getMessage());
    		 }
    	 }
     }
     public static class MedianButtonListener implements ActionListener{
    	 public void actionPerformed(ActionEvent e){
    		 try{
    			 valid=0;
    			 c=Integer.parseInt(ColumnField.getText());
    			 rset.beforeFirst();
				 pq =new PriorityQueue<String>();
				 while(rset.next()){
					 if(rset.getString(c)!=null){
						 pq.add(rset.getString(c));
						 valid++;
					 }
				 }
				for(int i=0;i<valid/2+1;i++){
					median=(String) pq.remove();
				}
				 medianField.setText(median);
    		 }
    		 catch(Exception x){
    			 outputArea.append(("\n"+x.getMessage()));
    		 }
    	 }
     }
  }
