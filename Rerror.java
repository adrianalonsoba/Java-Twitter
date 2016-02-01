package ist.a.alonsoba;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Rerror extends Msg {

	String info;
	Rerror(String info) {
		super(MsgType.RERROR);
		this.info=info;
	}
	
	@Override
	public void send(DataOutputStream dos) {
		super.send(dos);
		int size;
		try {
			size = info.getBytes("UTF-8").length;
			dos.write(Packer.pack(size));
			dos.write(info.getBytes("UTF-8"));
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Rerror readFrom(DataInputStream dis) {
		Rerror m = null;
		byte[] sizeBytes = new byte[4];
		try {
			dis.read(sizeBytes);
			int size = Packer.unpack(sizeBytes);
			byte[] strBytes = new byte[size];
			dis.read(strBytes);
			String str = new String(strBytes, "UTF-8");
			m = new Rerror(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}
	
	@Override
	public void process(ConcurrentHashMap<String, User> chm, Socket s) {
		//no se usa
	}
	
	public String toString(){
		return MsgType.RERROR.toString()+":"+info+"\n";
		
	}
}
