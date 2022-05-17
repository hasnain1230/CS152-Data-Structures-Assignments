package Assignment4;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) throws FileNotFoundException {
		HashMap<String, Occurrence> keyWordsFromDoc = new HashMap<>(1000, 2.0f);

		Scanner sc = new Scanner(new File(docFile));

		while (sc.hasNext()) {
			String keyWord = getKeyword(sc.next()); // Get the word

			if (keyWord != null) {
				Occurrence keyWordOccuerance = keyWordsFromDoc.get(keyWord); // Find in hash table if it exists

				if (keyWordOccuerance == null) { // Not in hash table. Need to add it.
					keyWordOccuerance = new Occurrence(docFile, 1);
					keyWordsFromDoc.put(keyWord, keyWordOccuerance);
				} else {
					keyWordOccuerance.frequency++;
				}
			}
		}

		return keyWordsFromDoc;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		kws.forEach((k, v) -> {
			ArrayList<Occurrence> occurrencesFromDocuments = keywordsIndex.get(k);

			if (occurrencesFromDocuments == null) {
				occurrencesFromDocuments = new ArrayList<>(); // ArrayList does not even exist in the table yet.
				occurrencesFromDocuments.add(v);
				keywordsIndex.put(k, occurrencesFromDocuments);
			} else {
				occurrencesFromDocuments.add(v);
				insertLastOccurrence(occurrencesFromDocuments); // occurrencesFromDocuments is really just a reference to the keyWords master HashMap.
			}
		});
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) { // RegEx can be slow at times, iterating through a character array may be faster, but we are already saving a lot of time with a HashMap so it is okay. That improvment can be made though.
		word = word.toLowerCase();
		word = word.strip();
		word = word.replaceAll("[.,?:;!]+$", ""); // Reg-Ex is just wonderful once you learn it. Though I always forget it, lol. God bless you if you can do RegEx without a cheat sheet.

		if (noiseWords.contains(word) || !(word.matches("[a-z]+"))) {
			return null;
		} else {
			return word;
		}
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) { /**/
		ArrayList<Integer> midPointsToReturn = new ArrayList<>();

		if (occs.size() == 1) {
			midPointsToReturn.add(0);
			return midPointsToReturn;
		}

		Occurrence lastOccurrence = occs.remove(occs.size() - 1);
		int lower = 0;
		int upper = occs.size() - 1; // The last thing is removed, so we would not minus two, but one.
		int midPoint = -1;

		// You can't really do this recursively since you don't have a controlling variable to keep the binary search in going
		// without an overflow exception. For this reason, time go to iterative.

		while (upper >= lower) {
			midPoint = lower + (upper - lower) / 2;
			midPointsToReturn.add(midPoint);

			if (occs.get(midPoint).frequency == lastOccurrence.frequency) {
				midPointsToReturn.add(midPoint);
				occs.add(midPoint, lastOccurrence);
				return midPointsToReturn;
			} else if (occs.get(midPoint).frequency < lastOccurrence.frequency) {
				upper = midPoint - 1;
			} else {
				lower = midPoint + 1;
			}
		}

		if (midPoint > -1) {
			occs.add(midPoint, lastOccurrence);
		}

		return midPointsToReturn;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}


	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies.
	 * <p>
	 * Note that a matching document will only appear once in the result.
	 * <p>
	 * Ties in frequency values are broken in favor of the first keyword.
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same
	 * frequency f1, then doc1 will take precedence over doc2 in the result.
	 * <p>
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * <p>
	 * See assignment description for examples
	 *
	 * @param kw1 First keyword
	 * @param kw2 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 * frequencies. The result size is limited to 5 documents. If there are no matches,
	 * returns null or empty array list.
	 */

	public ArrayList<String> top5search(String kw1, String kw2) { // I'll be honest. This was just the first thing that came to mind... I doubt it will work.
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();


		ArrayList<Occurrence> keyWords1 = keywordsIndex.get(kw1);
		ArrayList<Occurrence> keyWords2 = keywordsIndex.get(kw2);
		int keyWordIndex1, keyWordIndex2;

		if (keyWords1 != null) keyWordIndex1 = 0; else keyWordIndex1 = Integer.MIN_VALUE;
		if (keyWords2 != null) keyWordIndex2 = 0; else keyWordIndex2 = Integer.MIN_VALUE;

		ArrayList<String> fileNames = new ArrayList<>();
		HashSet<String> docsMap = new HashSet<>();

		while ((docsMap.size() < 5) && (keyWordIndex1 >= 0 || keyWordIndex2 >= 0)) {
			Occurrence occ1, occ2, temp;

			if (keyWordIndex1 >= 0) {
				occ1 = keyWords1.get(keyWordIndex1);
			} else occ1 = null;

			if (keyWordIndex2 >= 0) {
				if (keyWords2 == null) occ2 = null;
				else occ2 = keyWords2.get(keyWordIndex2);
			} else {
				occ2 = null;
			}

			if (occ1 == null) {
				temp = occ2;
				keyWordIndex2 = keyWordIndex2 + 1;
			} else if (occ2 == null) {
				temp = occ1;
				keyWordIndex1 = keyWordIndex1 + 1;
			} else {
				if (occ1.frequency >= occ2.frequency) {
					temp = occ1;
					keyWordIndex1 = keyWordIndex1 + 1;
				} else {
					temp = occ2;
					keyWordIndex2 = keyWordIndex2 + 1;
				}
			}

			if (temp != null) {
				if (!(docsMap.contains(temp.document))) {
					fileNames.add(temp.document);
					docsMap.add(temp.document); // Need hashset to make sure there are not any duplicates.
				}
			}

			if (keyWordIndex1 >= 0 && keyWordIndex1 == keyWords1.size()) keyWordIndex1 = Integer.MIN_VALUE;
			if (keyWordIndex2 >= 0 && keyWordIndex2 == keyWords2.size()) keyWordIndex2 = Integer.MIN_VALUE;
		}

		if (fileNames.isEmpty()) {
			return null;
		}

		return fileNames;
	}
}
