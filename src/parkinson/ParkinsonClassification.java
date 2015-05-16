package parkinson;

import javax.swing.JTextArea;

public class ParkinsonClassification {

	private JTextArea logsOut;
	private String trainDataPath, testDataPath, testOption;

	public ParkinsonClassification(String trainDataPath, String testDataPath,
			String testOption, JTextArea logsOut) {
		this.trainDataPath = trainDataPath;
		this.testDataPath = testDataPath;
		this.testOption = testOption;
		this.logsOut = logsOut;
		
		
	}
}
