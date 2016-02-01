package ist.a.alonsoba;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Rusers extends Msg {

	private String users;
	
	Rusers(String users) {
		super(MsgType.RUSERS);
		this.users=users;
	}
	
	@Override
	public void send(DataOutputStream dos) {
		super.send(dos);
		int size;
		try {
			size = users.getBytes("UTF-8").length;
			dos.write(Packer.pack(size));
			dos.write(users.getBytes("UTF-8"));
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Rusers readFrom(DataInputStream dis) {
		Rusers m = null;
		byte[] sizeBytes = new byte[4];
		try {
			dis.read(sizeBytes);
			int size = Packer.unpack(sizeBytes);
			byte[] strBytes = new byte[size];
			dis.read(strBytes);
			String str = new String(strBytes, "UTF-8");
			m = new Rusers(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}
	public String toString(){
		return MsgType.RUSERS.toString()+":"+users;
	}

	@Override
	public void process(ConcurrentHashMap<String, User> chm, Socket s) {
		//no se usa
	}

}
