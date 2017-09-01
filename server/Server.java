import java.net.*;
import java.util.ArrayList;
import java.util.*;
import java.io.*;

public class Server
{
    private static ServerSocket server;
    private static final int PORT = 9000;
    private static final String IPADDRESS = "130.209.221.132";
    private static final int BACKLOG = 50;
    private static EchoHandler eh = new EchoHandler();

    public static void main(String[] args)
    {
        try
        {
            server = new ServerSocket(PORT, BACKLOG, InetAddress.getByName(IPADDRESS));
            System.out.println("Server online!\nListening on port: " + server.getLocalPort());

            while(true)
            {
                Socket client = server.accept();
                Handler h = new Handler(client);
                h.start();
                eh.addClient(client);
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
        }

        public void run()
        {
            System.out.println("New connection from " + this.client.getInetAddress() + ":" + this.client.getPort() + '\n');
            String recv = "";
            
            try
            {
                while((recv = reader.readLine()) != null)
                {
                    if(recv.length() == 0 || recv.equals(""))
                        continue;

                    System.out.println(recv);
                    eh.echo(recv);
                }

                System.out.println("Client Disconnected: " + this.client.getInetAddress() + ":" + this.client.getPort() + '\n');
                eh.removeClient(this.client);
                this.client.close();
            }
            catch(IOException e){}
        }
    }

    /**
     * Class for echoing other commands to the other clients
     */
    private static class EchoHandler
    {
        private List<Socket> clients;

        public EchoHandler()
        {
            this.clients = new ArrayList<Socket>();
        }

        // Add a client to the list
        public void addClient(Socket c)
        {
            this.clients.add(c);
        }

        // Remove a client from the list
        public void removeClient(Socket c)
        {
            Iterator<Socket> iter = this.clients.iterator();
            while(iter.hasNext())
            {
                Socket client = iter.next();
                if(c == client)
                {
                    iter.remove();
                    break;
                }
            }
        }

        // Echo a message to all clients
        public void echo(String msg)
        {
            if(msg.length() == 0 || msg.equals(""))
                return;

            BufferedWriter writer;
            for(Socket client : this.clients)
            {
                try
                {
                    writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                    writer.write(msg + '\n');
                    writer.flush();
                }
                catch(IOException e){}
            }
        }
    }
}