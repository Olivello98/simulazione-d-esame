package it.polito.tdp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.db.EventsDao;

public class Model {
	private EventsDao dao ;
	 private Graph<Integer,DefaultWeightedEdge> grafo;
	 private Map<Integer,LatLng> MappaCentri;
	 private Map<Double,Double> MappaPunti;
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

}
