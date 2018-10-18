// package bin;
import bin.Node;
import bin.Parseconfig;
import bin.Message;
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

// Server class
public class Server
{
    private static int nodeId;
    private static int portNum;
    private static long timeoutSec = 5;
    public static void main(String[] args) throws IOException
    {

        // ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();


        if (args.length != 3)
        {
            throw new IllegalArgumentException("Please enter port#, node ID and config file path");
        }
        
        portNum = Integer.parseInt(args[0]);
        nodeId = Integer.parseInt(args[1]);
        
        // Parse the config.txt
        Parseconfig config = new Parseconfig(nodeId, args[2]);
        
        // Process config.txt info. into myNode, shall let every process and thread can access this.
        Node myNode = new Node(config.getNodeId(), config.getMyHost(), portNum, config.getRootId());
        myNode.setNeiborNodes(config.getNeighbors());
        System.out.println("Node: "+ myNode.getNodeId());



        Thread serverFactory = new ServerFactory(myNode);
        serverFactory.start();

        // Timeout for seconds
        timeout(timeoutSec);

        if(myNode.isRoot())
        {
            myNode.setIsRoot(true);
            Thread clientT = new ClientHandler(myNode);
            clientT.start();
        }else{

        }


    }
    private static void timeout(long second){
        long start = System.currentTimeMillis();
        long end = start + second * 1000; // second * 1000 ms/sec
        while (System.currentTimeMillis() < end)
        {
            // run
        }
    }
}

// ClientHandler class 
class ServerFactory extends Thread{
    private static int portNum;
    private static int neiborhoodNum;
    Node myNode;
    public ServerFactory(Node myNode)  
    { 
        this.myNode = myNode;
    } 
    @Override
    public void run()
    {
        try{
            ServerSocket ss = new ServerSocket(this.myNode.getPort());
            while (true)  
            { 
                Socket s = null; 
                try 
                { 
                    s = ss.accept(); 
                    Thread serverT = new ServerThread(s, ss, this.myNode);
                    serverT.start();
                } 
                catch (Exception e){ 
                    s.close(); 
                    e.printStackTrace(); 
                }
            }
        }
        catch(IOException e){
            System.out.println("Can not open socket, port Number is not correct" + this.myNode.getPort());

        }      
                
    } 
}

class ServerThread extends Thread{
    private ObjectInputStream incomingMsg; 
    private ObjectOutputStream returnMsg;
    Node myNode;
    Socket s; 
    ServerSocket ss;

    Message newComingObj;
    public ServerThread(Socket s, ServerSocket ss, Node myNode)  
    { 
        this.s = s;
        this.ss = ss;
        this.myNode = myNode;
    } 
    @Override
    public void run()  
    {
        while(true)
        {
            try
            { 
                incomingMsg = new ObjectInputStream(s.getInputStream()); 
                ObjectOutputStream returnMsg = new ObjectOutputStream(s.getOutputStream());

                // System.out.println("New connection");


                newComingObj = (Message) incomingMsg.readObject();
                System.out.println("Received msg from: " + newComingObj.getOrigin().getNodeId() + ", Message: "+ newComingObj.getType());
                // queue.add(newComingObj);
                // newComingObj.getMsgAsString();

                if(!myNode.isParent() && !myNode.isRoot()){
                    System.out.println("Replied received msg to: " + newComingObj.getOrigin().getNodeId() + ", Message: PACK");

                    myNode.setParent(newComingObj.getOrigin());
                    myNode.popParentFromNeighbors(newComingObj.getOrigin());
                    // System.out.println("After pop neiborhood num" + myNode.getNeighbors().size());
                    Message msg = new Message(myNode, newComingObj.getOrigin(), "PACK");
                    returnMsg.writeObject(msg);

                    
                    Thread clientH = new ClientHandler(myNode);
                    clientH.start();
                    
                    //Brocast to neiborhood;

                }else{
                    System.out.println("Replied received msg to: " + newComingObj.getOrigin().getNodeId() + ", Message: NACK");
                    Message msg = new Message(myNode, newComingObj.getOrigin(), "NACK");
                    returnMsg.writeObject(msg);                    
                }
                // returnMsg.writeChars("PACK");
                // returnMsg.flush();
                
                // myNode.printConfig();

                break;             
                  
            }
            catch (SocketException se)
            {
                System.exit(0);
            }catch (IOException e)
            {
                e.printStackTrace();
            }catch (ClassNotFoundException cn)
            {
                cn.printStackTrace();
            }catch (NullPointerException ne)
            {

            }
        }
        try{
            s.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        } 
                      
         
    } 
}
class ClientHandler extends Thread  
{ 
    // private RoutingTable currentTable;
    private Node targetNode;
    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;
    Node myNode;
    // private static int neiborhoodNum;
    // ConcurrentLinkedQueue<Message> queue;
    // private sendCount;
  
