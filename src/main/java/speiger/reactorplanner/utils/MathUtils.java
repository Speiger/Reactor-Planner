package speiger.reactorplanner.utils;

public class MathUtils {
	public static int sumUp(int base) {
		int sum = 0;
		for(int i = 1;i <= base;++i) {
			sum += i;
		}
		return sum;
	}
}
