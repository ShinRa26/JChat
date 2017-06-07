import java.net.*;
import java.io.*;
import java.util.Scanner;

public class CLIClient
{
    private static String host = "127.0.0.1";
    private static int port = 9000;

    public static void main(String[] args)
    {
        try
        {
            System.out.println("Connecting to " + host + " on port: " + port);
            Socket client = new Socket(host, port);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            Scanner in = new Scanner(System.in);

            System.out.println("Connected!\n");

            while(true)
            {
                System.out.print("~#: ");
                String msg = in.nextLine();

                writeMsg(writer, msg);

                String recv = reader.readLine();
                if(recv == null)
                    break;
                System.out.println(recv);
            }
        }
        catch(IOException e){System.out.println("Disconnected from server!");}
    }

    private static void writeMsg(BufferedWriter writer, String msg)
    {
        try
        {
            writer.write(msg + '\n');
            writer.flush();
        }
        catch(IOException e){}
    }
}