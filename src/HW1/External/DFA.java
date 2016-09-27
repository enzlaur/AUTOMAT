package HW1.External;

import java.util.Scanner;
import java.io.PrintStream;
import java.lang.String;
import java.util.HashSet;

public class DFA {
    final int numStates; // number of states in the state set
    final int inputAlphaSize; // number of characters in the input alphabet
    final String inputAlpha;
    final HashSet<Integer> accepting; // set of accepting states
    int[][] nextState; // this array is used to store the transition function

    public DFA(Scanner in) {
	// Creates a DFA by reading it (in YUFADF) from in.
	// Format is:
	// numStates
	// inputAlphabet
	// numAcceptingStates acceptState1 ... acceptStatek
	// numTransitions
        // i a delta(i,a)
	// ...
	// Assume states numbered 0..n-1 and state 0 is starting state
	// Don't use space as an input character!

	numStates = in.nextInt()+1; // add a junk state
	assert numStates >= 1;

	inputAlpha = in.next();
	inputAlphaSize = inputAlpha.length();

	for (int i = 0; i < inputAlphaSize; i++) 
	    // check that each character only appears once in inputAlpha
	    assert inputAlpha.lastIndexOf(inputAlpha.charAt(i)) == i;
	
	nextState = new int[numStates][inputAlphaSize];
	accepting = new HashSet<Integer>();

        boolean[][] done = new boolean[numStates][inputAlphaSize]; 
	// temporary variable used to make sure that each transition is 
	// only defined once in the input

	for (int i = 0; i < numStates; i++) {
	    for (int j = 0; j < inputAlphaSize; j++) {
		// set default values
		nextState[i][j] = numStates-1;
		done[i][j] = false;
	    }
	}
	
	// Read accepting states
	int numAccepting = in.nextInt();
	for (int k = 0; k < numAccepting; k++) {
	    int s = in.nextInt();
	    assert s >= 0 && s < numStates;
	    accepting.add(s);
	}

	// Read in transitions
	int numTransitions = in.nextInt();
	for (int k = 0; k < numTransitions; k++) {
	    int i = in.nextInt();
	    assert i >= 0 && i < numStates;
	    int j = inputAlpha.indexOf(in.next());
	    assert j >= 0 && j < inputAlphaSize; 
	    assert !done[i][j];
	    done[i][j] = true;

	    nextState [i][j] = in.nextInt();
	    assert nextState[i][j] >= 0 && nextState[i][j] < numStates;
	}
    }


    public void run(Scanner in, PrintStream out, boolean trace) {
	// Reads a string from in and runs the DFA on it.
	int state = 0; // start in state 0
	String inputString = in.next();
	for (int i = 0; i < inputString.length(); i++) {
	    state = nextState[state][inputAlpha.indexOf(inputString.charAt(i))];
	    if (trace) out.println("state after " + (i+1) + " transitions = " + state);
	}
	out.println("The DFA " + (accepting.contains(state) ? "accepts" : "rejects") + " the string " + inputString);
    }

    public String regex() {
	// Print a regex that describes the language accepted by DFA
	// (using the algorithm we went over in class).
	// This is how I generated the really long regular expression
	// in the answer to Assignment 5, question 1.

	String[][][] R = new String[numStates][numStates][numStates];

	for (int i=0; i < numStates; i++) {
	    for (int j=0; j < numStates; j++) {
		R[i][j][0] = "";
		for (int k=0; k<inputAlphaSize; k++) {
		    if (nextState[i][k] == j) {
			if (R[i][j][0].equals(""))
			    R[i][j][0] = inputAlpha.substring(k,k+1);
			else
			    R[i][j][0] = R[i][j][0] + "|" + inputAlpha.substring(k,k+1);
		    }
		}
		if (R[i][j][0].equals("")) {
		    if (i==j) R[i][j][0]="e";
		    else R[i][j][0] = "E";
		} else if (i==j)
		    R[i][j][0] = R[i][j][0] + "|e";
	    }
	}
	for (int k=1; k<numStates; k++) 
	    for (int i=0; i<numStates; i++) 
		for (int j=0; j<numStates; j++) 
		    R[i][j][k] = R[i][j][k-1] + "|(" + R[i][k][k-1] + ")(" + 
			R[k][k][k-1] + ")*(" + R[k][j][k-1] + ")";
	String result = "";
	if (accepting.isEmpty())
	    result = "E";
	else {
	    boolean first = true;
	    for (int j : accepting) {
		if (first) first = false;
		else result = result + "|";
		result = result + R[0][j][numStates-1];
	    }
	}
	return result;
    }

    public static void main(String[] args) {
	Scanner in = new Scanner(System.in);
	PrintStream out = System.out;

	DFA machine = new DFA(in);
	machine.run(in, out, true);
    }

}

