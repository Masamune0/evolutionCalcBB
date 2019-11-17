package evoCalc;

import java.util.Scanner;
public class Main {

	
	static Double RedSci = 0.001;
	static Double GreenSci = 0.0025;
	static Double MilSci = 0.0096;
	static Double BlueSci = 0.0264;
	static Double PurpleSci = 0.0887;
	static Double YellowSci = 0.0994;
	static Double WhiteSci = 0.2895;
	
	public static int getNbPotionNeeded(Integer curr, Integer target, Double difficulty, Double SciUsed){
		Double currEvo = curr.doubleValue() / 100;
		Double targetEvo = target.doubleValue() / 100;
		SciUsed *= difficulty;
		RedSci *= difficulty;
		GreenSci *= difficulty;
		MilSci *= difficulty;
		BlueSci *= difficulty;
		YellowSci *= difficulty;
		WhiteSci *= difficulty;
		int nbPotionUsed = 0;
		while (currEvo <= targetEvo){
			double e2 = currEvo * 100 + 1;
			double dimModifier = (1/(Math.pow(10.0, e2 * 0.017)))/(e2*0.5);
			double evoGain = SciUsed * dimModifier;
			currEvo = currEvo + evoGain;
			int scale = (int) Math.pow(10, 12);
			currEvo =  (double) Math.round(currEvo * scale) / scale;
			nbPotionUsed++;
		}
		return nbPotionUsed;
	}
	
	public static void printNbPotionNeeded(Integer curr, Integer target, Double difficulty){
		
		int Red = getNbPotionNeeded(curr,target,difficulty, RedSci);
		int Green = getNbPotionNeeded(curr,target,difficulty, GreenSci);
		int Mil = getNbPotionNeeded(curr,target,difficulty, MilSci);
		int Blue = getNbPotionNeeded(curr,target,difficulty, BlueSci);
		int Purple = getNbPotionNeeded(curr,target,difficulty, PurpleSci);
		int Yellow = getNbPotionNeeded(curr,target,difficulty, YellowSci);
		int White = getNbPotionNeeded(curr,target,difficulty, WhiteSci);
		
		System.out.println("Red Potion : " + Red + " potions to go from " +curr+ " to "+ target);
		System.out.println("Green Potion : " + Green + " potions to go from "+curr+ " to "+ target);
		System.out.println("Mil Potion : " + Mil + " potions to go from "+curr+ " to "+ target);
		System.out.println("Blue Potion : " + Blue + " potions to go from "+curr+ " to "+ target);
		System.out.println("Purple Potion : " + Purple + " potions to go from "+curr+ " to "+ target);
		System.out.println("Yellow Potion : " + Yellow + " potions to go from "+curr+ " to "+ target);
		System.out.println("White Potion : " + White + " potions to go from "+curr+ " to "+ target);
	}
	
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		Double PEACEFUL = 0.25;
		Double PIECEOFCAKE = 0.50;
		Double EASY = 0.75;
		Double NORMAL = 1.0;
		Double HARD = 1.5;
		Double NIGHTMARE = 2.0;
		Double INSANE = 3.0;
		
		System.out.println("Enter current evo:(example 20)");
		Scanner scan = new Scanner(System.in);
		Integer currEvo = Integer.parseInt(scan.next());
		System.out.println("value: "+currEvo);

		System.out.println("Enter desired evo:(example 30)");
		Scanner scan2 = new Scanner(System.in);
		Integer aimedEvo = Integer.parseInt(scan2.next());
		System.out.println("value: "+aimedEvo);
		scan.close();
		scan2.close();
		
		if (currEvo >= aimedEvo){
			throw new Exception("error curr evo >= aimed evo");
		}
		printNbPotionNeeded(currEvo,aimedEvo,NORMAL);
	}

}
