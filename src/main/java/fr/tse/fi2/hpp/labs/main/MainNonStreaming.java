package fr.tse.fi2.hpp.labs.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.LoadFirstDispatcher;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.projet.Query2;


/**
 * Main class of the program. Register your new queries here
 * 
 * Design choice: no thread pool to show the students explicit
 * {@link CountDownLatch} based synchronization.
 * 
 * @author Julien
 * 
 */
public class MainNonStreaming {

	final static Logger logger = LoggerFactory
			.getLogger(MainNonStreaming.class);
	// private static BloomFilterHash q;
	private static Query2 q;
	private DebsRecord r;

	/**
	 * @param args
	 * @throws IOException
	 */

	public static void main(String[] args) throws IOException {
		// Init query time measure
		QueryProcessorMeasure measure = new QueryProcessorMeasure();
		// Init dispatcher and load everything
		LoadFirstDispatcher dispatch = new LoadFirstDispatcher(
				"src/main/resources/data/test_01.csv");
		logger.info("Finished parsing");
		// Query processors
		List<AbstractQueryProcessor> processors = new ArrayList<>();
		// Add you query processor here
		q = new Query2(measure);
		processors.add(q);
		// processors.add(new SumQuery(measure));
		// Register query processors
		for (AbstractQueryProcessor queryProcessor : processors) {
			dispatch.registerQueryProcessor(queryProcessor);
		}
		// Initialize the latch with the number of query processors
		CountDownLatch latch = new CountDownLatch(processors.size());
		// Set the latch for every processor
		for (AbstractQueryProcessor queryProcessor : processors) {
			queryProcessor.setLatch(latch);
		}
		for (AbstractQueryProcessor queryProcessor : processors) {
			Thread t = new Thread(queryProcessor);
			t.setName("QP" + queryProcessor.getId());
			t.start();
		}
		// Start everything dispatcher first, not as a thread
		dispatch.run();
		logger.info("Finished Dispatching");
		// Wait for the latch
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("Error while waiting for the program to end", e);
		}
		// Output measure and ratio per query processor
		measure.setProcessedRecords(dispatch.getRecords());
		measure.outputMeasure();
		// Compare.tabmin();
		// String l1= "FF965116CF16F832F2C7F940F16746E5";
		// float x1 = (float) -73.98093;
		// float y1= (float) 40.779877;
		// float x2= (float) -73.96889;
		// float y2= (float) 40.788452f;
		// DebsRecord testF = new DebsRecord("", "", 4, 4, 4, 4, 4, 4, 4, 4, "",
		// 4, 4, 4, 4, 4, 4, false);
		// DebsRecord testV = new DebsRecord("", l1, 4, 4, 4, 4, x1, y1, x2, y2,
		// "", 4, 4, 4, 4, 4, 4, false);
		//
		// System.out.println("Route  : " + BloomFilterGuava.check(testV));
		// System.out.println("Route  : " + BloomFilterGuava.check(testF));
	}

}