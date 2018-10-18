package bin;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.Serializable;


public class Message implements Serializable{

    private Node origin;
    private Node destination;
    // int [] distance;
    private byte[] type;
    // public int[] getDistance(){
    //     return this.distance;
    // }
    public Message(Node newOrg, Node newDst, String str)
    {   
        this.origin = newOrg;
        this.destination = newDst;
        this.type = str.getBytes();

    }

    public Node getOrigin(){
        return this.origin;
    }

    public Node getDestination(){
        return this.destination;
    }

    public String getType(){
        return (new String(this.type));
    }

    public void getMsgAsString(){
        System.out.println("origin:"+origin.getNodeId()+" to "+destination.getNodeId()+",Msg:" + new String(this.type));
        // System.out.println("origin:"+origin.getNodeId()+" to "+destination.getNodeId());

        return;
    }
    // public void setDestination(Node destination){
    //     this.destination = destination;
    //     // System.out.println("after setting new dest:" + this.destination.getNodeId());
    //     return;
    // }
    // public void setDistance(int[] newDistance){
    //     this.distance = newDistance;
    //     return;
    // }

}

    