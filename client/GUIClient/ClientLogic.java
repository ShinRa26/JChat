import java.net.*;
import java.io.*;

public class ClientLogic
{
    private GUIClient gui;

    private Socket client;
    private BufferedReader reader;
    private BufferedWriter writer;

    private final String host = "127.0.0.1";
    private final int port = 9000;

    public ClientLogic(GUIClient g)
    {
        this.gui = g;
    }

    // Connect to the server
    public void connect()
    {
        this.client = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream()));
    }


}