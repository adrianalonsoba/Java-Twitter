package ist.a.alonsoba;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Tretuit extends Msg {

	private String login;
	private int seqN;
	Tuit rt;
	
	public Tretuit(String login, int tuitNum){
		super(MsgType.TRETUIT);
		this.login=login;
		this.seqN=tuitNum;
	}
	
	@Override
	public void send(DataOutputStream dos){
		super.send(dos);
		int size;
		try {
			size = login.getBytes("UTF-8").length;
			dos.write(Packer.pack(size));
			dos.write(login.getBytes("UTF-8"));
			dos.write(Packer.pack(seqN));
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Tretuit readFrom(DataInputStream dis){
		Tretuit m = null;
		byte[] sizeBytes = new byte[4];
		byte[] retNBytes = new byte[4];
		try {
			dis.read(sizeBytes);
			int size = Packer.unpack(sizeBytes);
			byte[] strBytes = new byte[size];
			dis.read(strBytes);
			String str = new String(strBytes, "UTF-8");
			dis.read(retNBytes);
			int retNum= Packer.unpack(retNBytes);
			m = new Tretuit(str,retNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}
	
	
	public String toString(){
		return MsgType.TRETUIT.toString()+":"+login+" "+seqN;
	}

	@Override
	public void process(ConcurrentHashMap<String,User> chm,Socket s) {
		DataOutputStream dos;
		Msg r;
		try {
			dos = new DataOutputStream(new BufferedOutputStream(
					s.getOutputStream()));
			if (chm.containsKey(login)) {
				User u = chm.get(login);
				Tuit t = u.getTuit(seqN-1);
				if(t!=null){
					t.incRet();
					t.setOwner(login);
					this.rt=t;
					r= new Rok();
					r.send(dos);
				}else{
					r = new Rerror("No existe el numero de tuit: "+seqN);
					r.send(dos);
				}
			}else{
				r = new Rerror("Debes loguearte antes de retuitear");
				r.send(dos);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addToQueue(BlockingQueue<Tuit> bq) {
		try {
			bq.put(this.rt);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
