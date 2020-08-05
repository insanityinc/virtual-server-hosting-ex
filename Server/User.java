package Server;
import java.net.Socket;
import java.util.ArrayList;

public class User {

    private String email;
    private String password;
    private Socket clSocket;
    private double debt;
    private ArrayList<Integer> subs;

    public User(String email, String password, Socket clSocket){
        this.email = email;
        this.password = password;
        this.clSocket = clSocket;
        this.debt = 0;
        this.subs = new ArrayList<Integer>();
    }

    public void addSubscription(int id){ subs.add(id); }
    public void removeContract(int id){
        for (int i=0;i<subs.size();i++)
            if (subs.get(i)==id)
                subs.remove(i);
    }

    public String getEmail(){ return email; }
    public String getPassword(){ return password; }
    public Socket getClsocket(){ return clSocket; }
    public double getDebt(){ return debt; }

    public ArrayList<Integer> getSubs() {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i=0;i<subs.size();i++)
            res.add(subs.get(i));
        return res;
    }

    public void addToDebt(double debt) {
        this.debt+=debt;
    }
}
