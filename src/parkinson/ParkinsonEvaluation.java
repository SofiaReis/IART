package parkinson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.JTextArea;

import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class ParkinsonEvaluation {

	private Evaluation evaluation;
	private String testOption;

	private JTextArea logsOut;

	private ParkinsonClassifier parkinsonClassifier;
	private Instances testDataInstances;
	private String testDataPath;
	private BufferedReader testBuffer;

	public ParkinsonEvaluation(ParkinsonClassifier parkinsonClassifier,
			String testDataPath, String testOption, JTextArea logsOut) {
		this.testDataPath = testDataPath;
		this.testOption = testOption;
		this.logsOut = logsOut;
		this.parkinsonClassifier = parkinsonClassifier;
	}

	private void testFile() throws Exception {
		evaluation = new Evaluation(testDataInstances);
		evaluation.evaluateModel(parkinsonClassifier.getClassifier(),
				testDataInstances);
	}

	private void crossValidation() throws Exception {
		evaluation = new Evaluation(parkinsonClassifier.getTrainDataInstances());
		evaluation.crossValidateModel(parkinsonClassifier.getClassifier(),
				parkinsonClassifier.getTrainDataInstances(), 10, new Random(1));
	}

	private void trainFileTest() throws Exception {
		evaluation = new Evaluation(parkinsonClassifier.getTrainDataInstances());
		evaluation.evaluateModel(parkinsonClassifier.getClassifier(),
				parkinsonClassifier.getTrainDataInstances());
	}

	public void startEvaluation() throws Exception {
		double elapsedTime;
		long tStart = System.currentTimeMillis();

		if (testOption.equals("cross")) {
			crossValidation();
		} else if (testOption.equals("trainData")) {
			trainFileTest();
		} else if (testOption.equals("testData")) {
			getFileBuffer();
			buildInstance();
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

		// logsOut.append(evaluation.toSummaryString());
		logsOut.append("Elapsed time evaluating\t\t" + elapsedTime + "s\n\n");
		logsOut.append(evaluation.toMatrixString() + "\n");

	}

	private void buildInstance() throws Exception {
		try {
			testDataInstances = new Instances(testBuffer);
		} catch (IOException e) {
			logsOut.append("Error while building test data instances. Check file for errors! \n\n");
			e.printStackTrace();
		}
		
		testDataInstances.setClassIndex(testDataInstances.numAttributes() - 1);

		Remove remove = new Remove();
		remove.setAttributeIndices("1,7,27,9,12,4,6,13,17,23,22");
		remove.setInvertSelection(false);
		remove.setInputFormat(testDataInstances);
		testDataInstances = Filter.useFilter(testDataInstances, remove);
	}

	private void getFileBuffer() {
		try {
			testBuffer = new BufferedReader(new FileReader(testDataPath));
		} catch (FileNotFoundException e) {
			logsOut.append("Test data file not found in selected path! Exiting...\n");
			e.printStackTrace();
		}
	}
}
