package cambioDeManosDeCalles;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

public class Cambio {
	
	private int cantEsquinas; //Nodos
	private int nodoOrigen; //De donde sale el colectivo
	private int nodoDestino; //Donde está la escuela
	private int cantCalles; //Cantidad de aristas
	private int[][] matriz; //Grafo originalmente DP
	private static final int INF = 99999; //La distancia más larga posible es 50 (según enunciado)
	private int costoCambios=0;
	private ArrayList<Arista> callesCambiadas;
	private ArrayList<Arista> calles;
	
	public Cambio(String path) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File(path));
		
		this.cantEsquinas = sc.nextInt();
		this.nodoOrigen= sc.nextInt() -1;
		this.nodoDestino=sc.nextInt() -1;
		sc.nextLine();		
		this.cantCalles=sc.nextInt();
		sc.nextLine();
		this.matriz = new int[this.cantEsquinas][this.cantEsquinas];
		for(int i=0;i<this.cantEsquinas;i++)
			Arrays.fill(this.matriz[i], INF);
		this.calles = new ArrayList<>();
		for(int i=0;i<this.cantCalles;i++)
		{
			int origen = sc.nextInt() -1;
			int destino = sc.nextInt() -1;
			int costo = sc.nextInt();
			this.matriz[origen][destino] = costo;
			this.calles.add(new Arista((i+1), origen, destino, costo));
		}
		sc.close();
	}
	
	
	public void resolver()
	{
		int[][] noDirigida = new int[this.cantEsquinas][this.cantEsquinas];
		LinkedList<Arista> original = new LinkedList<Arista>();
		LinkedList<Arista> noDir = new LinkedList<Arista>();
		
		for(int i=0;i<this.cantEsquinas;i++)
		{
			Arrays.fill(noDirigida[i], INF);
			for(int j=0;j<this.cantEsquinas;j++)
			{
				if(this.matriz[i][j]!=INF)
					noDirigida[i][j] = noDirigida[j][i] = this.matriz[i][j];
			}
		}
		
		//Puedo hacer Dijkstra de las calles originales y compararlas con NDP
		original = dijkstra(this.nodoOrigen, this.nodoDestino, this.matriz);
		noDir = dijkstra(this.nodoOrigen, this.nodoDestino, noDirigida);
		
		this.callesCambiadas = new ArrayList<>();
		if(original==null && noDir == null)
		{
			System.out.println("No anda nada acá, ni DP ni NDP");
			return;
		}
		else if(original==null)
			this.callesCambiadas.addAll(noDir);
		
		else if(noDir==null)
		{
			System.out.println("No se le puede cambiar el sentido a ninguna");
			return;
		}
		else if(original != null && noDir != null && !original.equals(noDir))
		{
			for(Arista a : noDir)
			{
				if(!original.contains(a) && a.getSentido() == true)
					this.callesCambiadas.add(a);
			}
		}
		else if(original.equals(noDir))
			System.out.println("No hace falta cambiarle el sentido a ninguna de las calles.");
		
		System.out.println("Costo mínimo: " + this.costoCambios);
		this.callesCambiadas.sort((n1, n2)-> n1.compareTo(n2));
		for(Arista c : this.callesCambiadas)
			System.out.print(c.getNombre() + " ");
	}
	
	
	public Arista getArista(int nodoOrigen, int nodoDestino)
	{
		Arista a = new Arista(nodoOrigen, nodoDestino);
		int index = this.calles.indexOf(a);
		if(index == -1)
		{
			a = new Arista(nodoDestino, nodoOrigen);
			index = this.calles.indexOf(a);
			(this.calles.get(index)).cambiarSentido();
		}
		return this.calles.get(index);
	}
	
	
	public LinkedList<Arista> dijkstra(int nodoOrigen, int nodoDestino, int[][] matriz)
	{
		int[] costos = new int[this.cantEsquinas];
		int[] camino = new int[this.cantEsquinas];
		Set<Integer> nodosRestantes = new HashSet<Integer>();
		Arrays.fill(costos, INF);
		Arrays.fill(camino, nodoOrigen);
		
		for(int i=0;i<this.cantEsquinas;i++)
			costos[i]=matriz[nodoOrigen][i];
		
		for(int i=0;i<this.cantEsquinas;i++)
			nodosRestantes.add(i);
		
		nodosRestantes.remove(nodoOrigen);
		
		//Ver si puede haber nodos aislados, no debería, ya que se trata de esquinas y calles, pero...
		//No hay nodos aislados, pero sí puede darse que estén todos los grafos dirigidos en sentido opuesto
		//Ergo, no tienen aristas para poder salir de ese nodo: ver el array de costos
		
		int[] inf = new int[this.cantEsquinas];
		Arrays.fill(inf, INF);
		
		if(Arrays.equals(costos, inf))
			return null;

		
		while(!nodosRestantes.isEmpty())
		{
			int nodoMenor=0, menorValor=INF;
			
			for(Integer i : nodosRestantes)
			{
				if(costos[i] < menorValor)
				{
					nodoMenor = i;
					menorValor = costos[i];
				}
			}
			
			for(Integer i : nodosRestantes)
			{
				if(costos[nodoMenor] + matriz[nodoMenor][i] < costos[i])
				{
					costos[i] = costos[nodoMenor] + matriz[nodoMenor][i];
					camino[i] = nodoMenor;
				}
			}
			
			nodosRestantes.remove(nodoMenor);
		}
		
		LinkedList<Arista> resultado = new LinkedList<Arista>();
		int c = camino[nodoDestino];
		//int ult=c;
		Arista a = getArista(c, nodoDestino);
		resultado.add(a);
		while(c != nodoOrigen)
		{
			//ult = camino[c];
			a = getArista(camino[c], c);
			c=camino[c];
			resultado.add(a);
		}		
		//resultado.add(getArista(nodoOrigen, ult));
		this.costoCambios=costos[nodoDestino];
		return resultado;
	}

}
