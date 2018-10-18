package bin;
import java.util.ArrayList;
import java.lang.Object;
import java.lang.Boolean;
import java.util.*;
// import java.util.*;
import java.util.concurrent.*;

public class Node implements java.io.Serializable{
    private int nodeId;
    private int port;
    private Boolean isRoot;
    private String host;

    private Boolean isParent;

    private int ackCnt;
    private ArrayList<Node> neighbors;
    private Node parent;
    private ArrayList<Node> children;
    private ConcurrentLinkedQueue<Message> queue;
    // queue = new ConcurrentLinkedQueue<Message>();

    public Node(int index, String hostName, int port, int rootId){
        this.nodeId = index;
        this.host = hostName;
        this.port = port;
        this.isRoot = (rootId == this.nodeId) ? true : false;

        this.isParent = false;
        this.ackCnt = 0;
        this.parent = null;

        this.neighbors = new ArrayList<>();
        this.children = new ArrayList<>();
        this.queue = new ConcurrentLinkedQueue<Message>();

    }
    
    public String configToSring(){
        return String.format("%d %s %s", nodeId, host, port);
    }

    public int getNodeId(){
        return this.nodeId;
    }
    
    public String getHostName(){
        return this.host;
    }
    
    public int getPort(){
        return this.port;
    }

    public ArrayList<Node> getNeighbors() {
        return this.neighbors;
    }

    public int getNeighborsCnt() {
        return this.neighbors.size();
    }

    public int getQueueSize(){
        return this.queue.size();
    }

    public Boolean isRoot(){
        return this.isRoot;
    }

    public Boolean isParent(){
        return this.isParent;
    }

    // public void setIsParent(Boolean value){
    //     this.isParent = value;
    // }

    public ArrayList<Node> popParentFromNeighbors(Node object){
        // this.neighbors.remove(object);
        // return this.neighbors;
        // System.out.println("-----parent host name-----" + object.getHostName());

        for(int i = 0;i < this.getNeighborsCnt(); i++){
            // System.out.println("-----nei host name-----" + this.neighbors.get(i).getHostName());

            if(object.getHostName().intern() == this.neighbors.get(i).getHostName().intern() ){ // Why can not compare port number?
                this.neighbors.remove(i);
                System.out.println("-----Pop parent from nei-----");

            }
            // Node targetNode = this.myNode.getNeighbors().get(i);
            // Message firstMessage = new Message(myNode, targetNode, "Explore");
            // sendMsg(firstMessage);
        }
        return this.neighbors;
    }

    public void addAckToQueue(Message msg){
        this.queue.add(msg);
        return;
    }


    public void incrementAck(){
        this.ackCnt++;
        return;
    }

    public void setIsRoot(Boolean value){
        this.isRoot = value;
    }

    public void setNeiborNodes(ArrayList<Node> neighbors){
        this.neighbors = neighbors;
    }

    public void addToChildren(Node newNode){
        this.children.add(newNode);
    }

    public void setParent(Node newNode){
        this.isParent = true;
        this.parent = newNode;
    }

    public void printConfig(){
        System.out.println(String.format("-------Node %d Configuration-----", this.nodeId));
        // Print hosts 
        // System.out.println("-----Host List-----");
        // for(Node node : hosts){
        //     System.out.println(node.configToSring());
        // }
        
        System.out.println("-----Neighbor List-----");
        // Print neighbors
        for(Node node : this.neighbors){
            System.out.println(node.configToSring());
        }

        if(!this.isRoot()){
            System.out.println("-----Parent List-----");
            // Print neighbors
            System.out.println(this.parent.configToSring());
        }


        System.out.println("-----Children List-----");
        // Print neighbors

        for(Node node : this.children){
            System.out.println(node.configToSring());
        }

        System.out.println("-----End of Configuration-----");
    }
}   
