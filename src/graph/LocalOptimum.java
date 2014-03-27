package graph;

import java.util.Comparator;

public class LocalOptimum {
	
	private int vertex;
	private int varToCompare;

	public LocalOptimum(int vertex, int nbAdjacency) {
		this.vertex = vertex;
		this.varToCompare = nbAdjacency;
	}
	
	public int getVertex() {
		return this.vertex;
	}
	
	public int getVarToCompare() {
		return this.varToCompare;
	}
	
	public static class compareVarToCompare implements Comparator<LocalOptimum> {
        @Override
        public int compare(LocalOptimum arg0, LocalOptimum arg1) {
            return arg1.getVarToCompare() - arg0.getVarToCompare();
        }
    }
}
