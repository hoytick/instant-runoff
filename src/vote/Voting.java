package vote;

import java.util.ArrayList;
import java.util.HashMap;

public class Voting
{	
	public static HashMap<Vote, Integer> tallyVotes(ArrayList<Vote> votes)
	{
		HashMap<Vote, Integer> tally = new HashMap<Vote, Integer>();
		for (Vote v : votes)
		{
			if (tally.containsKey(v)) tally.put(v, tally.get(v) + 1);
			else tally.put(v, 1);
		}
		tally.remove(new Vote());
		return tally;
	}
}
