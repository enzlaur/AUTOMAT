/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HW1;

/**
 *
 * @author CCS
 */
public class asdf {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
        /*
            Additional Features:
                Automate receiving of values (i.e. number of states, first set of Rs, etc.)
                Regex simplification
        */
		int nStates = 3; // number of states (+1 for array)

		// R[states_used][start_state][destination_state]
		String[][][] R = new String[nStates+1][nStates][nStates];

		// R[0][][] means it cannot go to any other states
        /* R[0][x][y] doesn't have values because we'll be using q1 (x) as start state instead of q0.
            In case you start with q0 (adjust for loop initializations into 0)...
            R[0][0][1] = "e";
            R[0][0][2] = "1";
            R[0][0][3] = "0";
        */
		R[0][0][0] = "a";
		R[0][0][1] = "b";
		R[0][0][2] = "0";
		R[0][1][0] = "0";
		R[0][1][1] = "b";
		R[0][1][2] = "c";
		R[0][2][0] = "0";
		R[0][2][1] = "0";
		R[0][2][2] = "c";

		for(int k = 1; k < nStates+1; k++)
			for(int i = 0; i < nStates; i++)
				for(int j = 0; j < nStates; j++) {
					R[k][i][j] = "(" + R[k-1][i][j] + "+" + R[k-1][i][k-1] + "(" + R[k-1][k-1][k-1] + ")*" + R[k-1][k-1][j] + ")";

					// insert simplification rules here... (if empties have no value here, we can delete all e and clear out excessive parenthesis)
					R[k][i][j] = R[k][i][j].replaceAll("\\(e\\+e\\(e\\)\\*e\\)", "e"); // all empty
					R[k][i][j] = R[k][i][j].replaceAll("\\(1\\+e\\(e\\)\\*1\\)", "1");
					R[k][i][j] = R[k][i][j].replaceAll("\\(0\\+e\\(e\\)\\*0\\)", "0");
					R[k][i][j] = R[k][i][j].replaceAll("\\(0\\+0\\(e\\)\\*e\\)", "0");
					R[k][i][j] = R[k][i][j].replaceAll("\\(0\\+e\\(e\\)\\*0\\)", "0");
					R[k][i][j] = R[k][i][j].replaceAll("\\(\\+0\\(e\\)\\*0\\)", "00");
					R[k][i][j] = R[k][i][j].replaceAll("\\(e\\)\\*", ""); // all empty
					R[k][i][j] = R[k][i][j].replaceAll("\\(1\\+1e\\)", "1");
					R[k][i][j] = R[k][i][j].replaceAll("\\(\\+11\\)", "11");
					R[k][i][j] = R[k][i][j].replaceAll("\\(e\\+0\\)", "0");
					R[k][i][j] = R[k][i][j].replaceAll("\\(e\\+1\\)", "1");
					//R[k][i][j] = R[k][i][j].replaceAll("\\([\\(]+([10\\(\\)\\+]+)\\)[\\)]+\\*", "($1)*"); Removal of excessive parenthesis

					System.out.println("R[" + k + "][" + i + "][" + j + "] = " + R[k][i][j]);
				}

		// Print ones from start to final state/s
		System.out.println("Regular Expression: " + R[3][0][2]);

	}

}
