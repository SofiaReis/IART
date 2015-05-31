package parkinson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.JTextArea;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class ParkinsonClassifier implements Serializable {

	private static final long serialVersionUID = 4156871752001773253L;
	private JTextArea logsOut;
	private String trainDataPath;

	private Instances trainDataInstances;
	private BufferedReader trainBuffer;
	private J48 classifier;
	private boolean unprunedTree;
	protected static boolean filterAttributes;

	public ParkinsonClassifier(String trainDataPath, JTextArea logsOut,
			boolean unprunedTree, boolean filterAttributes) throws Exception {
		this.trainDataPath = trainDataPath;
		this.logsOut = logsOut;
		this.unprunedTree = unprunedTree;
		ParkinsonClassifier.filterAttributes = filterAttributes;
	}

	private boolean buildInstance() {
		try {
			trainDataInstances = new Instances(trainBuffer);
		} catch (IOException e) {
			logsOut.append("Error while building train data instances. Check file for errors! \n\n");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean getFileBuffer() {
		try {
			trainBuffer = new BufferedReader(new FileReader(trainDataPath));
		} catch (FileNotFoundException e) {
			logsOut.append("* Train data file not found in selected path! Exiting...\n\n");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean buildClassifier() throws Exception {
		double elapsedTime;
		long tStart = System.currentTimeMillis();

		classifier = new J48();
		if (unprunedTree) {
			String[] options = new String[1];
			options[0] = "-U";
			classifier.setOptions(options);
		}

		if (!getFileBuffer()) {
			logsOut.append("Error while building file buffer!");
			return false;
		}

		if (!buildInstance()) {
			logsOut.append("Error while building instance!");
			return false;
		}

		trainDataInstances
				.setClassIndex(trainDataInstances.numAttributes() - 1);

		// atributos a remover
		Remove remove = new Remove();
		remove.setAttributeIndices("1,28");
		if (filterAttributes)
			remove.setAttributeIndices("1,7,27,28,9,12,4,6,13,17,23,22");
		remove.setInvertSelection(false);
		remove.setInputFormat(trainDataInstances);
		trainDataInstances = Filter.useFilter(trainDataInstances, remove);

		classifier.buildClassifier(trainDataInstances);

		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		elapsedTime = tDelta / 1000.0;
		logsOut.append("Elapsed time training\t\t" + elapsedTime + "s\n\n");

		return true;
	}

	public J48 getClassifier() {
		return classifier;
	}

	public Instances getTrainDataInstances() {
		return trainDataInstances;
	}

}
