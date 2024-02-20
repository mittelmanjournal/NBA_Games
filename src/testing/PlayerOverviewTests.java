package testing;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PlayerOverviewTests {
	public static void main(String [] args) {
		try {
			Document d = Jsoup.connect("https://www.pro-football-reference.com/players/G/GarcJe00.htm").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
