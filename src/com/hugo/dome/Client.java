package com.hugo.dome;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;



public class Client {
				
	 public static void Commande2() throws IOException 
	    { 
	        Socket sock = new Socket(InetAddress.getByName("Dynalink"),2027);
	    
	        Commun.transfert(
	                new FileInputStream("/mnt/sdcard/videorecording.mp4"),
	                sock.getOutputStream(),
	                true);
	        
	        sock.close();
	    } 
}


