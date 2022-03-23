package Tp_pro_res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMT extends Thread{
    private int numeroClient;
	public static void main(String[] args) {
		 new ServerMT().start();
	}
	@Override
	public void run() {
		try {
			ServerSocket ss=new ServerSocket(1234);
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
				pw.println("Connexion de client numéro "+numeroClient);
				pw.println("Bien venue vous etes le client de numéro "+numeroClient+" et d'adresse ip="+socket.getRemoteSocketAddress().toString());
			    while(true) {
			    	String req=br.readLine();
			    	String rep="Lenght="+req.length();
			    	pw.println(rep);
			    }
			} catch (IOException e) {
				e.printStackTrace();
			}		
		} 
	}
}
