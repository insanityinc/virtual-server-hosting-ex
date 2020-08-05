package Server;
import java.io.*;
import java.net.ServerSocket;
import java.time.LocalDateTime;

public class AppServer {

    public static void main(String args[]) throws IOException {

        int PORT_NUMBER = 22222;
        ServerSocket svSocket = new ServerSocket(PORT_NUMBER);
        System.out.println("> Server Running");

        UsersDB usersDB = new UsersDB();
        ServersDB serversDB = new ServersDB();

        while (true){
            Thread server = new Thread(new SvWorker(svSocket.accept(), usersDB, serversDB));
            System.out.println("> Client Connected");
            server.start();
        }
    }
}