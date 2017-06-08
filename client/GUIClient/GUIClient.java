import javax.swing.*;
import javax.swing.UIManager.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.*;

import java.io.IOException;

public class GUIClient extends JFrame implements KeyListener, ActionListener
{
    public String chatDisplayName;
    private ClientLogic client;
    
    private JButton enter;
    private JPanel displayPanel, textPanel;
    public JTextPane chatDisplay, textEntry;
    private JScrollPane chatScroll, textScroll;

    public GUIClient()
    {
        this.chatDisplayName = getChatDisplayName();
        this.client = new ClientLogic(this);
        try
        {
            this.client.connect();
        }
        catch(IOException e){displayConnectionError();}

        setTitle("JChat");
        setSize(650, 450);
        setLocation(500,200);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        customCloseOperation();

        placeComponents();
        setResizable(false);
        setVisible(true);
    }

    // Initialises the components
    private void initialiseComponents()
    {
        this.displayPanel = new JPanel();
        this.textPanel = new JPanel();

        this.chatDisplay = new JTextPane();
        this.chatDisplay.setPreferredSize(new Dimension(550, 300));
        this.chatDisplay.setEditable(false);
        this.chatDisplay.setText("Welcome to JChat!\n\n");
        this.chatScroll = new JScrollPane(this.chatDisplay);

        this.textEntry = new JTextPane();
        this.textEntry.setPreferredSize(new Dimension(477, 75));
        // Add Keyboard Listener here
        
        this.textScroll = new JScrollPane(this.textEntry);

        this.enter = new JButton("Send");
        this.enter.setPreferredSize(new Dimension(70, 75));
        this.enter.addActionListener(this);
    }

    // Place the components on the frame
    private void placeComponents()
    {
        initialiseComponents();
        this.displayPanel.add(this.chatScroll);
        this.textPanel.add(this.textScroll);
        this.textPanel.add(this.enter);

        add(this.displayPanel, BorderLayout.NORTH);
        add(this.textPanel, BorderLayout.SOUTH);
    }


    // Gets the chat name for the user
    private String getChatDisplayName()
    {
        String name = "";
        while(true)
        {
            name = JOptionPane.showInputDialog(null, "Enter Chat Name: ", "Chat Name", JOptionPane.INFORMATION_MESSAGE);

            try
            {
                if(name.length() == 0)
                {
                    JOptionPane.showMessageDialog(null, "Enter a valid name!", "Invalid Name", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                String[] splitName = name.split(" ");
                if(splitName.length > 1)
                {
                    JOptionPane.showMessageDialog(null, "No spaces in names!", "Invalid Name", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                break;
            }
            catch(NullPointerException e){System.exit(0);}
        }

        return name;
    }

    // Connection Error
    private void displayConnectionError()
    {
        JOptionPane.showMessageDialog(null, "Error on connecting to server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    // Custom close operations
    private void customCloseOperation()
    {
        addWindowListener(new WindowListener() {
            @Override
            public void windowDeactivated(WindowEvent e){}

            @Override
            public void windowActivated(WindowEvent e){}

            @Override
            public void windowDeiconified(WindowEvent e){}

            @Override
            public void windowIconified(WindowEvent e){}

            @Override
            public void windowClosed(WindowEvent e){}

            @Override
            public void windowOpened(WindowEvent e){}

            @Override
            public void windowClosing(WindowEvent e)
            {
                try
                {
                    client.close();
                    System.exit(0);
                }
                catch(IOException x){}
            }
        });
    }

    // Button Pressed
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == this.enter)
        {
            try
            {
                this.client.sendMessage();
            }
            catch(IOException x){}
        }
    }

    // Key Presses
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){}
    public void keyReleased(KeyEvent e){}


    // Main Method!
    public static void main(String[] args)
    {
        try
        {
            for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            {
                if("Nimbus".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(Exception e)
        {
            try
            {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
            catch(Exception x){}
        }

        new GUIClient();
    }
}