package it.polito.tdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.model.Distretto;
import it.polito.tdp.model.Event;
import it.polito.tdp.model.Event.eventTypeEnum;
import it.polito.tdp.model.Event.gestioneEvento;


public class EventsDao {
	Map<Integer,LatLng> MappaCentri;
	public List<Event> listAllEvents(int anno, int mese , int giorno){
		String sql = "SELECT * FROM events WHERE YEAR(reported_date)= ? AND MONTH(reported_date) = ? AND DAY(reported_date) = ? "
				+ "ORDER BY reported_date" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic"),eventTypeEnum.OTHERS_2,gestioneEvento.EVENTO_BEN_GESTITO));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	public List<Integer> ListaAnni(){
		String sql = " SELECT DISTINCT YEAR(reported_date) as year" + 
				" from EVENTS" + 
				" ORDER BY YEAR(reported_date)" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				try {
			
				list.add(res.getInt("year"));
			}
			catch (Throwable t) {
			t.printStackTrace();
			System.out.println(res.getInt("id"));
		}
				}
		conn.close();
			return list;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
}
	public List<Integer> ListaDistrict(){
		String sql = " SELECT DISTINCT district_id" + 
				" from EVENTS\n" + 
				" ORDER BY district_id" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				try {
			
				list.add(res.getInt("district_id"));
			}
			catch (Throwable t) {
			t.printStackTrace();
			System.out.println(res.getInt("id"));
		}
				}
		conn.close();
			return list;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
}

	public Map<Integer,LatLng> MappaCentri(Integer anno){
		String sql = " SELECT district_id ,AVG(geo_lon), AVG(geo_lat)" + 
				" FROM EVENTS" + 
				" WHERE YEAR(reported_date) = ?" + 
				" GROUP BY district_id" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			Map<Integer,LatLng> MappaCentri = new HashMap<Integer,LatLng>() ;
		
			st.setInt(1,anno);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
			LatLng l =new LatLng(res.getDouble("AVG(geo_lat)"),res.getDouble("AVG(geo_lon)"));
					
				MappaCentri.put(res.getInt("district_id"), l);
			
			}
			catch (Throwable t) {
			t.printStackTrace();
			System.out.println(res.getInt("id"));
		}
				}
			
			conn.close();
			return MappaCentri;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		}	
	public List<LatLng> MappaPunti(Integer anno, Integer distretto){

		String sql = " SELECT district_id ,geo_lon, geo_lat" + 
				" FROM EVENTS" + 
				" WHERE YEAR(reported_date) = ?" + 
				" AND district_id = ?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<LatLng> lista = new ArrayList<LatLng>();
			st.setInt(1,anno);
			st.setInt(2, distretto);
			ResultSet res = st.executeQuery() ;
		
			while(res.next()) {
				try {
				lista.add(new LatLng(res.getDouble("geo_lon"),res.getDouble("geo_lat")));
				
				
			}
			catch (Throwable t) {
			t.printStackTrace();
			System.out.println(res.getInt("id"));
		}
				}
			
			conn.close();
			return lista;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		}		


	public Integer DistrettoCrimineMinore(int anno){
		String sql = "SELECT id" + 
				" FROM (SELECT	district_id	AS id, COUNT(is_crime) AS cnt" + 
				" FROM EVENTS e1" + 
				" WHERE YEAR(reported_date) = ? " + 
				" GROUP BY district_id) AS t1" + 
				" WHERE cnt = (SELECT MIN(cnt) FROM(SELECT	district_id	AS id, COUNT(is_crime) AS cnt" + 
				" FROM EVENTS e1" + 
				" WHERE YEAR(reported_date) = ? " + 
				" GROUP BY district_id) AS t2)"  	;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			int h = 0;
			st.setInt(1,anno);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
			
					
				h = res.getInt("id");
			
			}
			catch (Throwable t) {
			t.printStackTrace();
			System.out.println(res.getInt("id"));
		}
				}
			
			conn.close();
			return h;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		}
		
	
	
	
	
	}



	


