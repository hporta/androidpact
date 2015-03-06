package com.hugo.dome;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;



public class Client {

	public static void commande(String a) {
		
		
				Socket socket;
				PrintWriter out;
				Audio audio= new  Audio(a);
				
				//System.out.println(audio.getResultReco());
				try { socket = new Socket(InetAddress.getByName("Dynalink"),2025); // Connexion au serveur sur port 2025
				    //in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
				     //   String message_distant = in.readLine();
				      //  System.out.println(message_distant);
				        
				        out = new PrintWriter(socket.getOutputStream());
				        out.println("biere brune");
				        out.flush();
			         socket.close();   // Fermeture socket

				}catch (UnknownHostException e) {
					
					e.printStackTrace();
				}catch (IOException e) {
					
					e.printStackTrace();

		

				}
				
	}
}


