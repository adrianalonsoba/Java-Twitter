package ist.a.alonsoba;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Tlogin extends Msg {

	String login;

	public Tlogin(String login) {
		super(MsgType.TLOGIN);
		this.login = login;
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

	public static Tlogin readFrom(DataInputStream dis) {
		Tlogin m = null;
		byte[] sizeBytes = new byte[4];
		try {
			dis.read(sizeBytes);
			int size = Packer.unpack(sizeBytes);
			byte[] strBytes = new byte[size];
			dis.read(strBytes);
			String str = new String(strBytes, "UTF-8");
			m = new Tlogin(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}

	public String toString() {
		return MsgType.TLOGIN.toString() + ":" + login;
	}

	@Override
	public void process(ConcurrentHashMap<String, User> chm, Socket s) {
		DataOutputStream dos;
		Msg r;
		try {
			dos = new DataOutputStream(
					new BufferedOutputStream(s.getOutputStream()));
			if (!chm.containsKey(login)) {
				User u = new User(login, s);
				chm.put(login, u);
				r = new Rok();
				r.send(dos);
			}else{
				r = new Rerror("El login "+login+" ya esta en el servidor");
				r.send(dos);
			}
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

}
