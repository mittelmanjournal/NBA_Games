package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Test2 {
	public static void main(String[] args) {
		try {
			Document d = Jsoup.connect("https://www.pro-football-reference.com/boxscores/200209050nyg.htm").get();
			
			Consumer<Element> printPlayerIds = player -> System.out.println(player.attr("href").substring(11, 19));
			Consumer<Element> printPlayerIds2 = new Consumer<Element>() {
			    @Override
			    public void accept(Element player) {
			        System.out.println(player.attr("href").substring(11, 19));
			    }
			};

			Jsoup.parse(d.select("#all_returns").toString().replaceAll("<!--", "").replaceAll("-->", ""))
					.select("#all_returns #div_returns table#returns tbody tr th a")
					.forEach(printPlayerIds);
			
			System.out.println();
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		
		//
//		try {
//			System.out.println(getIdsInRange(2000, 2001));
//		} catch (Exception e) {
//
//		}

	}
	
	private static void isValidId(String id) throws IllegalArgumentException {
        if(!id.matches("[A-Za-z.\\-]{6}\\d{2}")) 
        	throw new IllegalArgumentException();
    }

	public static Set<String> getIdsInRange(final int low, final int high) throws Exception {
		Set<String> ids = new HashSet();
		List<String> yearLinks = new ArrayList();
		Document allYearsPage = Jsoup.connect("https://www.pro-football-reference.com/years/").get();
		Thread.sleep(2000);
		
		allYearsPage.select("#content #all_years #div_years table#years tbody tr th a").forEach(year -> {
			int yearInt = Integer.parseInt(year.text());

			if (inRange(yearInt, low, high)) {
				yearLinks.add("https://www.pro-football-reference.com/" + year.attr("href") + "games.htm");
				System.out.println("https://www.pro-football-reference.com" + year.attr("href") + "games.htm");
			}

		});
		
		yearLinks.forEach(yearLink -> {
			try {
				Document schedule = Jsoup.connect(yearLink).get();
				Thread.sleep(2000);
				
				schedule.select("#content #all_games #div_games table#games tbody tr td[data-stat=boxscore_word] a")
						.forEach(gameRow -> ids.add(gameRow.attr("href").substring(11, 23)));
				
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		return ids;
	}

	private static boolean inRange(int yearInt, int low, int high) {
		return low <= high && yearInt >= low && yearInt <= high;
	}
}
