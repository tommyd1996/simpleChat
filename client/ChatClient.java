// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************

  /**
   * The interface type variable.  It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;
  private String loginId; // ajouté pour l'exercice 3a

  //Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */

  public ChatClient(String loginId, String host, int port, ChatIF clientUI)
    throws IOException
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginId = loginId;
    openConnection();
    sendToServer("#login " + loginId);  // ajouté pour l'exercice 3b
  }


  //Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg)
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  /**
   * This method handles all command coming from the UI
   * Ajouté pour l'exercice 2a
   * @param message The message from the UI.
   */
  public void handleCommandFromClientUI(String message){
    String[] commands = message.split(" ");

    switch (commands[0]) {
      case "#quit":
        quit();
        break;

      case "#logoff":
        try {
          closeConnection();
        } catch (IOException e) {
          System.out.println("Une erreur est survenue lors de la deconnexion");
        }
        break;

      case "#setHost":
        if (!isConnected()) {
          setHost(commands[1]);
        } else {
          System.out.println("Vous etes deja connecte, vous ne pouvez pas utiliser cette commande");
        }
        break;

      case "#setport":
        if (!isConnected()) {
          setPort(Integer.parseInt(commands[1]));
        } else {
          System.out.println("Vous etes deja connecte, vous ne pouvez pas utiliser cette commande");
        }
        break;

      case "#login":
        if (!isConnected()) {
          try {
            openConnection();
          } catch (IOException e) {
            System.out.println("Une erreur est survenue lors de la connection au serveur");
          }
        } else {
          System.out.println("Vous etes deja connecte, vous ne pouvez pas utiliser cette commande");
        }
        break;

      case "#gethost":
        System.out.println(getHost());
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
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

  /**
   * Hook method called after the connection has been closed. The default
   * implementation does nothing. The method may be overriden by subclasses to
   * perform special processing such as cleaning up and terminating, or
   * attempting to reconnect.
   */
   // ajouté pour Exercice 1a
  protected void connectionClosed() {
    System.out.println("La connection s\'est arrete");
  }

  /**
   * Hook method called each time an exception is thrown by the client's
   * thread that is waiting for messages from the server. The method may be
   * overridden by subclasses.
   *
   * @param exception
   *            the exception raised.
   */
   // ajouté pour Exercice 1a
  protected void connectionException(Exception exception) {
    System.out.println("Le serveur s\'est arrete");
  }
}
//End of ChatClient class
