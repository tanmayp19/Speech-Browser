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

public class BrowserPane extends JTabbedPane implements WebBrowserListener,ChangeListener{

  
JavaBrowser jb;
WebBrowser webBrowser;
JPanel jBrowserPanel;
JPanel jtp;
JScrollPane jp;
final JTextArea jtf;
BrowserContent bcont;
readHTML rd;
int flag=0;
  public BrowserPane(JavaBrowser jb) {
	this.jb=jb;
	jtf=new JTextArea(30,30);
	jBrowserPanel=new JPanel();

        try {
            webBrowser = new WebBrowser(new URL("http://java.net"));
            // Print out debug messages in the command line.
            //webBrowser.setDebug(true);
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
            return;
        }

        webBrowser.addWebBrowserListener(this);

        jBrowserPanel.setLayout(new BorderLayout());
        jBrowserPanel.add(webBrowser);
        addTab("HTML View", jBrowserPanel);

   	
	jp=new JScrollPane(jtf);
        

	addTab("Text View",jp);
        

    setSelectedIndex(1);

   	addChangeListener(this);
 
  }

public void stateChanged(ChangeEvent e) {
        int i = getSelectedIndex();
	if(i==1)
	{
		bcont=new BrowserContent();
		try
		{
			jtf.setText("");
			bcont.writeContent(webBrowser.getContent());
			jtf.setText(bcont.readContent());
		}
		catch(Exception em)
		{
			em.printStackTrace();
		}
	}
}
void loadURL() {
        String inputValue = jb.locationTextField.getText();

        if (inputValue == null) {
            JOptionPane.showMessageDialog(this, "The given URL is NULL:",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            // Check if the text value is a URL string.
            URL curUrl = null;

            try {
                // Check if the input string is a local path by checking if it starts
                // with a driver name(on Windows) or root path(on Unix).               
                File[] roots = File.listRoots();

                for (int i = 0; i < roots.length; i++) {
                    if (inputValue.toLowerCase().startsWith(roots[i].toString().toLowerCase())) {
                        File curLocalFile = new File(inputValue);

                        curUrl = curLocalFile.toURL();
                        break;
                    }
                }

                if (curUrl == null) {
                    // Check if the text value is a valid URL.
                    try {
                        curUrl = new URL(inputValue);
                    } catch (MalformedURLException e) {
                            if (inputValue.toLowerCase().startsWith("ftp.")) {
                                curUrl = new URL("ftp://" + inputValue);
                            } else if (inputValue.toLowerCase().startsWith("gopher.")) {
                                curUrl = new URL("gopher://" + inputValue);
                            } else {
                                curUrl = new URL("http://" + inputValue);
                            }
                    }
                }
                            
                webBrowser.setURL(curUrl);
		
                // Update the address text field, statusbar, and toolbar info.
                jb.updateStatusInfo("Loading " + curUrl.toString() + " ......");

            } catch (MalformedURLException mue) {
                JOptionPane.showMessageDialog(this,
                    "The given URL is not valid:" + inputValue, "Warning",
                    JOptionPane.WARNING_MESSAGE);
            }                
        }
    }



 	   public void downloadStarted(WebBrowserEvent event) {
                jb.updateStatusInfo("Loading started.");
            }

            public void downloadCompleted(WebBrowserEvent event) {
                jb.backButton.setEnabled(webBrowser.isBackEnabled());
                jb.forwardButton.setEnabled(webBrowser.isForwardEnabled());

                jb.updateStatusInfo("Loading completed.");

                URL currentUrl = webBrowser.getURL();

                if (currentUrl != null) {
                    jb.locationTextField.setText(currentUrl.toString());
                }
		jb.items[12].setEnabled(true);
            }

            public void downloadProgress(WebBrowserEvent event) {
                // updateStatusInfo("Loading in progress...");
            }

            public void downloadError(WebBrowserEvent event) {
                jb.updateStatusInfo("Loading error.");
            }

            public void documentCompleted(WebBrowserEvent event) {
                jb.updateStatusInfo("Document loading completed.");
		bcont=new BrowserContent();
		try
		{
			bcont.writeContent(webBrowser.getContent());
			jtf.setText("");
			jtf.setText(bcont.readContent());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
            }

            public void titleChange(WebBrowserEvent event) {
		jb.setTitle("");
               jb.updateStatusInfo("Title of the browser window changed.");
            }  

            public void statusTextChange(WebBrowserEvent event) {
                // updateStatusInfo("Status text changed.");
            } 
            public void windowClose(WebBrowserEvent event) {
                jb.updateStatusInfo("Closed by script.");
                if(JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(
                    webBrowser,
                    "The webpage you are viewing is trying to close the window.\n Do you want to close this window?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE))
                {
                     System.exit(0);
                }
            }


	   public void read()
        	{
			if(flag==0)
			{
				rd=new readHTML(jtf.getText());
				rd.start();
				flag=1;
			}
			else if(flag==1)
			{
				//rd.helloVoice.notify();
				//rd.helloVoice.allocate();
			}
		}
	   public void pause()
		{	
		try
		{
			System.out.println("pause");
			rd.suspended();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		}
	   public void stop()
		{
		try
		{	if(rd.helloVoice.isLoaded())
			{
				rd.helloVoice.deallocate();
				flag=0;
				
			}
			System.out.println("stop2");
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		}

}
