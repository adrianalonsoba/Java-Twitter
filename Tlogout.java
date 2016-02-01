package ist.a.alonsoba;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class Tlogout extends Msg {

	String login;
	
	Tlogout(String login) {
		super(MsgType.TLOGOUT);
		this.login=login;
	}

	@Override
	public void send(DataOutputStream dos) {
		super.send(dos);
		int size;
		try {
			size = login.getBytes("UTF-8").length;
			dos.write(Packer.pack(size));
			dos.write(login.getBytes("UTF-8"));
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Tlogout readFrom(DataInputStream dis) {
		Tlogout m = null;
		byte[] sizeBytes = new byte[4];
		try {
			dis.read(sizeBytes);
			int size = Packer.unpack(sizeBytes);
			byte[] strBytes = new byte[size];
			dis.read(strBytes);
			String str = new String(strBytes, "UTF-8");
			m = new Tlogout(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}
	
	@Override
	public void process(ConcurrentHashMap<String, User> chm, Socket s) {
		User u= chm.get(login);
		LinkedList<String> follows = u.getFollows();
		for(String follow: follows){
			User u1 = chm.get(follow);
			u1.rmFollower(login);
		}
		chm.remove(login);
	}
	
	public String toString() {
		return MsgType.TLOGOUT.toString() + ":" + login;
	}

}
