package GreyWolfOptimization;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Matt
 *
 */
public class WolfPack {
	private List<Wolf> pack;
	Wolf wAlpha = null;
	Wolf wBeta = null;
	Wolf wDelta = null;
	double wAlphaBest = Double.POSITIVE_INFINITY;
	double wBetaBest = Double.POSITIVE_INFINITY;
	double wDeltaBest = Double.POSITIVE_INFINITY;
	List<Double> lLimits, uLimits;
	
	/**
	 * Empty constructor
	 */
	public WolfPack() {
		
	}
	
	/**
	 * Initializes wolf pack
	 * @param N - number of wolves
	 * @param D - Number of dimensions of loaded function
	 * @param lLimits - lower bounds
	 * @param uLimits - upper bounds
	 */
	private void initializePack(int N, int D, List<Double> lLimits, List<Double> uLimits) {
		Random rand = new Random();
		pack = new ArrayList<Wolf>();
		this.lLimits = lLimits;
		this.uLimits = uLimits;
		
		for(int i=0;i<N;i++) {
			List<Double> pos = new ArrayList<Double>();
			for(int j=0;j<D;j++) {
				double P = rand.nextDouble() * (uLimits.get(j) - lLimits.get(j)) + lLimits.get(j);
				pos.add(P);
			}
			pack.add(new Wolf(pos));
		}
		wAlpha = null;
		wBeta = null;
		wDelta = null;
		wAlphaBest = Double.POSITIVE_INFINITY;
		wBetaBest = Double.POSITIVE_INFINITY;
		wDeltaBest = Double.POSITIVE_INFINITY;
		
	}
	
	/**
	 * Assigns new three best wolves in a pack, default less than comparator
	 * @param f - Function loaded into function
	 */
	private void chooseLeadingWolves(Function f) {
		Comparator comp = new Comparator() {
			public boolean compare(double x, double y) { return x < y;}
		};
		
		chooseLeadingWolves(f, comp);
	}
	
	/**
	 * Assigns new three best wolves in a pack, according to provided comparator
	 * @param f - Function loaded into function
	 * @param comp - object Comparator that acts as a comparing mechanism 
	 */
	private void chooseLeadingWolves(Function f, Comparator comp) {

		wAlphaBest = Double.POSITIVE_INFINITY;
		wBetaBest = Double.POSITIVE_INFINITY;
		wDeltaBest = Double.POSITIVE_INFINITY;
		for(Wolf w : pack) {
			double fVal = f.eval(w.getPos());
			if(comp.compare(fVal, wAlphaBest)) {
				wAlpha = w;
				wAlphaBest = fVal;
			}
		}
		for(Wolf w : pack) {
			double fVal = f.eval(w.getPos());
			if(comp.compare(fVal, wBetaBest) && w != wAlpha) {
				wBeta = w;
				wBetaBest = fVal;
			}
		}
		for(Wolf w : pack) {
			double fVal = f.eval(w.getPos());
			if(comp.compare(fVal, wDeltaBest) && w != wBeta && w != wAlpha) {
				wDelta = w;
				wDeltaBest = fVal;
			}
		}
		
		wAlpha = new Wolf(wAlpha);
		wBeta = new Wolf(wBeta);
		wDelta = new Wolf(wDelta);
	}
	
	/**
	 * Moves the given wolf in the pack, hunting behavior
	 * @param w - Wolf to be moved
	 * @param a - a coefficient, dictating how to move the wolf
	 */
	private void moveTheWolf(Wolf w, double a) {
		Random rand = new Random();
		for(int j=0;j<w.getPos().size();j++) {
			
			/**
			 * Move the wolves					
			 */
			double r1 = rand.nextDouble();
			double r2 = rand.nextDouble();
			
			double A1 = 2 * a * r1 - a;
			double C1 = 2 * r2;
			double DAlpha = Math.abs(C1 * wAlpha.posAtIndex(j) - w.posAtIndex(j));
			double X1 = wAlpha.posAtIndex(j) - A1 * DAlpha;
			
			r1 = rand.nextDouble();
			r2 = rand.nextDouble();
			
			double A2 = 2 * a * r1 - a;
			double C2 = 2 * r2;
			double DBeta = Math.abs(C2 * wBeta.posAtIndex(j) - w.posAtIndex(j));
			double X2 = wBeta.posAtIndex(j) - A2 * DBeta;
			
			r1 = rand.nextDouble();
			r2 = rand.nextDouble();
			
			double A3 = 2 * a * r1 - a;
			double C3 = 2 * r2;
			double DDelta = Math.abs(C3 * wDelta.posAtIndex(j) - w.posAtIndex(j));
			double X3 = wDelta.posAtIndex(j) - A3 * DDelta;
			
			w.setAtIndex(j, (X1 + X2 + X3) / 3);	
		}
	}
	
