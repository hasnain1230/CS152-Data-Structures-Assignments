package Assignment5.Friends.src.friends;

import java.util.*;

import Assignment5.Friends.src.structures.Queue;
import Assignment5.Friends.src.structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		ArrayList<String> shortestChain = new ArrayList<>();

		if (p1.equals(p2) && g.map.containsKey(p1) && g.map.containsKey(p2)) {
			shortestChain.add(p1);
			return shortestChain;
		} else if (g == null) {
			return null;
		} else if (!(g.map.containsKey(p1)) || !(g.map.containsKey(p2))) {
			return null;
		}

		Queue<Person> stillToBeVisited = new Queue<>();
		Person[] people = g.members;
		HashMap<String, Integer> peopleMap = g.map;
		HashMap<String, Person> previousParent = new HashMap<>();
		HashSet<Person> alreadyVisited = new HashSet<>();
		Person currentPerson = people[peopleMap.get(p1)];

		stillToBeVisited.enqueue(currentPerson);

		while (!(stillToBeVisited.isEmpty())) {
			currentPerson = stillToBeVisited.dequeue();
			alreadyVisited.add(currentPerson);
			Friend currentFriend = currentPerson.first;

			while (currentFriend != null) {
				Person testing = people[currentFriend.fnum];
				if (!(alreadyVisited.contains(people[currentFriend.fnum]))) {
					stillToBeVisited.enqueue(people[currentFriend.fnum]);
				} else {
					previousParent.put(currentPerson.name, people[currentFriend.fnum]);
				}
				currentFriend = currentFriend.next;
			}
		}


		if (previousParent.containsKey(p2)) { // There is a better way to do this, but since I am doing this in a rush, this will have to do for now.
			return rebuildPath(previousParent, p1, p2);
		} else {
			return null;
		}
	}

	private static ArrayList<String> findCliques(Person[] people, boolean[] visited, int currentIndex, ArrayList<String> currentClique, String school) { // Finds the currentClique of the current person in the clique method. This is done easiest recursively, even though it is a bit slower.
		currentClique.add(people[currentIndex].name);
		visited[currentIndex] = true;

		Friend currentFriend = people[currentIndex].first;

		while (currentFriend != null) {
			if (people[currentFriend.fnum].student && people[currentFriend.fnum].school.equals(school) && !(visited[currentFriend.fnum])) {
				int currentFriendIndex = currentFriend.fnum;
				currentClique = findCliques(people, visited, currentFriendIndex, currentClique, school);
			}
			currentFriend = currentFriend.next;
		}

		return currentClique;
	}


	private static ArrayList<String> rebuildPath(HashMap<String, Person> previousParent, String p1, String p2) {
		String currentNode = p2;
		ArrayList<String> path = new ArrayList<>();
		path.add(p2);


		while (previousParent.get(currentNode) != null) {
			currentNode = previousParent.get(currentNode).name;
			path.add(currentNode);
		}

		Collections.reverse(path);

		if (path.get(0).equals(p1) && path.get(path.size() - 1).equals(p2)) {
			return path;
		} else {
			return null;
		}
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		Person[] people = g.members;
		ArrayList<ArrayList<String>> cliques = new ArrayList<>();
		boolean[] visited = new boolean[people.length];

		for (int index = 0; index < visited.length; index++) {
			ArrayList<String> currentClique = new ArrayList<>();

			if (people[index].student && !(visited[index]) && people[index].school.equals(school)) {
				currentClique = findCliques(people, visited, index, currentClique, school);
			}

			if (!(currentClique.isEmpty())) cliques.add(currentClique);
		}

		return cliques;
	}


	private static void DFSSearch(ArrayList<String> connectorPeople, boolean[] visited, int startingIndex,
								  int[] iterations, HashMap<String, Integer> DFSNumbers, HashMap<String, Integer> previousNumbers,
								  HashMap<String, Integer> memberMap, HashSet<String> temporaryNames, boolean firstPerson, Person[] people) {
		Person currentPerson = people[startingIndex];
		visited[memberMap.get(currentPerson.name)] = true;
		DFSNumbers.put(currentPerson.name, iterations[0]);
		previousNumbers.put(currentPerson.name, iterations[1]);

		Friend currentFriend = currentPerson.first;

		while (currentFriend != null) {
			int currentIndex = currentFriend.fnum;
			Person friend = people[currentIndex];

			if (!(visited[currentIndex])) {
				iterations[0] += 1;
				iterations[1] += 1;

				DFSSearch(connectorPeople, visited, currentIndex, iterations, DFSNumbers, previousNumbers, memberMap, temporaryNames, false, people);

				if (previousNumbers.get(friend.name) < DFSNumbers.get(currentPerson.name)) previousNumbers.put(currentPerson.name, Math.min(previousNumbers.get(friend.name), previousNumbers.get(currentPerson.name)));
				if (previousNumbers.get(friend.name) >= DFSNumbers.get(currentPerson.name))
					if (!(firstPerson) || temporaryNames.contains(currentPerson.name))
						if (!(connectorPeople.contains(currentPerson.name)))
							connectorPeople.add(currentPerson.name);

				temporaryNames.add(currentPerson.name);
			} else previousNumbers.put(currentPerson.name, Math.min(DFSNumbers.get(friend.name), previousNumbers.get(currentPerson.name)));

			currentFriend = currentFriend.next;
		}
	}

	/**
	 * Finds and returns all connectors in the graph.
	 *
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		Person[] people = g.members;
		HashMap<String, Integer> memberMap = g.map;

		boolean[] visitedMembers = new boolean[people.length];
		HashMap<String, Integer> DFSNumbers = new HashMap<>();
		HashMap<String, Integer> previousNumbers = new HashMap<>();
		HashSet<String> temp = new HashSet<>();
		ArrayList<String> connectorPeople = new ArrayList<>();

		int[] iterations = {0, 0};

		for (int index = 0; index < people.length; index++) {
			if (visitedMembers[index]) continue;
			DFSSearch(connectorPeople, visitedMembers, index, iterations, DFSNumbers, previousNumbers, memberMap, temp, true, people);
		}

		return connectorPeople;
	}
}