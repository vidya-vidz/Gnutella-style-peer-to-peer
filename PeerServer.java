/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vidya
 */

//------------------- Creating Server for each Client using port number

class PeerServer implements Runnable {
   String config;
   Properties prop ;
    @Override
    public void run() {
       Registry registry = null;
      
      //-------------setting config file to get the port numbers from the config file
      
       config="star.properties";
       prop = new Properties();
       InputStream is = null;
      
      //--------------Opening config file and reading values from it
      
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
         try {
           // Reading the current thread name 
                String portnumber=getPortNumber((Thread.currentThread().getName()));
                int portNumber=Integer.parseInt(portnumber);
                registry = LocateRegistry.createRegistry(portNumber);
              } catch (Exception e) {
                e.printStackTrace();
            }
         try {
            Peer helloServer = new PeerClass();
           
           //--------------binding servername to the port number
           
            registry.rebind("Server", helloServer); 
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
           Thread.sleep(30000);
       } catch (InterruptedException e) {
           
           e.printStackTrace();
       }
         
    }
  
  //--------------Running the server thread
  
      public static void main(String[] args){
       PeerServer tc;
       tc=new PeerServer();
       tc.run();  
    }  

  //--------------get portNumber from the peer using config file
  
    private String getPortNumber(String string) {
        return prop.getProperty(string);
    }

   
}
