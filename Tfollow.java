package ist.a.alonsoba;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Tfollow extends Msg{

	//login de quien envia la peticion
	private String login; 
	//login de la persona a la que se quiere seguir
	private String folLogin;
	
	Tfollow(String myLogin, String folLogin) {
		super(MsgType.TFOLLOW);
		this.login=myLogin;
		this.folLogin=folLogin;
	}

	@Override
	public void send(DataOutputStream dos){
		super.send(dos);
		int size;
		try {
			size = login.getBytes("UTF-8").length;
			dos.write(Packer.pack(size));
			dos.write(login.getBytes("UTF-8"));
			size = folLogin.getBytes("UTF-8").length;
			dos.write(Packer.pack(size));
			dos.write(folLogin.getBytes("UTF-8"));
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Tfollow readFrom(DataInputStream dis){
		Tfollow m = null;
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
			byte[] strBytes2 = new byte[size];
			dis.read(strBytes2);
			String strF = new String(strBytes2, "UTF-8");
			m = new Tfollow(strL, strF);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;	
	}
	
	public String toString(){
		return MsgType.TFOLLOW.toString()+":"+login+" "+folLogin;
	}

	@Override
	public void process(ConcurrentHashMap<String,User> chm,Socket s) {
		DataOutputStream dos;
		Msg r;
		try {
			dos = new DataOutputStream(
					new BufferedOutputStream(s.getOutputStream()));
			if (!chm.containsKey(login)) {
				r = new Rerror("No puedes hacer follow sin estar logueado");
				r.send(dos);
			}else if(!chm.containsKey(folLogin)){
				r = new Rerror("No existe el usuario:"+folLogin);
				r.send(dos);
			}else if(login.equals(folLogin)){
				r = new Rerror("No puedes seguirte a ti mismo");
				r.send(dos);
			}else{
				User u1 = chm.get(login);
				if(!u1.isFollowing(folLogin)){
					u1.addFollow(folLogin);
					User u2 = chm.get(folLogin);
					u2.addFollower(login);
				}
				r = new Rok();
				r.send(dos);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
