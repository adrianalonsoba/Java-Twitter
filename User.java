package ist.a.alonsoba;

import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class User {

	private String login;
	private ArrayList<Tuit> tuits;
	//a quienes sigo:
	private LinkedList<String> follows;
	//quienes me siguen:
	private LinkedList<String> followers;
	private Socket uS;
	private Boolean isOnLine;

	User(String login,Socket uS) {
		this.login= new String(login);
		this.uS=uS;
		this.tuits= new ArrayList<Tuit>();
		this.follows= new LinkedList<String>();
		this.followers= new LinkedList<String>();
	}
	
 	public int getTuitIndex(Tuit t){
 		
 		return tuits.indexOf(t);
 	}
	
	public int addTuit(Tuit t){
		int seqN = this.numberOfTuits() + 1;
		tuits.add(t);	
		return seqN;
	}
	
	public void rmTuit(Tuit t){
		tuits.remove(t);
	}
	/**
	 * Añade un usuario al que se sigue
	 * @param name usuario al que se va a seguir
	 */
	public void addFollow(String name){
		follows.add(name); //PUEDO SEGUIR DOS VECES, HAY QUE ARREGLARLO
	}
	/**
	 * Elimina al usuario al que se deja de seguir
	 * @param name usuario al que se deja de seguir
	 */
	public void rmFollow(String name){
		follows.remove(name);
	}
	/**
	 * Añade un seguidor
	 * @param name nombre del seguidor
	 */
	public void addFollower(String name){
		followers.add(name);
	}
	/**
	 * Elimina un seguidor
	 * @param name nombre del seguidor
	 */
	public void rmFollower(String name){
		followers.remove(name);
	}
	/**
	 * Cambia el estado del usuariode conectado a no conectado y viceversa
	 */
	public void setOnLine(){
		isOnLine=!isOnLine;
	}
	
	public Boolean isFollowing(String login){
		if(follows.contains(login)){
			return true;
		}else{
			return false;
		}
	}
	/** 
	 * @return numero de tuits del usuario(propios mas ajenos)
	 */
	public int numberOfTuits(){
		return tuits.size();
	}
	
	public LinkedList<String> getFollowers(){
		return this.followers;
	}
	
	public LinkedList<String> getFollows(){
		return follows;
	}
	
	public Socket getSocket(){
		return this.uS;
	}
	
	/**
	 * Devuelve null si el numero de tuit no esta
	 * @param seqN Numero de secuencia del tuit buscado
	 * @return Tuit buscado
	 */
	public Tuit getTuit(int seqN){
		Tuit t;
		if(seqN<0||seqN>=tuits.size()){
			t=null;
		}else{
			t= tuits.get(seqN);
		}
		return t;		
	}	
	
	public String toString(){
		return login+"FOLLOWS:"+follows+"FOLLOWERS:"+followers+"\n";
	
	}
	
	public Boolean hasTuit(Tuit t){
		return tuits.contains(t);
	}
}
