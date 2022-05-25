package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
           final String sql="SELECT  Localita AS l, DATA AS d, umidita AS u "
           		+ "FROM situazione "
           		+ "WHERE MONTH(DATA)=? AND localita=? "
           		+ "ORDER BY DATA asc";
           List<Rilevamento> rilevamenti = new ArrayList<>();
           try {
        	   Connection conn=ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setString(2, localita);
			ResultSet res= st.executeQuery();
			while(res.next()) {
				Rilevamento r = new Rilevamento(res.getString("l"),res.getDate("d"),res.getInt("u"));
				rilevamenti.add(r);
			}
				conn.close();
				return rilevamenti;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("ERRORE NEL DB",e);
		}
	}
	
	public List<Citta> getCitta(){
		final String sql="SELECT DISTINCT localita "
				+ "FROM situazione "
				+ "ORDER BY localita";
		List<Citta> lista= new ArrayList<>();
		try {
			Connection conn= ConnectDB.getConnection();
			PreparedStatement st= conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Citta c= new Citta(rs.getString("localita"));
				lista.add(c);
			}
			conn.close();
			return lista;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("ERRORE NEL DB",e);
		}
	}
	
	public Double getUmiditàMediaPerCittà(int mese,Citta citta) {
		final String sql="SELECT AVG(Umidita) AS U "
				+ "	FROM situazione "
				+ "	WHERE MONTH(DATA)=? AND localita=? ";
		try {
			Connection conn= ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setString(2, citta.getNome());
			ResultSet rs= st.executeQuery();
			rs.first();
			Double u= rs.getDouble("U");
			conn.close();
			return u;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("ERRORE NEL DB",e);
		}
	}

	/*SELECT AVG(Umidita)
	FROM situazione
	WHERE MONTH(DATA)=1 AND localita='Torino'
	GROUP BY Localita*/
}
