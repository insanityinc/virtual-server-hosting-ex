package Server;
import java.time.LocalDateTime;

public class Subscription {

    private int id;
    private String user;
    private String instanceType;
    private String instanceModel;
    private int svId;
    private LocalDateTime init;

    public Subscription(int id, String user, String instanceType, String instanceModel, int svId) {
        this.id = id;
        this.user = user;
        this.instanceType = instanceType;
        this.instanceModel = instanceModel;
        this.svId = svId;
        this.init = LocalDateTime.now();
    }

    public int getId(){return id; }
    public String getUser(){return user; }
    public String getInstanceType(){return instanceType; }
    public String getInstanceModel(){return instanceModel; }
    public int getSvId(){return svId; }
    public LocalDateTime getInit(){return init; }

    @Override
    public String toString() {
        return "Subscription-> " +
                "id=" + id +
                " user=" + user +
                " instanceType=" + instanceType +
                " instanceModel=" + instanceModel +
                " sv id=" + svId +
                " date=" + init;
    }
}
