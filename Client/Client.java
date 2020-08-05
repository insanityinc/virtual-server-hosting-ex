package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Pattern;

public class Client {
    public static void main(String args[]) throws IOException {
        /** MENUS AND VARIABLES */
        String email=null, password=null; int PORT_NUMBER = 22222;
        String[] main = {"Sign In", "Log In"};
        String[] logged = {"New Server Subscription", "Bid a Server", "View Subscriptions", "Cancel Subscription", "Current Debt"};
        String[] serverRequest = {"A1", "T3", "T2", "M5"};
        String[] serverBid = {"A1", "T3", "T2", "M5"};
        Menu mainM = new Menu(main), loggedM = new Menu(logged), svrM = new Menu(serverRequest), svbM = new Menu(serverBid);
        /** INPUTS AND OUTPUTS */
        BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("localhost", PORT_NUMBER);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        /** Auction Listener */
        AuctionListener auctionListener = new AuctionListener(in);
        Thread auction = new Thread(auctionListener);

        System.out.println("> Connected to Server");

        do {
            if (email != null) {
                do {
                    loggedM.run(email);
                    switch (loggedM.getOp()) {
                        case 1: newServerSub(in, out, systemIn);break;
                        case 2: bidServer(in, out, systemIn, auctionListener, auction);break;
                        case 3: viewSubscriptions(in, out);break;
                        case 4: cancelSubscription(in, out, systemIn);break;
                        case 5: checkDebt(in, out);break;
                    }
                } while (loggedM.getOp() != 0 && email != null);
                email = null;
                out.println("logout");
            }
            do {
                mainM.run(email);
                switch (mainM.getOp()) {
                    case 1: email=signin(in, out, systemIn); break;
                    case 2: email=login(in, out, systemIn); break;
                }
            } while (mainM.getOp() != 0 && email == null);
        } while (mainM.getOp() != 0);

        clientSocket.shutdownInput();clientSocket.shutdownOutput();clientSocket.close();
    }

    @SuppressWarnings("Duplicates")
    private static void bidServer(BufferedReader in, PrintWriter out, BufferedReader systemIn, AuctionListener auctionListener, Thread auction) {
        try {
            String t, m, buf;
            System.out.println("\n-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-\n" + "|   Virtual Server Hosting    |\n" + "-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-\n");
            System.out.println("Bid Server ->\n");

            out.println("bidserver");
            System.out.println("Choose type of server to bid:");
            System.out.println("-m4 (General Purpose)       -r5 (Memory Optimized)\n" +
                               "-f1 (Accelerated Computing) -i3 (Storage Optimized)");
            System.out.print("> ");
            if ((t=systemIn.readLine())!=null && (t.equals("m4") || t.equals("r5") || t.equals("f1") || t.equals("i3"))){
                out.println(t);
                System.out.println("Choose server model:");
                System.out.println("   -Large\n" + "   -xLarge\n" + "   -xxLarge");
                System.out.print("> ");
                if ((m=systemIn.readLine())!=null && (m.equals("Large") || m.equals("xLarge") || m.equals("xxLarge"))){
                    out.println(m);
                    if ((buf=in.readLine())!=null && buf.equals("server available")) {
                        System.out.println("Auction started! Type your bid!");
                        auction.start();
                        while ((buf = in.readLine()) != null && !buf.equals("auction closed")) {
                            out.println(systemIn.readLine());
                        }
                        out.println("close");
                        auctionListener.terminate();
                        //auction.join();
                    } else System.out.println("Unavailable servers to bid");
                } else out.println("abort");
            } else out.println("abort");
        } catch (IOException e) { e.printStackTrace(); }
    }
    private static void checkDebt(BufferedReader in, PrintWriter out) {
        try {
            String buf;
            out.println("checkdebt");
            if ((buf=in.readLine())!=null)
                System.out.println(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void cancelSubscription(BufferedReader in, PrintWriter out, BufferedReader systemIn) {
        try {
            viewSubscriptions(in, out);
            out.println("cancelsub");
            System.out.println("Choose which subscription to cancel:");
            System.out.print("> ");
            out.println(systemIn.readLine());
            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void viewSubscriptions(BufferedReader in, PrintWriter out) {
        try {
            out.println("viewsubs");
            System.out.println("\nAvailable Subscriptions:");
            String[] result = in.readLine().split("#", -2);
            for (int x=0; x<result.length; x++){
                if(x==0) System.out.println(result[x]);
                else System.out.println(result[x]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("Duplicates")
    private static void newServerSub(BufferedReader in, PrintWriter out, BufferedReader systemIn) {
        try {
            String t, m, res;
            System.out.println("\n-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-\n" + "|   Virtual Server Hosting    |\n" + "-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-\n");
            System.out.println("Server Subscription ->\n");

            out.println("newserversub");
            System.out.println("Choose type of server:");
            System.out.println("-m4 (General Purpose)       -r5 (Memory Optimized)\n" +
                               "-f1 (Accelerated Computing) -i3 (Storage Optimized)");
            System.out.print("> ");
            if ((t=systemIn.readLine())!=null && (t.equals("m4") || t.equals("r5") || t.equals("f1") || t.equals("i3"))){
                out.println(t);
                System.out.println("Choose server model:");
                System.out.println("   -Large\n" + "   -xLarge\n" + "   -xxLarge");
                System.out.print("> ");
                if ((m=systemIn.readLine())!=null && (m.equals("Large") || m.equals("xLarge") || m.equals("xxLarge"))){
                    out.println(m);
                } else out.println("abort");
            } else out.println("abort");
            if ((res=in.readLine())!=null){
                System.out.println(res);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String login(BufferedReader in, PrintWriter out, BufferedReader systemIn) {
        try {
            String a,b;
            System.out.println("\n-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-\n" + "|   Virtual Server Hosting    |\n" + "-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-\n");
            System.out.println("Login to account ->\n");

            out.println("login");
            System.out.println("Email address: ");
            System.out.print("> ");
            out.println(a = systemIn.readLine());
            System.out.println("Password: ");
            System.out.print("> ");
            out.println(systemIn.readLine());
            b = in.readLine();
            if (b!=null && b.equals("success")) {
                System.out.println("Account created. Login in...");
                return a;
            }
            else if (b!=null && b.equals("error")){
                System.out.println("Wrong email/password. Try again!");
                return null;
            }
            else {
                System.out.println(b);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String signin(BufferedReader in, PrintWriter out, BufferedReader systemIn) {
        try {
            String a,b;
            System.out.println("\n-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-\n" + "|   Virtual Server Hosting    |\n" + "-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-\n");
            System.out.println("Create an account ->\n");

            out.println("newuser");
            System.out.println("Email address: ");
            System.out.print("> ");
            out.println(a = systemIn.readLine());
            System.out.println("Password: ");
            System.out.print("> ");
            out.println(systemIn.readLine());
            b = in.readLine();
            if (b!=null && b.equals("success")) {
                System.out.println("Account created. Login in...");
                return a;
            }
            else if (b!=null && b.equals("error")){
                System.out.println("Email already in use ... Pick another!");
                return null;
            }
            else {
                System.out.println(b);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}