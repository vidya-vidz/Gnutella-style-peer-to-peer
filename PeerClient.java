


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */





/**
 *
 * @author vidya
 */



class PeerClient implements Runnable {
   String config;
   Properties prop ;
   ArrayList<String> Peerfound = new ArrayList<>();
   String hostName="localhost";
  
  //-----------------overriding run() to act as thread
  
   @Override
    public void run() {
       
       String options="Select an option\n"+"1.Search\n"+"2.exit";
       int optionsselect;
       String Keyword;
       config=setConfigfile();
       System.out.println(config);
       prop = new Properties();
       InputStream is = null;
      
      //------------opening config file to read portnumber from the file
      
       try {
           is = new FileInputStream(config);
       } catch (FileNotFoundException ex) {
           Logger.getLogger(PeerClient.class.getName()).log(Level.SEVERE, null, ex);
       }
       try {
           prop.load(is);
       } catch (IOException ex) {
           Logger.getLogger(PeerClient.class.getName()).log(Level.SEVERE, null, ex);
       }
      //-----------------get portnumber from the peer
      
       String portnumber=getPortNumber((Thread.currentThread().getName()));
       String neighbour=getNeighbour("peer"+(Thread.currentThread().getName()));
       String neighbour1=getNeighbour("peer"+(Thread.currentThread().getName()));
      
      int TTL;
       
      while(true){
        System.out.println(options);
       Scanner sc=new Scanner(System.in);
       optionsselect=sc.nextInt();
        switch(optionsselect){
           case 1:
            //----------start file search
            
               System.out.println("Enter file name to search:");
               Keyword=sc.next();
            
            //-----------------generate random id
                        
               UUID UID=UUID.randomUUID();
               ArrayList<String> neigh_list = new ArrayList<>();
               
               ArrayList<String> neigh_list1 = new ArrayList<>();
               StringTokenizer st = new StringTokenizer(neighbour,",");  
               while (st.hasMoreTokens()) {  
               neigh_list.add(getPortNumber(st.nextToken()));  
               }
               //System.out.println(neigh_list);
               StringTokenizer st1=new StringTokenizer(neighbour1,",");
               while(st1.hasMoreTokens()){
                   neigh_list1.add(st1.nextToken());
               }
               //System.out.println(neigh_list1);
                for(int i=0;i<neigh_list.size();i++){
                   int neigh_portnumber=Integer.parseInt(neigh_list.get(i));
            try {
              //-------------Connecting to server using portnumber
                Registry registry=LocateRegistry.getRegistry(hostName,neigh_portnumber);
                Peer peer=(Peer)registry.lookup("Server");
                peer.setPeer(neigh_list1.get(i));
                int found=peer.query(UID, 4, Keyword);
                if(found==1){
                  //if file is found on other peers it will check other ppers also
                    Peerfound.add(neigh_list1.get(i));
                    Checkneighbours("peer"+neigh_list1.get(i),UID,Keyword);
                }
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(PeerClient.class.getName()).log(Level.SEVERE, null, ex);
            } 
            }
            
               Set<String> p = new LinkedHashSet<String>(Peerfound);
               Peerfound.clear();
               Peerfound.addAll(p);
            
               //---------------Displays file on other peers
            
               System.out.println("File: "+Keyword+" is found on the following peers:");
               System.out.println(Peerfound);
            
               //----------------Selects the peer you wanted to connect
            
            System.out.println("Select the peer you wanted to connect to:");
            String DestinationPeer=sc.next();
            int Destinationport=Integer.parseInt(getPortNumber(DestinationPeer));
            Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(hostName,Destinationport);
        } catch (RemoteException ex) {
            Logger.getLogger(PeerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        {
            try {
                Peer peer=(Peer)registry.lookup("Server");
                peer.setDirectory(DestinationPeer,Thread.currentThread().getName());
              //-------------obtain a file from the other servers  
              int sucess=peer.Obtain(Keyword);
                if(sucess==1)
                    System.out.println("Sucessfull downloaded");
                else
                    System.out.println("Download unsuccessfull");
            } catch (RemoteException ex) {
                Logger.getLogger(PeerClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(PeerClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
               
               break;
           case 2:System.exit(0);
                  break;
           default:System.out.println("Wrong selection");
        }
       }
       
       
        
        
    }
      //------------ running the thread using run()
      public static void main(String[] args){
       PeerClient tc;
       tc=new PeerClient();
       tc.run();  
    }  
  
    //----------------Setting config file 
    private String setConfigfile() {
        System.out.println("Select an option:\n1.Star Network\n2.Mesh Network\n3.exit");
        Scanner sc=new Scanner(System.in);
        int options=sc.nextInt();
        if(options==1){
            return "star.properties";
        }
        else if(options==2){
            return "mesh.properties";
        }
        else{
            System.exit(0);
        }
        return "";    }
    
    //-------------------get port numbers from the config file
  
    private String getPortNumber(String string) {
        return prop.getProperty(string);
    }
    
  //get neighbours list from peers
    private String getNeighbour(String string) {
        return prop.getProperty(string);
    }
 
  //-----------------------Check wheather neighbours have file or not

    private void Checkneighbours(String neigh,UUID UID,String Keyword) {
        
        String neighbou=getNeighbour(neigh);
        String neighbour1=getNeighbour(neigh);
        ArrayList<String> neighlist = new ArrayList<>();
        ArrayList<String> neighlist1 = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(neighbou,",");  
        while (st.hasMoreTokens()) {  
               neighlist.add(getPortNumber(st.nextToken()));  
        }
        StringTokenizer st1=new StringTokenizer(neighbour1,",");
               while(st1.hasMoreTokens()){
                   neighlist1.add(st1.nextToken());
               }
        for(int i=0;i<neighlist.size();i++){
                   int neigh_portnumber=Integer.parseInt(neighlist.get(i));
            try {
                Registry registry=LocateRegistry.getRegistry(hostName,neigh_portnumber);
                Peer peer=(Peer)registry.lookup("Server");
                peer.setPeer( neighlist1.get(i));
                int found=peer.query(UID, 4, Keyword);
                if(found==1){
                    Peerfound.add(neighlist1.get(i));
                }
            } catch (RemoteException ex) {
                Logger.getLogger(PeerClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(PeerClient.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
   }
}
