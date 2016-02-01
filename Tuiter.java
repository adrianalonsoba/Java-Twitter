package ist.a.alonsoba;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Tuiter {

	private Socket s;
	private  DataOutputStream dos;
	private  DataInputStream dis;
	private  String login;
	
	public Tuiter(String host, int PORT) {
		try {
			s = new Socket(host, PORT);
		} catch (Exception e) {
			System.err.println("error: " + e);
			System.exit(-1);
		}
		createStreams();
	}

	private void createStreams() {
		try {
			dos =new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
			dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public String toString() {
		return s.toString();
	}
	
	public void close(){
		try {
			dis.close();
			dos.close();
			s.close();

		} catch (Exception e) {
			System.err.println("error: " + e);
		}
	}

	/**
	 * Procesa el comando introducido
	 */
	private void commProc(String comm) {
		String type = comm.split(" ")[0];
		String name;
		Msg m;
		Msg r;
		
		switch (type) {
			case "login":
				name= comm.split(" ")[1];
				m= new Tlogin(name);
				m.send(dos);
				r= Msg.receive(dis);
				if(r.type.equals(MsgType.ROK)){
					login=name;
					Thread t = new Thread(new TuitReceptor(s));
					t.start();
				}
				break;
			case "follow":
				m= new Tfollow(login,comm.split(" ")[1]);
				m.send(dos);
				break;
			case "users":
				m=new Tusers(login);
				m.send(dos);
				break;
			case "unfollow":
				m= new Tunfollow(login, comm.split(" ")[1]);
				m.send(dos);
				break;
			case "retuit":
				m= new Tretuit(login,Integer.parseInt(comm.split(" ")[1]));
				m.send(dos);
				break;
			case "tuit":
				m= new Ttuit(login,comm.substring(4));
				m.send(dos);
				break;
			default:
				System.out.println(type + " no es un comando válido \n");
		}
	}
	
	public void sendLogout(){
		if(login==null){
			System.out.println("Debes loguearte antes de hacer nada");
		}else{
			Msg m = new Tlogout(login);
			m.send(dos);
		}
	}

	public static void main(String[] args) {

		if (args.length != 1) {
			throw new RuntimeException(
					"El numero de argumentos es 1: <maquina>:<puerto>");
		}
		String[] st = args[0].split(":");
		String host = st[0];
		int port = Integer.parseInt(st[1]);
		Tuiter t = new Tuiter(host, port);
		System.out.print("Tuiter atado a " + host + ":" + port + "\n");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String line = br.readLine();
			while (line != null) {
				t.commProc(line);
				line = br.readLine();
			}
		} catch (Exception e) {
			System.err.println("error: " + e);
		}
		t.sendLogout();
		t.close();
		System.out.println("Cliente cerrado");	
	}

}
