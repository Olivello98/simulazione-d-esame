package it.polito.tdp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.db.EventsDao;

public class Model implements Comparator<Integer>{
	private EventsDao dao ;
	 private Graph<Integer,DefaultWeightedEdge> grafo;
	 private Map<Integer,LatLng> MappaCentri;
	 List<LatLng> Lista;
	
	public Model() {
		this.dao = new EventsDao();
		}
	
	public List<Integer> getListaAnni() {
	return	this.dao.ListaAnni();	
	}
	
	public void createGraph(int anno) {
		this.grafo = new DefaultUndirectedWeightedGraph<Integer,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo,this.dao.ListaDistrict());
		
			MappaCentri = this.dao.MappaCentri(anno);
		
		for(int i : grafo.vertexSet()) {
			for(int h : grafo.vertexSet()) {
				if(i>h) {
				double distance = LatLngTool.distance(MappaCentri.get(i),MappaCentri.get(h), LengthUnit.KILOMETER);
				Graphs.addEdgeWithVertices(this.grafo,i,h,distance);
				}
				}
		}
		
	}
	public List<LatLng >getPunti(int anno,int distretto) {
		Lista = new ArrayList<LatLng>();
	return this.dao.MappaPunti(anno, distretto);
	
	}
	
	public Map<Distretto,DefaultWeightedEdge>getDistrettiVicini(int id) {
		
		Map<Distretto,DefaultWeightedEdge> MappaDistretti= new HashMap<Distretto,DefaultWeightedEdge>();
		
		for(int i : grafo.vertexSet()) {
			for(int h : grafo.vertexSet()) {
				if(i>h) {
			MappaDistretti.put(new Distretto(id,this.MappaCentri.get(id)), this.grafo.getEdge(i,h));
			}
			}
			}
		return MappaDistretti;
	}
	
	public List<Integer> getDistrettiOrdinati(Integer d) {
		List<Integer> ListaDistretti = new ArrayList<>();
		ListaDistretti = Graphs.neighborListOf(grafo, d);
		
		
		Collections.sort(ListaDistretti, new Comparator<Integer>(){
			
		@Override
		public int compare(Integer d1, Integer d2) {
			DefaultWeightedEdge e1 = grafo.getEdge(d, d1);
			DefaultWeightedEdge e2 = grafo.getEdge(d,d2);
			Double peso1 = grafo.getEdgeWeight(e1);
			Double peso2 = grafo.getEdgeWeight(e2);
					return peso1.compareTo(peso2);

		}
		}); //Questo è un comparator inizializzato dentro la classe
			return ListaDistretti;
			
		}
		


	
//	public Distretto getDistretti(int h){
////		Map<Integer,Distretto> mappa= new HashMap<Integer,Distretto>();
////		for(int i : this.grafo.vertexSet()) {
////			mappa.put(i, new Distretto(i));
////		}
////		return mappa;
//		return new Distretto(h);
////		
//	}

	@Override
	public int compare(Integer arg0, Integer arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	}
