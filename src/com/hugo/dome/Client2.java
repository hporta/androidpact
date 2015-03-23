package com.hugo.dome;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client2 {
	 public static void Commande2() throws IOException 
	    { 
	        Socket sock = new Socket(InetAddress.getByName("Dynalink"),2026);
	    
	        Commun.transfert(
	                new FileInputStream("/mnt/sdcard/imageoutput.jpg"),
	                sock.getOutputStream(),
	                true);
	        
	        sock.close();
	    } 

}
