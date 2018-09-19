package vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class InstantRunoff
{
	public static Set<String> getWinners(HashMap<Vote, Integer> tally, int seats)
	{
		if (seats == 0 || tally.size() == 0) return new HashSet<String>();
		
		System.out.println("=============");
		tally = new HashMap<Vote, Integer>(tally);
		
		int total_votes = 0;
		for (int x : tally.values()) total_votes += x;
		int quota = total_votes/(seats + 1) + 1;
		
		HashMap<String, Integer> vote_count = new HashMap<String, Integer>();
		for (Vote v : tally.keySet())
		{
			for (String c : v)
			{
				vote_count.put(c, 0);
			}
		}
		for (Vote v : tally.keySet())
		{
			vote_count.put(v.get(0), (vote_count.get(v.get(0)) + tally.get(v)));
		}
		
		if (vote_count.size() <= seats)
		{
			return vote_count.keySet();
		}
		
		String winner = "";
		int max = 0;
		for (String c : vote_count.keySet())
		{
			System.out.println(vote_count.get(c) + "/" + quota + "\t" + c);
			if (vote_count.get(c) >= quota)
			{
				if (vote_count.get(c) > max)
				{
					winner = c;
					max = vote_count.get(winner);
				}
			}
		}
		if (max > 0)
		{
			System.out.println(winner + " has won this round!");
			Set<String> winners = new HashSet<String>();
			tally = InstantRunoff.removeCandidate(tally, winner, quota);
			winners.add(winner);
			winners.addAll(getWinners(tally, seats - 1));
			return winners;
		}
		
		int min = quota;
		String loser = "";
		for (String c : vote_count.keySet())
		{
			if (vote_count.get(c) < min)
			{
				min = vote_count.get(c);
				loser = c;
			}
		}
		
		System.out.println(loser + " is the loser!");
		tally = InstantRunoff.removeCandidate(tally, loser, 0);
		Set<String> winners = new HashSet<String>();
		winners.addAll(getWinners(tally, seats));
		return winners;
	}
	
	public static HashMap<Vote, Integer> removeCandidate(HashMap<Vote, Integer> tally, String toRemove, int quota)
	{
		int num_toRemove = 0;
		for (Vote v : tally.keySet())
		{
			if (v.get(0) == toRemove)
				num_toRemove += tally.get(v);
		}
		int num_toMove = num_toRemove - quota;
		
		ArrayList<Vote> votes = new ArrayList<Vote>();
		int remaining_to_move = num_toMove;
		for (Vote v : tally.keySet())
		{
			int q = tally.get(v);
			if (v.get(0) == toRemove)
			{
				q = Math.max(0, (int) Math.round(1.0*q*num_toMove/num_toRemove));
				if (q > remaining_to_move) q = remaining_to_move;
				if (v.size() > 1)
					System.out.println("+" + q + "\t" + v.get(1));
				remaining_to_move -= q;
			}
			for (int i = 0; i < q; ++i) votes.add(new Vote(v));
		}
		for (Vote v : votes) while (v.remove(toRemove));
		return Voting.tallyVotes(votes);
	}

	public static ArrayList<String> getWinners2(HashMap<Vote, Integer> votes, int seats)
	{
		System.out.println("Max seats: " + seats);
		
		int total_votes = 0;
		for (int x : votes.values()) total_votes += x;
		
		int droop_quota = total_votes/(seats + 1) + 1;
		System.out.println("Quota: " + droop_quota);
		
		HashSet<String> candidates = new HashSet<String>();
		for (Vote v : votes.keySet())
			for (String s : v) candidates.add(s);

		ArrayList<String> winners = new ArrayList<String>();
		HashMap<String, Integer> tally = new HashMap<String, Integer>();
		for (String s : candidates) tally.put(s, 0);
		for (Vote v : votes.keySet())
			tally.put(v.get(0), tally.get(v.get(0)) + votes.get(v));
		
		for (int i = 0; winners.size() < seats; ++i)
		{
			System.out.println("Round " + (i+1) + " ===================");
			System.out.println("Winners: " + winners);

			boolean someone_won = false;
			
			if (candidates.size() + winners.size() <= seats)
			{
				for (String s : candidates) winners.add(s);
				return winners;
			}

			ArrayList<String> new_winners = new ArrayList<String>();
			
			for (String s : candidates)
			{
				int num_votes = tally.get(s);
				if (num_votes >= droop_quota)
				{
					someone_won = true;
					System.out.println("+ " + s + " (" + num_votes + "/" + droop_quota + " votes)");
					new_winners.add(s);
				}
				else System.out.println("- " + s + " (" + num_votes + "/" + droop_quota + " votes)");
			}
			
			for (String s : new_winners)
			{	
				int num_votes = tally.get(s);
				if (num_votes > droop_quota)
				{
					HashMap<String, Integer> realloc = new HashMap<String, Integer>();
					for (Vote v : votes.keySet())
					{
						if (v.get(i) == s && !winners.contains(v.get(i+1)) && v.get(i+1) != "")
						{
							int new_votes = (int) Math.round(votes.get(v)*1.0*(num_votes - droop_quota)/num_votes);
							System.out.println("[+] " + s + " -(" + new_votes + ")-> " + v.get(i+1));
							if (realloc.containsKey(v.get(i+1)))
								realloc.put(v.get(i+1), realloc.get(v.get(i+1)) + new_votes);
							else
								realloc.put(v.get(i+1), new_votes);
							votes.put(v, new_votes);
						}
					}
					for (String str : realloc.keySet())
					{
						tally.put(str, tally.get(str) + realloc.get(str));
					}
				}
			}
			
			if (!someone_won)
			{
				int min = droop_quota;
				ArrayList<String> losers = new ArrayList<String>();
				for (String s : candidates)
				{
					if (tally.get(s) < min)
					{
						losers.clear();
						losers.add(s);
						min = tally.get(s);
					}
					else if (tally.get(s) == min)
					{
						losers.add(s);
						min = tally.get(s);
					}
				}
				
				String loser = losers.get(0);
				
				System.out.println(loser + " is a loser!");
				candidates.remove(loser);
				
				HashMap<String, Integer> realloc = new HashMap<String, Integer>();
				realloc.put(loser, -tally.get(loser));
				for (Vote v : votes.keySet())
				{
					System.out.println(v.get(i) + "CHEESE");
					if (v.get(i) == loser)
					{
						if (realloc.containsKey(v.get(i+1)))
							realloc.put(v.get(i+1), realloc.get(v.get(i+1)) + votes.get(v));
						else
							realloc.put(v.get(i+1), votes.get(v));
						System.out.println("[-] " + loser + " -(" + realloc.get(v.get(i+1)) + ")-> " + v.get(i+1));
					}
				}
				for (String str : realloc.keySet())
				{
					tally.put(str, tally.get(str) + realloc.get(str));
				}
			}
			
			for (String w : new_winners)
			{
				candidates.remove(w);
				winners.add(w);
			}
		}
		
		return winners;
	}
	
	protected static HashMap<Vote, Integer> tallyVotes(ArrayList<Vote> votes)
	{
		HashMap<Vote, Integer> tally = new HashMap<Vote, Integer>();
		for (Vote v : votes)
		{
			if (tally.containsKey(v)) tally.put(v, tally.get(v) + 1);
			else tally.put(v, 1);
		}
		return tally;
	}
}
