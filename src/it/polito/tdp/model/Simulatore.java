package it.polito.tdp.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.db.EventsDao;
import it.polito.tdp.model.Event.eventTypeEnum;
import it.polito.tdp.model.Event.gestioneEvento;


public class Simulatore {
	private EventsDao dao ;
	private PriorityQueue<Event> eventList;
	private ArrayList <Agente> listaAgenti ;
	private int id ;
	private double distance;
	private Map<Integer, LatLng> MappaDistretti;
//parametri di simulazione
	private int velocitaAgente = 60; //km/h
	private Duration tempoGestioneEvento1 = Duration.ofHours(1);//all_other_crimes con p = 0.5
	private Duration tempoGestioneEvento2 = Duration.ofHours(2);//all_other_crimes con p = 0.5
	//restanti eventi tutti 2h
	private double probabilita = 0.5;
	private Duration deltaArrivo = Duration.ofMinutes(15);//evento mal gestito se si supera
	
	
//statistiche da calcolare
	private int eventiMalGestiti;
	
public int getEventiMalGestiti() {
		return eventiMalGestiti;
	}

	//variabili interne
	public Simulatore(int N, int anno, int mese , int giorno) {
		this.dao = new EventsDao();
		 eventList = new PriorityQueue<Event>(this.dao.listAllEvents(anno,mese,giorno));
		 listaAgenti = new ArrayList<Agente>();
		id =  this.dao.DistrettoCrimineMinore(anno);
		MappaDistretti = new HashMap<Integer,LatLng>(this.dao.MappaCentri(anno));
		generaAgenti(N,anno);
		}
	
	public void addEvent(Event e) {
		eventList.add(e);
	}

	public void simulate() {
		while (!eventList.isEmpty()) {
			doSimulationStep();
		}
	}
	private void doSimulationStep() {
		while(!eventList.isEmpty()) {
			
			Agente agente = listaAgenti.get(0) ;
			Event ev = eventList.poll();
			LatLng posizioneEv = new LatLng(ev.getGeo_lat(),ev.getGeo_lon());
			
			for(Agente ag : listaAgenti) {
			if( (LatLngTool.distance(ag.getPosizione(),posizioneEv,
					LengthUnit.KILOMETER)<LatLngTool.distance(agente.getPosizione(),posizioneEv, LengthUnit.KILOMETER)&&ag.getTempoOccupato().equals(0))) {
				agente = ag;
			}
			}
			
	
		switch(ev.getTipo()) {				
			
				case OTHERS_2:
					if( deltaArrivo.minusMinutes((long) LatLngTool.distance(posizioneEv,agente.getPosizione(),
						LengthUnit.KILOMETER)).isNegative()) {
						ev.setGestione(gestioneEvento.EVENTO_BEN_GESTITO);
						agente.setTempoOccupato(Duration.ofHours(2));
						agente.setPosizione(posizioneEv);
						break;
						
					}
					else {
						ev.setGestione(gestioneEvento.EVENTO_MAL_GESTITO);
						agente.setTempoOccupato(Duration.ofHours(2));
						agente.setPosizione(posizioneEv);
						eventiMalGestiti++;
						break;
					}
					
					
					
				case ALL_OTHERS12:
					if( deltaArrivo.minusMinutes((long) LatLngTool.distance(posizioneEv,agente.getPosizione(),
							LengthUnit.KILOMETER)).isNegative()) {
							ev.setGestione(gestioneEvento.EVENTO_BEN_GESTITO);
							agente.setTempoOccupato(Duration.ofHours((int)(Math.random()+1)));
							agente.setPosizione(posizioneEv);
							break;
										
						}
						else {
							ev.setGestione(gestioneEvento.EVENTO_MAL_GESTITO);
							eventiMalGestiti++;
							agente.setTempoOccupato(Duration.ofHours((int)(Math.random()+1)));
							agente.setPosizione(posizioneEv);
							break;
						}
						
			}
				
			}
		}
	
	public void generaAgenti(int N, int anno){
		for(int i = 1 ; i<=N; i++) {
			listaAgenti.add(new Agente(Duration.ofMinutes(0),i,MappaDistretti.get(this.dao.DistrettoCrimineMinore(anno).intValue())));
			}
		
	}
	
	
}
