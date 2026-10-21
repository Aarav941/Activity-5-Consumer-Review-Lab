import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

public class Airlines {
    private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
    static{
        try {
        Scanner input = new Scanner(new File("cleanSentiment.csv"));
        while(input.hasNextLine()){
            String[] temp = input.nextLine().split(",");
            sentiment.put(temp[0],Double.parseDouble(temp[1]));
            // System.out.println("added "+ temp[0]+", "+temp[1]);
        } 
        input.close();
        }
        catch(Exception e){
        System.out.println("Error reading or parsing cleanSentiment.csv");
        } 
    }

    public static double sentimentVal(String word){
        try{
            return sentiment.get(word.toLowerCase());
        }
        catch(Exception e){
            return 0;
        }
     }

     public static String removePunctuation(String word){
        while(word.length() > 0 && !Character.isAlphabetic(word.charAt(0)))
        {
        word = word.substring(1);
        }
        while(word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length()-1)))
        {
        word = word.substring(0, word.length()-1);
        }
    
         return word;
    }

    public static HashMap<String, ArrayList<String>> getReviews() throws Exception{
        Scanner allReviews = new Scanner(new File("Airline Review.csv"));
        HashMap<String, ArrayList<String>> reviews = new HashMap<String, ArrayList<String>>();

        while (allReviews.hasNextLine()){
            String line = allReviews.nextLine();
            String[] parts = line.split(",", 7);
            String airline = parts[1];
            String review = parts[6];

            if (reviews.containsKey(airline) == false){
                reviews.put(airline, new ArrayList<>());
            }

            reviews.get(airline).add(review);
        }

        allReviews.close();
        return reviews;
    }

    public static double calculateSentiment(ArrayList<String> airline){
        double totalSentiment = 0.0;
        for (String review : airline){
            String[] words = review.split("\\s+");
            for (String word : words){  
                word = removePunctuation(word).toLowerCase();
                totalSentiment += sentimentVal(word);
            }
        }
        return totalSentiment;
    }

     public static  HashMap<String, Double> getSentiment(HashMap<String, ArrayList<String>> reviews) throws Exception{
        ArrayList<String> southwest = reviews.get("Southwest Airlines");
        ArrayList<String> american = reviews.get("American Airlines");
        ArrayList<String> eagle = reviews.get("American Eagle");
        ArrayList<String> united = reviews.get("United Airlines");
        ArrayList<String> spirit = reviews.get("Spirit Airlines");
        ArrayList<String> delta = reviews.get("Delta Air Lines");
        HashMap<String, Double> averages = new HashMap<>();

        double southwestSentiment = calculateSentiment(southwest);
        double americanSentiment = calculateSentiment(american);
        double eagleSentiment = calculateSentiment(eagle);
        double unitedSentiment = calculateSentiment(united);
        double spiritSentiment = calculateSentiment(spirit);
        double deltaSentiment = calculateSentiment(delta);

        averages.put("Southwest Airlines", southwestSentiment/southwest.size());
        averages.put("American Airlines", americanSentiment/american.size());
        averages.put("American Eagle", eagleSentiment/eagle.size());
        averages.put("United Airlines", unitedSentiment/united.size());
        averages.put("Spirit Airlines", spiritSentiment/spirit.size());
        averages.put("Delta Air Lines", deltaSentiment/delta.size());

        return averages;
    }

    public static void main(String[] args) throws Exception{
        HashMap<String, ArrayList<String>> reviews = getReviews();
        HashMap<String, Double> results = getSentiment(reviews);

        String bestAirline = "";
        double highestScore = Double.NEGATIVE_INFINITY;

        for (String airline:results.keySet()){
            double score = results.get(airline);
            System.out.printf("%s: %.3f%n", airline, score);

            if (score > highestScore){
                highestScore = score;
                bestAirline = airline;
            }
        }
        System.out.println("The airline people like flying the most is " + bestAirline);
    }

}