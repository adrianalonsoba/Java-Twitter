package ist.a.alonsoba;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class CliHandler implements Runnable {

	
	private Socket s;
	private ConcurrentHashMap<String,User> chm;
	DataInputStream dis;
	BlockingQueue<Tuit> bq;
	
	public CliHandler(Socket s,ConcurrentHashMap<String,User> chm,BlockingQueue<Tuit> bq){
		this.s=s;
		this.chm=chm;
		this.bq=bq;
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
		while(true){
			Msg m= Msg.receive(dis);
			if (m!=null){
				if(m.type.equals(MsgType.TTUIT)){
					m.process(chm,s);
					if(((Ttuit)m).t!=null){
						((Ttuit)m).addToQueue(bq);	
					}
				}else if(m.type.equals(MsgType.TRETUIT)){
					m.process(chm,s);
					if(((Tretuit)m).rt!=null){
						((Tretuit)m).addToQueue(bq);
					}
				}else{
					m.process(chm,s);
				}
				synchronized(chm){
					System.out.print(chm.toString());}
			}else{
				/*try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				*/
			}
		}
	}
}
