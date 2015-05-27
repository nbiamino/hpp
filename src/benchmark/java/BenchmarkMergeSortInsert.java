@State(Scope.Thread)
public class BenchmarkMergeSortInsert {
	@Benchmark
	@Fork(value = 1)
	@Warmup(iterations = 1)
	@Measurement(iterations = 1)
	public boolean testMethod() {
	}
}