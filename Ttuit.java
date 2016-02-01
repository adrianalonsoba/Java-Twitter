package ist.a.alonsoba;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Ttuit extends Msg {

	private String login;
	private String text;
	Tuit t;

	public Ttuit(String login, String text) {
		super(MsgType.TTUIT);
		this.login = login;
		this.text = text;
	}

	@Override
	public void send(DataOutputStream dos) {
		super.send(dos);
		int size;
		try {
			size = login.getBytes("UTF-8").length;
			dos.write(Packer.pack(size));
			dos.write(login.getBytes("UTF-8"));
			size = text.getBytes("UTF-8").length;
			dos.write(Packer.pack(size));
			dos.write(text.getBytes("UTF-8"));
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Ttuit readFrom(DataInputStream dis) {
		Ttuit m = null;
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
			m = new Ttuit(strL, strT);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}

	public String toString() {
		return MsgType.TTUIT.toString() + ":" + login + ":" + " " + text;
	}

	@Override
	public void process(ConcurrentHashMap<String, User> chm, Socket s) {
		DataOutputStream dos;
		Msg r;
		try {
			dos = new DataOutputStream(new BufferedOutputStream(
					s.getOutputStream()));
			if (chm.containsKey(login)) {
				User u = chm.get(login);
				this.t = new Tuit(login, text, 0);
				u.addTuit(this.t);
				r = new Rok();
				r.send(dos);
			} else {
				r = new Rerror("Debes loguearte antes de tuitear");
				r.send(dos);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addToQueue(BlockingQueue<Tuit> bq) {
		try {
			bq.put(this.t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
