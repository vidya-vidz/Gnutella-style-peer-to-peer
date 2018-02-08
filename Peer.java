/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author vidya
 */


//Remote interface for client

import java.rmi.Remote;
    import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;


    public interface Peer extends Remote {
        
      //--------To create remote methods we need to inherit Remote interface------------
      
       
        void setPeer(String peer);
        int query (UUID id,int time,String filename); 
        public int Obtain(String Keyword);
        public void setDirectory(String DestinationPeer, String name);
  
     }
