package multiClientChatApp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ClientThread extends Thread{
     private String clientName = null;
  private DataInputStream dataInputStream = null;
  private PrintStream printStream = null;
  private Socket clientSocket = null;
  private final ClientThread[] clientThreads;
  private int maxClientsCount;

  public ClientThread(Socket clientSocket, ClientThread[] clientThreads) {
    this.clientSocket = clientSocket;
    this.clientThreads = clientThreads;
    maxClientsCount = clientThreads.length;
  }

  public void run() {
    int maxClientsCount = this.maxClientsCount;
    ClientThread[] clientThreads = this.clientThreads;

    try {
     
      dataInputStream = new DataInputStream(clientSocket.getInputStream());
      printStream = new PrintStream(clientSocket.getOutputStream());
      String name;
      while (true) {
        printStream.println("Enter your name.");
        name = dataInputStream.readLine().trim();
        if (name.indexOf('@') == -1) {
          break;
        } else {
          printStream.println("The name should not contain '@' character.");
        }
      }

      printStream.println("Welcome " + name
          + " to our chat room.\nTo leave enter /quit in a new line.");
      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (clientThreads[i] != null && clientThreads[i] == this) {
            clientName = "@" + name;
            break;
          }
        }
        for (int i = 0; i < maxClientsCount; i++) {
          if (clientThreads[i] != null && clientThreads[i] != this) {
            clientThreads[i].printStream.println("*** A new user " + name
                + " entered in the chatRoom  ***");
          }
        }
      }
      while (true) {
        String line = dataInputStream.readLine();
        if (line.startsWith("/leave")) {
          break;
        }
        if (line.startsWith("@")) {
          String[] words = line.split("\\s", 2);
          if (words.length > 1 && words[1] != null) {
            words[1] = words[1].trim();
            if (!words[1].isEmpty()) {
              synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                  if (clientThreads[i] != null && clientThreads[i] != this
                      && clientThreads[i].clientName != null
                      && clientThreads[i].clientName.equals(words[0])) {
                    clientThreads[i].printStream.println("<" + name + "> " + words[1]);

                    this.printStream.println(">" + name + "> " + words[1]);
                    break;
                  }
                }
              }
            }
          }
        } else {
          synchronized (this) {
            for (int i = 0; i < maxClientsCount; i++) {
              if (clientThreads[i] != null && clientThreads[i].clientName != null) {
                clientThreads[i].printStream.println("<" + name + "> " + line);
              }
            }
          }
        }
      }
      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (clientThreads[i] != null && clientThreads[i] != this
              && clientThreads[i].clientName != null) {
            clientThreads[i].printStream.println("*** The user " + name
                + " dataInputStream.. leaving the chat room !!! ***");
          }
        }
      }
      printStream.println("*** Bye " + name + " ***");

      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (clientThreads[i] == this) {
            clientThreads[i] = null;
          }
        }
      }
 
      dataInputStream.close();
      printStream.close();
      clientSocket.close();
    } catch (IOException e) {
    }
  }
}