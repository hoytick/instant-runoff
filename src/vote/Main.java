package vote;

import java.util.ArrayList;
import java.util.Random;

public class Main
{
	public static void main(String[] args)
	{
		String A = "Abigail Washingtoast";
		String B = "Bill Eisenhamper";
		String C = "Charles Obample";
		String D = "Darren Forb";
		
		String C1 = "Gruyere";
		String C2 = "Mozarella";
		String C3 = "Blue";
		String C4 = "Crystal";
		String C5 = "Gouda";
		String C6 = "Goat";
		String C7 = "Cheddar";
		
		long seed = System.currentTimeMillis();
		System.out.println(seed + "L");
		Random rand = new Random(seed);
		
		ArrayList<Vote> votes = new ArrayList<Vote>();
		
		for (int i = 0; i < rand.nextInt(100); ++i) votes.add(new Vote(C6, C1, C5));
		for (int i = 0; i < rand.nextInt(100); ++i) votes.add(new Vote(C2, C6, C5));
		for (int i = 0; i < rand.nextInt(100); ++i) votes.add(new Vote(C2, C3, C1));
		for (int i = 0; i < rand.nextInt(100); ++i) votes.add(new Vote(C2, C4, C3));
		for (int i = 0; i < rand.nextInt(100); ++i) votes.add(new Vote(C3, C7, C1));
		for (int i = 0; i < rand.nextInt(100); ++i) votes.add(new Vote(C3, C1, C5));
		for (int i = 0; i < rand.nextInt(100); ++i) votes.add(new Vote(C4, C5, C1));
		for (int i = 0; i < rand.nextInt(100); ++i) votes.add(new Vote(C7, C1, C6));
		
		for (int i = 0; i < rand.nextInt(100); ++i) votes.add(new Vote(A, B, C, D));
		for (int i = 0; i < rand.nextInt(100); ++i) votes.add(new Vote(B, C, A, D));
		for (int i = 0; i < rand.nextInt(100); ++i) votes.add(new Vote(C, B, D));
		for (int i = 0; i < rand.nextInt(100); ++i) votes.add(new Vote(A, C));
		for (int i = 0; i < rand.nextInt(100); ++i) votes.add(new Vote(C));
		
		System.out.println(InstantRunoff.getWinners(Voting.tallyVotes(votes), 5));
	}
}
