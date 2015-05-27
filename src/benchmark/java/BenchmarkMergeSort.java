@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Fork(1)
@State(Scope.Benchmark)
public class benchmarkMergeSort {

	int[] liste;
	
	@Param({"100000", "1000000", "10000000"})
	private int taille;

	
	@Setup
	public void init(){
		liste = MergeSortMono.genererAleatoire(taille);
	}

	@Benchmark
	public void testMethod1() {
		int[] listeTrie = MergeSortMono.trier(liste);
		System.out.println("Fin du tri");
	}
	
	@Benchmark
	public void testMethod2() {
		int[] listeTrie = MergeSortMono.trierInsertionSort(liste);
		System.out.println("Fin du tri");
	}
	
	@Benchmark
	public void testMethod3() {
		MergeSortMulti tri1 = new MergeSortMulti(liste);
		int cores = Runtime.getRuntime().availableProcessors();
		ForkJoinPool forkJoinPool = new ForkJoinPool(cores);
		int[] listeTrie = forkJoinPool.invoke(tri1);
		System.out.println("Fin du tri");
	}
	
	
	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
		.include(benchmarkMergeSort.class.getSimpleName())
		.build();
		new Runner(opt).run();
	}

}