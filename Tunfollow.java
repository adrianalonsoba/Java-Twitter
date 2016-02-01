package ist.a.alonsoba;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Tunfollow extends Msg {

	//login de quien envia la peticion
	private String login; 
	//login de la persona a la que se quiere dejar de seguir
	private String unfLogin;
	
	Tunfollow(String myLogin, String unfLogin) {
		super(MsgType.TUNFOLLOW);
		this.login=myLogin;
		this.unfLogin=unfLogin;
	}

	@Override
	public void send(DataOutputStream dos){
		super.send(dos);
		int size;
		try {
			size = login.getBytes("UTF-8").length;
			dos.write(Packer.pack(size));
			dos.write(login.getBytes("UTF-8"));
			size = unfLogin.getBytes("UTF-8").length;
			dos.write(Packer.pack(size));
			dos.write(unfLogin.getBytes("UTF-8"));
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Tunfollow readFrom(DataInputStream dis){
		Tunfollow m = null;
		byte[] sizeBytes = new byte[4];
		int size;
		try {
			dis.read(sizeBytes);
			size = Packer.unpack(sizeBytes);
			byte[] strBytes1 = new byte[size];
			dis.read(strBytes1);
			String strL = new String(strBytes1, "UTF-8");
			dis.read(sizeBytes);
			size = Packer.unpack(sizeBytes);
			//ME DA UN ERROR AQUI AL HACER DOS UNFOLLOW CON EL MISMO CLIENTE
			byte[] strBytes2 = new byte[size];
			dis.read(strBytes2);
			String strF = new String(strBytes2, "UTF-8");
			m = new Tunfollow(strL, strF);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}
	
	public String toString(){
		return MsgType.TUNFOLLOW.toString()+":"+login+" "+unfLogin;
	}

	@Override
	public void process(ConcurrentHashMap<String,User> chm,Socket s) {
		DataOutputStream dos;
		Msg r;
		try {
			dos = new DataOutputStream(
					new BufferedOutputStream(s.getOutputStream()));
			if (!chm.containsKey(login)) {
				r = new Rerror("No puedes hacer unfollow sin estar logueado");
				r.send(dos);
			}else if(!chm.containsKey(unfLogin)){
				r = new Rerror("No existe el usuario:"+unfLogin);
				r.send(dos);
			}else{
				User u1 = chm.get(login);
				u1.rmFollow(unfLogin);
				User u2 = chm.get(unfLogin);
				u2.rmFollower(login);
				r= new Rok();
				r.send(dos);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
