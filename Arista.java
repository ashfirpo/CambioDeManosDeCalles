package cambioDeManosDeCalles;

public class Arista {
	
	private int origen;
	private int destino;
	private int costo;
	private int nombre;
	private boolean sentido;
	
	public Arista(int nombre, int origen, int destino,int costo)
	{
		this.nombre=nombre;
		this.origen=origen;
		this.destino=destino;
		this.costo=costo;
		this.sentido=false;
	}
	
	public Arista(int origen, int destino)
	{
		this.origen=origen;
		this.destino=destino;
		this.sentido=false;
	}
	
	public int getNombre()
	{
		return this.nombre;
	}
	
	public int getOrigen()
	{
		return this.origen;
	}
	
	public int getDestino()
	{
		return this.destino;
	}
	
	public int getCosto()
	{
		return this.costo;
	}
	
	public boolean getSentido()
	{
		return this.sentido;
	}
	
	public void cambiarSentido()
	{
		this.sentido = !this.sentido;
	}
	
	@Override
	public boolean equals(Object o)
	{
		Arista param = (Arista)o;
		return this.origen == param.origen && this.destino == param.destino;
	}
	
	
	public int compareTo(Arista a)
	{
		return this.nombre - a.nombre;
	}

}
