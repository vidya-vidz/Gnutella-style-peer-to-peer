/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author vidya
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

    

public class PeerClass implements Peer,Serializable{

         String Peername;
         String Dir;
         String SourceDir;
         String DestDir;
         ArrayList<UUID> ids=new ArrayList<>();
    
		//--------Query is send to the neighbouring peers to find a file  in its local file system
			 
		@Override
    public int query(UUID id, int time, String filename) {
        if(CheckUID(id)==1){
            return 0;
        }
        else
        {
        CheckTTL(time);
				Dir=setDir();
				int value=Find(filename);
        if(value==1){
            return 1;
        }
        }
        return 0;
     }
			 
			 //-----------lists all the files in the dir of local file system and find the required file

    private int Find(String filename) {
       File folder = new File(Dir);
       File[] listOfFiles = folder.listFiles();
       for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if(listOfFile.getName().equals(filename)){
                    return 1;
                }
            }
        }
        return 0;
    }
			 
			 //----------Check the Time to live for each peer 
    
		public void CheckTTL(int time){
			time=time-1;
		}
			 
			//------Set the peer id for each peer
			 
    @Override
    public void setPeer(String peer) {
        Peername=peer;

    }
    
			 //--------set the dir for each peer
			 
    String setDir(){
        switch("peer"+Peername){
            case "peerone": return "Peer1";
                            
            case "peertwo":return "Peer2";
                            
            case "peerthree": return "Peer3";
                            
            case "peerfour": return "Peer4";
                            
            case "peerfive": return "Peer5";
                            
            case "peersix": return "Peer6";
                            
            case "peerseven": return "Peer7";
                            
            case "peereight":return "Peer8";
                            
            case "peernine": return "Peer9";
                            
            
                
        }
        return "";
    }
			 //--------overloading method to set Dir for each peer 
			 
    String setDir(String dir){
        switch("peer"+dir){
            case "peerone": return "Peer1";
                            
            case "peertwo":return "Peer2";
                            
            case "peerthree": return "Peer3";
                            
            case "peerfour": return "Peer4";
                            
            case "peerfive": return "Peer5";
                            
            case "peersix": return "Peer6";
                            
            case "peerseven": return "Peer7";
                            
            case "peereight":return "Peer8";
                            
            case "peernine": return "Peer9";
                            
            
                
        }
        return "";
    }
			
			 //-----------Downloading file from the particular directory
			 
    @Override
    public int Obtain(String Keyword) {
        
        
        SourceDir=setDir(SourceDir);
        DestDir=setDir(DestDir);
				System.out.println(SourceDir);
				System.out.println(DestDir);
        InputStream input = null;
				OutputStream output = null;
	         
             try {
                 input = new FileInputStream(SourceDir+"/"+Keyword);
             } catch (FileNotFoundException ex) {
                 Logger.getLogger(PeerClass.class.getName()).log(Level.SEVERE, null, ex);
             }
             
             try {
                 output = new FileOutputStream(DestDir+"/"+Keyword);
             } catch (FileNotFoundException ex) {
                 Logger.getLogger(PeerClass.class.getName()).log(Level.SEVERE, null, ex);
             }
               
						byte[] buf = new byte[1024];
						int bytesRead;
             try {
                 while ((bytesRead = input.read(buf)) > 0) {
                     output.write(buf, 0, bytesRead);
                 }
                 return 1;
             } catch (IOException ex) {
                 Logger.getLogger(PeerClass.class.getName()).log(Level.SEVERE, null, ex);
             }
	
        return 0;

    }

			 //-------Setting Destination directory and file name
			 
    @Override
    public void setDirectory(String DestinationPeer, String name) {
        SourceDir=DestinationPeer;
        DestDir=name;

    }

			 //------------Checking Message id for each peer
			 
    private int CheckUID(UUID id) {
        if(ids.isEmpty()){
          ids.add(id);
          //System.out.println(ids);
          return 0;
        }
        else{
            for (UUID ides:ids) {
                if(ides.equals(id))
                    return 1;
                
            }
            System.out.println(ids);
        }
        return 0;
		}
    

  }