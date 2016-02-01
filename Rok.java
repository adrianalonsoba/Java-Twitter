package ist.a.alonsoba;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Rok extends Msg {

	public Rok() {
		super(MsgType.ROK);
	}
	
	public void send(DataOutputStream dos) {
		super.send(dos);
		try {
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(ConcurrentHashMap<String,User> chm,Socket s) {
		//no se usa
	}
	
	public String toString(){
		return MsgType.ROK.toString()+"\n";
	}
}
