package main;

import java.util.Date;

import abstractClasses.DatabaseManager;
import football.FootballGameDataset;
import football.FootballPlayerCareerGameLogDataset;

public class Test3 {
	public static void main(String []args) {
		DatabaseManager dbm = new DatabaseManager("C:\\", "DatabasesManager");
		dbm.createDatabase("footballDatabase__p");
		
		FootballGameDataset fgds = dbm.getDataset("footballDatabase__p", "footballGameDataset__g");

		FootballPlayerCareerGameLogDataset fpcglds = dbm.getDataset("footballDatabase__p", "footballPlayerCareerGameLogDataset__c");
		
//		String year = getId().substring(0, 4);
//        String month = getId().substring(4, 6);
//        String day = getId().substring(6, 8);
//        return new Date(Integer.parseInt(year) - 1900, Integer.parseInt(month) - 1, Integer.parseInt(day));
		
		Date low = new Date(2000 - 1900, 9 - 1, 3);

		Date high = new Date(2024 - 1900, 2 - 1, 11);
		
		fpcglds.createDatasetItems(fgds.getDatasetItems(low, high));
	}
}
