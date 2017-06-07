import java.net.*;
import java.io.*;
import javax.swing.text.*;

public class ClientLogic extends Thread
{
    private GUIClient gui;
    private String name;

    private Socket client;
    private BufferedReader reader;
    private BufferedWriter writer;

    private final String host = "127.0.0.1";
    private final int port = 9000;

    public ClientLogic(GUIClient g)
    {
        this.gui = g;
        this.name = this.gui.chatDisplayName;
    }

    // Connect to the server
    public void connect() throws IOException
    {
        this.client = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream()));
    }

    // Closes the socket and exits "gracefully"
    public void close() throws IOException
    {
        this.client.close();
    }

    // Send a message to the server
    public void sendMessage() throws IOException
    {
        String msg = this.gui.textEntry.getText();
        String msgWithName = ("[" + this.name + "]: " + msg);

        this.writer.write(msgWithName + '\n');
        this.writer.flush();

        this.gui.textEntry.setText("");
    }

    // Thread for reading messages from the server
    public void run()
    {
        String recv = "";
        StyledDocument doc;

        try
        {
            while((recv = this.reader.readLine()) != null)
            {
                // this.gui.chatDisplay.getText();
                try
                {
                    doc = this.gui.chatDisplay.getStyledDocument();
                    doc.insertString(doc.getLength(), '\n' + recv, null);
                }
                catch(Exception e){}
            }
        }
        catch(IOException e){}
    }
}