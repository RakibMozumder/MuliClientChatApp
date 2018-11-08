package multiClientChatApp;

import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class Server {

  private static ServerSocket serverSocket = null;

  private static Socket clientSocket = null;

 private static final int maxClients = 10;
  private static final ClientThread[] threads = new ClientThread[maxClients];

  public static void main(String args[]) {

    int port = 3333;
    if (args.length < 1) {
      System.out.println("Usage: java MultiThreadChatServerSync <port>\n"
          + "Now using port number=" + port);
    } else {
      port = Integer.valueOf(args[0]).intValue();
    }
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      System.out.println(e);
    }
    while (true) {
      try {
        clientSocket = serverSocket.accept();
       int i = 0;
        for (i = 0; i < maxClients; i++) {
          if (threads[i] == null) {
            (threads[i] = new ClientThread(clientSocket, threads)).start();
            break;
         }
        }
        if (i == maxClients) {
          PrintStream os = new PrintStream(clientSocket.getOutputStream());
         os.println("Server too busy. Try later.");
          os.close();
          clientSocket.close();
       }
     } catch (IOException e) {
        System.out.println(e);
      }
    }
  }  
}