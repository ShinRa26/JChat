import java.net.*;
import java.io.*;

import javax.swing.SwingUtilities;
import javax.swing.text.*;

public class ClientLogic
{
    private GUIClient gui;
    private String name;
    private String serverIP;
    private ClientEchoHandler clientEH;

    private Socket client;
    private BufferedReader reader;
    private BufferedWriter writer;

    private final String host = "127.0.0.1";
    private final int port = 9000;


    public ClientLogic(GUIClient g)
    {
        this.gui = g;
        this.name = this.gui.chatDisplayName;
        this.serverIP = this.gui.serverIP;
    }

    // Connect to the server
    public void connect() throws IOException
    {
        this.client = new Socket(this.serverIP, port);
        this.reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream()));

        this.clientEH = new ClientEchoHandler(this.client, this.gui);
        this.clientEH.start();
    }

    // Closes the socket and exits "gracefully"
    public void close() throws IOException
    {
        leftChat();
        this.client.close();
    }

    // Send a message to the server
    public void sendMessage(String msg, int flag) throws IOException
    {
        if(isMessageEmpty(msg, flag))
            return;
        /**
         * Flag is for the server.
         * Flag 0 means that the message will be displayed with the client's username
         * Flag 1 means the server will print the message w/o username
         */
        switch(flag)
        {
            case 0:
                String msgWithName = ("[" + this.name + "]: " + msg);
                this.writer.write(msgWithName + '\n');
                this.writer.flush();
                break;
            case 1:
                this.writer.write(msg + '\n');
                this.writer.flush();
                break;
            default:
                break;
        }
        
    }

    // Message for client joining the chat
    public void joinedChat() throws IOException
    {
        String joined = String.format("%s has joined the server.\n", this.name);
        sendMessage(joined, 1);
    }

    // Message for client leaving the chat
    public void leftChat() throws IOException
    {
        String left = String.format("%s has left the server.\n", this.name);
        sendMessage(left, 1);
    }

    // Checks if the sent message is empty, null, non-existant, new-line, jesus etc.
    private boolean isMessageEmpty(String msg, int flag)
    {
        // Messages for the server don't count!
        if(flag == 1)
            return false;

        if(msg.equals("") || msg == null || msg.length() == 0)
            return true;

        // Checks for new line at start of msg
        // String[] split = msg.split(":");
        // System.out.println("Split: " + split[0]);
        // char[] strippedMsg = split[1].toCharArray();

        // if(strippedMsg[0] == 10)
        //     return true;

        return false;
    }


    /**
     * Class for handling the echo commands form the server
     */
    private static class ClientEchoHandler extends Thread
    {
        private Socket client;
        private GUIClient gui;
        private BufferedReader reader;

        public ClientEchoHandler(Socket c, GUIClient g)
        {
            try
            {
                this.client = c;
                this.gui = g;
                this.reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            }
            catch(IOException e){} // TODO: Add error for failing to launch the echo handler
        }

        public void run()
        {
            String recv = "";
            StyledDocument doc;

            try
            {
                while((recv = this.reader.readLine()) != null)
                {
                    try
                    {
                        doc = this.gui.chatDisplay.getStyledDocument();
                        doc.insertString(doc.getLength(), '\n' + recv, null);
                        this.gui.scrollToBottom();
                    }
                    catch(Exception e){}
                }
            }
            catch(IOException e){}
        }
    }
}