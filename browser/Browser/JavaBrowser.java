import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
import javax.swing.border.*;
import java.io.*;
import org.jdesktop.jdic.browser.*;


// The Mini Web Browser.
public class JavaBrowser extends JFrame  implements  ActionListener ,PopupMenuListener
{
	public JButton backButton, forwardButton , stopButton , refreshButton , speakButton;
	public JButton goButton ;
	public JTextField locationTextField;
	JFileChooser filechoose = new JFileChooser();
	File file;
MyStatusBar statusBar = new MyStatusBar();
BrowserPane jBrowser;


	JMenuBar mb;
	JToolBar myTool,adTool;
	
	private JPopupMenu popup = new JPopupMenu();
	private JPopupMenu tpopup = new JPopupMenu();

	JMenu[] menus = { new JMenu("File"), new JMenu("Edit") , new JMenu("Navigation"),new JMenu("Command"),new JMenu("Help")};

	JMenuItem[] items = { new JMenuItem("open"),new JMenuItem(""),new JMenuItem("exit"), new JMenuItem("cut"), new JMenuItem("copy"),
			new JMenuItem("paste"),new JMenuItem("select all") , new JMenuItem("back"),
			new JMenuItem("Forward") , new JMenuItem("Stop") ,new JMenuItem("Refresh"),new JMenuItem(""),
			new JMenuItem("Speak"),new JMenuItem("About Us"), new JMenuItem("Help Index") , new JMenuItem("Stop") };


	public JavaBrowser()
	{
		super("Java Browser");
		setSize(640, 480);

		// Handle closing events.
		addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
				actionExit();
			}
		}
		);

		for(int i=0;i<16;i++)
		{
			items[i].addActionListener(this);
		}

		// Set up file menu.
		
menus[0].add(items[0]);

menus[0].add(items[2]);
//set up edit menu

menus[1].add(items[3]);
menus[1].add(items[4]);
menus[1].add(items[5]);
menus[1].add(items[6]);
//set up navigation menu

menus[2].add(items[7]);
menus[2].add(items[8]);
menus[2].add(items[9]);
menus[2].add(items[10]);


//set up command menu 
menus[3].add(items[12]);
menus[3].add(items[15]);

