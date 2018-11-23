package GreyWolfOptimization;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WolfPack {
	private List<Wolf> pack;
	List<Double> lLimits, uLimits;
	
	public WolfPack(int N, int D, List<Double> lLimits, List<Double> uLimits) {
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
		
		
	}
	
	public Wolf findMinimum(Function f, int I) {
		Wolf wAlpha = null;
		Wolf wBeta = null;
		Wolf wDelta = null;
		double wAlphaBest = Double.POSITIVE_INFINITY;
		double wBetaBest = Double.POSITIVE_INFINITY;
		double wDeltaBest = Double.POSITIVE_INFINITY;
		Random rand = new Random();
		

		System.out.println("Initial:");
		int cnt = 1;
		for(Wolf w : pack) {
			System.out.println("\tWolf#" + cnt++ + ": " + w + ", Value: " + f.eval(w.getPos()));
		}
		
		for(Wolf w : pack) {
			double fVal = f.eval(w.getPos());
			if(fVal < wAlphaBest) {
				wAlpha = w;
				wAlphaBest = fVal;
			}
		}
		for(Wolf w : pack) {
			double fVal = f.eval(w.getPos());
			if(fVal < wBetaBest && w != wAlpha) {
				wBeta = w;
				wBetaBest = fVal;
			}
		}
		for(Wolf w : pack) {
			double fVal = f.eval(w.getPos());
			if(fVal < wDeltaBest && w != wBeta && w != wAlpha) {
				wDelta = w;
				wDeltaBest = fVal;
			}
		}
		
		System.out.println(" Alpha: " + wAlpha);
		System.out.println(" Beta: " + wBeta);
		System.out.println(" Delta: " + wDelta);
		
		for(int h=0;h<I;h++) {
			System.out.println("Iteration: " + h);
			
			double a = h * 2 / (double) I;
			
			for(int i=0;i<pack.size();i++) {
				for(int j=0;j<pack.get(i).getPos().size();j++) {
					
					double r1 = rand.nextDouble();
					double r2 = rand.nextDouble();
					
					double A1 = 2 * a * r1 - a;
					double C1 = 2 * r2;
					double DAlpha = Math.abs(C1 * wAlpha.posAtIndex(j) - pack.get(i).posAtIndex(j));
					double X1 = wAlpha.posAtIndex(j) - A1 * DAlpha;
					
					r1 = rand.nextDouble();
					r2 = rand.nextDouble();
					
					double A2 = 2 * a * r1 - a;
					double C2 = 2 * r2;
					double DBeta = Math.abs(C2 * wBeta.posAtIndex(j) - pack.get(i).posAtIndex(j));
					double X2 = wBeta.posAtIndex(j) - A2 * DBeta;
					
					r1 = rand.nextDouble();
					r2 = rand.nextDouble();
					
					double A3 = 2 * a * r1 - a;
					double C3 = 2 * r2;
					double DDelta = Math.abs(C3 * wDelta.posAtIndex(j) - pack.get(i).posAtIndex(j));
					double X3 = wDelta.posAtIndex(j) - A3 * DDelta;
					
					pack.get(i).setAtIndex(j, (X1 + X2 + X3) / 3);	
				}
			}
			
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
			cnt = 1;
			for(Wolf w : pack) {
				System.out.println("\tWolf#" + cnt++ + ": " + w + ", Value: " + f.eval(w.getPos()));
			}	
			
			wAlphaBest = Double.POSITIVE_INFINITY;
			wBetaBest = Double.POSITIVE_INFINITY;
			wDeltaBest = Double.POSITIVE_INFINITY;
			for(Wolf w : pack) {
				double fVal = f.eval(w.getPos());
				if(fVal < wAlphaBest) {
					wAlpha = w;
					wAlphaBest = fVal;
				}
			}
			for(Wolf w : pack) {
				double fVal = f.eval(w.getPos());
				if(fVal < wBetaBest && w != wAlpha) {
					wBeta = w;
					wBetaBest = fVal;
				}
			}
			for(Wolf w : pack) {
				double fVal = f.eval(w.getPos());
				if(fVal < wDeltaBest && w != wBeta && w != wAlpha) {
					wDelta = w;
					wDeltaBest = fVal;
				}
			}
			System.out.println(" Alpha: " + wAlpha);
			System.out.println(" Beta: " + wBeta);
			System.out.println(" Delta: " + wDelta);
		}
		
		return wAlpha;
	}
	
	public Wolf findMaximum(Function f, int I) {
		Wolf wAlpha = null;
		Wolf wBeta = null;
		Wolf wDelta = null;
		double wAlphaBest = Double.NEGATIVE_INFINITY;
		double wBetaBest = Double.NEGATIVE_INFINITY;
		double wDeltaBest = Double.NEGATIVE_INFINITY;
		Random rand = new Random();
		
		System.out.println("Initial:");
		int cnt = 1;
		for(Wolf w : pack) {
			System.out.println("\tWolf#" + cnt++ + ": " + w + ", Value: " + f.eval(w.getPos()));
		}
		
		for(Wolf w : pack) {
			double fVal = f.eval(w.getPos());
			if(fVal > wAlphaBest) {
				wAlpha = w;
				wAlphaBest = fVal;
			}
		}
		for(Wolf w : pack) {
			double fVal = f.eval(w.getPos());
			if(fVal > wBetaBest && w != wAlpha) {
				wBeta = w;
				wBetaBest = fVal;
			}
		}
		for(Wolf w : pack) {
			double fVal = f.eval(w.getPos());
			if(fVal > wDeltaBest && w != wBeta && w != wAlpha) {
				wDelta = w;
				wDeltaBest = fVal;
			}
		}
		
		System.out.println(" Alpha: " + wAlpha);
		System.out.println(" Beta: " + wBeta);
		System.out.println(" Delta: " + wDelta);
		
		for(int h=0;h<I;h++) {
			System.out.println("Iteration: " + h);
			
			double a = h * 2 / (double) I;
			
			for(int i=0;i<pack.size();i++) {
				for(int j=0;j<pack.get(i).getPos().size();j++) {
					
					double r1 = rand.nextDouble();
					double r2 = rand.nextDouble();
					
					double A1 = 2 * a * r1 - a;
					double C1 = 2 * r2;
					double DAlpha = Math.abs(C1 * wAlpha.posAtIndex(j) - pack.get(i).posAtIndex(j));
					double X1 = wAlpha.posAtIndex(j) - A1 * DAlpha;
					
					r1 = rand.nextDouble();
					r2 = rand.nextDouble();
					
					double A2 = 2 * a * r1 - a;
					double C2 = 2 * r2;
					double DBeta = Math.abs(C2 * wBeta.posAtIndex(j) - pack.get(i).posAtIndex(j));
					double X2 = wBeta.posAtIndex(j) - A2 * DBeta;
					
					r1 = rand.nextDouble();
					r2 = rand.nextDouble();
					
					double A3 = 2 * a * r1 - a;
					double C3 = 2 * r2;
					double DDelta = Math.abs(C3 * wDelta.posAtIndex(j) - pack.get(i).posAtIndex(j));
					double X3 = wDelta.posAtIndex(j) - A3 * DDelta;
					
					pack.get(i).setAtIndex(j, (X1 + X2 + X3) / 3);	
				}
			}
			
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
			cnt = 1;
			for(Wolf w : pack) {
				System.out.println("\tWolf#" + cnt++ + ": " + w + ", Value: " + f.eval(w.getPos()));
			}	
			
			wAlphaBest = Double.NEGATIVE_INFINITY;
			wBetaBest = Double.NEGATIVE_INFINITY;
			wDeltaBest = Double.NEGATIVE_INFINITY;
			for(Wolf w : pack) {
				double fVal = f.eval(w.getPos());
				if(fVal > wAlphaBest) {
					wAlpha = w;
					wAlphaBest = fVal;
				}
			}
			for(Wolf w : pack) {
				double fVal = f.eval(w.getPos());
				if(fVal > wBetaBest && w != wAlpha) {
					wBeta = w;
					wBetaBest = fVal;
				}
			}
			for(Wolf w : pack) {
				double fVal = f.eval(w.getPos());
				if(fVal > wDeltaBest && w != wBeta && w != wAlpha) {
					wDelta = w;
					wDeltaBest = fVal;
				}
			}
			System.out.println(" Alpha: " + wAlpha);
			System.out.println(" Beta: " + wBeta);
			System.out.println(" Delta: " + wDelta);
		}
		
		return wAlpha;
	}
}
