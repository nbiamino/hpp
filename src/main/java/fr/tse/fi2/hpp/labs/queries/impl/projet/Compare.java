package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Compare extends AbstractQueryProcessor {

	private static LinkedList<DebsRecord> recs = new LinkedList<>();
	private static LinkedList<ArrayList<Integer>> recsCell = new LinkedList<>();
	private static long firstTime;
	private static long lastTime;
	private String sortie;

	public Compare(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		recs.add(record);
		lastTime = record.getDropoff_datetime();
		GetCell(record.getPickup_longitude(), record.getPickup_latitude(),
				record.getDropoff_longitude(), record.getDropoff_latitude());
		while ((lastTime - recs.getFirst().getDropoff_datetime()) / 60000 > 30) {
			recs.removeFirst();
			recsCell.removeFirst();
		}
		prepareSortie();
		System.out.println(Count(recsCell));
		this.listsum.add(sortie);

	}

	public void prepareSortie() {
		Date dd = new Date(firstTime);
		Date df = new Date(lastTime);
		sortie = dd + " / " + df;
	}

	public static void GetCell(double pickup_longitude, double pickup_latitude,
			double dropoff_longitude, double dropoff_latitude) {
		/*
		 * Le grille fait 300 case x 300 cases. Chaque case fait 500m x 500m
		 * Coordonnées de la 1ère case : 41.474937, -74.913585 500 m to South =
		 * 0.004491556 (lattitude) 500 m to East = 0.005986 (longitude)
		 */
		ArrayList<Integer> recCell = new ArrayList<>();
		double a = pickup_longitude + 74.913585 + 0.005986 / 2;
		double xDepDouble = a / 0.005986;
		int xDep = (int) xDepDouble + 1;

		a = -(pickup_latitude - 41.474937 - 0.004491556 / 2);
		double yDepDouble = a / 0.004491556;
		int yDep = (int) yDepDouble + 1;

		a = dropoff_longitude + 74.913585 + 0.005986 / 2;
		double xArrDouble = a / 0.005986;
		int xArr = (int) xArrDouble + 1;

		a = -(dropoff_latitude - 41.474937 - 0.004491556 / 2);
		double yArrDouble = a / 0.004491556;
		int yArr = (int) yArrDouble + 1;

		recCell.add(xDep);
		recCell.add(yDep);
		recCell.add(xArr);
		recCell.add(yArr);
		recsCell.add(recCell);
	}

	public static TreeMap<ArrayList<Integer>, Integer> Count(
			LinkedList<ArrayList<Integer>> recsCell2) {
		Map<ArrayList<Integer>, Integer> recsCellCount = new Hashtable<ArrayList<Integer>, Integer>();
		for (int i = 0; i < recsCell2.size(); i++) {
			if (recsCellCount.containsKey(recsCell2.get(i))) {
				recsCellCount.put(recsCell2.get(i),
						recsCellCount.get(recsCell2.get(i)) + 1);
			} else {
				recsCellCount.put(recsCell2.get(i), 1);
			}
		}

		ValueComparator bvc = new ValueComparator(recsCellCount);
		// TreeMap<ArrayList<Integer>, Integer> sorted_map = new
		// TreeMap<ArrayList<Integer>, Integer>();
		// sorted_map.putAll(recsCellCount);
		//
		// ArrayList<ArrayList<Integer>> sorted_List = new ArrayList<>();
		// for (ArrayList<Integer> key : sorted_map.keySet()) {
		// sorted_List.add(key);
		// }
		TreeMap<ArrayList<Integer>, Integer> sorted_map = new TreeMap<ArrayList<Integer>, Integer>(
				bvc);
		return sorted_map;
	}

	static class ValueComparator implements Comparator<ArrayList<Integer>> {

		Map<ArrayList<Integer>, Integer> base;

		public ValueComparator(Map<ArrayList<Integer>, Integer> base) {
			this.base = base;
		}

		// Note: this comparator imposes orderings that are inconsistent with
		// equals.
		public int compare(ArrayList<Integer> a, ArrayList<Integer> b) {
			if (base.get(a) >= base.get(b)) {
				return -1;
			} else {
				return 1;
			} // returning 0 would merge keys
		}
	}
}