	/**
	 * Trims the wolf pack back into the limits
	 */
	private void trimToLimits() {
		for(int i=0;i<pack.size();i++) {
			Wolf w = pack.get(i);
			for(int j=0;j<w.getPos().size();j++) {
				if(w.posAtIndex(j) < lLimits.get(j)) {
					w.setAtIndex(j, lLimits.get(j));
				}
				if(w.posAtIndex(j) > uLimits.get(j)) {
					w.setAtIndex(j, uLimits.get(j));
				}
			}
		}
	}
	
	/**
	 * Finds a global minimum of a function on a given domain
	 * @param f - Function to be processed
	 * @param I - Number of iterations, minimum number of iterations is 10
	 * @param N - Number of wolves, minimum number of wolves is 3
	 * @param D - Argument count of function f()
	 * @param lLimits - lower bounds on the solution
	 * @param uLimits - upper bounds on the solution
	 * @return best Wolf in the pack, the best solution produced by the algorithm
	 */
	public Wolf findMinimum(Function f, int I, int N, int D, List<Double> lLimits, List<Double> uLimits) {
		
		double MaxA = 2.0;
		double[] progression = new double[I];
		
		initializePack(N, D, lLimits, uLimits);
		chooseLeadingWolves(f);
		
		for(int h=0;h<I;h++) {
			
			double a = MaxA - h * MaxA / (double) I;
			for(int i=0;i<pack.size();i++) {
				moveTheWolf(pack.get(i), a);
			}
			trimToLimits();
			chooseLeadingWolves(f);
			progression[h] = f.eval(wAlpha.getPos());
		}
		
		System.out.println("Best of each iteration");
		for(int i = 0; i < progression.length; i++) {
			System.out.println(i + "#: " + progression[i]);
		}
		return wAlpha;
	}
	
	/**
	 * Finds a global minimum of a function on a given domain, logged version
	 * @param f - Function to be processed
	 * @param I - Number of iterations, minimum number of iterations is 10
	 * @param N - Number of wolves, minimum number of wolves is 3
	 * @param D - Argument count of function f()
	 * @param lLimits - lower bounds on the solution
	 * @param uLimits - upper bounds on the solution
	 * @return best Wolf in the pack, the best solution produced by the algorithm
	 */
	public Wolf findMinimumLogged(Function f, int I, int N, int D, List<Double> lLimits, List<Double> uLimits) {
		
		double MaxA = 2.0;
		double[] progression = new double[I];
		
		initializePack(N, D, lLimits, uLimits);
		System.out.println("Initial:");
		int cnt = 1;
		for(Wolf w : pack) {
			System.out.println("\tWolf#" + cnt++ + ": " + w + ", Value: " + f.eval(w.getPos()));
		}
		
		chooseLeadingWolves(f);
		System.out.println(" Alpha: " + wAlpha);
		System.out.println(" Beta: " + wBeta);
		System.out.println(" Delta: " + wDelta);
		
		for(int h=0;h<I;h++) {
			System.out.println("Iteration: " + h);
			
			double a = MaxA - h * MaxA / (double) I;
			System.out.println(" a-coefficient: " + a);
			
			for(int i=0;i<pack.size();i++) {
				moveTheWolf(pack.get(i), a);
			}
			
			trimToLimits();
			
			cnt = 1;
			for(Wolf w : pack) {
				System.out.println("\tWolf#" + cnt++ + ": " + w + ", Value: " + f.eval(w.getPos()));
			}
			chooseLeadingWolves(f);
			System.out.println(" Alpha: " + wAlpha);
			System.out.println(" Beta: " + wBeta);
			System.out.println(" Delta: " + wDelta);
			
			progression[h] = f.eval(wAlpha.getPos());
		}
		
		
		System.out.println("Best of each iteration");
		for(int i = 0; i < progression.length; i++) {
			System.out.println(i + "#: " + progression[i]);
		}
		return wAlpha;
	}
	
