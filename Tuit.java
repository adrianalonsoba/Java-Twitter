package ist.a.alonsoba;

public class Tuit {

	private String text;
	//private int seqN;
	//el que lo envia
	private String owner;
	//el escritor original
	private String writer;
	private int retuits;
	
	public Tuit(Tuit tuit) {
		this.owner = tuit.owner;
		this.text = tuit.text;
		this.retuits=tuit.retuits;
	}

	public Tuit(String owner,String text,int retuits){
		this.owner=owner;
		this.writer=owner;
		this.text=text;
		this.retuits=retuits;
	}
	/**
	 * Incrementa en uno el numero de retuits del tuit
	 */
	public void incRet(){
		retuits++;
	}

	/**
	 * 
	 * @return El que lo envia
	 */
	public String getLogin(){
		return this.owner;
	}
	
	public void setOwner(String owner){
		this.owner=owner;
		
	}
	/**
	 * 
	 * @return El que lo escribio originalmente
	 */
	public String getWriter(){
		return this.writer;
	}
	
	public String getText(){
		return this.text;
	}

	
	public int getRetuits(){
		return this.retuits;
	}
	
	public String toString(){
		return "Escritor:"+writer+" enviante:"+owner+ " Texto:"+text;
	}
	
}
