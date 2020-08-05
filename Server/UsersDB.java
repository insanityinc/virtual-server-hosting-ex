package Server;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class UsersDB {

    private HashMap<String,User> usersHM;

    public UsersDB(){
        usersHM = new HashMap<String,User>();
    }

    public synchronized boolean newUser(User u){
        if(usersHM.containsKey(u.getEmail()))
            return false;
        usersHM.put(u.getEmail(), u);
        return true;
    }
    public synchronized boolean login(String email, String password){
        if(usersHM.containsKey(email))
            if(password.equals(usersHM.get(email).getPassword()))
                return true;
        return false;
    }
    public synchronized boolean removeUser(User u){
        if(usersHM.containsKey(u.getEmail())) {
            usersHM.remove(u.getEmail());
            return true;
        }
        return false;
    }
    public synchronized void addUserSubId(String email, int subId){
        if(usersHM.containsKey(email)) {
            usersHM.get(email).addSubscription(subId);
        }
    }
    public synchronized ArrayList<Integer> getSubscriptions(String email) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        if(usersHM.containsKey(email)) {
            res = usersHM.get(email).getSubs();
        }
        return res;
    }
    public synchronized void removeSubscription(String email, Subscription r, int PPH) {
        LocalDateTime end = LocalDateTime.now();
        if(usersHM.containsKey(email)) {
            usersHM.get(email).removeContract(r.getId());
            long hours = ChronoUnit.SECONDS.between(r.getInit(), end);
            usersHM.get(email).addToDebt(PPH*hours);
        }
    }
    public synchronized double getUserDebt(String email) {
        if(usersHM.containsKey(email)){
            return usersHM.get(email).getDebt();
        }
        return -1;
    }
    public synchronized User getUser(String email){
        if (usersHM.containsKey(email))
            return usersHM.get(email);
        return null;
    }
}
