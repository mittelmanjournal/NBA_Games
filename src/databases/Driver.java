package databases;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Driver {
	public static void main(String[] args) {
		Game di = new Game("C:\\Users\\urimi\\OneDrive\\Desktop\\NFLDatabase\\Games\\", "200209050nyg");
		di.update();
		
		try (FileWriter fileWriter = new FileWriter(
				"C:\\Users\\urimi\\OneDrive\\Desktop\\NFLDatabase\\Games\\200209050nyg_2.html");
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
			bufferedWriter.write(di.decompressToDocument().outerHtml());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
