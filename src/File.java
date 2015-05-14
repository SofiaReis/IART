import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class File {
	
	String train_p;
	String test_p;
	BufferedReader buffer;
	
	File(String train_d, String test_d){
		 train_p = train_d;
		 test_p = test_d;
	}
	
	public void readFile(String file){

		try {	
			String line;
			buffer = new BufferedReader(new FileReader(file));
			while((line = buffer.readLine())!= null){
				//Create cenas
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("General I/O exception: " + e.getMessage());
		}

		if(buffer != null){
			try {
				buffer.close();
			} catch (IOException e) {
				System.out.println("General I/O exception: " + e.getMessage());
			}
		}

	}
	
}
