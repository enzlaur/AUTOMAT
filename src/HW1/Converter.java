package HW1;

import java.util.HashMap;
import java.util.Scanner;
import java.util.jar.Pack200;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by laurenztolentino on 09/26/2016.
 */
public class Converter
{
	public void DFAtoRE()
	{
		/* Acquire number of states */
		int stateCount;
//		stateCount = scanInt();
		stateCount = 3;

		/* variables for storing rules in a HashMap */
		HashMap<Integer, String[][]> R = new HashMap<Integer, String[][]>();
		String paths[][] = new String[stateCount][stateCount];
		/* start */
//		for( int i = 0; i < stateCount*stateCount; i++)
//		{
//
//		}

		paths[0][0] = "a";
		paths[0][1] = "b";
		paths[0][2] = "N";
		paths[1][0] = "N";
		paths[1][1] = "b";
		paths[1][2] = "c";
		paths[2][0] = "N";
		paths[2][1] = "N";
		paths[2][2] = "E + c";
		R.put(0,paths);

		for( int m = 1; m < stateCount; m++ )
		{
			int k, i, j;
			String temp[][] = new String[stateCount][stateCount];
			String prev[][] = R.get(m - 1);

			k = m;

			for( i = 0; i < stateCount; i++ )
			{
				for( j = 0; j < stateCount; j++ )
				{
					temp[i][j] = prev[i][j] + " + "+ prev[i][k] + "(" + prev[k][k] + ")" + prev[k][j];
					/* simplify temp[][] here*/
					temp[i][j] = simplifyParts1(temp[i][j]);
					/* simplify temp[][] here */
					println("["+m+"]" + i+"-"+j + ": " + temp[i][j]);
				}
			}
			R.put(m, temp);
		}
	}

	public void patternTesting()
	{
		String formula = "b + b(b)b + N + b(b)c(E + c + N(b)c)N + N(b)b";
		Pattern p = Pattern.compile("\\(.*?\\)", Pattern.DOTALL);
		Matcher matcher = p.matcher(formula);
		while(matcher.find())
		{
			System.out.println("found match:"+matcher.group(0));
		}
	}

	public String simplifyPartPlus(String expression)
	{
		/* Resulting string */
		String result = "";
		/* Split by '+' */
		String[] tempSplit 	= expression.split("\\+");
		String left 		= tempSplit[0];
		String right 		= tempSplit[1];

		/*if( right.equals("") || right != null )
		{
			if (right.contains(")") || right.contains("(")) {
				simplifyRightHand(right);
			}
		}*/
		println("tempSplit size: " + tempSplit.length);
		if( right.contains("N"))
		{
			/* there can be more than one + in the part */
			if( right.contains("+") )
			{
				right = "+" + right;
			}
			else
			{
				right = "+" + right;
			}
		}
		else
		{
			right = "+" + right;
		}

		result = left + "" + right;
		return result;
	}

	public String simplifyParts1(String expression)
	{
		/* Resulting String */
		String result = "";
		/* Split by '+' */
		String[] parts	= expression.split("\\+");

		for( int i = 0; i < parts.length; i++)
		{
			parts[i] = breakdownPart(parts[i]);
			if( i == 0)
			{

				result = parts[i];
			}
			else
			{
				if( parts[i].equals("") || parts[i] == null )
				{
					/* do nothing */
				}
				else
				{
					result = result + "+" + parts[i];
				}
			}

		}

		return result;
	}

	public String breakdownPart(String part)
	{
		/* Resulting string */
		String result = "";

		if( part.contains(")N") || part.contains("N(") )
		{
			result = "";
		}
		else
		{
			result = part;
		}

		return result;
	}

	public String simplifyRightHand(String expression)
	{
		/* Resulting string */
		String result = "";
		/* Split split split*/
		String left, mid, right, temp;
		String[] tempArray;

		/* populate variables */
		temp 		= expression;
		tempArray 	= temp.split("\\(");
		left 		= tempArray[0];
		temp 		= tempArray[1];
		tempArray 	= null;
		tempArray	= temp.split("\\)");
		mid 		= tempArray[0];
		right		= tempArray[1];

		if( mid.contains(right) )
		{
			mid = right+"*";
		}

		return result;
	}

	/**
	 * Hello world!
	 *
	 * @param args
	 */
	public static void main(String[] args)
	{
		Converter c = new Converter();
		c.DFAtoRE();
//		c.patternTesting();
	}

	/*
	*
	* UTILITY CODES
	*
	* */
	public static int scanInt()
	{
		Scanner sc = new Scanner(System.in);
		int i = sc.nextInt();
		return i;
	}


	public static void println(Object output)
	{
		System.out.println(output.toString() + "");
	}
}
