package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query1 extends AbstractQueryProcessor {


	// Tableau contenant tous les debsRecord des 30 dernières minutes
	private static LinkedList<DebsRecord> recs = new LinkedList<>();
	// Tableau contenant tous les cellules des debsRecord des 30 dernières minutes
	private static LinkedList<ArrayList<Integer>> recsCell = new LinkedList<>();
	// Première date des DebsRecord présent dans recs
	private static long firstTime;
	// Dernière date des DebsRecord présent dans recs
	private static long lastTime;
	// Valeur ecrite dans le fichier 
	private String sortie;

	
	public Query1(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	// Fonction permettant de recuperer toutes les DebsRecord ayant eu lieu les 30 dernieres minutes
	
	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		long start = System.nanoTime();
		recs.add(record);
		lastTime = record.getDropoff_datetime();
		getCell(record.getPickup_longitude(),record.getPickup_latitude(),record.getDropoff_longitude(),record.getDropoff_latitude());
		firstTime = recs.getFirst().getDropoff_datetime();
		while ((lastTime - recs.getFirst().getDropoff_datetime()) / 60000 > 30) {
			recs.removeFirst();
			recsCell.removeFirst();
		}
		prepareSortie(start, count(recsCell));
		// écriture de la sortie dans un Thread
		this.listsum.add(sortie);
	}
	
	// Fonction permettant de préparer l'écriture de la sortie
	
	public void prepareSortie(long start, ArrayList<ArrayList<Integer>> list){
		Date dd = new Date(firstTime);
		Date df = new Date(lastTime);
		long delay = System.nanoTime() - start;
		String listnull = "";
		int taille = list.size();
		if (taille > 10){
			for (int j = 10; j < taille; j++){
				list.remove(10);
			}
		}
		for (int k = 0; k < 10 - taille ; k++){
			listnull += " , NULL";
		}
		sortie = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dd) +" , "
		+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(df) + list + listnull + " , " + delay ;
	}
	
	// Fonction transformant les coordonnées des DepsRecords en coordonnée cellulaire

	public static void getCell(double pickup_longitude, double pickup_latitude,
			double dropoff_longitude, double dropoff_latitude) {
		/*
		 * Le grille fait 300 case x 300 cases. Chaque case fait 500m x 500m
		 * Coordonnées de la 1ère case : 41.474937, -74.913585 500 m to South =
		 * 0.004491556 (lattitude) 500 m to East = 0.005986 (longitude)
		 */
		ArrayList<Integer> recCell = new ArrayList<>();
		double a = pickup_longitude + 74.913585 + 0.005986 / 2;
		double xDepDouble = a / 0.005986;
		recCell.add((int) xDepDouble + 1);

		a = -(pickup_latitude - 41.474937 - 0.004491556 / 2);
		double yDepDouble = a / 0.004491556;
		recCell.add((int) yDepDouble + 1);

		a = dropoff_longitude + 74.913585 + 0.005986 / 2;
		double xArrDouble = a / 0.005986;
		recCell.add((int) xArrDouble + 1);

		a = -(dropoff_latitude - 41.474937 - 0.004491556 / 2);
		double yArrDouble = a / 0.004491556;
		recCell.add((int) yArrDouble + 1);
		
		recsCell.add(recCell);
	}
	
	// Fonction permettant de compter le nombre de Taxis faisant le même parcours
	
	public static ArrayList<ArrayList<Integer>> count(
			LinkedList<ArrayList<Integer>> recsCell2) {
		
		HashMap<ArrayList<Integer>, Integer> recsCellCount = new HashMap<ArrayList<Integer>, Integer>();
		ValueComparator bvc =  new ValueComparator(recsCellCount);
		TreeMap<ArrayList<Integer>, Integer> sorted_map = new TreeMap<ArrayList<Integer>, Integer>(bvc);
		
		for (int i = 0; i < recsCell2.size(); i++) {
			if (recsCellCount.containsKey(recsCell2.get(i))) {
				recsCellCount.put(recsCell2.get(i),
						recsCellCount.get(recsCell2.get(i)) + 1);
			} else {
				recsCellCount.put(recsCell2.get(i), 1);
			}
		}
		sorted_map.putAll(recsCellCount);

		ArrayList<ArrayList<Integer>> sorted_List = new ArrayList<>();
			for (ArrayList<Integer> key : sorted_map.keySet()) {
				sorted_List.add(key);
		}
			return sorted_List;
	}
	
	// Fonction permettant de classer les routes de la plus fréquentée à la moins fréquentée
	
	static class ValueComparator implements Comparator<ArrayList<Integer>> {

	    Map<ArrayList<Integer>, Integer> base;
	    public ValueComparator(HashMap<ArrayList<Integer>, Integer> recsCellCount) {
	        this.base = recsCellCount;
	    }

		@Override
		public int compare(ArrayList o1, ArrayList o2) {
			// TODO Auto-generated method stub
	        if (base.get(o1) >= base.get(o2)) {
	            return -1;
	        } else {
	            return 1;
	        }
		}
	}
}