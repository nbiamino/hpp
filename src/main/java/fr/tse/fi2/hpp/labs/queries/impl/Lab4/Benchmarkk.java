package fr.tse.fi2.hpp.labs.queries.impl.Lab4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.StreamingDispatcher;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

@State(Scope.Thread)
public class Benchmarkk {

	final static Logger logger = LoggerFactory.getLogger(Benchmarkk.class);
	private MembershipQuery q;

	/**
	 * @param args
	 * @throws IOException
	 */

	@Setup
	public void prepare() {
		// Init query time measure
		QueryProcessorMeasure measure = new QueryProcessorMeasure();
		// Init dispatcher
		StreamingDispatcher dispatch = new StreamingDispatcher(
				"src/main/resources/data/sorted_data.csv");

		// Query processors
		List<AbstractQueryProcessor> processors = new ArrayList<>();
		// Add you query processor here
		q = new MembershipQuery(measure);
		processors.add(q);
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
		// Start everything
		for (AbstractQueryProcessor queryProcessor : processors) {
			// queryProcessor.run();
			Thread t = new Thread(queryProcessor);
			t.setName("QP" + queryProcessor.getId());
			t.start();
		}
		Thread t1 = new Thread(dispatch);
		t1.setName("Dispatcher");
		t1.start();

		// Wait for the latch
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("Error while waiting for the program to end", e);
		}
		// Output measure and ratio per query processor
		measure.setProcessedRecords(dispatch.getRecords());
		measure.outputMeasure();

		logger.info(new Integer(q.getRecs().size()).toString());

	}

	@Benchmark
	@Fork(value = 1)
	@Warmup(iterations = 1)
	@Measurement(iterations = 1)
	public boolean testMethod() {
		Boolean res = q.Search(-73.987129f, 40.744877f, -73.980278f,
				40.735222f, "DB9AC95FB46B93081701BCC50F84B378");
		return res;

	}
}
