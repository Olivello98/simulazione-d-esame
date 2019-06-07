package it.polito.tdp.model;

import java.time.Duration;

import com.javadocmd.simplelatlng.LatLng;

public class Agente {
	private Duration tempoOccupato = Duration.ofMinutes(0);
	private int id;
	private LatLng posizione;
	
	public Agente(Duration tempoOccupato, int id, LatLng posizione) {
		super();
		this.tempoOccupato = tempoOccupato;
		this.id = id;
		this.posizione = posizione;
	}
	public LatLng getPosizione() {
		return posizione;
	}
	public void setPosizione(LatLng posizione) {
		this.posizione = posizione;
	}
	

	public Duration getTempoOccupato() {
		return tempoOccupato;
	}
	public void setTempoOccupato(Duration tempoOccupato) {
		this.tempoOccupato = tempoOccupato;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	

}