	/**
	 * Finds a global maximum of a function on a given domain
	 * @param f - Function to be processed
	 * @param I - Number of iterations, minimum number of iterations is 10
	 * @param N - Number of wolves, minimum number of wolves is 3
	 * @param D - Argument count of function f()
	 * @param lLimits - lower bounds on the solution
	 * @param uLimits - upper bounds on the solution
	 * @return best Wolf in the pack, the best solution produced by the algorithm
	 */
	public Wolf findMaximum(Function f, int I, int N, int D, List<Double> lLimits, List<Double> uLimits) {
		
		double MaxA = 2.0;
		double[] progression = new double[I];
		Comparator comp = new Comparator() {
			public boolean compare(double x, double y) { return x > y;}
		};
		
		initializePack(N, D, lLimits, uLimits);
		chooseLeadingWolves(f, comp);
		
		for(int h=0;h<I;h++) {
			
			double a = MaxA - h * MaxA / (double) I;
			for(int i=0;i<pack.size();i++) {
				moveTheWolf(pack.get(i), a);
			}
			trimToLimits();
			chooseLeadingWolves(f, comp);
			progression[h] = f.eval(wAlpha.getPos());
		}
		
		System.out.println("Best of each iteration");
		for(int i = 0; i < progression.length; i++) {
			System.out.println(i + "#: " + progression[i]);
		}
		return wAlpha;
	}
	
	/**
	 * Finds a global maximum of a function on a given domain, logged version
	 * @param f - Function to be processed
	 * @param I - Number of iterations, minimum number of iterations is 10
	 * @param N - Number of wolves, minimum number of wolves is 3
	 * @param D - Argument count of function f()
	 * @param lLimits - lower bounds on the solution
	 * @param uLimits - upper bounds on the solution
	 * @return best Wolf in the pack, the best solution produced by the algorithm
	 */
	public Wolf findMaximumLogged(Function f, int I, int N, int D, List<Double> lLimits, List<Double> uLimits) {
		
		double MaxA = 2.0;
		double[] progression = new double[I];
		Comparator comp = new Comparator() {
			public boolean compare(double x, double y) { return x > y;}
		};
		
		initializePack(N, D, lLimits, uLimits);
		System.out.println("Initial:");
		int cnt = 1;
		for(Wolf w : pack) {
			System.out.println("\tWolf#" + cnt++ + ": " + w + ", Value: " + f.eval(w.getPos()));
		}
		
		chooseLeadingWolves(f, comp);
		System.out.println(" Alpha: " + wAlpha);
		System.out.println(" Beta: " + wBeta);
		System.out.println(" Delta: " + wDelta);
		
		for(int h=0;h<I;h++) {
			System.out.println("Iteration: " + h);
			
			double a = MaxA - h * MaxA / (double) I;
			System.out.println(" a-coefficient: " + a);
			
			for(int i=0;i<pack.size();i++) {
				moveTheWolf(pack.get(i), a);
			}
			
			trimToLimits();
			
			cnt = 1;
			for(Wolf w : pack) {
				System.out.println("\tWolf#" + cnt++ + ": " + w + ", Value: " + f.eval(w.getPos()));
			}
			chooseLeadingWolves(f, comp);
			System.out.println(" Alpha: " + wAlpha);
			System.out.println(" Beta: " + wBeta);
			System.out.println(" Delta: " + wDelta);
			
			progression[h] = f.eval(wAlpha.getPos());
		}
		
		
		System.out.println("Best of each iteration");
		for(int i = 0; i < progression.length; i++) {
			System.out.println(i + "#: " + progression[i]);
		}
		return wAlpha;
	}
}
