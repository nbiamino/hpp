package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.util.ArrayList;
import java.util.LinkedList;

public class RecRentable {
int x=0;
int y=0;
int taxiVide=0;
LinkedList<Double> fares = new LinkedList<>();
LinkedList<String> iDs = new LinkedList<>();
Double mediane=(double) 0;
Double profit=null;

public int getX() {
	return x;
}
public void setX(int x) {
	this.x = x;
}
public int getY() {
	return y;
}
public void setY(int y) {
	this.y = y;
}
public int getTaxiVide() {
	return taxiVide;
}
public void setTaxiVide(int taxiVide) {
	this.taxiVide = taxiVide;
}

public Double getMediane() {
	return mediane;
}
public void setMediane(Double mediane) {
	this.mediane = mediane;
}
public Double getProfit() {
	return profit;
}
public void setProfit(Double profit) {
	this.profit = profit;
}
public LinkedList<Double> getFares() {
	return fares;
}
public void setFares(LinkedList<Double> fares) {
	this.fares = fares;
}
public LinkedList<String> getiDs() {
	return iDs;
}
public void setiDs(LinkedList<String> iDs) {
	this.iDs = iDs;
}
}
