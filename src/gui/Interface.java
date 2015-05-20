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

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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

	private ParkinsonClassification parkinsonClassification = null;

	private JFrame tree = new JFrame("Decision Tree");

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 400);
		getContentPane().setLayout(null);

		filesLoader();

		testOptions();

		JTextArea outputArea = new JTextArea();
		outputArea.setBounds(0, 183, 494, 182);
		getContentPane().add(outputArea);

		JButton btnStart = new JButton("Start");
		btnStart.setBounds(100, 145, 97, 25);
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
		btnViewTree.setBounds(297, 145, 97, 25);
		getContentPane().add(btnViewTree);
		btnViewTree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (parkinsonClassification != null)
						viewTree();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void filesLoader() {
		JLabel lblSelectTrainData = new JLabel("Select train data");
		lblSelectTrainData.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSelectTrainData.setBounds(12, 13, 110, 25);
		getContentPane().add(lblSelectTrainData);

		trainDataPathField = new JTextField();
		trainDataPathField.setBounds(134, 13, 230, 25);
		getContentPane().add(trainDataPathField);
		trainDataPathField.setColumns(10);

		JButton btnSelectTrainData = new JButton("Select File");
		btnSelectTrainData.setBounds(376, 10, 97, 30);
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
		lblSelectTestData.setBounds(12, 51, 110, 25);
		getContentPane().add(lblSelectTestData);

		testDataPathField = new JTextField();
		testDataPathField.setColumns(10);
		testDataPathField.setBounds(134, 51, 230, 25);
		getContentPane().add(testDataPathField);

		JButton btnSelectTestData = new JButton("Select File");
		btnSelectTestData.setBounds(376, 48, 97, 30);
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
		lblTestOptions.setBounds(205, 89, 90, 20);
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
}
