 package Tp_pro_res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerChat extends Thread{
    private int numeroClient;
    private List<Conversation> cliens=new ArrayList<Conversation>();
	public static void main(String[] args) {
		 new ServerChat().start();
	}
	@Override
	public void run() {
		try {
			ServerSocket ss=new ServerSocket(1234);
			while(true) {
				//System.out.println("J'attend la connexion d'un client ..");
				Socket s=ss.accept();
				++numeroClient;
				Conversation conversation=new Conversation(numeroClient,s);
				cliens.add(conversation);
				conversation.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	class Conversation extends Thread{
		protected int numeroClient;
		protected Socket socket;
		public Conversation(int numeroClient, Socket socket) {
			this.numeroClient = numeroClient;
			this.socket = socket;
		}
		public void broadcastMessage(String req, Socket socket, int numeroClient) throws IOException { 
			for(Conversation client:cliens) {
				if(client.socket!=socket) {
					if(client.numeroClient==numeroClient || numeroClient==-1) {
						PrintWriter pw=new PrintWriter(client.socket.getOutputStream(),true);
						pw.println(req);
					}
				}
			}
		}
		@Override
		public void run() {
			try {
				InputStream is=socket.getInputStream();
				InputStreamReader isr=new InputStreamReader(is);
				BufferedReader br=new BufferedReader(isr);
				
				PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);
				System.out.println("Connexion de client numéro "+numeroClient+", IP "+socket.getRemoteSocketAddress().toString());
				pw.println("Bien venue vous etes le client de numéro "+numeroClient);
			    while(true) {
			    	String req=br.readLine();
			    	if(req.contains("=>")) {
			    		String[] requestParams=req.split("=>");
			    		if(requestParams.length==2);
			    		String message=requestParams[1];
			    		int numeroClient=Integer.parseInt(requestParams[0]);
			    		broadcastMessage(message,socket,numeroClient);
			    	}else {
			    		broadcastMessage(req,socket,-1);
			    	}	    	
			    }
			} catch (IOException e) {
				e.printStackTrace();
			}		
		} 
	}
}
