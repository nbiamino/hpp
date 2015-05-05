package fr.tse.fi2.hpp.labs.queries.impl.Lab2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Writter implements Runnable {

	DebsRecord record;
	public final BlockingQueue<String> blockingQueue;
	private BufferedWriter outputWriter;
	private int id;
	private final static Logger logger = LoggerFactory
			.getLogger(AbstractQueryProcessor.class);

	public Writter(BlockingQueue<String> blockingQueue, int id) {
		super();
		this.id = id;
		this.blockingQueue = blockingQueue;

		try {
			outputWriter = new BufferedWriter(new FileWriter(new File(
					"result/query" + id + ".txt")));
		} catch (IOException e) {
			logger.error("Cannot open output file for " + id, e);
			System.exit(-1);
		}
	}

	@Override
	public void run() {
		String line = null;
		while (true) {
			try {
				line = blockingQueue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writeLine(line);
			if (line.equals("DIE!!!")) {
				break;
			}
		}
		finish();

	}

	protected void writeLine(String line) {
		try {
			outputWriter.write(line);
			outputWriter.newLine();
		} catch (IOException e) {
			logger.error("Could not write new line for query processor " + id
					+ ", line content " + line, e);
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

}