    // Constructor 
    Message response;
    public ClientHandler(Node myNode)  
    { 
        this.myNode = myNode;
    } 
    public void sendMsg(Message msg){
        targetNode = msg.getDestination();
        try{
            // currentTable.getMsg().setDestination(targetNode);
            try {
                socket = new Socket(targetNode.getHostName(), targetNode.getPort());
                isConnected = true;
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(msg);


                try{
                    // try{
                    inputStream = new ObjectInputStream(socket.getInputStream());
                    response = (Message) inputStream.readObject();
                    // System.out.println("Message: " + response.);
                    // response.getMsgAsString();
                    // System.out.println(response.getType().getBytes());
                    // System.out.println(("PACK").getClass());
                    // System.out.println("PACK");

                    // System.out.println(response.getType().getClass());
                    // System.out.println(response.getType());

                    
                    if((String)response.getType().intern() == ("PACK").intern()){
                        //add to children
                        System.out.println("Received Pack from: " + response.getOrigin().getNodeId());

                        myNode.addToChildren(response.getOrigin());

                    }else if(response.getType().intern() == "NACK".intern()){

                        System.out.println("Received Pack from: " + response.getOrigin().getNodeId());

                    }
                    // myNode.incrementAck();

                    myNode.addAckToQueue(response);
                    // this.myNode.printConfig();

                    // }
                    // String response = (String) inputStream.readObject();

                    // System.out.println("Message: " + response);

                }catch (ClassNotFoundException cn)
                {
                    cn.printStackTrace();
                }catch (NullPointerException ne)
                {
                   ne.printStackTrace();
                } 

                // getResult maybe Pack? Nack?
                // count 

            } catch (SocketException se) {
                se.printStackTrace();
                System.out.println("Connection fail, not able connect to : " + targetNode.getHostName());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (NullPointerException e){
            System.out.println("table is null");
        }


        return;
    }
    
    @Override
    public void run()  
    {
        for(int i = 0;i < this.myNode.getNeighbors().size(); i++){
            System.out.println("Send Explore to:"+ myNode.getNeighbors().get(i).getNodeId());

            Node targetNode = this.myNode.getNeighbors().get(i);
            Message firstMessage = new Message(myNode, targetNode, "Explore");
            sendMsg(firstMessage);

        }

        // this.myNode.printConfig();

        while(true){
            // if(numOfSendCnt == this.currentTable.getConfig().getNumOfNode()-1){
            //     // System.out.println("==========End=========");
            //     // System.out.println("For node:" + this.currentTable.getNodeId());
            //     // System.out.println("Your result: "+ Arrays.toString(this.currentTable.getMsg().getDistance()));
            //     // return;

            // }
            if(myNode.getQueueSize() == myNode.getNeighborsCnt()){
                System.out.println("********End*******");
                this.myNode.printConfig();
                // System.exit(0); 
                return;
            }
            // fetchMsg = queue.poll();
            // if(fetchMsg == null){

            // }else{
            //     System.out.println("Original table:" + Arrays.toString(this.currentTable.getMsg().getDistance()));
            //     System.out.println("Going to merge this msg:" + Arrays.toString(fetchMsg.getDistance()));
            //     this.currentTable.updateTable(fetchMsg);
            //     numOfMergeTimes++;
            //     if(numOfMergeTimes == this.neiborhoodNum){
            //         System.out.println("Merge successful! Send out!:" + Arrays.toString(this.currentTable.getMsg().getDistance()));
            //         for(int i = 0;i < this.neiborhoodNum;i++){

            //             Node targetNode = this.currentTable.getConfig().getNeighbors().get(i);

            //             this.sendMsg(this.currentTable.getMsg(), targetNode);
                        
            //         }
            //         numOfMergeTimes = 0;
            //         numOfSendCnt++;

            //     }
            // }
        }

    }

} 