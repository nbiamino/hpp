package projetDEB;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

public class TaxiMethods {

	public static void GetCell(double pickup_longitude, double pickup_latitude,
			double dropoff_longitude, double dropoff_latitude) {
		/*
		 * Le grille fait 300 case x 300 cases. Chaque case fait 500m x 500m
		 * Coordonnées de la 1ère case : 41.474937, -74.913585 500 m to South =
		 * 0.004491556 (lattitude) 500 m to East = 0.005986 (longitude)
		 */

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

		System.out.println("(" + xDep + ", " + yDep + ") , (" + xArr + ", "
				+ yArr + ")");
	}

	public static Hashtable<ArrayList<Integer>, Integer> Count(
			LinkedList<ArrayList<Integer>> recsCell) {
		Hashtable<ArrayList<Integer>, Integer> recsCellCount = new Hashtable<ArrayList<Integer>, Integer>();

		for (int i = 0; i < recsCell.size(); i++) {
			if (recsCellCount.containsKey(recsCell.get(i))) {
				recsCellCount.put(recsCell.get(i),
						recsCellCount.get(recsCell.get(i)) + 1);
			}
			else{
				recsCellCount.put(recsCell.get(i), 1);
			}
		}

		return recsCellCount;

	}
}