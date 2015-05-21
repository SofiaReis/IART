package parkinson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.JTextArea;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class ParkinsonClassification {

	private JTextArea logsOut;
	private String trainDataPath, testDataPath, testOption;

	private Instances trainDataInstances, testDataInstances;

	private BufferedReader trainBuffer, testBuffer;

	private Evaluation evaluation;
	private J48 classifier;

	private double elapsedTime;

	public ParkinsonClassification(String trainDataPath, String testDataPath,
			String testOption, JTextArea logsOut) throws Exception {
		this.trainDataPath = trainDataPath;
		this.testDataPath = testDataPath;
		this.testOption = testOption;
		this.logsOut = logsOut;

		classifier = new J48();

		if (!getFileBuffers())
			return;
		if (!buildInstances())
			return;

		start();
	}

	private void start() throws Exception {

		buildClassifier();

		long tStart = System.currentTimeMillis();

		if (testOption.equals("cross")) {
			crossValidation();
		} else if (testOption.equals("trainData")) {
			trainFileTest();
		} else if (testOption.equals("testData")) {
			testFile();
		}

		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		elapsedTime = tDelta / 1000.0;

		logsOut.append("Correctly Classified Instances\t\t"
				+ (int) evaluation.correct() + "\t"
				+ (double) Math.round(evaluation.pctCorrect() * 1000) / 1000
				+ "%\n");
		logsOut.append("Incorrectly Classified Instances\t\t"
				+ (int) evaluation.incorrect() + "\t"
				+ (double) Math.round(evaluation.pctIncorrect() * 1000) / 1000
				+ "%\n");
		
//		logsOut.append(evaluation.toSummaryString());
		logsOut.append("Elapsed time\t\t\t" + elapsedTime + "\n\n");
		logsOut.append(evaluation.toMatrixString() + "\n");

	}

	private boolean buildInstances() {
		try {
			trainDataInstances = new Instances(trainBuffer);
		} catch (IOException e) {
			logsOut.append("Error while building train data instances. Check file for errors! \n\n");
			e.printStackTrace();
			return false;
		}

		if (!testDataPath.equals("")) {
			try {
				testDataInstances = new Instances(testBuffer);
			} catch (IOException e) {
				logsOut.append("Error while building test data instances. Check file for errors! \n\n");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private boolean getFileBuffers() {
		try {
			trainBuffer = new BufferedReader(new FileReader(trainDataPath));
		} catch (FileNotFoundException e) {
			logsOut.append("* Train data file not found in selected path! Exiting...\n\n");
			e.printStackTrace();
			return false;
		}

		if (!testDataPath.equals("")) {
			try {
				testBuffer = new BufferedReader(new FileReader(testDataPath));
			} catch (FileNotFoundException e) {
				logsOut.append("Test data file not found in selected path! Exiting...\n");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private void testFile() throws Exception {
		evaluation = new Evaluation(testDataInstances);
		evaluation.evaluateModel(classifier, testDataInstances);
	}

	private void crossValidation() throws Exception {
		evaluation = new Evaluation(trainDataInstances);
		evaluation.crossValidateModel(classifier, trainDataInstances, 10,
				new Random(1));
	}

	private void trainFileTest() throws Exception {
		evaluation = new Evaluation(trainDataInstances);
		evaluation.evaluateModel(classifier, trainDataInstances);
	}

	private void buildClassifier() throws Exception {
		trainDataInstances
				.setClassIndex(trainDataInstances.numAttributes() - 1);
		if (!testDataPath.equals(""))
			testDataInstances
					.setClassIndex(testDataInstances.numAttributes() - 1);

		Remove remove1 = new Remove();
		remove1.setAttributeIndices("1,2,4,7,12,13,14,25,28"); // por aqui os
																// indices dos
		// atributos a remover
		remove1.setInvertSelection(false);
		remove1.setInputFormat(trainDataInstances);
		trainDataInstances = Filter.useFilter(trainDataInstances, remove1);

		if (!testDataPath.equals("")) {
			Remove remove2 = new Remove();
			remove2.setAttributeIndices("1,2,4,7,12,13,14,25");
			remove2.setInvertSelection(false);
			remove2.setInputFormat(testDataInstances);
			testDataInstances = Filter.useFilter(testDataInstances, remove2);
		}

		classifier.buildClassifier(trainDataInstances);
	}

	public J48 getClassifier() {
		return classifier;
	}
}
