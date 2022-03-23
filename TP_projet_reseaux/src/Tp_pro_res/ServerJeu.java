package Tp_pro_res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import Tp_pro_res.ServerMT.Conversation;

public class ServerJeu extends Thread{
    private int numeroClient;
    private int nombreSecret;
    private boolean fin;
    private String gangnant;
	public static void main(String[] args) {
		 new ServerJeu().start();
	}
	@Override
	public void run() {
		try {
			ServerSocket ss=new ServerSocket(1234);
			nombreSecret=new Random().nextInt(1000); //nombre int entre 0 et 1000
			System.out.println("Le nombre aléatoire est "+nombreSecret);
			while(true) {
				System.out.println("J'attend la connexion d'un client ..");
				Socket s=ss.accept();
				++numeroClient;
				new Conversation(numeroClient,s).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	class Conversation extends Thread{
		private int numeroClient;
		private Socket socket;
		public Conversation(int numeroClient, Socket socket) {
			super();
			this.numeroClient = numeroClient;
			this.socket = socket;
		}
		@Override
		public void run() {
			try {
				InputStream is=socket.getInputStream();
				InputStreamReader isr=new InputStreamReader(is);
				BufferedReader br=new BufferedReader(isr);
				
				PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);
				System.out.println("Connexion de client numéro "+numeroClient);
				pw.println("Bien venue vous etes le client de numéro "+numeroClient+" et d'adresse ip="+socket.getRemoteSocketAddress().toString());
			    pw.println("Devinez le nombre secret....?");
				while(true) {
			    	String req=br.readLine();
			    	int nombre=0;
			    	boolean correctReq=false;
			    	try {
			    		nombre=Integer.parseInt(req);
			    		correctReq=true;
			    	}catch(NumberFormatException e) {
			    		correctReq=false;
			    	}
			    	if(correctReq==true) {
			    	System.out.println("Client "+socket.getRemoteSocketAddress().toString()+" Tentative avec le nombre: "+nombre);
			    	if(fin==false) {
			    		if(nombre>nombreSecret) {
			    			pw.println("Votre nombre est superieur au nombre secret");
			    		}else if(nombre<nombreSecret) {
			    			pw.println("Votre nombre est inferieur au nombre secret");
			    		}else {
			    			pw.println("Bravo, vous avez gagné");
			    			gangnant=socket.getRemoteSocketAddress().toString();
			    			System.out.println("BRAVO au gagnant, IP Client "+gangnant);
			    		    fin=true;
			    		}
			    	}else {
			    		pw.println("Jeu terminé, le gangnant est "+gangnant);
			    	}
			    	}else {
			    		pw.println("Format de nombre incorrect");
			    	}
			    }
			} catch (IOException e) {
				e.printStackTrace();
			}		
		} 
	}
}