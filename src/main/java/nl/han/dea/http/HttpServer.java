package nl.han.dea.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private static final int TCP_PORT = 8383;

    private ServerSocket serverSocket;

    public static void main(String[] args) {
        new HttpServer().startServer();
    }

    public void startServer() {
        openServerSocket();

        while (true) {
            Socket clientSocket;
            try {
                // The accept() method is a "blocking" method. This means that when it is called, the execution will
                // wait until a request is made and the serverSocket can "accept" it. As soon as it has accepted the
                // request, a Socket is created to handle the request and code execution is continued.
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Unable to accept Client Connection. Try The next one");
                break;
            }

            try {
                // A new instance of the ConnectionHandler is created, which uses the Socket to receive the Request and
                // create a Response
                var connectionHandler = new ConnectionHandler(clientSocket);
                connectionHandler.accept();
            } catch (IOException e) {
                System.out.println("Unable to respond to current request. Try The next one");
                break;
            }
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(TCP_PORT);
            System.out.println("Welcome the the JAVA-based HTTP-server!");
            System.out.println("-> Server accepting requests on port " + TCP_PORT);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + TCP_PORT, e);
        }
    }
}