//set up help menu
menus[4].add(items[13]);
menus[4].add(items[14]);

		items[12].setEnabled(false);

		items[15].setEnabled(false);
		//menus[0].setMnemonic(KeyEvent.VK_F);
		//menus[1].setMnemonic(KeyEvent.VK_E);

		mb = new JMenuBar();
		for (int i = 0; i < menus.length; i++)
		mb.add(menus[i]);
		setJMenuBar(mb);

		//JMenuItem items[4] = new JMenuItem("Exit",KeyEvent.VK_X);

		// Set up button panel.
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());

		//JPanel downPanel = new JPanel();

		 myTool=new JToolBar();
		myTool.setBorder(new BevelBorder(BevelBorder.LOWERED));

		backButton = new JButton(new ImageIcon(getClass().getResource("images/left.gif" )));
		backButton.addActionListener(this);
		backButton.setEnabled(false);
		myTool.add(backButton);
		backButton.setToolTipText("move to previous page");
		backButton.setMargin(new Insets(5,5,5,5));

		forwardButton = new JButton(new ImageIcon(getClass().getResource("images/right.gif")));

		forwardButton.addActionListener(this);
		forwardButton.setEnabled(false);
		myTool.add(forwardButton);
		forwardButton.setToolTipText("move to next page");
		forwardButton.setMargin(new Insets(5,5,5,5));

		myTool.addSeparator();

		stopButton = new JButton(new ImageIcon(getClass().getResource("images/delete.gif")));
		stopButton.addActionListener(this);
		//stopButton.setEnabled(false);
		myTool.add(stopButton);
		stopButton.setToolTipText("Stop surfing the web page");
		stopButton.setMargin(new Insets(5,5,5,5));

		refreshButton = new JButton(new ImageIcon(getClass().getResource("images/refresh.gif")));
		refreshButton.addActionListener(this);
		//refreshButton.setEnabled(false);
		myTool.add(refreshButton);
		refreshButton.setToolTipText("refresh the page");
		refreshButton.setMargin(new Insets(5,5,5,5));


		speakButton = new JButton(new ImageIcon(getClass().getResource("images/speak.gif")));
		speakButton.addActionListener(this);
		//speakButton.setEnabled(false);
		myTool.add(speakButton);
		speakButton.setToolTipText("speak");
		speakButton.setMargin(new Insets(5,5,5,5));

		

		//JLabel img=new JLabel(new ImageIcon("sunlogo.gif"));
		//myTool.add(img);

 		adTool=new JToolBar();
		adTool.setBorder(new BevelBorder(BevelBorder.LOWERED));

		JLabel jb=new JLabel("Address :");
		adTool.add(jb);
	
		ActionListener al = new ActionListener() 
		{
      			public void actionPerformed(ActionEvent e) 
			{
			       // t.setText(((JMenuItem) e.getSource()).getText());
      			}
    		};

		locationTextField = new JTextField(35);
		JMenuItem m = new JMenuItem("Cut");
    		m.addActionListener(al);
   		tpopup.add(m);
    		m = new JMenuItem("copy");
    		m.addActionListener(al);
    		tpopup.add(m);
    		m = new JMenuItem("Paste");
    		m.addActionListener(al);
    		tpopup.add(m);
    		tpopup.addSeparator();
    		m = new JMenuItem("Select all");
    		m.addActionListener(al);
    		tpopup.add(m);
		tpopup.setBorder(new BevelBorder(BevelBorder.RAISED));
    		tpopup.addPopupMenuListener(this);

		locationTextField.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent e) 
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					jBrowser.loadURL();
				}
			}
		}
		);
		locationTextField.addMouseListener(new MouseAdapter()
		{
    			public void mousePressed(MouseEvent e) 
			{
     				 ShowTextPopup(e);
   			}

			public void mouseReleased(MouseEvent e) 
			{
				 ShowTextPopup(e);
    			}
		}
		);

		adTool.add(locationTextField);
		locationTextField.setToolTipText("Enter the web address in here");

		goButton = new JButton("GO");
		goButton.addActionListener(this);
		adTool.add(goButton);
		goButton.setToolTipText("click to view a web page from the address bar");

		buttonPanel.add(myTool);
		buttonPanel.add("South",adTool);

		
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        statusBar.lblDesc.setText("JDIC API Demo - Browser");

	jBrowser=new BrowserPane(this);



		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(buttonPanel, BorderLayout.NORTH);
		getContentPane().add(jBrowser,
		BorderLayout.CENTER);
		getContentPane().add(statusBar, BorderLayout.SOUTH);
}


 /**
     * Check the current input URL string in the address text field, load it,
     * and update the status info and toolbar info.
     */
    


 void updateStatusInfo(String statusMessage) {
        statusBar.lblStatus.setText(statusMessage);
    }

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==backButton || ae.getSource()==items[7])
		{
			jBrowser.webBrowser.back();

		}
		if(ae.getSource()==forwardButton ||  ae.getSource()==items[8])
		{
			jBrowser.webBrowser.forward();

		}
		if(ae.getSource()==goButton)
		{
			items[12].setEnabled(false);
			jBrowser.loadURL();
		}
		if(ae.getSource()==refreshButton ||  ae.getSource()==items[10])
		{
		        jBrowser.webBrowser.refresh();

		}
		if(ae.getSource()==stopButton ||  ae.getSource()==items[9])
		{
			jBrowser.webBrowser.stop();

		}
		if(ae.getSource()==speakButton ||  ae.getSource()==items[12])
		{
			if(((JMenuItem)ae.getSource()).getText()=="Speak")
			{
				((JMenuItem)ae.getSource()).setText("Pause");
				items[15].setEnabled(true);
				jBrowser.read();
				
			}
			else if(((JMenuItem)ae.getSource()).getText()=="Pause")
			{
				((JMenuItem)ae.getSource()).setText("Speak");
				jBrowser.pause();
			}
		}
		if( ae.getSource()==items[15])
		{
			System.out.println("HIIII");
			jBrowser.stop();
		}
		if(ae.getSource()==items[0])
		{
			filechoose.addChoosableFileFilter(new InputFileFilter());
            		if (filechoose.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                		file = filechoose.getSelectedFile();
                		locationTextField.setText(file.getAbsolutePath());
                		jBrowser.loadURL();
			}
            	
		}
		if(ae.getSource()==items[13])
		{
			inform();
		}
		if(ae.getSource()==items[3])
		{
			System.out.println("loc");
			locationTextField.cut();
			//if(gotFocus(ae,locationTextField))
			{
				System.out.println("location");
				locationTextField.cut();
			}
			if(jBrowser.jtf.hasFocus())
			{
				
			}
		}

		if(ae.getSource()==items[4])
		{
			
		}

		if(ae.getSource()==items[5])
		{
			
		}

		if(ae.getSource()==items[6])
		{
			
		}
		
	}
	
	// Show popup options.
	private void ShowTextPopup(MouseEvent e) 
	{
      		if (e.isPopupTrigger())
		        tpopup.show( locationTextField, e.getX(), e.getY());
    	}

	// Exit this program.
	private void actionExit() 
	{
		System.exit(0);
	}

	
public void inform() {
        JLabel title = new JLabel("Java Web Browser Implementation ");
        title.setFont(new Font("Times-Roman", Font.BOLD, 24));
        title.setForeground(Color.GREEN);
        JLabel name1=new JLabel("<html><center>ABC.");
        name1.setFont(new Font("Monotype Corsiva", Font.BOLD, 20)) ;
        JLabel name2=new JLabel("<html><center>XYZ.") ;
        name2.setFont(new Font("Monotype Corsiva", Font.BOLD, 20)) ;
        JLabel image = new JLabel(new ImageIcon("images/s3.gif"));
        Object[] ob = new Object[10];
        ob[0] = (Object)title;
        ob[1] = new JSeparator();
        ob[2] = "\nThis application \"JAVA WEB BROWSER\" deals with the web browserused for surfing on internet.\n" ;
                
        ob[3] = new JSeparator();
        ob[4] = new JLabel(" ");
        ob[5] = new JLabel(" ");
        ob[6] = null;
        ob[7] = new String("Developers :");
        ob[8] = (Object)name1;
        ob[9] = (Object)name2;
        String[] but = new String[1];
        but[0] = "OK";
            int result = JOptionPane.showOptionDialog(
                    null,					        // the parent that the dialog blocks
                    ob,							// the dialog message array
                    "About Java Browser",				// the title of the dialog window
                    JOptionPane.DEFAULT_OPTION,			        // option type
                    JOptionPane.INFORMATION_MESSAGE,	                // message type
                    new ImageIcon("images/sunlogo.gif"),			// optional icon, use null to use the default icon
                    but,						// options string array, will be made into buttons
                    but[0]						// option that should be made into a default button
                         );
    }
	// Run the Java Browser.
	public static void main(String[] args)
	{
		JavaBrowser browser = new JavaBrowser();
		browser.show();
	}


    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	//if(e.getSource()==m)

    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
      System.out.println("Popup menu will be invisible!");
    }

    public void popupMenuCanceled(PopupMenuEvent e) {
      System.out.println("Popup menu is hidden!");
    }


}
