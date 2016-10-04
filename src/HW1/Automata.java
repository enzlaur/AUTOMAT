package HW1;

/**
 * Created by laurenztolentino on 10/04/2016.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 *
 * @author CCS
 */
public class Automata {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		String output = "";

		//output = dfa_regex_0();
		//System.out.println("Regular Expression: " + output);
		nfa_dfa();
		//nfae_nfa();
	}

	public static void nfae_nfa (){
        /*
            Additional Features:
                Automate receiving of values (i.e. NFA transition table generation)
                Sorting of values {q0,q1,qf} != {q0,qf,q1}
        */
		String output = "";
		String finalState = "";
		int nStates = 4; // number of states
		int nAlphabet = 3 + 1; // number of alphabets (+2 (0 index) in the array limit for first column state labels and e-closure column)
		String alphabet = "0,1"; // e-closure is included in the alphabet for table building
		String alphabetWithEpsilon = alphabet + ",e,e-closure";
		int ctr = 0;
		int tailState = nStates; // index of the last state in the table
		ArrayList<String> StatesList = new ArrayList<String>();
		ArrayList<String> FinalStatesList = new ArrayList<String>();
		ArrayList<String> SortingList = new ArrayList<String>();
		ArrayList<String> EClosureList = new ArrayList<String>();

		String[][] TransitionTable = new String[nStates+1][nAlphabet+1];

        /* TransitionTable array uses first bracket as rows (initial states) and second as columns (transitions/destination states)
            [b1]    [b2]
            label   0       1       e       e-closure
            q0      q0      /       qf      {qo,qf}      = [0][1], [0][2], [0,3], [0,4]
            qf      /       qf      /       qf           = [1][1], [1][2], [1,3], [1,4]
        */
		// NFA with Epsilon Moves Transition Table (Second [][x] with 0 is always the label)
		TransitionTable[0][0] = "q0"; //label
		TransitionTable[0][1] = "{q2,q3}";
		TransitionTable[0][2] = "q1";
		TransitionTable[0][3] = "q1";

		TransitionTable[1][0] = "q1"; //label
		TransitionTable[1][1] = "q2";
		TransitionTable[1][2] = "";
		TransitionTable[1][3] = "q2";

		TransitionTable[2][0] = "q2"; //label
		TransitionTable[2][1] = "";
		TransitionTable[2][2] = "{q2,q3}";
		TransitionTable[2][3] = "";

		TransitionTable[3][0] = "q3"; //label
		TransitionTable[3][1] = "q3";
		TransitionTable[3][2] = "";
		TransitionTable[3][3] = "";

		finalState = "q3";

		// Setting default value of e-closure
		for (int i = 0; i < nStates; i++)
			TransitionTable[i][nAlphabet] = "";

		// Populating the e-closure
		for (int i = 0; i < nStates; i++) {
			if(!EClosureList.contains(TransitionTable[i][0]))
				EClosureList.add(TransitionTable[i][0]);
			if(!EClosureList.contains(TransitionTable[i][nAlphabet-1]))
				EClosureList.add(TransitionTable[i][nAlphabet-1]);

			//add tokenization of e to get others
			for (int k = i+1; k < nStates; k++){
				if(!TransitionTable[k][nAlphabet-1].equals("")){
					if(TransitionTable[k][0] == TransitionTable[i][nAlphabet-1])
						EClosureList.add(TransitionTable[k][nAlphabet-1]);
				}
				else break;
			}
			TransitionTable[i][nAlphabet] += EClosureList.toString().replaceAll("[\\[\\] ]", "");

			EClosureList.clear();
		}


		// State initializations
		for (int i = 0; i < nStates; i++){
			StatesList.add(TransitionTable[i][0]);
		}
		FinalStatesList.add(finalState);

        /* NFA with Epsilon Moves */
		System.out.println("NFA with Epsilon Moves Transition Table");
		System.out.println("States\t\t\t" + alphabetWithEpsilon.replaceAll(",","\t\t\t"));

		for(int i = 0; i < nStates; i++){
			for(int j = 0; j < nAlphabet+1; j++){
				if(TransitionTable[i][j]!=null && !TransitionTable[i][j].equals("")){
					if(!TransitionTable[i][0].equals("")){
						// String sorting
						StringTokenizer ST = new StringTokenizer(TransitionTable[i][j], ",");
						String sStates;
						while (ST.hasMoreTokens()) {
							sStates = ST.nextToken();
							sStates = sStates.replaceAll("[{}]", "");
							if (!SortingList.contains(sStates)) // Only add distinct states
								SortingList.add(sStates);
						}
						Collections.sort(SortingList);
						if (SortingList.size() > 1)
							TransitionTable[i][j] = "{" + SortingList.toString().replaceAll("[\\[\\] ]", "") + "}";
						else TransitionTable[i][j] = SortingList.toString().replaceAll("[\\[\\] ]", "");
						SortingList.clear();

						System.out.print(TransitionTable[i][j] + "\t\t\t");
					}
				}
				else {
					System.out.print("\t\t\t");
					ctr++;
				}
			}
			if(ctr != nAlphabet+1)
				System.out.println();
			ctr = 0;
		}
		System.out.println();
		Collections.sort(StatesList);
		System.out.println("List of States (" + StatesList.size() + "): " + StatesList);
		System.out.println("Final State/s: " + FinalStatesList + "\n");

        /* DFA */
		System.out.println("NFA Transition Table");
		System.out.println("States\t\t\t" + alphabet.replaceAll(",","\t\t\t"));

		for(int i = 0; i < nStates; i++){
			// replace labels with e-closure names
			TransitionTable[i][0] = TransitionTable[i][nAlphabet];

			for(int j = 0; j < nAlphabet-1; j++){
				if(TransitionTable[i][j]!=null && !TransitionTable[i][j].equals("")){
					if(!TransitionTable[i][0].equals("")){
						// Re-run string sorting
						StringTokenizer ST = new StringTokenizer(TransitionTable[i][j], ",");
						String sStates;
						while (ST.hasMoreTokens()) {
							sStates = ST.nextToken();
							sStates = sStates.replaceAll("[{}]", "");
							if (!SortingList.contains(sStates)) // Only add distinct states
								SortingList.add(sStates);
						}
						Collections.sort(SortingList);
						if (SortingList.size() > 1)
							TransitionTable[i][j] = "{" + SortingList.toString().replaceAll("[\\[\\] ]", "") + "}";
						else TransitionTable[i][j] = SortingList.toString().replaceAll("[\\[\\] ]", "");
						SortingList.clear();

						// search if distinct (add state if distinct)
                        /*if(!StatesList.contains(TransitionTable[i][j]) && !TransitionTable[i][j].equals("")){
                            StatesList.add(TransitionTable[i][j]);
                            tailState = StatesList.size() - 1;
                            TransitionTable[tailState][0] = TransitionTable[i][j];

                            // fill in the transitions -- another nigga to loop
                            StringTokenizer ST2 = new StringTokenizer(TransitionTable[i][j], ",");
                            while (ST2.hasMoreTokens()) {
                                sStates = ST2.nextToken().replaceAll("[{}]", "");
                                // match with [x][0]
                                for (int y = 1; y < nAlphabet+1; y++){
                                    for (int x = 0; x < tailState; x++){
                                        if(TransitionTable[x][0].equals(sStates)){
                                            if(!TransitionTable[x][y].equals("")){
                                                TransitionTable[tailState][y] += TransitionTable[x][y] + ",";
                                            }
                                       }
                                    }
                                    //TransitionTable[tailState-1][y] += "{" + + "}";
                                }
                            }
                            // get all of the values per [][x] -- loop
                        }*/

						// if with final state, add to finalState
						if(TransitionTable[i][j].contains(finalState) && !TransitionTable[i][j].equals(finalState) && !FinalStatesList.contains(TransitionTable[i][j]))
							FinalStatesList.add(TransitionTable[i][j]);

						System.out.print(TransitionTable[i][j] + "\t\t\t");
					}
				}
				else {
					System.out.print("\t\t\t");
					ctr++;
				}
			}
			if(ctr != nAlphabet+1)
				System.out.println();
			ctr = 0;
		}
		System.out.println();
		System.out.println("List of States (" + StatesList.size() + "): " + StatesList);
		System.out.println("Final State/s: " + FinalStatesList);

	}

	public static void nfa_dfa (){
        /*
            Additional Features:
                Automate receiving of values (i.e. NFA transition table generation)
                Sorting of values {q0,q1,qf} != {q0,qf,q1}
        */
		String output = "";
		String finalState = "";
		int nStates = 4; // number of states
		int nAlphabet = 2; // number of alphabets (+1 (0 index) in the array limit for first column state labels)
		String alphabet = "0,1";
		int ctr = 0;
		int tailState = nStates; // index of the last state in the table
		ArrayList<String> StatesList = new ArrayList<String>();
		ArrayList<String> FinalStatesList = new ArrayList<String>();
		ArrayList<String> SortingList = new ArrayList<String>();

		String[][] TransitionTable = new String[nStates*nStates][nAlphabet+1];

        /* TransitionTable array uses first bracket as rows (initial states) and second as columns (transitions/destination states)
            [b1]    [b2]
            label   0       1
            q0      q2      q1      = [0][1], [0][2]
            q1      q1,q2   q0,qf   = [1][1], [1][2]
            q2      qf      q2      = [2][1], [2][2]
            qf      /       /       = [3][1], [3][2]
        */
		// NFA Transition Table (Second [][x] with 0 is always the label)
		TransitionTable[0][0] = "q0"; // label
		TransitionTable[0][1] = "q2";
		TransitionTable[0][2] = "q1";

		TransitionTable[1][0] = "q1"; // label
		TransitionTable[1][1] = "{q2,q1}";
		TransitionTable[1][2] = "{q0,qf}";

		TransitionTable[2][0] = "q2"; // label
		TransitionTable[2][1] = "qf";
		TransitionTable[2][2] = "q2";

		TransitionTable[3][0] = "qf"; // label
		TransitionTable[3][1] = "";
		TransitionTable[3][2] = "";

		finalState = "qf";
		for (int i = nStates; i < (nStates*nStates); i++)
			for (int j = 0; j < nAlphabet+1; j++)
				TransitionTable[i][j] = "";


		// State initializations
		for (int i = 0; i < nStates; i++){
			StatesList.add(TransitionTable[i][0]);
		}
		FinalStatesList.add(finalState);

        /* NFA */
		System.out.println("NFA Transition Table");
		System.out.println("States\t\t\t" + alphabet.replaceAll(",","\t\t\t"));

		for(int i = 0; i < (nStates*nStates); i++){
			for(int j = 0; j < nAlphabet+1; j++){
				if(TransitionTable[i][j]!=null && !TransitionTable[i][j].equals("")){
					if(!TransitionTable[i][0].equals("")){
						// String sorting
						StringTokenizer ST = new StringTokenizer(TransitionTable[i][j], ",");
						String sStates;
						while (ST.hasMoreTokens()) {
							sStates = ST.nextToken();
							sStates = sStates.replaceAll("[{}]", "");
							if (!SortingList.contains(sStates)) // Only add distinct states
								SortingList.add(sStates);
						}
						Collections.sort(SortingList);
						if (SortingList.size() > 1)
							TransitionTable[i][j] = "{" + SortingList.toString().replaceAll("[\\[\\] ]", "") + "}";
						else TransitionTable[i][j] = SortingList.toString().replaceAll("[\\[\\] ]", "");
						SortingList.clear();

						System.out.print(TransitionTable[i][j] + "\t\t\t");
					}
				}
				else {
					System.out.print("\t\t\t");
					ctr++;
				}
			}
			if(ctr != nAlphabet+1)
				System.out.println();
			ctr = 0;
		}
		System.out.println();
		Collections.sort(StatesList);
		System.out.println("List of States (" + StatesList.size() + "): " + StatesList);
		System.out.println("Final State/s: " + FinalStatesList + "\n");

        /* DFA */
		System.out.println("DFA Transition Table");
		System.out.println("States\t\t\t" + alphabet.replaceAll(",","\t\t\t"));

		for(int i = 0; i < (nStates*nStates); i++){
			for(int j = 0; j < nAlphabet+1; j++){
				if(TransitionTable[i][j]!=null && !TransitionTable[i][j].equals("")){
					if(!TransitionTable[i][0].equals("")){
						// Re-run string sorting
						StringTokenizer ST = new StringTokenizer(TransitionTable[i][j], ",");
						String sStates;
						while (ST.hasMoreTokens()) {
							sStates = ST.nextToken();
							sStates = sStates.replaceAll("[{}]", "");
							if (!SortingList.contains(sStates)) // Only add distinct states
								SortingList.add(sStates);
						}
						Collections.sort(SortingList);
						if (SortingList.size() > 1)
							TransitionTable[i][j] = "{" + SortingList.toString().replaceAll("[\\[\\] ]", "") + "}";
						else TransitionTable[i][j] = SortingList.toString().replaceAll("[\\[\\] ]", "");
						SortingList.clear();

						// search if distinct (add state if distinct)
						if(!StatesList.contains(TransitionTable[i][j]) && !TransitionTable[i][j].equals("")){
							StatesList.add(TransitionTable[i][j]);
							tailState = StatesList.size() - 1;
							TransitionTable[tailState][0] = TransitionTable[i][j];

							// fill in the transitions -- another nigga to loop
							StringTokenizer ST2 = new StringTokenizer(TransitionTable[i][j], ",");
							while (ST2.hasMoreTokens()) {
								sStates = ST2.nextToken().replaceAll("[{}]", "");
								// match with [x][0]
								for (int y = 1; y < nAlphabet+1; y++){
									for (int x = 0; x < tailState; x++){
										if(TransitionTable[x][0].equals(sStates)){
											if(!TransitionTable[x][y].equals("")){
												TransitionTable[tailState][y] += TransitionTable[x][y] + ",";
											}
										}
									}
									//TransitionTable[tailState-1][y] += "{" + + "}";
								}
							}
							// get all of the values per [][x] -- loop
						}

						// if with final state, add to finalState
						if(TransitionTable[i][j].contains(finalState) && !TransitionTable[i][j].equals(finalState) && !FinalStatesList.contains(TransitionTable[i][j]))
							FinalStatesList.add(TransitionTable[i][j]);

						System.out.print(TransitionTable[i][j] + "\t\t\t");
					}
				}
				else {
					System.out.print("\t\t\t");
					ctr++;
				}
			}
			if(ctr != nAlphabet+1)
				System.out.println();
			ctr = 0;
		}
		System.out.println();
		System.out.println("List of States (" + StatesList.size() + "): " + StatesList);
		System.out.println("Final State/s: " + FinalStatesList);

	}

	public static String dfa_regex_1 (){
        /*
            Additional Features:
                Automate receiving of values (i.e. number of states, first set of Rs, etc.)
                Regex simplification
        */
		String output = "";
		int nStates = 3 + 1; // number of states (+1 for array)


		// R[states_used][start_state][destination_state]
		String[][][] R = new String[nStates][nStates][nStates];

		// R[0][][] means it cannot go to any other states
        /* R[0][x][y] doesn't have values because we'll be using q1 (x) as start state instead of q0.
            In case you start with q0 (adjust for loop initializations into 0)...
            R[0][0][1] = "e";
            R[0][0][2] = "1";
            R[0][0][3] = "0";
        */
		R[0][1][1] = "e";
		R[0][1][2] = "1";
		R[0][1][3] = "0";
		R[0][2][1] = "0";
		R[0][2][2] = "(e+1)";
		R[0][2][3] = ""; // No answer
		R[0][3][1] = "1";
		R[0][3][2] = "";
		R[0][3][3] = "(e+0)";

		for(int k = 1; k < nStates; k++)
			for(int i = 1; i < nStates; i++)
				for(int j = 1; j < nStates; j++) {
					R[k][i][j] = "(" + R[k-1][i][j] + "+" + R[k-1][i][k] + "(" + R[k-1][k][k] + ")*" + R[k-1][k][j] + ")";

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
		//System.out.println("Regular Expression: 1*01*0" + R[3][1][3] + " + " + R[3][1][2]);

		return output = "1*01*0" + R[3][1][3] + " + " + R[3][1][2];
	}

	public static String dfa_regex_0 (){
        /*
            Additional Features:
                Automate receiving of values (i.e. number of states, first set of Rs, etc.)
                Regex simplification
        */
		String output = "";
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
		R[0][1][2] = "c"; // No answer
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
		//System.out.println("Regular Expression: " + R[3][0][2]);

		return output = R[3][0][2];
	}

}
