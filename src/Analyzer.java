import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/*
 * SD2x Homework #3
 * Implement the methods below according to the specification in the assignment description.
 * Please be sure not to change the method signatures!
 */
public class Analyzer {

    /*
     * Implement this method in Part 1
     */
    public static List<Sentence> readFile(String filename) {
        List<Sentence> returnList = new LinkedList<>();
        if (filename == null) {
            return returnList;
        }
        File file = new File(filename);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                int score;
                String text;
                String line = scanner.nextLine();
                String[] splitLine = line.split(" ", 2);
                if (splitLine.length == 2) {
                    try {
                        score = Integer.parseInt(splitLine[0]);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    if (score > -3 && score < 3) {
                        text = splitLine[1];
                        Sentence temp = new Sentence(score, text);
                        returnList.add(temp);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return returnList;
        }
        return returnList;
    }

    /*
     * Implement this method in Part 2
     */
    public static Set<Word> allWords(List<Sentence> sentences) {
        if (sentences == null) {
            return Collections.emptySet();
        }
        Set<Word> uniqueWords = new HashSet<>();
        for (Sentence sentence : sentences) {
            if (sentence == null) {
                continue;
            }
            StringTokenizer wordsInSentence = new StringTokenizer(sentence.getText());
            while (wordsInSentence.hasMoreTokens()) {
                String checkWord = wordsInSentence.nextToken().toLowerCase();
                if (!checkWord.matches("[a-z]+")) {
                    continue;
                }
                Word tempWord = new Word(checkWord);
                if (!uniqueWords.contains(tempWord)) {
                    uniqueWords.add(tempWord);
                    tempWord.count++;
                    tempWord.increaseTotal(sentence.getScore());
                } else {
                    for (Word word : uniqueWords) {
                        if (word.equals(tempWord)) {
                            word.increaseTotal(sentence.getScore());
                            break;
                        }
                    }
                }
            }
        }
        return uniqueWords;
    }

    /*
     * Implement this method in Part 3
     */
    public static Map<String, Double> calculateScores(Set<Word> words) {
        Map<String, Double> scoredWords = new HashMap<>();
        if (words == null || words.isEmpty()) {
            return scoredWords;
        }
        for (Word word : words) {
            if (word != null) {
                scoredWords.put(word.getText(), word.calculateScore());
            }
        }
        return scoredWords;
    }

    /*
     * Implement this method in Part 4
     */
    public static double calculateSentenceScore(Map<String, Double> wordScores, String sentence) {
        Double sentenceScore = 0.0;
        Double totalScore = 0.0;
        Double numberOfWordsInSentence = 0.0;
        if ((wordScores == null || wordScores.isEmpty()) || (sentence == null || sentence.isEmpty())) {
            return sentenceScore;
        }
        StringTokenizer st = new StringTokenizer(sentence);
        while (st.hasMoreTokens()) {
            String word = st.nextToken().toLowerCase();
            if (!word.matches("[a-z]+")) {
                continue;
            }
            Double scoreFromMap;
            scoreFromMap = wordScores.getOrDefault(word, 0.0);
            totalScore += scoreFromMap;
            numberOfWordsInSentence++;
        }
        sentenceScore = totalScore / numberOfWordsInSentence;
        if (Double.isNaN(sentenceScore)) {
            return 0;
        }
        return sentenceScore;
    }

    /**
     * This method is here to help you run your program. Y
     * You may modify it as needed.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify the name of the input file");
            System.exit(0);
        }
        String filename = args[0];
        System.out.print("Please enter a sentence: ");
        Scanner in = new Scanner(System.in);
        String sentence = in.nextLine();
        in.close();
        List<Sentence> sentences = Analyzer.readFile(filename);
        Set<Word> words = Analyzer.allWords(sentences);
        Map<String, Double> wordScores = Analyzer.calculateScores(words);
        double score = Analyzer.calculateSentenceScore(wordScores, sentence);
        System.out.println("The sentiment score is " + score);
    }
}
