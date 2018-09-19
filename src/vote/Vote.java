package vote;

import java.util.ArrayList;

public class Vote extends ArrayList<String>
{
	private static final long serialVersionUID = 4345569108880831251L;
	
	Vote(String... rankings)
	{
		for (String s : rankings) add(s);
	}
	
	Vote(ArrayList<String> rankings)
	{
		super(rankings);
	}
	
	Vote(Vote other)
	{
		super(other);
	}

	public String toString()
	{
		String str = "";
		for (int i = 0; i < size(); ++i)
		{
			str += "(" + (i+1) + ") '" + get(i) + "'";
			if (i < size() - 1) str += " ";
		}
		return str;
	}
	
	public boolean equals(Object o)
	{
		if (o == null) return false;
		if (!(o instanceof Vote)) return false;
		
		Vote v = (Vote) o;
		return hashCode() == v.hashCode();
	}
	
	public int hashCode()
	{
		return super.hashCode();
	}
}
