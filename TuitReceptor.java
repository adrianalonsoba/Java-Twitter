package ist.a.alonsoba;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class TuitReceptor implements Runnable {

	Socket s;
	DataInputStream dis;

	public TuitReceptor(Socket s){
		this.s=s;
		createStreams();
	}
	
	private void createStreams() {
		try {
			dis = new DataInputStream(new BufferedInputStream(this.s.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		Msg m;
		while (true){
			m = Msg.receive(dis); 
			if (m == null) {
				break;
			}
		}
	}

}
