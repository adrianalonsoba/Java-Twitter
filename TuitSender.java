package ist.a.alonsoba;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class TuitSender implements Runnable {

	private BlockingQueue<Tuit> bq;
	private ConcurrentHashMap<String,User> chm;
	
	public TuitSender(BlockingQueue<Tuit> bq, ConcurrentHashMap<String,User> chm ){
		this.bq=bq;
		this.chm=chm;
	}
	
	@Override
	public void run() {
		Tuit t;
		DataOutputStream dos;
		while (true){
			 try {
				 t = bq.take();
				 String owner = t.getLogin();
				 User u= this.chm.get(owner);
				 int tuitIndex=u.getTuitIndex(t);
				 Socket so= u.getSocket();
				 try {
					 dos=new DataOutputStream(
								new BufferedOutputStream(so.getOutputStream()));
					 Rtuit m= new Rtuit(t.getWriter(), t.getText(),tuitIndex+1);
					 m.send(dos);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				 String text= t.getText();
				 
				 if (t.getRetuits()>=2&&!text.startsWith("!",1)){
					 Enumeration<String> users= chm.keys();
					 while(users.hasMoreElements()){
						 User u1 = chm.get(users.nextElement());
						 if (!u1.hasTuit(t)){
							 Socket s= u1.getSocket();
							 try {
								dos=new DataOutputStream(
											new BufferedOutputStream(s.getOutputStream()));
								int seqN = u1.addTuit(t);
								Rtuit m= new Rtuit(t.getWriter(), t.getText(),seqN);
								m.send(dos);
							} catch (IOException e) {
								e.printStackTrace();
							}
						 }
					 }
				 }else{
					 LinkedList<String> followers= u.getFollowers();
					 for(String f: followers){
						 User u2= chm.get(f);
						 System.out.println(u2.toString());
						 Socket s= u2.getSocket();
							try {
								dos = new DataOutputStream(
										new BufferedOutputStream(s.getOutputStream()));
								 int seqN = u2.addTuit(t);
								 Rtuit m= new Rtuit(t.getWriter(), t.getText(),seqN);
								 m.send(dos);
							} catch (IOException e) {
								e.printStackTrace();
							}		 
					 }
				 }
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
