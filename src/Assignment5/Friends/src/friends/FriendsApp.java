package Assignment5.Friends.src.friends;

import Assignment5.Friends.src.structures.Queue;
import Assignment5.Friends.src.structures.Stack;

import java.io.*;
import java.util.*;

// Testing client for Friends
public class FriendsApp {

    public static void main (String[] args) {

		if ( args.length < 1 ) {
			System.out.println("Expecting graph text file as input");
			return;
		}

		String filename = args[0];
		try {
			Graph g = new Graph(new Scanner(new File(filename)));

			// Update p1 and p2 to refer to people on Graph g
			// sam and sergei are from sample graph
			String p1 = "p301";
			String p2 = "p198";
			ArrayList<String> shortestChain = Friends.shortestChain(g, p1, p2);

			System.out.println(shortestChain);

			// ADD test for Friends.cliques() here



			// ADD test for Friends.connectors() here

		} catch (FileNotFoundException e) {
			System.out.println(filename + " not found");
		}
    }
}
