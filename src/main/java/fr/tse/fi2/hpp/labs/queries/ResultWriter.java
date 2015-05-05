package fr.tse.fi2.hpp.labs.queries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultWriter implements Runnable {
	private BufferedWriter outputWriter;

	private int id;
	public final BlockingQueue<String> resultqueue;
	final static Logger logger = LoggerFactory
			.getLogger(AbstractQueryProcessor.class);

	public ResultWriter(int id, BlockingQueue<String> resultqueue) {
		super();
		this.id = id;
		this.resultqueue = resultqueue;
		try {
			outputWriter = new BufferedWriter(new FileWriter(new File(
					"result/query" + id + ".txt")));
		} catch (IOException e) {
			logger.error("Cannot open output file for" + id, e);
			System.exit(-1);
		}
	}

	public void run() {
		// récupérer dans la queue
		// écrire
		while (true) {
			try {
				String line = resultqueue.take();
				writeLine(line);
				if (line.equals("DIE")) {
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		finish();
	}

	private void finish() {

	}

	protected void writeLine(String line) {
		try {
			outputWriter.write(line);
			outputWriter.newLine();
		} catch (IOException e) {
			logger.error("Could not write new line for query processor" + id
					+ ", line content" + line, e);
		}
	}
}
