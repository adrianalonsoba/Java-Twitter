package ist.a.alonsoba;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class TuiterServer {

	private ServerSocket sSk;
	private ConcurrentHashMap<String,User> chm;
	private BlockingQueue<Tuit> bq;

	/**
	 * Construye un ServerSocket con el puerto que se le pasa
	 * @param PORT puerto al que se ata para escuchar peticiones        
	 */
	public TuiterServer(int PORT) {
		chm= new ConcurrentHashMap<String,User>();
		bq= new  LinkedBlockingQueue<Tuit>();
		try {
			sSk = new ServerSocket(PORT);
		} catch (Exception e) {
			System.err.println("error: " + e);
			System.exit(-1);
		}
	}

	/**
	 * Arranca el servidor de tuiter atendiendo a cada cliente 
	 *  en un nuevo hilo y previamente un thread que se encarga de enviar los tuits a los clientes
	 */
	private void start() {
		Thread t0= new Thread(new TuitSender(bq,chm));
		t0.start();
		while (true) {
			Socket cSk = null;
			try {
				cSk = sSk.accept();
				System.out.println("\nNueva conexión aceptada:"+cSk);
			} catch (Exception e) {
				System.err.println("error: " + e);
				e.printStackTrace();
			}
			Thread t = new Thread(new CliHandler(cSk,chm,bq));
			t.start();
		}
	}

	public static void main(String[] args) {

		if (args.length != 1) {
			throw new RuntimeException("El numero de argumentos es 1: <puerto>");
		}
		TuiterServer ts = new TuiterServer(Integer.parseInt(args[0]));
		System.out.print("Servidor de tuiter arrancado en:" + args[0]);
		ts.start();
		
	}

}
