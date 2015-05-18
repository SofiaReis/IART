package parkinson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JTextArea;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class ParkinsonClassification {

	private JTextArea logsOut;
	private String trainDataPath, testDataPath, testOption;

	private Instances trainDataInstances, testDataInstances;

	private BufferedReader trainBuffer, testBuffer;

	private Evaluation evaluation;
	private Classifier classifier;

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
		
		startTraining();
		startWithTestFile();
	}

	private boolean buildInstances() {
		try {
			trainDataInstances = new Instances(trainBuffer);
		} catch (IOException e) {
			logsOut.append("Error while building train data instances. Check file for errors! \n\n");
			e.printStackTrace();
			return false;
		}

		if (testDataPath.equals("")) {
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

	private void startWithTestFile() throws Exception {
//		testDataInstances.delete(0);

		testDataInstances.setClassIndex(testDataInstances.numAttributes() - 1);
		evaluation = new Evaluation(testDataInstances);
		evaluation.evaluateModel(classifier, testDataInstances);
		
		logsOut.append("Correto: " + evaluation.pctCorrect());
	}

	private void startTraining() throws Exception {
		// apagar os atributos que queremos deixar de parte da analise
//		trainDataInstances.delete(0);
//		trainDataInstances.delete(trainDataInstances.numAttributes() - 2);

		trainDataInstances
				.setClassIndex(trainDataInstances.numAttributes() - 1);
		
		
		classifier.buildClassifier(trainDataInstances);
	}

}
