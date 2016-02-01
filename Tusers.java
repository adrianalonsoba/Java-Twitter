package ist.a.alonsoba;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class Tusers extends Msg{

	private String login;
	
	public Tusers(String login) {
		super(MsgType.TUSERS);
		this.login=login;
	}

	@Override
	public void send(DataOutputStream dos){
		super.send(dos);
		int size;
		try {
			size = login.getBytes("UTF-8").length;
			//envio el tamaño del login para saber cuanto leer
			dos.write(Packer.pack(size));
			dos.write(login.getBytes("UTF-8"));
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Tusers readFrom(DataInputStream dis){
		Tusers m = null;
		byte[] sizeBytes = new byte[4];
		try {
			dis.read(sizeBytes);
			int size = Packer.unpack(sizeBytes);
			byte[] strBytes = new byte[size];
			dis.read(strBytes);
			String str = new String(strBytes, "UTF-8");
			m = new Tusers(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}
	
	public String toString(){
		return MsgType.TUSERS.toString()+":"+login;
	}

	@Override
	public void process(ConcurrentHashMap<String,User> chm,Socket s) {
		DataOutputStream dos;
		Msg r;
			try {
				dos = new DataOutputStream(
						new BufferedOutputStream(s.getOutputStream()));
				if(chm.containsKey(login)){
					//CONDICIONES DE CARRERA?
					 Enumeration<String> keys= chm.keys();
					 String users = "";
					 while(keys.hasMoreElements()){
						 users+=" "+keys.nextElement();
					 } 
					 r= new Rusers(users);
					 r.send(dos);
				}else{
					r = new Rerror("Debes loguearte antes de pedir los usuarios");
					r.send(dos);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	
}
