package Server;

import java.util.concurrent.locks.ReentrantLock;

public class ServerInstance {

    private String name;
    private String description;
    private int[] largeInstances;
    private int[] xLargeInstances;
    private int[] xxLargeInstances;
    private int largePPH;
    private int xLargePPH;
    private int xxLargePPH;
    private ReentrantLock lL, lxL, lxxL;

    public ServerInstance(String name, String description, int largePPH, int xLargePPH, int xxLargePPH){
        this.name = name;
        this.description = description;
        this.largePPH = largePPH;
        this.xLargePPH = xLargePPH;
        this.xxLargePPH = xxLargePPH;

        this.largeInstances = new int[3];
        this.xLargeInstances = new int[3];
        this.xxLargeInstances = new int[3];
        this.lL = new ReentrantLock();
        this.lxL = new ReentrantLock();
        this.lxxL = new ReentrantLock();
    }

    public synchronized String getName(){return name; }
    public synchronized String getDescription(){return description; }
    public synchronized int getLargePPH(){return largePPH; }
    public synchronized int getXLargePPH(){return xLargePPH; }
    public synchronized int getXXLargePPH(){return xxLargePPH; }

    public synchronized int useLargeInstance(int opt){
        for (int id=0;id<=2;id++){
            if(largeInstances[id]==0) {
                largeInstances[id]=opt;
                return id;
            }
        }
        return -1;
    }
    @SuppressWarnings("Duplicates")
    public synchronized int useXLargeInstance(int opt){
        for (int id=0;id<=2;id++){
            if(xLargeInstances[id]==0){
                largeInstances[id]=opt;
            }
            return id;
        }
        return -1;
    }
    @SuppressWarnings("Duplicates")
    public synchronized int useXXLargeInstance(int opt){
        for (int id=0;id<=2;id++){
            if(xxLargeInstances[id]==0){
                largeInstances[id]=opt;
            }
            return id;
        }
        return -1;
    }

    public synchronized void freeLargeInstance(int id){largeInstances[id]=0; }
    public synchronized void freeXLargeInstance(int id){xLargeInstances[id]=0; }
    public synchronized void freeXXLargeInstance(int id){xxLargeInstances[id]=0; }
}
