import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by zibing on 8/26/15.
 */

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    static <K extends Comparable<? super K>, V extends Comparable<? super V>>
    List<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {

        List<Map.Entry<K, V>> sortedEntries = new ArrayList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        if (e2.getValue().compareTo(e1.getValue()) != 0) {
                            return e2.getValue().compareTo(e1.getValue());
                        } else {
                            return e1.getKey().compareTo(e2.getKey());
                        }
                    }
                }
        );

        return sortedEntries;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("MP1 <User ID>");
        } else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item : topItems) {
                System.out.println(item);
            }
        }
    }

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];

        //TODO
        BufferedReader br = new BufferedReader(new FileReader(this.inputFileName));
        Integer[] indexes = this.getIndexes();
        List<Integer> indexList = Arrays.asList(indexes);
        List<String> stopWordsList = Arrays.asList(stopWordsArray);
        //System.out.println("Number of lines to read: " + indexList.size());
        Map<String, Integer> map = new HashMap<String, Integer>();
        ArrayList<String> lines = new ArrayList<String>();
        String pattern = "[" + Pattern.quote(this.delimiters) + "]";

        try {
            String line;
            int i = -1;

            do {
                line = br.readLine();
                i++;

                if (line == null) {
                    //System.out.println(">> File read complete...");
                    break;
                }
                lines.add(line);

            } while (true);

            //System.out.println("Lines read: " + i);
        } finally {
            br.close();
        }

        for (int index : indexes) {
            String[] lineTokens = lines.get(index).split(pattern);
            //System.out.println(Arrays.toString(lineTokens));
            for (String tokenWord : lineTokens) {
                String token = tokenWord.toLowerCase();
                if (stopWordsList.contains(token)) {
                    continue;
                }

                // Build HashMap for token sorting
                if (map.containsKey(token)) {
                    Integer count = map.get(token);
                    map.put(token, new Integer(count.intValue() + 1));
                } else {
                    map.put(token, new Integer(1));
                }
            }

        }

        List<Map.Entry<String, Integer>> wordList = entriesSortedByValues(map);

        List<String> keyArrayList = new ArrayList<String>();

        for (Map.Entry entry : wordList) {
            keyArrayList.add(entry.getKey().toString());
        }

        ret = keyArrayList.subList(0, 21).toArray(ret);

        return ret;
    }
}
