package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private Graph<Director, DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private Map<Integer, Director> idMap;
	
	private List<Director> best;
	private int totAttoriCondivisi;
	
	public Model() {
		super();
		this.dao = new ImdbDAO();
		this.totAttoriCondivisi = 0;
	}
	
	public void creaGrafo(int year) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		
		this.dao.getVertici(year, idMap);
		
		//Aggiungo vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//Aggiungo gli archi
		for(Adiacenza a: this.dao.getAdiacenze(year, idMap)) {
			if(this.grafo.containsVertex(a.getD1()) && this.grafo.containsVertex(a.getD2())) {
				Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(), a.getPeso());
			}
		}

	    System.out.println("Grafo creato!");
	  	System.out.println("#VERTICI: "+ this.grafo.vertexSet().size());
	  	System.out.println("#ARCHI: "+ this.grafo.edgeSet().size());
	
	}
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Director> getVertici(){
		List<Director> vertici = new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(vertici, new Comparator<Director>(){

			@Override
			public int compare(Director o1, Director o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		return vertici;
	}
	
	public List<RegistaAttoriCondivisi> getAdiacenti(Director director){
		List<RegistaAttoriCondivisi> adiacenti = new ArrayList<>();
		for(Director d: Graphs.neighborListOf(this.grafo, director)) {
			RegistaAttoriCondivisi regista = new RegistaAttoriCondivisi(d, (int) this.grafo.getEdgeWeight(this.grafo.getEdge(d, director)));
			adiacenti.add(regista);
		}
		Collections.sort(adiacenti, new Comparator<RegistaAttoriCondivisi>(){

			@Override
			public int compare(RegistaAttoriCondivisi o1, RegistaAttoriCondivisi o2) {
				return (-1)*o1.getAttoriCondivisi().compareTo(o2.getAttoriCondivisi());
			}
			
		});
		return adiacenti;
	}
	
	public List<Director> trovaPercorso(int maxCondivisi, Director d){
		this.best = new ArrayList<>();
		
		List<Director> parziale = new ArrayList<>();
		
		int sommaPesi = 0;
		
		parziale.add(d);
		
		cerca(parziale, maxCondivisi, sommaPesi);
		
		return best;
	}

	private void cerca(List<Director> parziale, int maxCondivisi, int sommaPesi) {
		if(parziale.size() > this.best.size() && sommaPesi <= maxCondivisi) {
			this.best = new ArrayList<>(parziale);
		}
		
		if(sommaPesi > maxCondivisi) {
			return;
		}
		
		Director ultimo = parziale.get(parziale.size()-1);
		for(Director d: Graphs.neighborListOf(this.grafo, ultimo)) {
		      sommaPesi += this.grafo.getEdgeWeight(this.grafo.getEdge(d, ultimo));
			   if(!parziale.contains(d)) {
				   parziale.add(d);
				   cerca(parziale, maxCondivisi, sommaPesi);
				   parziale.remove(parziale.size()-1);
				   sommaPesi -= this.grafo.getEdgeWeight(this.grafo.getEdge(d, ultimo));	  
			}
		}
	}
	
	public int getTotAttoriCondivisi(Director director, int c) {
		List<Director> migliore = trovaPercorso(c, director);
		int tot = 0;
		for(Director d: migliore) {
			tot+= this.grafo.getEdgeWeight(this.grafo.getEdge(d, director));
		}
		return tot;
	}

}
