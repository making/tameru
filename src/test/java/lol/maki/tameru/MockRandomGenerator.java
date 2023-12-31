package lol.maki.tameru;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class MockRandomGenerator implements Supplier<BigInteger> {

	private final AtomicLong counter;

	public MockRandomGenerator(AtomicLong counter) {
		this.counter = counter;
	}

	@Override
	public BigInteger get() {
		return BigInteger.valueOf(counter.getAndIncrement());
	}

}
