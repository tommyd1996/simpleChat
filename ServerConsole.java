
import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * Classe crée pour l'exercice 2b. Copie de ClientConsole modifié pour le serveur.
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ServerConsole implements ChatIF
{
  //Class variables *************************************************

  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Instance variables **********************************************

  /**
   * The instance of the client that created this ConsoleChat.
   */
  EchoServer server;



  /**
   * Scanner to read from the console
   */
  Scanner fromConsole;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(int port)
  {
    server = new EchoServer(port);

    try
    {
      server.listen(); //Start listening for connections
    }
    catch (IOException ex)
    {
      System.out.println("ERROR - Could not listen for clients!");
    }

    // Create scanner object to read from console
    fromConsole = new Scanner(System.in);
  }


  //Instance methods ************************************************

  /**
   * This method waits for input from the console.  Once it is
   * received, it sends it to the client's message handler.
   */
  public void accept()
  {
    try
    {

      String message;

      while (true)
      {
        message = fromConsole.nextLine();
        if (message.startsWith("#")) {  // ajouté pour l'exercice 2a, si le message commance avec #, c'est une commande
          server.handleCommandFromServerConsole(message);
        }
        else {
          server.handleMessageFromServerConsole(message);
          display(message);
        }
      }
    }
    catch (Exception ex)
    {
      System.out.println("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message)
  {
    System.out.println("SERVER MSG> " + message);
  }


  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args)
  {
    int port;

    try{
      port = Integer.parseInt(args[0]);
    } catch (ArrayIndexOutOfBoundsException e) {
      port = DEFAULT_PORT;
    }
    ServerConsole serverC= new ServerConsole(port);
    serverC.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
