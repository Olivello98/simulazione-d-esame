package it.polito.tdp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.graph.DefaultWeightedEdge;

import com.javadocmd.simplelatlng.LatLng;

public class Distretto {
	Model m = new Model();
	private int id;
	private LatLng centro;
	private Map<Distretto,DefaultWeightedEdge> MappaDistretti = new HashMap<Distretto,DefaultWeightedEdge>(m.getDistrettiVicini(this.id));
	
	public Distretto(int id, LatLng centro) {
		super();
		this.id = id;
		this.centro = centro;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LatLng getCentro() {
		return centro;
	}
	public void setCentro(LatLng centro) {
		this.centro = centro;
	}
	

}
