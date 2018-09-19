package vote;

import java.util.HashMap;

public class Fptp
{
	public String getWinner(HashMap<Vote, Integer> tally)
	{		
		String winner = "No winner!";
		int max = 0;
		for (Vote s : tally.keySet())
		{
			if (tally.get(s) > max)
			{
				max = tally.get(s);
				winner = s.get(0);
			}
		}
		
		return winner;
	}
}
