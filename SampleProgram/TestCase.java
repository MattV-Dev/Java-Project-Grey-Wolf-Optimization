package SampleProgram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import GreyWolfOptimization.DimensionsUnmatchedException;
import GreyWolfOptimization.Function;
import GreyWolfOptimization.WolfPack;
import GreyWolfOptimization.WolfPackParameters;
import GreyWolfOptimization.WolfPackSolution;

public class TestCase {

	public static void main(String[] args)  throws DimensionsUnmatchedException {
		// TODO Auto-generated method stub
		
		Function f = new Function(1) {
			public double eval(List<Double> args) throws DimensionsUnmatchedException {
				checkDimensions(args);
				double x = args.get(0);
				double wyn = (x - Math.PI) * (x - Math.PI) + 1;
				return wyn;
			}
		};
		
		try(Scanner input = new Scanner(System.in)){
			System.out.println("This is a tes for finding minimum and maximum of a premade function on a range.");
			
			int N = 20;
			int D = 1;
			int I = 30;
			String buffer;
			
			System.out.print("How many wolves do you want? (Press Enter for default of 20): ");
			buffer = input.nextLine();
			if(!buffer.isEmpty()) {
				N = Integer.parseInt(buffer);
			}
			
			System.out.print("How many iterations do you want? (Press Enter for default of 30): ");
			buffer = input.nextLine();
			if(!buffer.isEmpty()) {
				I = Integer.parseInt(buffer);
			}
			
			List<Double> low = new ArrayList<Double>();
			List<Double> upp = new ArrayList<Double>();
			
			System.out.print("Input lower bounds on domain separated by spaces? (Press Enter for default of 0.0's): ");
			buffer = input.nextLine();
			if(!buffer.isEmpty()) {
				String[] nums = buffer.split(" ");
				if(nums.length == D) {
					for(String s : nums) {
						low.add(Double.parseDouble(s));
					}
				}
			}
			
			System.out.print("Input upper bounds on domain separated by spaces? (Press Enter for default of 1.0's): ");
			buffer = input.nextLine();
			if(!buffer.isEmpty()) {
				String[] nums = buffer.split(" ");
				if(nums.length == D) {
					for(String s : nums) {
						upp.add(Double.parseDouble(s));
					}
				}
			}
			
			WolfPackParameters params = new WolfPackParameters(D);
			params.setPackParameters(N, I);
			if(!low.isEmpty() && !upp.isEmpty()) {
				params.setLimits(low, upp);
			}
			
			WolfPack pack = new WolfPack();
			
			System.out.println("Minimum:");
			WolfPackSolution solution = pack.findMinimum(f, params);
			System.out.println(solution);
			System.out.println("Maximum:");
			solution = pack.findMaximum(f, params);
			System.out.println(solution);
			
		}
	}

}
