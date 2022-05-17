package Assignment3;

import java.util.ArrayList;

/**
 * This class implements a Trie.
 *
 * @author Sesh Venugopal
 *
 */
public class Trie {

	// prevent instantiation
	private Trie() { }

	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 *
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		TrieNode root = new TrieNode(null, null, null);

		if (allWords.length == 0) {
			return null;
		}

		for (int x = 0; x < allWords.length; x++) {
			Indexes rootIndex = new Indexes(x, (short) 0, (short) (allWords[x].length() - 1));
			root.firstChild = new TrieNode(rootIndex, null, null);
			TrieNode ptr = root.firstChild;
			while (ptr != null) {
				int startIndex = ptr.substr.startIndex, endIndex = ptr.substr.endIndex, wordIndex = ptr.substr.wordIndex;

				Indexes matching = findIndex(allWords[wordIndex], allWords[x], startIndex, wordIndex);
				

			}
		}



		return root;
	}

	public static Indexes findIndex(String word1, String word2, int startIndex, int wordIndex) { // Is working as far as I am concerned.
		int minLength = Math.min(word1.length(), word2.length());

		for (int x = startIndex; x < minLength; x++) {
			if (word1.charAt(x) != word2.charAt(x)) {
				if (word1.substring(startIndex, x).length() == 0) {
					return null;
				} else {
					return new Indexes(wordIndex, (short) startIndex, (short) x);
				}
			}
		}

		return null;
	}

	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the
	 * trie whose words start with this prefix.
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell";
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell",
	 * and for prefix "bell", completion would be the leaf node that holds "bell".
	 * (The last example shows that an input prefix can be an entire word.)
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix,
	 *             order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		System.out.println(root.firstChild);

		return null;
	}


	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}

	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
					.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}

		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
}