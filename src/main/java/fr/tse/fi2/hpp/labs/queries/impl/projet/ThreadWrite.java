package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class ThreadWrite implements Runnable {

	/**
	 * Writer to write the output of the queries
	 */
	protected BufferedWriter outputWriter;

	protected final static Logger logger = LoggerFactory
			.getLogger(AbstractQueryProcessor.class);

	private int id;
	final BlockingQueue<String> queueEcriture;

	public ThreadWrite(BlockingQueue<String> qE, int id) {

		this.id = id;
		this.queueEcriture = qE;
		// Initialize writer
		try {
			outputWriter = new BufferedWriter(new FileWriter(new File(
					"result/DebsReccordsQuery" + id + ".txt")));
		} catch (IOException e) {
			logger.error("Cannot open output file for " + id, e);
			System.exit(-1);
		}
	}

	protected void finish() {
		// Close writer
		try {
			outputWriter.flush();
			outputWriter.close();
		} catch (IOException e) {
			logger.error("Cannot property close the output file for query "
					+ id, e);
		}
	}

	@Override
	public void run() {
		String line = null;
		while (true) {

			try {
				line = queueEcriture.take();
				if (!line.equals("FINISHED")) {
					outputWriter.write(line);
					outputWriter.newLine();
				} else {
					break;
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();

			} catch (IOException e) {
				logger.error("Could not write new line for query processor "
						+ id + ", line content " + line, e);
			}
		}
		finish();
	}

}
