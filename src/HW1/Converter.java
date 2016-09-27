package HW1;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by laurenztolentino on 09/26/2016.
 */
public class Converter
{
	/**
	 * Hello world!
	 * @param args
	 */
	public static void main(String[] args)
	{
		/* Acquire number of states */
		int stateCount;
		stateCount = scanInt();
		/* variables for storing rules in a HashMap */
		HashMap<Integer, String[][]> R = new HashMap<Integer, String[][]>();
		String paths[][] = new String[stateCount][stateCount];
		/* start */
//		for( int i = 0; i < stateCount*stateCount; i++)
//		{
//
//		}

		paths[0][0] = " E ";
		paths[0][1] = " 1 ";
		paths[0][2] = " 0 ";
		paths[1][0] = " 0 ";
		paths[1][1] = " E + 1 ";
		paths[1][2] = " N ";
		paths[2][0] = " 1 ";
		paths[2][1] = " N ";
		paths[2][2] = " E + 0 ";
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
					temp[i][j] = prev[i][j] + "+" + prev[i][k] + "(" + prev[k][k] + ")" + prev[k][j];
					println(temp[i][j]);
				}
			}

		}


	}



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
