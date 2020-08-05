package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Auction implements Runnable{

    private int aucId;
    private LocalDateTime init;
    private LocalDateTime end;

    private String svType;
    private String svModel;
    private int svId;

    private ArrayList<User> connectedUsers;
    private double highestBid;
    private User highestBidUser;

    private AtomicBoolean running = new AtomicBoolean(true);

    public Auction(int aucId, String svType, String svModel, int svId){
        this.aucId = aucId;
        this.svType = svType;
        this.svModel = svModel;
        this.svId = svId;
        this.connectedUsers = new ArrayList<User>();
        this.highestBid = 0;
        this.highestBidUser = null;
        this.init = LocalDateTime.now();
        this.end = init.plusSeconds(5);
    }

    public void connectUser(User u){ connectedUsers.add(u); }
    public double getHighestBid(){ return highestBid; }
    public User getHighestBidUser(){ return highestBidUser; }
    public void terminate(){ this.running.set(false); }

    @SuppressWarnings("Duplicates")
    public void broadcastBid(String msg, User u){
        if (Integer.parseInt(msg)>highestBid){
            highestBid=Double.parseDouble(msg);
            highestBidUser = u;
        }
        for (int i=0; i<connectedUsers.size(); i++){
            Socket c = connectedUsers.get(i).getClsocket();
            try {
                PrintWriter out = new PrintWriter(c.getOutputStream(), true);
                out.println(msg);
                c.shutdownInput();
                c.shutdownOutput();
                c.close();
            }
            catch (IOException e) { e.printStackTrace(); }
        }
    }
    @SuppressWarnings("Duplicates")
    public void startEndAuction(String msg){
        for (int i=0; i<connectedUsers.size(); i++){
            Socket c = connectedUsers.get(i).getClsocket();
            try {
                PrintWriter out = new PrintWriter(c.getOutputStream(), true);
                out.println(msg);
                c.shutdownInput();
                c.shutdownOutput();
                c.close();
            }
            catch (IOException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void run() {
        startEndAuction("Auction for server type " + svType + "and model " + svModel + "started.");
        //LocalDateTime now = LocalDateTime.now();
        while (running.get());
          //  System.out.println("TESTE");
        //startEndAuction("auctionClosed");
    }
}

/*

now.isAfter(init) && now.isBefore(end)
 */
