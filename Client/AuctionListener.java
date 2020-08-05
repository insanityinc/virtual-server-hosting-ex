package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class AuctionListener implements Runnable {

    private BufferedReader in;
    private AtomicBoolean running = new AtomicBoolean(true);

    public AuctionListener(BufferedReader in){
        this.in = in;
    }

    public void terminate(){
        this.running.set(false);
    }

    @Override
    public void run() {
        try {
            String buf;
            while (running.get())
                while ((buf=in.readLine())!=null && !buf.equals("auction closed")) {
                    System.out.println("New offer! --> " + buf);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
