package fr.tse.fi2.hpp.labs.queries.impl.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

@SuppressWarnings("serial")
public class MergeSortMulti extends RecursiveTask<int[]> {

	private int longueur;
	int[] liste_;

	public MergeSortMulti(int[] liste) {
		liste_ = liste;
		longueur = liste_.length;
	}

	protected int[] compute() {

		if (longueur > 20) {

			List<MergeSortMulti> subtasks = new ArrayList<MergeSortMulti>();
			subtasks.addAll(createSubtasks());

			for (MergeSortMulti subtask : subtasks) {
				subtask.fork();
			}

			int[] sousListe1 = subtasks.get(0).join();
			int[] sousListe2 = subtasks.get(1).join();

			int a = 0;
			int b = 0;

			for (int i = 0; i < longueur; i++) {

				if (a < sousListe1.length
						&& ((b >= sousListe2.length) || (sousListe1[a] < sousListe2[b]))) {
					liste_[i] = sousListe1[a];
					a++;
				} else if (b < sousListe2.length) {

					liste_[i] = sousListe2[b];
					b++;
				}
			}
		} else {
			InsertionSort(liste_);
		}
		return liste_;
	}

	private List<MergeSortMulti> createSubtasks() {
		List<MergeSortMulti> subtasks = new ArrayList<MergeSortMulti>();

		MergeSortMulti subtask1 = new MergeSortMulti(cutArrayList(liste_, 0,
				longueur / 2));
		MergeSortMulti subtask2 = new MergeSortMulti(cutArrayList(liste_,
				longueur / 2, longueur));

		subtasks.add(subtask1);
		subtasks.add(subtask2);

		return subtasks;
	}

	public static int[] cutArrayList(int[] liste, int debut, int fin) {
		int[] tab = new int[(fin - debut)];

		int j = 0;
		for (int i = debut; i < fin; i++) {
			tab[j] = liste[i];
			j++;
		}
		return tab;
	}

	public static void InsertionSort(int[] num) {
		int j; // the number of items sorted so far
		int key; // the item to be inserted
		int i;

		for (j = 1; j < num.length; j++) // Start with 1 (not 0)
		{
			key = num[j];
			for (i = j - 1; (i >= 0) && (num[i] > key); i--) {
				num[i + 1] = num[i];
			}
			num[i + 1] = key; // Put the key in its proper location
		}
	}

	public static int[] genererAleatoire(int taille) {
		int[] tableau = new int[taille];
		Random random = new Random();

		for (int i = 0; i < tableau.length; i++) {
			int valeur = random.nextInt();
			tableau[i] = valeur;
		}

		return tableau;
	}

	public static void main(String[] args) {

		int[] liste = new int[100];

		// liste = MergeSortMulti.genererAleatoire(1000);

		for (int i = 0; i < liste.length; i++) {
			liste[i] = 100 - i;
		}

		for (int i = 0; i < liste.length; i++) {
			System.out.println(liste[i]);
		}

		System.out.println("\nDebut tri\n");

		MergeSortMulti tri1 = new MergeSortMulti(liste);
		int cores = Runtime.getRuntime().availableProcessors();
		ForkJoinPool forkJoinPool = new ForkJoinPool(cores);

		int[] listeTrie = forkJoinPool.invoke(tri1);

		for (int i = 0; i < listeTrie.length; i++) {
			System.out.println(listeTrie[i]);
		}
	}
}