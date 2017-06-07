import java.net.*;
import java.io.*;

// TODO: Add Echo Handler!
public class Server
{
    private static ServerSocket server;
    private static int port = 9000;

    public static void main(String[] args)
    {
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server online!\nListening on port: " + server.getLocalPort());
            while(true)
            {
                Handler h = new Handler(server.accept());
                h.start();
            }
        }
        catch(IOException e) {System.out.println("Unable to conenct to server: Server offline.");}
    }

    private static class Handler extends Thread
    {
        private Socket client;
        BufferedReader reader;
        BufferedWriter writer;

        public Handler(Socket c) throws IOException
        {
            this.client = c;
            this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        }

        public void run()
        {
            System.out.println("New connection from " + this.client.getInetAddress() + ":" + this.client.getPort() + '\n');
            String recv = "";
            
            try
            {
                while((recv = reader.readLine()) != null)
                {
                    System.out.println("[RECV]: " + recv);
                    writeMsg(recv);
                }

                System.out.println("Client Disconnected: " + this.client.getInetAddress() + ":" + this.client.getPort() + '\n');
                this.client.close();
            }
            catch(IOException e){}
        }

        private void writeMsg(String msg)
        {
            try
            {
                this.writer.write(msg + '\n');
                this.writer.flush();
            }
            catch(IOException e){}
        }
    }
}