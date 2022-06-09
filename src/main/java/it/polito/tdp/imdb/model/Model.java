package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private Graph<Director, DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private Map<Integer, Director> idMap;
	private List<Director> best;
	private List<Director> registiConAttoriCondivisi;
	private int totAttoriCondivisi;
	
	
	public Model() {
		this.dao = new ImdbDAO();
		this.idMap = new HashMap<>();
		this.dao.getAllDirectors(idMap);
		
	}
	
	public void creaGrafo(int anno) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	
		//Aggiunta vertici:
		Graphs.addAllVertices(this.grafo, dao.getDirectors(anno, idMap));
	
		System.out.println("Grafo creato!");
		System.out.println("#VERTICI: "+ this.grafo.vertexSet().size());
		
		//Aggiunta archi:
		for(Adiacenza a: this.dao.getArchi(anno, idMap)) {
			if(this.grafo.containsVertex(a.getD1()) && this.grafo.containsVertex(a.getD1())){
				Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(), a.getPeso());
			}
		
		}
		System.out.println("# ARCHI: "+ this.grafo.edgeSet().size());	

	
	}

	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Director> getVertici(){
		List<Director> result = new ArrayList<>(this.grafo.vertexSet());
	    return result;
	}

	public boolean grafoCreato() {
		if(this.grafo == null)
		return false;
		else
			return true;
	}
	
	public List<RegistaAttoriCondivisi> getAttoriCondivisi(Director director){
		List<RegistaAttoriCondivisi> result = new ArrayList<>();
		List<Director> adiacenti = Graphs.neighborListOf(this.grafo, director);
		for(Director d: adiacenti) {
			result.add(new RegistaAttoriCondivisi(d, (int) this.grafo.getEdgeWeight(this.grafo.getEdge(director, d))));
		}
		//Ordine decrescente del numero di attori condivisi (ordine decrescente del peso)
		Collections.sort(result, new Comparator<RegistaAttoriCondivisi>() {

			@Override
			public int compare(RegistaAttoriCondivisi r1, RegistaAttoriCondivisi r2) {
				return -(r1.getnAttoriCondivisi()-r2.getnAttoriCondivisi());
			}
			
		});
		
		return result;
	}

	public List<Director> trovaPercorso(Director d, int c){
		
		this.best = new ArrayList<>(); //Percorso massimo
		
		List<Director> parziale = new ArrayList<>();
		
		int sommaPesi = 0;
		
		parziale.add(d);
		
		cerca(parziale, sommaPesi, c);
		//System.out.println(best);
		return best;
	}

	private void cerca(List<Director> parziale, int sommaPesi, int maxAttoriCondivisi) {
		
		if(parziale.size() > best.size()) {
			this.best = new ArrayList<>(parziale);
		}
		
		if(sommaPesi > maxAttoriCondivisi) {
			return;
		}
		
		Director ultimo = parziale.get(parziale.size()-1); //Prendo l'ultimo inserito, che inizialmente è il regista selezionato dal menu a tendina
		for(Director d: Graphs.neighborListOf(this.grafo, ultimo)) {
			sommaPesi += this.grafo.getEdgeWeight(this.grafo.getEdge(d, ultimo)); //non è orientato
			//System.out.println(sommaPesi);
			if(!parziale.contains(d)) {
				parziale.add(d);
				//System.out.println(parziale);
				cerca(parziale, sommaPesi, maxAttoriCondivisi);
				parziale.remove(parziale.size()-1); //accesso posizionale
				sommaPesi -= this.grafo.getEdgeWeight(this.grafo.getEdge(d, ultimo));
			}
		}
		
	}
	
	/*public int getTotAttoriCondivisi(Director director, int c) {
		List<Director> migliore = trovaPercorso(director, c);
		int tot = 0;
		for(Director d: migliore) {
			tot+= this.grafo.getEdgeWeight(this.grafo.getEdge(d, director));
		}
		return tot;
	}*/
	
}
