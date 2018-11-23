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
					wyn = Math.abs(d - 7.0);
				}
				return wyn;
			}
		};
		
		List<Double> low = new ArrayList<Double>();
		List<Double> upp = new ArrayList<Double>();
		low.add(0.0);
		upp.add(15.0);
		
		int N = 20;
		int D = 1;
		int I = 130;
		
		WolfPack pack = new WolfPack(N, D, low, upp);
		Wolf solution = pack.findMinimum(f, I);
		
		System.out.println("Solution: " + solution);
		System.out.println("Value at solution: " + f.eval(solution.getPos()));
		
	}

}
