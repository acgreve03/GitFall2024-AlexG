import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SlayTheSpire {

    private static ArrayList<String> invalidCards = new ArrayList<>();
    private static int cardNum;
    private static int voidNum;
    private static String cardName;

    public static void main(String[] args) throws IOException {
        generateReport(readCardFile("input.txt"));
    }

    //read a plain text file containing <card name> : <cost>
    public static HashMap<String, Integer> readCardFile(String fileName) throws IOException{
        //create deck hashmap
        HashMap<String, Integer> deck = new HashMap<String, Integer>();

        //initialize the reader
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        
        //line variable
        String line;

        //check for bounds, cannot exceed 1000 cards
         cardNum = 0;
         voidNum = 0;

        //iterate thru file, for each line
        while((line = reader.readLine()) != null) {

            //get cardname from line in file
            try{cardName = line.substring(0, line.indexOf(':')).trim();}
            catch(StringIndexOutOfBoundsException e){
                voidNum++;
                invalidCards.add('\n' + line); // Add to invalid cards if parsing fails
                continue;
            }
            
            // Check for empty or whitespace-only card name
            if (cardName.isEmpty()) {
                voidNum++;
                invalidCards.add('\n' + line);
                continue; // Skip this card
            }
                // Try to parse the cost and check for validity
            try {
                int cost = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                deck.put(cardName, cost);
            } 
            catch (NumberFormatException e) {
                voidNum++;
                invalidCards.add('\n' + line); // Add to invalid cards if parsing fails
                continue;
                }
                cardNum++;
            }          
            reader.close();
            return deck;
        }

    public static void generateReport(HashMap<String, Integer> cardDeck) throws IOException{
        Random random = new Random();
        int deckID = 100000000 + random.nextInt(999999999);
        BufferedWriter writer;

        if(voidNum <= 10 && cardNum  <= 1000){
            writer = new BufferedWriter(new FileWriter("SpireDeck_" + deckID + ".pdf"));
            writer.write("Deck ID: " + deckID);

                    //get total cost of all of the cards in the deck
        int sum = 0;
            for(int value: cardDeck.values()){
                sum += value;
            }

        writer.write("\nTotal cost of all costs: " + sum + " energy");

        if(invalidCards != null){
            writer.write("\n\nInvalid Cards(" + voidNum + "):\n");

            // Add invalid cards to the report
            for (String invalidCard : invalidCards) { 
                writer.write(invalidCard);
            }
        }
        }

        else{
            writer = new BufferedWriter(new FileWriter("SpireDeck_" + deckID + "(VOID).pdf"));
            writer.write("VOID");
        }
        writer.close();
        }
    }