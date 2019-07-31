import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.JavaClipAudioPlayer;

public class readHTML extends Thread{
Throwable t=new Throwable();
	Voice helloVoice;
	String data="";
	public readHTML(String content)
	{
		data=content;	
		
	}
	public void suspended()
	{
		try{
		
                    
                       helloVoice.wait();
                
		}
		catch(Exception e)
		{
			e.getMessage();
		}
	}
    public void run() {
	try
	{
        
        String voiceName = "kevin16";
        System.out.println("Using voice: " + voiceName);
        
        VoiceManager voiceManager = VoiceManager.getInstance();
        helloVoice = voiceManager.getVoice(voiceName);

        if (helloVoice == null) {
            System.err.println(
                "Cannot find a voice named "
                + voiceName + ".  Please specify a different voice.");
            
        }
        
        helloVoice.allocate();
        
        helloVoice.speak(data);

        /* Clean up and leave.
         */
        helloVoice.deallocate();
	}
	catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	
        
    }
}
