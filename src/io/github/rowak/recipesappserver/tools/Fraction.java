package io.github.rowak.recipesappserver.tools;

public class Fraction {
	private int num;
	private int denom;
	
	public Fraction(int num, int denom) {
		if (denom < 0) {
			num *= -1;
			denom *= -1;
		}
		this.num = num;
		this.denom = denom;
	}
	
	public String toMixed() {
		
	}
	
	@Override
	public String toString() {
		return num + "/" + denom;
	}
	
	public Fraction simplify() {
		Fraction simple = new Fraction(num, denom);
		int cd = gcd(simple.num, simple.denom);
		simple.num /= cd;
		simple.denom /= cd;
		return simple;
	}
	
	private int gcd(int a, int b) {
		return a % b == 0 ? b : gcd(b, a % b);
	}
}
