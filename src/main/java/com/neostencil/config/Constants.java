package com.neostencil.config;

import java.util.ArrayList;

public class Constants {

  public static int defaultCourseValidity = 0;
	public static int maxAllowedPCs = 2;
	public static int maxAllowedMobiles = 1;
	public static ArrayList<String> redCarpetEmailList = new ArrayList<String>();
	public static ArrayList<String> sendAssignmentAlertList = new ArrayList<>();

	static {
		redCarpetEmailList.add("ashishsingla@neostencil.com");
		redCarpetEmailList.add("shriram@neostencil.com");
		redCarpetEmailList.add("aman@neostencil.com");
		redCarpetEmailList.add("dev@neostencil.com");

		sendAssignmentAlertList.add("shivam@neostencil.com");
		sendAssignmentAlertList.add("ashishsingla@neostencil.com");
		sendAssignmentAlertList.add("ashima@neostencil.com");
		sendAssignmentAlertList.add("aman@neostencil.com");
		sendAssignmentAlertList.add("shriram@neostencil.com");
		sendAssignmentAlertList.add("heena@neostencil.com");
		sendAssignmentAlertList.add("gurpreet@neostencil.com");
	}

}
