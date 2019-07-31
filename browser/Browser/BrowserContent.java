
import java.net.*;
import java.util.*;
import java.io.*;
import org.jdesktop.jdic.browser.*;

class BrowserContent 
{
public BrowserContent()
{

}

public void writeContent(String data) throws Exception
{
  try
    {
	char buffer[]=new char[data.length()];
	data.getChars(0,data.length(),buffer,0);
	FileWriter fo=new FileWriter("data.txt");
	for(int i=0;i<buffer.length;i++)
	{
		fo.write(buffer[i]);
	}
	fo.close();
    }
    catch(IOException ie)
	{
		ie.printStackTrace();
	}
			
}

public String readContent() throws Exception
{
String data="";
   try
     {
	
	FileReader reader = new FileReader("data.txt");	
	List<String> lines = HTMLUtils.extractText(reader);
	for (String line : lines) 
	{
		data=data + line + "\n";
	}


      }
      catch(IOException sex)
	{
		sex.printStackTrace();
	}

	return data;
}
}