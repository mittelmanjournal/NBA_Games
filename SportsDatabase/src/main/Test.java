package main;

import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import abstractClasses.DatabaseManager;
import basketball.BasketballGameDataset;
import football.FootballGame;
import football.FootballGameDataset;
import football.FootballPlayerCareerGameLog;

public class Test {
	public static void main(String[] args) {
		
//		DatabaseManager dbm = new DatabaseManager("C:\\", "DatabasesManager");
//		dbm.createDatabase("footballDatabase__p");
//		dbm.createDataset("footballDatabase__p", "footballGameDataset__g");
//		FootballGameDataset fgd = dbm.getDataset("footballDatabase__p", "footballGameDataset__g");
//		fgd.createDatasetItems(2000, 2023);

//		FootballPlayerCareerGameLog fpcgl = new FootballPlayerCareerGameLog("C:\\DatabasesManager\\footballDatabase__p\\footballPlayerCareerGameLogDataset__c\\", "AhYoC.00");
//		
//		fpcgl.create();
//		
//		
//		
//		System.out.println(fpcgl.getActiveGameRowByNumber(20).text());
		
		String year = "202401060rav".substring(0, 4);
        String month = "202401060rav".substring(4, 6);
        String day = "202401060rav".substring(6, 8);
        Date d = new Date(Integer.parseInt(year) - 1900, Integer.parseInt(month) - 1, Integer.parseInt(day));
        System.out.println(d.getYear() + 1900);
        System.out.println(d.getMonth() + 1);
        System.out.println(d.getDate());
		
//		dbm.createDataset("footballDatabase__p", "footballPlayerCareerGameLogDataset__g");
	}
}
