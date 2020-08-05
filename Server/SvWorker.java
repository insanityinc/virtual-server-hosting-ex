package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SvWorker implements Runnable {

    /**
     * connected -> is user logged in
     * sv -> client socket from server side
     * usersDB -> db with registered users
     * */
    private boolean connected;
    private String email;
    private String password;
    private Socket sv;
    private UsersDB usersDB;
    private ServersDB serversDB;
    private int subId;
    private int aucId;

    public SvWorker(Socket sv, UsersDB usersDB, ServersDB serversDB) {
        this.connected = false;
        this.email = null;
        this.password = null;
        this.sv = sv;
        this.usersDB = usersDB;
        this.serversDB = serversDB;
        this.subId = 322;
        this.aucId = 655;

    }

    public void run() {
        try {
            String buf;
            BufferedReader in = new BufferedReader(new InputStreamReader(sv.getInputStream()));
            PrintWriter out = new PrintWriter(sv.getOutputStream(), true);

            while((buf=in.readLine())!=null){
                System.out.println(buf);
                switch (buf){
                    case "newuser":
                        if (connected == false){
                            if ((email = in.readLine())!=null && (password = in.readLine())!=null){
                                User user = new User(email, password, sv);
                                if (usersDB.newUser(user)) {
                                    System.out.println("adicionado");
                                    out.println("success");
                                    connected = true;
                                }
                                else {
                                    email=null; password=null;
                                    System.out.println("error");
                                    out.println("error");
                                }
                            }
                        }
                        break;
                    case "login":
                        if (connected == false){
                            if ((email = in.readLine())!=null && (password = in.readLine())!=null){
                                if (usersDB.login(email, password)) {
                                    out.println("success");
                                    connected = true;
                                    System.out.println("loggado");
                                }
                                else {
                                    out.println("error");
                                    System.out.println("nao loggado");
                                }
                            }
                        }
                        break;
                    case "logout":
                        email = null; password = null; connected = false;
                        break;
                    case "newserversub":
                        if (connected) {
                            String t, m; int id;
                            if ((t = in.readLine()) != null && !t.equals("abort") && (m = in.readLine()) != null && !m.equals("abort")) {
                                if ((id = serversDB.useInstance(t, m, 1)) == 0 || id == 1 || id == 2) {
                                    Subscription s = new Subscription(subId, email, t, m, id);
                                    serversDB.newSubscription(subId, s);
                                    usersDB.addUserSubId(email, subId);
                                    out.println("Subscription id: " + subId);
                                    subId++;
                                } else out.println("Requested server not available. All in use!");
                            } else out.println("Error");
                        }
                        break;
                    case "viewsubs":
                        if (connected) {
                            ArrayList<Integer> subs = usersDB.getSubscriptions(email);
                            out.println(serversDB.reportUserSubscriptions(subs));
                        }
                        break;
                    case "cancelsub":
                        if (connected) {
                            String a; Subscription r;
                            if ((a = in.readLine()) != null) {
                                r = serversDB.cancelSubscription(Integer.parseInt(a));
                                usersDB.removeSubscription(email, r, serversDB.getPPH(r.getInstanceType(), r.getInstanceModel()));
                                out.println("Subscription cancelled");
                            } else out.println("Error");
                        }
                        break;
                    case "checkdebt":
                        if (connected) {
                            double d = usersDB.getUserDebt(email);
                            if (d != -1) out.println(d);
                            if (d == -1) out.println("Error");
                        }
                        break;
                    case "bidserver":
                        if (connected){
                            String t, m, bid; int svId;
                            if ((t = in.readLine()) != null && !t.equals("abort") && (m = in.readLine()) != null && !m.equals("abort")) {
                                if ((svId=serversDB.useInstance(t, m, 2)) != -1){
                                    out.println("server available");
                                    Auction a = new Auction(aucId, t, m, svId);
                                    serversDB.newAuction(aucId, a);
                                    a.connectUser(usersDB.getUser(email));
                                    LocalDateTime init = LocalDateTime.now();
                                    LocalDateTime now = LocalDateTime.now();
                                    LocalDateTime end = init.plusSeconds(30);
                                    new Thread(a).start();
                                    while ((bid=in.readLine())!=null && now.isAfter(init) && now.isBefore(end)) {
                                        System.out.println(bid);
                                        a.broadcastBid(bid, usersDB.getUser(email));
                                    }
                                    a.terminate();
                                    serversDB.removeAuction(aucId);
                                    Subscription s = new Subscription(subId, a.getHighestBidUser().getEmail(), t, m, svId);
                                    usersDB.addUserSubId(a.getHighestBidUser().getEmail(), s.getId());
                                } else out.println("error");
                            } else out.println("error");
                        }
                        aucId++;
                        subId++;
                        break;
                }
                System.out.println("LOGGED USER: " + email + password + connected + "\n");
                System.out.println("\nWAITING COMMAND: ");
            }
            sv.shutdownInput();sv.shutdownOutput();sv.close();
            System.out.println("> Client Disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
