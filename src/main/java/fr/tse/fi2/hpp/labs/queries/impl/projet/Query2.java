package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.util.ArrayList;
import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query2 extends AbstractQueryProcessor {

	private static LinkedList<DebsRecord> recs15 = new LinkedList<>();
	private static LinkedList<DebsRecord> recs30 = new LinkedList<>();
	private static LinkedList<DebsRecord> deletedRecs15 = new LinkedList<>();
	private static LinkedList<DebsRecord> deletedRecs30 = new LinkedList<>();
	
	// elements de recsRentable au format (longitudeCell, latitudeCell, taxiVides, Mediane, profit)
	private static LinkedList<RecRentable> recsRentable = new LinkedList<>();
	private static long lastTime;
	private static int DepX, DepY,ArrX, ArrY;
	

	public Query2(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub

		recs15.add(record);
		recs30.add(record);
		lastTime = record.getDropoff_datetime();
		deletedRecs15.clear();
		deletedRecs30.clear();
		
		while ((lastTime - recs15.getFirst().getDropoff_datetime()) / 60000 > 15) {
			deletedRecs15.add(recs15.getFirst());
			recs15.removeFirst();
		}
		
		while ((lastTime - recs30.getFirst().getDropoff_datetime()) / 60000 > 30) {
			deletedRecs30.add(recs30.getFirst());
			recs30.removeFirst();
		}
		
		majRecsRentable();
	}

	private void majRecsRentable() {
		// TODO Auto-generated method stub
		//modification due à l'ajout du dernier record
		getCell(recs30.getLast());
		boolean isInRecsRentable = false;
		for(int i =0; i<recsRentable.size();i++){			
			if (recsRentable.get(i).getX()==ArrX &&recsRentable.get(i).getY()==ArrY){
				recsRentable.get(i).setTaxiVide(recsRentable.get(i).getTaxiVide()+1);
				recsRentable.get(i).setProfit(recsRentable.get(i).getMediane()/recsRentable.get(i).getTaxiVide());   
			}
			else{
				RecRentable recRentable = null;
				recRentable.setX(ArrX);
				recRentable.setY(ArrY);
				recRentable.setTaxiVide(1);
				recsRentable.add(recRentable);
			}
		}
			
		
		for(int i =0; i<recsRentable.size();i++){			
			if (recsRentable.get(i).getX()==DepX &&recsRentable.get(i).getY()==DepY){
				for (int j=0 ; j<recsRentable.get(i).getFares().size();i++){
					if(recs30.getLast().getFare_amount()+recs30.getLast().getTip_amount()<recsRentable.get(i).getFares().get(j)){
						recsRentable.get(i).getFares().add(j,(double) (recs30.getLast().getFare_amount()+recs30.getLast().getTip_amount()) );
						isInRecsRentable=true;
						break;
						
					// problème à régler si c'est l'élément le plus grand du tableau
					}
				}
			}
			if(isInRecsRentable==true){
				RecRentable recRentable = new RecRentable();
				recRentable.setX(DepX);
				recRentable.setY(DepY);
				recRentable.getFares().add( (double) (recs30.getLast().getFare_amount()+recs30.getLast().getTip_amount()));
				recRentable.setMediane(recRentable.getFares().get(recRentable.getFares().size()/2));
				recRentable.setProfit(recRentable.getMediane()/recRentable.getTaxiVide());
				recsRentable.add(recRentable);
			}
		}
		
		//modification due aux supppressions des taxi -30 min 
		
		
		
		// modification dues aux supppressions des taxi -30 min à 15min
	}

	public static void getCell(DebsRecord record) {

		/*
		 * Le grille fait 600 case x 600 cases. Chaque case fait 250m x 250m
		 * Coordonnées de la 1ère case : 20.7374685, -37.4567925 250 m to South
		 * = 0.002245778 (lattitude) 250 m to East = 0.002993 (longitude)
		 */
		
		
		double pickup_longitude = record.getPickup_longitude();
		double pickup_latitude = record.getPickup_latitude();
		double dropoff_longitude = record.getDropoff_longitude();
		double dropoff_latitude = record.getDropoff_latitude();
		
		double a = pickup_longitude + 37.4567925 + 0.005986 / 2;
		double xDepDouble = a / 0.002993;
		DepX = (int) xDepDouble + 1;

		a = -(pickup_latitude - 20.7374685 - 0.004491556 / 2);
		double yDepDouble = a / 0.002245778;
		DepY = (int) yDepDouble + 1;

		a = dropoff_longitude + 37.4567925 + 0.005986 / 2;
		double xArrDouble = a / 0.002993;
		ArrX = (int) xArrDouble + 1;

		a = -(dropoff_latitude - 20.7374685 - 0.004491556 / 2);
		double yArrDouble = a / 0.002245778;
		ArrY = (int) yArrDouble + 1;
	}

	public static int getProfit(LinkedList<DebsRecord> allRecs,int x, int y) {
		LinkedList<DebsRecord> recsLast15 = new LinkedList<>();
		while (lastTime - recsLast15.getFirst().getDropoff_datetime() / 60000 > 15) {
			recsLast15.removeFirst();
		}
		for (int i=0;i<recsLast15.size();i++){
			if(!(x==DepX && y==DepY)){
				recsLast15.remove(i);
				i--;
			}
		}
		return 0;
	}
}
