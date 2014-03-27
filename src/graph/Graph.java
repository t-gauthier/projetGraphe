package graph;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class Graph implements Runnable {
	/**
	 * Chemin du fichier contenant le graphe.
	 */
	private String path;
	/**
	 * Nombre de sommets du graphe.
	 */
	private int order;
	/**
	 * Nombre d'arêtes du graphes.
	 */
	private long nbEdges;

	/**
	 * Matrice d'adjacence du graphe.
	 */
	private int[][] matrix;
	/**
	 * Ce tableau contient le nombre de sommets adjacents de chaque sommet.
	 * Exemple : match[1] contient le nombre de sommets adjacents au sommet 1.
	 */
	private int[] match;
	
	/**
	 * Liste des sommets formant la clique maximum trouvée.
	 */
	private ArrayList<Integer> maxClique;
	
	/**
	 * Booléens servant à déterminer l'état du thread et de la recherche.
	 */
	private boolean searchMaxClique;
	private boolean randomAlgorithm;
	private boolean highNumberAdjacency;
	private boolean highNumberCommon;
	public boolean completed;
	
	private double start;
	private double end;
	private double execTime;
	private int value;

	/**
	 * Constructeur de la classe Graph.
	 */
	public Graph() {
		maxClique = new ArrayList<Integer>();
		path = "";
		order = 0;
		nbEdges = 0;
		reset();
	}
	
	public void reset() {
		maxClique.clear();
		start = end = execTime = value = 0;
		searchMaxClique = randomAlgorithm = highNumberAdjacency = highNumberCommon = completed = false;
	}

	/**
	 * Permet de charger le fichier contenant le graphe.
	 */
	public void loadFile() {
		start = System.currentTimeMillis();
		order = 0;
		nbEdges = 0;
		try {
			InputStream ips = new FileInputStream(path);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			String cutting[];
			int s1, s2;
			while ((line = br.readLine()) != null) {
				if (line.charAt(0) == 'e') {
					nbEdges++;
					cutting = line.split(" ");
					s1 = Integer.parseInt(cutting[1]);
					s2 = Integer.parseInt(cutting[2]);
					matrix[s1][s2] = 1;
					matrix[s2][s1] = 1;
					match[s1]++;
					match[s2]++;
				} else if (line.charAt(0) == 'p') {
					line = line.replaceAll("\\s+", " ");
					cutting = line.split(" ");
					order = Integer.parseInt(cutting[2]);
					matrix = new int[order + 1][order + 1];
					match = new int[order + 1];
					for (int i = 0; i < order; i++) {
						for (int j = 0; j < order; j++) {
							matrix[i][j] = 0;
							matrix[j][i] = 0;
						}
						match[i] = 0;
					}
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		end = System.currentTimeMillis();
		execTime = end - start;
	}

	/**
	 * Permet de tester si une arête existe entre deux sommets.
	 * @param a1 Premier sommet.
	 * @param a2 Second sommet.
	 * @return Retourne vrai si l'arête existe, faux sinon.
	 */
	public boolean checkEdge(int a1, int a2) {
		if (matrix[a1][a2] == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Algorithme glouton permettant de rechercher une solution approchée de clique maximum.
	 */
	public void searchMaximumClique() {
		
		start = System.currentTimeMillis();
		
		int sizePossible = 0;
		int sizeReal = 0;
		int sizeMax = 0;

		ArrayList<Integer> temp = new ArrayList<Integer>();
		ArrayList<Integer> toCheck = new ArrayList<Integer>();

		for (int i = 1; i < order ; i++) {
			value++;
			sizePossible = sizeReal = 0;
			temp.clear();
			toCheck.clear();
			temp.add(i);

			for (int j = 0; j < order; j++) {
				if (checkEdge(i, j)) {
					sizePossible++;
					toCheck.add(j);
				}
			}

			if (sizePossible > sizeMax) {
				if (highNumberAdjacency) {
					sortByNumberAdjacency(toCheck);
				} 
				if (highNumberCommon) {
					sortByNumberCommon(toCheck);
				}
				if (randomAlgorithm) {
					Collections.shuffle(toCheck);
				}
				for (int m : toCheck) {
					boolean test = true;
					for (int n : temp) {
						if (!checkEdge(m, n)) {
							test = false;
							break;
						}
					}
					if (test) {
						temp.add(m);
						sizeReal++;
					}
				}

			}

			if (sizeReal > sizeMax) {
				sizeMax = sizeReal;
				maxClique.clear();
				for (int o : temp) {
					maxClique.add(o);
				}

			}

		}
		end = System.currentTimeMillis();
		execTime = end - start;
	}
	
	/**
	 * Permet de trier les sommets de la liste en fonction du nombre de sommets adjacents qu'ils possèdent.
	 * @param list Liste des sommets à trier.
	 */
	public void sortByNumberAdjacency(ArrayList<Integer> list) {
		ArrayList<LocalOptimum> localOptimum = new ArrayList<LocalOptimum>();
		for (int i : list) {
			localOptimum.add(new LocalOptimum(i, match[i]));
		}
		list.clear();
		Collections.sort(localOptimum, new LocalOptimum.compareVarToCompare());
		for (LocalOptimum local : localOptimum) {
			list.add(local.getVertex());
		}
		localOptimum.clear();
	}
	
	/**
	 * Permet de trier les sommets de la liste en fonction du nombre de sommets adjacents qu'ils possèdent dans le liste elle-même.
	 * @param list Liste des sommets à trier.
	 */
	public void sortByNumberCommon(ArrayList<Integer> list) {
		int inc = 0;
		int temp[][] = new int[list.size()][2];
		ArrayList<LocalOptimum> localOptimum = new ArrayList<LocalOptimum>();
		for (int is : list) {
			temp[inc][0] = is;
			for (int is2 : list) {
				if (matrix[is][is2] == 1) {
					temp[inc][1]++;
				}
			}
			inc++;
		}
		for (int i=0 ; i<list.size() ; i++) {
			localOptimum.add(new LocalOptimum(temp[i][0], temp[i][1]));
		}
		list.clear();
		Collections.sort(localOptimum, new LocalOptimum.compareVarToCompare());
		for (LocalOptimum local : localOptimum) {
			list.add(local.getVertex());
		}
		localOptimum.clear();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public long getNbEdges() {
		return nbEdges;
	}

	public void setNbEdges(long nbEdges) {
		this.nbEdges = nbEdges;
	}

	public ArrayList<Integer> getMaxClique() {
		return maxClique;
	}

	public void setMaxClique(ArrayList<Integer> maxClique) {
		this.maxClique = maxClique;
	}

	public boolean isSearchMaxClique() {
		return searchMaxClique;
	}

	public void setSearchMaxClique(boolean searchMaxClique) {
		this.searchMaxClique = searchMaxClique;
	}

	public boolean isRandomAlgorithm() {
		return randomAlgorithm;
	}

	public void setRandomAlgorithm(boolean randomAlgorithm) {
		this.randomAlgorithm = randomAlgorithm;
	}

	public boolean isHighNumberAdjacency() {
		return highNumberAdjacency;
	}

	public void setHighNumberAdjacency(boolean highNumberAdjacency) {
		this.highNumberAdjacency = highNumberAdjacency;
	}

	public boolean isHighNumberCommon() {
		return highNumberCommon;
	}

	public void setHighNumberCommon(boolean highNumberCommon) {
		this.highNumberCommon = highNumberCommon;
	}

	public double getExecTime() {
		return execTime;
	}

	public void setExecTime(double execTime) {
		this.execTime = execTime;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public void run() {
		if (searchMaxClique) {
			searchMaximumClique();
			completed = true;
		}
	}

}