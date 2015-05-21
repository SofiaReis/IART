package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import parkinson.ParkinsonClassification;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class Interface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField trainDataPathField;
	private JTextField testDataPathField;

	private String trainDataPath = "";
	private String testDataPath = "";

	private JRadioButton rdbtnUseTestData;
	private JRadioButton rdbtnCrossvalidation;
	private JRadioButton rdbtnUseTrainingData;

	private JTextArea outputArea;

	private ParkinsonClassification parkinsonClassification = null;

	private JFrame tree = new JFrame("Decision Tree");
	private JFrame rules = new JFrame("Rules");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface frame = new Interface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Interface() {

		setResizable(false);
		setTitle("Parkinson Diagnosis");
		setBounds(100, 100, 500, 400);
		setLocationRelativeTo(null); 
		getContentPane().setLayout(null);

		filesLoader();

		testOptions();

		buttons();

		outputArea = new JTextArea();
		getContentPane().add(outputArea);
		JScrollPane scrollPane = new JScrollPane(outputArea);
		scrollPane.setBounds(0, 183, 495, 183);
		getContentPane().add(scrollPane);
	}

	private void buttons() {
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(50, 145, 97, 25);
		getContentPane().add(btnStart);
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String testOption;

				if (rdbtnCrossvalidation.isSelected())
					testOption = "cross";
				else if (rdbtnUseTrainingData.isSelected())
					testOption = "trainData";
				else
					testOption = "testData";

				if (trainDataPath.equals(""))
					JOptionPane.showMessageDialog(null,
							"Select train data first!");
				else
					try {
						parkinsonClassification = new ParkinsonClassification(
								trainDataPath, testDataPath, testOption,
								outputArea);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			}
		});

		JButton btnViewTree = new JButton("View tree");
		btnViewTree.setBounds(197, 145, 97, 25);
		getContentPane().add(btnViewTree);
		btnViewTree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (parkinsonClassification != null)
						viewTree();
					else
						JOptionPane.showMessageDialog(null,
								"Make classification first!");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton btnViewRules = new JButton("View rules");
		btnViewRules.setBounds(344, 145, 97, 25);
		getContentPane().add(btnViewRules);
		btnViewRules.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (parkinsonClassification != null)
						viewRules();
					else {
						JOptionPane.showMessageDialog(null,
								"Make classification first!");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

	}

	private void filesLoader() {
		JLabel lblSelectTrainData = new JLabel("Select train data");
		lblSelectTrainData.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSelectTrainData.setBounds(14, 13, 110, 25);
		getContentPane().add(lblSelectTrainData);

		trainDataPathField = new JTextField();
		trainDataPathField.setBounds(138, 13, 230, 25);
		getContentPane().add(trainDataPathField);
		trainDataPathField.setColumns(10);

		JButton btnSelectTrainData = new JButton("Select File");
		btnSelectTrainData.setBounds(382, 10, 97, 30);
		getContentPane().add(btnSelectTrainData);
		btnSelectTrainData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"ARFF File", "arff");
				fileChooser.setFileFilter(filter);
				int temp = fileChooser.showOpenDialog(null);
				if (temp == JFileChooser.APPROVE_OPTION) {
					trainDataPath = fileChooser.getSelectedFile()
							.getAbsolutePath();
					trainDataPathField.setText(trainDataPath);
				}
			}
		});

		JLabel lblSelectTestData = new JLabel("Select test data");
		lblSelectTestData.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSelectTestData.setBounds(14, 51, 110, 25);
		getContentPane().add(lblSelectTestData);

		testDataPathField = new JTextField();
		testDataPathField.setColumns(10);
		testDataPathField.setBounds(138, 51, 230, 25);
		getContentPane().add(testDataPathField);

		JButton btnSelectTestData = new JButton("Select File");
		btnSelectTestData.setBounds(382, 48, 97, 30);
		getContentPane().add(btnSelectTestData);
		btnSelectTestData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"ARFF File", "arff");
				fileChooser.setFileFilter(filter);
				int temp = fileChooser.showOpenDialog(null);
				if (temp == JFileChooser.APPROVE_OPTION) {
					testDataPath = fileChooser.getSelectedFile()
							.getAbsolutePath();
					testDataPathField.setText(testDataPath);
					rdbtnUseTestData.setEnabled(true);
				}
			}
		});
	}

	private void testOptions() {
		JLabel lblTestOptions = new JLabel("Test Options");
		lblTestOptions.setHorizontalAlignment(SwingConstants.CENTER);
		lblTestOptions.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTestOptions.setBounds(208, 89, 90, 20);
		getContentPane().add(lblTestOptions);

		rdbtnUseTrainingData = new JRadioButton("Use training data");
		rdbtnUseTrainingData.setBounds(28, 111, 127, 25);
		getContentPane().add(rdbtnUseTrainingData);

		rdbtnCrossvalidation = new JRadioButton("Cross-validation");
		rdbtnCrossvalidation.setBounds(183, 111, 127, 25);
		getContentPane().add(rdbtnCrossvalidation);

		rdbtnUseTestData = new JRadioButton("Use test data");
		rdbtnUseTestData.setBounds(338, 111, 127, 25);
		getContentPane().add(rdbtnUseTestData);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnCrossvalidation);
		buttonGroup.add(rdbtnUseTestData);
		buttonGroup.add(rdbtnUseTrainingData);
		rdbtnUseTrainingData.setSelected(true);
		rdbtnUseTestData.setEnabled(false);
	}

	public void viewTree() throws Exception {
		tree.setSize(1300, 700);
		tree.getContentPane().setLayout(new BorderLayout());
		TreeVisualizer visualizer = null;
		visualizer = new TreeVisualizer(null, parkinsonClassification
				.getClassifier().graph(), new PlaceNode2());

		tree.getContentPane().add(visualizer, BorderLayout.CENTER);
		tree.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				tree.dispose();
			}
		});

		tree.setVisible(true);
		visualizer.fitToScreen();
	}

	private void viewRules() {

		rules.setSize(500, 700);
		rules.setVisible(true);

		// TEXT AREA
		JTextArea textArea = new JTextArea();
		rules.add(textArea, BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane(textArea);
		rules.add(scrollPane);
		textArea.setText(parkinsonClassification.getClassifier().toString());

		// BUTTON
		JButton btnExportRulesTo = new JButton("Export rules to file");
		rules.add(btnExportRulesTo, BorderLayout.SOUTH);
		btnExportRulesTo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Save File");
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"TXT file", "txt");
				fileChooser.setFileFilter(filter);
				int userSelection = fileChooser.showSaveDialog(rules);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = new File(fileChooser.getSelectedFile()
							+ ".txt");
					BufferedWriter outFile = null;
					try {
						outFile = new BufferedWriter(new FileWriter(fileToSave));
						textArea.write(outFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}
}
