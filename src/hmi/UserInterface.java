package hmi;

import graph.Graph;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;

/**
 * La classe UserInterace permet à l'utilisateur de manier le graphe.
 * Les actions sont threadées pour permettre de mettre à jour une JProgressBar.
 * @author Thibault GAUTHIER
 *
 */
public class UserInterface extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldFileLoadTime;
	private JTextField textFieldMaxCliqueSize;
	private JTextField textFieldSearchTime;
	private JTextField textFieldVertices;
	private JTextField textFieldEdges;
	private JTextField textFieldDensity;
	private JTextArea textAreaClique;
	private JProgressBar progressBar;
	private ButtonGroup choice;
	private JRadioButton rdbtnRandomAlgorithm;
	private JRadioButton rdbtnHighNumberAdjacency;
	private JRadioButton rdbtnHighNumberCommon;
	
	private Graph myGraph;
	
	private boolean loadFile_start;
	private boolean loadFile_end;
	private boolean searchMaxClique_start;
	private boolean searchMaxClique_end;
	private JTextField tfFilePath;
	private String filePath;

	/**
	 * Création de la JFrame.
	 */
	public UserInterface() {
		
		loadFile_start = false;
		loadFile_end = false;
		searchMaxClique_start = false;
		searchMaxClique_end = false;
		
		myGraph = new Graph();
		
		setTitle("Matrix Representation - Gauthier - 2013");
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(12, 390, 420, 14);
		contentPane.add(progressBar);
		
		JLabel lblFilePath = new JLabel("File path");
		lblFilePath.setBounds(12, 12, 120, 15);
		contentPane.add(lblFilePath);
		
		JButton btnLoadFile = new JButton("");
		btnLoadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clear();
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("CLQ FILES", "clq", "graph");
				fileChooser.setFileFilter(filter);
				int value = fileChooser.showOpenDialog(contentPane);
				if (value == JFileChooser.APPROVE_OPTION) {
					filePath = fileChooser.getSelectedFile().getAbsolutePath();
					tfFilePath.setText(filePath);
					loadFile_start = true;
				}
			}
		});
		btnLoadFile.setBounds(407, 7, 25, 25);
		contentPane.add(btnLoadFile);
		
		JButton btnNewButton = new JButton("Search Maximum Clique");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clear();
				if (rdbtnRandomAlgorithm.isSelected()) {
					myGraph.setRandomAlgorithm(true);
				}
				if (rdbtnHighNumberAdjacency.isSelected()) {
					myGraph.setHighNumberAdjacency(true);
				} 
				if (rdbtnHighNumberCommon.isSelected()) {
					myGraph.setHighNumberCommon(true);
				}
				myGraph.setSearchMaxClique(true);
				searchMaxClique_start = true;
				Thread t = new Thread(myGraph);
				t.start();
			}
		});
		btnNewButton.setBounds(222, 183, 210, 25);
		contentPane.add(btnNewButton);
		
		JLabel lblMaximumCliqueSizeFind = new JLabel("Maximum clique size find (vertices)");
		lblMaximumCliqueSizeFind.setBounds(12, 247, 300, 15);
		contentPane.add(lblMaximumCliqueSizeFind);
		
		textFieldMaxCliqueSize = new JTextField();
		textFieldMaxCliqueSize.setBounds(357, 245, 75, 19);
		contentPane.add(textFieldMaxCliqueSize);
		textFieldMaxCliqueSize.setColumns(10);
		
		JLabel lblSearchTime = new JLabel("Search time (ms)");
		lblSearchTime.setBounds(12, 220, 300, 15);
		contentPane.add(lblSearchTime);
		
		textFieldSearchTime = new JTextField();
		textFieldSearchTime.setBounds(357, 218, 75, 19);
		contentPane.add(textFieldSearchTime);
		textFieldSearchTime.setColumns(10);
		
		textAreaClique = new JTextArea();
		textAreaClique.setBounds(12, 274, 420, 104);
		textAreaClique.setLineWrap(true);
		textAreaClique.setWrapStyleWord(true);
		contentPane.add(textAreaClique);
	
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Graph properties", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 39, 420, 98);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblVertices1 = new JLabel("Vertices ");
		lblVertices1.setBounds(11, 36, 75, 15);
		panel.add(lblVertices1);
		
		textFieldVertices = new JTextField();
		textFieldVertices.setBounds(100, 32, 100, 23);
		panel.add(textFieldVertices);
		textFieldVertices.setColumns(10);
		
		JLabel lblEdges = new JLabel("Edges");
		lblEdges.setBounds(11, 63, 75, 15);
		panel.add(lblEdges);
		
		textFieldEdges = new JTextField();
		textFieldEdges.setBounds(100, 59, 100, 23);
		panel.add(textFieldEdges);
		textFieldEdges.setColumns(10);
		
		JLabel lblDensity = new JLabel("Density ");
		lblDensity.setBounds(218, 36, 75, 15);
		panel.add(lblDensity);
		
		textFieldDensity = new JTextField();
		textFieldDensity.setBounds(333, 32, 75, 23);
		panel.add(textFieldDensity);
		textFieldDensity.setColumns(10);
		
		JLabel lblLoadTime = new JLabel("Load time (ms)");
		lblLoadTime.setBounds(218, 63, 120, 15);
		panel.add(lblLoadTime);
		
		textFieldFileLoadTime = new JTextField();
		textFieldFileLoadTime.setBounds(333, 59, 75, 23);
		panel.add(textFieldFileLoadTime);
		textFieldFileLoadTime.setColumns(10);
		
		choice = new ButtonGroup();
		
		rdbtnRandomAlgorithm = new JRadioButton("Random");
		rdbtnRandomAlgorithm.setBounds(12, 145, 210, 23);
		rdbtnRandomAlgorithm.setSelected(true);
		contentPane.add(rdbtnRandomAlgorithm);
		choice.add(rdbtnRandomAlgorithm);
		
		rdbtnHighNumberAdjacency = new JRadioButton("High number adjacency");
		rdbtnHighNumberAdjacency.setBounds(222, 145, 210, 23);
		contentPane.add(rdbtnHighNumberAdjacency);
		choice.add(rdbtnHighNumberAdjacency);
		
		rdbtnHighNumberCommon = new JRadioButton("High number common");
		rdbtnHighNumberCommon.setBounds(12, 184, 202, 23);
		contentPane.add(rdbtnHighNumberCommon);
		choice.add(rdbtnHighNumberCommon);
		
		tfFilePath = new JTextField();
		tfFilePath.setBounds(95, 10, 300, 19);
		contentPane.add(tfFilePath);
		tfFilePath.setColumns(10);

		this.setLocationRelativeTo(null);
		run();
		
	}
	
	/**
	 * La méthode clear permet de remettre certains composants graphiques dans leur état initial.
	 * 
	 */
	private void clear() {
		progressBar.setValue(0);
		textFieldSearchTime.setText(null);
		textFieldMaxCliqueSize.setText(null);
		textAreaClique.setText(null);
	}

	@Override
	public void run() {
		
		this.setVisible(true);
		
		while (true) {
			
			if (loadFile_start) {
				progressBar.setIndeterminate(true);
				myGraph.setPath(filePath);
				myGraph.loadFile();
				loadFile_start = false;
				loadFile_end = true;
			} 
			if (loadFile_end) {
				textFieldFileLoadTime.setText(String.valueOf(myGraph.getExecTime()));
				int order = myGraph.getOrder();
				long nbEdges = myGraph.getNbEdges();
				double density = (float)(2*nbEdges) / (double)(order*(order-1));
				DecimalFormat df = new DecimalFormat();
		        df.setMaximumFractionDigits(3); //arrondi à 2 chiffres apres la virgules
		        df.setMinimumFractionDigits(3);
		        df.setDecimalSeparatorAlwaysShown(true);
		        textFieldVertices.setText(String.valueOf(order));
		        textFieldEdges.setText(String.valueOf(nbEdges));
		        textFieldDensity.setText(df.format(density));
				progressBar.setIndeterminate(false);
				progressBar.setMinimum(0);
				progressBar.setMaximum(myGraph.getOrder()-1);
				loadFile_end = false;
			}
			
			if (searchMaxClique_start) {
				progressBar.setValue(myGraph.getValue());
				if (myGraph.isCompleted()) {
					searchMaxClique_start = false;
					searchMaxClique_end = true;
				}
			}
			if (searchMaxClique_end) {
				textFieldSearchTime.setText(String.valueOf(myGraph.getExecTime()));
				textFieldMaxCliqueSize.setText(String.valueOf(myGraph.getMaxClique().size()));
				for (int p : myGraph.getMaxClique()) {
					textAreaClique.append(p + " ");
				}
				myGraph.reset();
				searchMaxClique_end = false;
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
