package bin;

// import Node;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Parseconfig {
    
    private int numOfNode;
    //private Path path;
    private Path file;
    private int nodeId;
    private ArrayList<Node> hosts;
    private ArrayList<Node> neighbors;
    private Node myNode;
    private String myHost;
    private int myPort;
    private int[] intArray;
    private int rootid;
    private Node root;
    
    public Parseconfig(int nodeId, String relativePath){
        this.nodeId = nodeId;
        this.file = Paths.get(relativePath).toAbsolutePath();

        loadConfig();
    }
    
    public int getNodeId(){
        return this.nodeId;
    }
    public int getNumOfNode() {
        return this.numOfNode;
    }
    public Node getmyNode() {
        return this.myNode;
    }   
    public ArrayList<Node> getHosts(){
        return this.hosts;
    }
    public String getMyHost(){
        return this.myHost;
    }
    public int getRootId(){
        return this.rootid;
    }
    
    public ArrayList<Node> getNeighbors() {
        return this.neighbors;
    }
    public int getNeighborsCnt() {
        return this.neighbors.size();
    }

    private void loadConfig(){
    Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            int n = 0;
            
            // to get the number of nodes (numOfNode)
            //............................................................
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("#.*",""); 
                if(line.length() == 0)
                    continue;
                numOfNode = Integer.parseInt(line);
                break;
            }
            
            // System.out.println("Number of nodes: "+ numOfNode);
            n = numOfNode;
            //............................................................
            
            // to get the root id for building a spinning tree
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("#.*",""); 
                if(line.length() == 0)
                    continue;
                rootid = Integer.parseInt(line);
                // System.out.println("RootID: "+ rootid);
                //this.root = hosts.get(rootid);
                break;
            }
      
            //System.out.println("Root node: "+ root.configToSring());
            
            hosts = new ArrayList<>(numOfNode);
            int currentId=1;
            
            // Load host list.
            while ((line = reader.readLine()) != null && n != 0) {
                line = line.replaceAll("#.*","");  
                if(line.length() == 0)
                    continue;
                
                String[] hostInfo = line.split("\\s+");
                
                Node host = new Node(currentId, hostInfo[0], Integer.parseInt(hostInfo[1]), this.rootid);
                hosts.add(host);
                //String[] neighborIds = hostInfo[2].split("\\s+");
                //System.out.println("CurrentId: "+ currentId);
                if( currentId == nodeId){
                    //System.out.println("CurrentId: "+ currentId);
                    this.myNode = hosts.get(currentId-1);
                    this.myHost = hosts.get(currentId-1).getHostName();
                    this.myPort = hosts.get(currentId-1).getPort();
                    //neighbors = new ArrayList<>();
                    //Node node = hosts.get(currentId-1);
                    //neighbors.add(node);
                    intArray=new int[hostInfo.length-2];
                    for(int i = 2; i <  hostInfo.length ; i++){
                       int id = Integer.parseInt(hostInfo[i]);
                       
                       intArray[i-2] = id;
                       //System.out.println("intArray: "+ intArray[i-2]);
                       
                    }
                 }
                
                 currentId++;
                 n--;
            }
            neighbors = new ArrayList<>();
            for(int i = 0; i < intArray.length; i++){
                int id = intArray[i];
                Node node = hosts.get(id-1);
                neighbors.add(node);
            }
            
            if(n != 0){
                throw new IOException("Insufficent valid lines in config file.");
            }
            
            
            if(neighbors == null){
                throw new NullPointerException("Expect adjacent neighbors for node " + nodeId);
            }
            
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        } catch (NullPointerException e){
            System.err.println(e.getMessage());
        }
    }
    
    // public void printConfig(int nodeID){
    //     System.out.println(String.format("-------Node %d Configuration-----", nodeId));
    //     // Print hosts 
    //     System.out.println("-----Host List-----");
    //     for(Node node : hosts){
    //         System.out.println(node.configToSring());
    //     }
        
    //     System.out.println("-----Neighbor List-----");
    //     // Print neighbors
    //     for(Node node : neighbors){
    //         System.out.println(node.configToSring());
    //     }
    //     System.out.println("-----End of Configuration-----");
    // }
  
}

