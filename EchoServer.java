// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer
{
  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port)
  {
    super(port);
  }


  //Instance methods ************************************************

  /**
   * This method handles all data coming from the server Console
   * ajouté pour l'exercice 2b
   * @param message The message from the console.
   */
  public void handleMessageFromServerConsole(String message){
    sendToAllClients(message);
  }
  /**
   * This method handles all command coming from the server console
   * Ajouté pour l'exercice 2b
   * @param message The message from the console.
   */
  public void handleCommandFromServerConsole(String message){
    String[] commands = message.split(" ");

    switch (commands[0]) {
      case "#quit":
        try {
          close();
          System.exit(0);
        } catch (IOException e) {
          System.out.println("Une erreur est survenue lors de l\'execution de la commande");
          System.exit(-1);
        }
        break;

      case "#stop":
        stopListening();
        break;

      case "#close":
        try {
          close();
        } catch (IOException e) {
          System.out.println("Une erreur est survenue lors de l\'execution de la commande");
        }
        break;

      case "#setport":
        if (!isListening() && getNumberOfClients() == 0) {
          setPort(Integer.parseInt(commands[1]));
          System.out.println("port set to:" + getPort());
        } else {
          System.out.println("Le serveur n\'est pas ferme");
        }
        break;

      case "#start":
        if (!isListening()) {
          try {
            listen();
          } catch (IOException e) {
            System.out.println("Une erreur est survenue lors de l\'execution de la commande");
          }
        } else {
          System.out.println("Vous etes deja connecte, vous ne pouvez pas utiliser cette commande");
        }
        break;

      case "#getport":
        System.out.println(getPort());
        break;

      default:
        System.out.println("Cette commande n\'est pas valide");
        break;

    }

  }



  /**
   * This method handles any messages received from the client.
   * modifié pour l'exercice 3c
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));


    String clientMessage = msg.toString();  // L'object msg est converti en String
    if (clientMessage.startsWith("#login")) {  // Le message du client commence avec #
      String[] commands = clientMessage.split(" ");
      if (client.getInfo("loginID") == null) {
        client.setInfo("loginID", commands[1]); // Il s'agit de la première connection
        System.out.println(client.getInfo("loginID") + " has logged on.");
        this.sendToAllClients(client.getInfo("loginID") + " has logged on");
      } else {
        try {
          client.sendToClient("Vous avez deja un login ID, fin de la connection");
          client.close();
        } catch(IOException e) {
          System.out.println("Une erreur est survenue lors de la fermeture de la connection");
        }
      }
    } else {
        if (client.getInfo("loginID") == null) {
          try {
            client.sendToClient("Vous devez avoir un login ID, fin de la connection");
            client.close();
          } catch(IOException e) {
            System.out.println("Une erreur est survenue lors de la fermeture de la connection");
          }
        } else {
          this.sendToAllClients(client.getInfo("loginID") + " > " + msg);
        }
    }

  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
   // ajouté pour Exercice 1c
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    System.out.println(client.getInfo("loginID") + " is disconnected.");
  }
  /**
   * Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   * The method may be overridden by subclasses but should remains
   * synchronized.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
      System.out.println(client.getInfo("loginID") + " has disconnected");
      sendToAllClients(client.getInfo("loginID") + " has disconnected");
    }

  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
   // ajouté pour Exercice 1c
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("A new client is attempting to connect to the server.");
  }

  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555
   *          if no argument is entered.
   */
  public static void main(String[] args)
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }

    EchoServer sv = new EchoServer(port);

    try
    {
      sv.listen(); //Start listening for connections
    }
    catch (Exception ex)
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
