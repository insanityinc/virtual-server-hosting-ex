package Server;
import java.util.ArrayList;
import java.util.HashMap;

public class ServersDB {

    private ServerInstance m4, r5, f1, i3;
    private HashMap<Integer, Subscription> inUseInstances;
    private HashMap<Integer, Auction> onlineAuctions;

    public ServersDB(){
        this.m4 = new ServerInstance(
                "m4", "General Purpose", 5, 10, 15);
        this.r5 = new ServerInstance(
                "r5", "Memory Optimized", 10, 15, 20);
        this.f1 = new ServerInstance(
                "f1", "Accelerated Computing", 20, 30, 40);
        this.i3 = new ServerInstance(
                "i3", "Storage Optimized", 10, 15, 20);

        this.inUseInstances = new HashMap<Integer, Subscription>();
        this.onlineAuctions = new HashMap<Integer, Auction>();
    }

    public synchronized int useInstance(String t, String m, int opt) {
        int instanceId = -1;
        switch (t){
            case "m4":
                if (m.equals("Large")) instanceId = m4.useLargeInstance(opt);
                if (m.equals("xLarge")) instanceId = m4.useXLargeInstance(opt);
                if (m.equals("xxLarge")) instanceId = m4.useXXLargeInstance(opt);
                break;
            case "r5":
                if (m.equals("Large")) instanceId = r5.useLargeInstance(opt);
                if (m.equals("xLarge")) instanceId = r5.useXLargeInstance(opt);
                if (m.equals("xxLarge")) instanceId = r5.useXXLargeInstance(opt);
                break;
            case "f1":
                if (m.equals("Large")) instanceId = f1.useLargeInstance(opt);
                if (m.equals("xLarge")) instanceId = f1.useXLargeInstance(opt);
                if (m.equals("xxLarge")) instanceId = f1.useXXLargeInstance(opt);
                break;
            case "i3":
                if (m.equals("Large")) instanceId = i3.useLargeInstance(opt);
                if (m.equals("xLarge")) instanceId = i3.useXLargeInstance(opt);
                if (m.equals("xxLarge")) instanceId = i3.useXXLargeInstance(opt);
                break;
        }
        return instanceId;
    }
    public synchronized void newSubscription(int subId, Subscription c) {
        inUseInstances.put(subId, c);
    }
    public synchronized String reportUserSubscriptions(ArrayList<Integer> subs) {
        StringBuilder subsInfo = new StringBuilder();
        for (int i=0;i<subs.size();i++){
            subsInfo.append(inUseInstances.get(subs.get(i)).toString());
            subsInfo.append('#');
        }
        return subsInfo.toString();
    }
    public synchronized Subscription cancelSubscription(int subId) {
        Subscription r = null;
        if (inUseInstances.containsKey(subId)){
            r = inUseInstances.get(subId);
            String t = r.getInstanceType();
            String m = r.getInstanceModel();
            int svId = r.getSvId();
            switch (t){
                case "m4":
                    if (m.equals("Large")) m4.freeLargeInstance(svId);;
                    if (m.equals("xLarge")) m4.freeXLargeInstance(svId);
                    if (m.equals("xxLarge")) m4.freeXXLargeInstance(svId);
                    break;
                case "r5":
                    if (m.equals("Large")) r5.freeLargeInstance(svId);
                    if (m.equals("xLarge")) r5.freeXLargeInstance(svId);
                    if (m.equals("xxLarge")) r5.freeXXLargeInstance(svId);
                    break;
                case "f1":
                    if (m.equals("Large")) f1.freeLargeInstance(svId);
                    if (m.equals("xLarge")) f1.freeXLargeInstance(svId);
                    if (m.equals("xxLarge")) f1.freeXXLargeInstance(svId);
                    break;
                case "i3":
                    if (m.equals("Large")) i3.freeLargeInstance(svId);
                    if (m.equals("xLarge")) i3.freeXLargeInstance(svId);
                    if (m.equals("xxLarge")) i3.freeXXLargeInstance(svId);
                    break;
            }
            inUseInstances.remove(subId);
        }

        return r;
    }
    public synchronized int getPPH(String t, String m) {
        switch (t){
            case "m4":
                if (m.equals("Large")) return m4.getLargePPH();
                if (m.equals("xLarge")) return m4.getXLargePPH();
                if (m.equals("xxLarge")) return m4.getXXLargePPH();
                break;
            case "r5":
                if (m.equals("Large")) return r5.getLargePPH();
                if (m.equals("xLarge")) return r5.getXLargePPH();
                if (m.equals("xxLarge")) return r5.getXXLargePPH();
                break;
            case "f1":
                if (m.equals("Large")) return f1.getLargePPH();
                if (m.equals("xLarge")) return f1.getXLargePPH();
                if (m.equals("xxLarge")) return f1.getXXLargePPH();
                break;
            case "i3":
                if (m.equals("Large")) return i3.getLargePPH();
                if (m.equals("xLarge")) return i3.getXLargePPH();
                if (m.equals("xxLarge")) return i3.getXXLargePPH();
                break;
        }
        return -1;
    }
    public synchronized void newAuction(int aucId, Auction a) {
        onlineAuctions.put(aucId, a);
    }
    public synchronized void removeAuction(int aucId) {
        onlineAuctions.remove(aucId);
    }
}
