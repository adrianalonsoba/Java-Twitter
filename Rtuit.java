package ist.a.alonsoba;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Rtuit extends Msg {

	String writer;
	String text;
	int seqN;
	
	public Rtuit( String login, String text,int id) {
		super(MsgType.RTUIT);
		this.text=text;
		this.seqN=id;
		this.writer=login;
	}
	
	@Override
	public void send(DataOutputStream dos) {
		super.send(dos);
		int size;
		try {
			size = writer.getBytes("UTF-8").length;
			dos.write(Packer.pack(size));
			dos.write(writer.getBytes("UTF-8"));
			size = text.getBytes("UTF-8").length;
			dos.write(Packer.pack(size));
			dos.write(text.getBytes("UTF-8"));
			dos.write(Packer.pack(this.seqN));
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Rtuit readFrom(DataInputStream dis) {
		Rtuit m = null;
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
			String strT = new String(strBytes2, "UTF-8");
			dis.read(sizeBytes);
			size = Packer.unpack(sizeBytes);
			m = new Rtuit(strL,strT,size);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}
	
	public String toString(){
		return seqN+":"+writer+" "+text;
	}
	@Override
	public void process(ConcurrentHashMap<String, User> chm, Socket s) {

	}

}
