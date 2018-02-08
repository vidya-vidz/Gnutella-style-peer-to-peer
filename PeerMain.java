/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author vidya
 */
public class PeerMain {


    public static void main(String[] args) throws IOException {
       
        PeerClient pc=new PeerClient();
        PeerServer ps=new PeerServer();
        
        //--------------Creating a Client-Server architecture using Threads
			
	//Taking peer name as an arguments
        String abc=args[0];
				
	//-----------------Creating two Thread to make client and server
			
        Thread t1=new Thread(pc,abc);
        Thread t2=new Thread(ps,abc);
        
        
        t2.start();// starting server thread
      
        t1.start();//starting client thread
       
       
             
    }
}
