package GreyWolfOptimization;

import java.util.ArrayList;
import java.util.List;

public class TestCase {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Function f = new Function() {
			public double eval(List<Double> args) {
				double wyn = 0;
				for(Double d : args) {
					wyn = Math.abs(1 / d) + Math.pow(Math.E, d) ;
				}
				return wyn;
			}
		};
		
		List<Double> low = new ArrayList<Double>();
		List<Double> upp = new ArrayList<Double>();
		low.add(0.0);
		upp.add(5.0);
		
		int N = 10;
		int D = 1;
		int I = 30;
		
		WolfPack pack = new WolfPack(N, D, low, upp);
		Wolf solution = pack.findMinimum(f, I);
		
		System.out.println("Solution: " + solution);
		System.out.println("Value at solution: " + f.eval(solution.getPos()));
		
	}

}
