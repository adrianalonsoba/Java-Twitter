package ist.a.alonsoba;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Clase abstracta para mensajes, los mensajes concretos la extienden
 */

public abstract class Msg {

	MsgType type;
	
	Msg(MsgType type) {
		this.type=type;
	}

	public void send(DataOutputStream dos) {
		int numType = type.ordinal();
		try {
			dos.write(Packer.pack(numType));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Msg receive(DataInputStream dis){
		Msg m=null;
		byte[] typeBytes = new byte[4];
		try {
			int i = dis.read(typeBytes);
			if(i!=-1){
				MsgType type = MsgType.values()[Packer.unpack(typeBytes)];
				switch(type){
				case TLOGIN:
					m= Tlogin.readFrom(dis);
					System.out.println(m.toString());
					break;
				case TFOLLOW:
					m= Tfollow.readFrom(dis);
					System.out.println(m.toString());
					break;
				case TUSERS:
					m= Tusers.readFrom(dis);
					System.out.println(m.toString());
					break;
				case TUNFOLLOW:
					m=Tunfollow.readFrom(dis);
					System.out.println(m.toString());
					break;
				case TRETUIT:
					m = Tretuit.readFrom(dis);
					System.out.println(m.toString());
					break;
				case TTUIT:
					m = Ttuit.readFrom(dis);
					System.out.println(m.toString());
					break;
				case ROK:
					m = new Rok();
					System.out.println(m.toString());
					break;
				case RERROR:
					m= Rerror.readFrom(dis);
					System.out.println(m.toString());
					break;
				case RUSERS:
					m= Rusers.readFrom(dis);
					System.out.println(m.toString());
					break;
				case RTUIT:
					m= Rtuit.readFrom(dis);
					System.out.println(m.toString());
					break;
				case TLOGOUT:
					m=Tlogout.readFrom(dis);
					System.out.println(m.toString());
					break;
				default:
					dis.close();
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("Conexion cerrada");
		}
		return m;
	}
	
	/**
	 * Metodo absracto para que los mensajes concretos se procesen
	 */
	public abstract void process(ConcurrentHashMap<String,User> chm, Socket s);
	
}
