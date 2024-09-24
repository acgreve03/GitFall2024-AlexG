import com.itextpdf.io.image.ImageData;
import org.jfree.chart.JFreeChart;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import javax.imageio.ImageIO;

public class SlayTheSpire {

    private static ArrayList<String> invalidCards = new ArrayList<>();
    private static int cardNum;
    private static int voidNum;
    private static String cardName;

    public static void main(String[] args) throws IOException {
        HashMap<String, Double> deck = readCardFile("input.txt");
        Histogram h = new Histogram();
        generateReport(deck, h);


    }

    //read a plain text file containing <card name> : <cost>

    /**
     * read a plain text file containing <card name> : <cost>
     * @param fileName the name of the file to read
     * @return the HashMap containing the data from the inout file, where card names are mapped to energy costs
     * @throws IOException
     */
    public static HashMap<String, Double> readCardFile(String fileName) throws IOException{
        //create deck hashmap
        HashMap<String, Double> deck = new HashMap<String, Double>();

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
            
            //check for empty or whitespace-only card name
            if (cardName.isEmpty()) {
                voidNum++;
                invalidCards.add('\n' + line);
                continue; // Skip this card
            }
                //try to parse the cost and check for validity
            try {
                double cost = 1.0 * (Integer.parseInt(line.substring(line.indexOf(":") + 1).trim()));
                deck.put(cardName,cost);
            } 
            catch (NumberFormatException e) {
                voidNum++;
                invalidCards.add('\n' + line); //add to invalid cards if parsing fails
                continue;
                }
                cardNum++;
            }          
            reader.close();
            return deck;
        }

    /**
     * Generates a PDF report of the deck, showing the total cost of the deck and a histogram of the deck.
     * Uses JFreeChart to create a histogram, and iText to convert the histogram chart to a png and append it to the pdf report.
     * @param cardDeck the hashmap containing the data from the input file
     * @param histogram a histogram object used to call the Histogram class to create and display the histogram
     * @throws IOException
     */
    public static void generateReport(HashMap<String, Double> cardDeck, Histogram histogram) throws IOException {
        Random random = new Random();
        int deckID = 100000000 + random.nextInt(999999999);

        //check if the deck is invalid
        boolean isDeckInvalid = (voidNum > 10 || cardNum > 1000);

        //declaring what the file name is based on if the deck is valid or not.
        String filename = "SpireDeck_" + deckID;

        //append "(VOID)" if the deck is invalid
        if (isDeckInvalid) {
            filename += "(VOID)";
        }

        filename += (".pdf");
        PdfWriter writer = new PdfWriter(filename);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        if (voidNum <= 10 && cardNum <= 1000) {
            document.add(new com.itextpdf.layout.element.Paragraph("Deck ID: " + deckID));

            //calculate the total cost of all cards in the deck
            double sum = 0;
            for (double value : cardDeck.values()) {
                sum += value;
            }
            document.add(new com.itextpdf.layout.element.Paragraph("\nTotal cost of all cards: " + sum + " energy"));

            //if there are invalid cards, add them to the report
            if (!invalidCards.isEmpty()) {
                document.add(new com.itextpdf.layout.element.Paragraph("\n\nInvalid Cards (" + voidNum + "):"));
                for (String invalidCard : invalidCards) {
                    document.add(new com.itextpdf.layout.element.Paragraph(invalidCard));
                }
            }

            //generate the bar chart using JFreeChart
            JFreeChart chart = histogram.getBarChart("Card Cost Chart", cardDeck);

            //convert the chart to a BufferedImage
            BufferedImage bufferedImage = chart.createBufferedImage(600, 400);

            //convert BufferedImage to ImageData for iText
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] chartImageBytes = byteArrayOutputStream.toByteArray();
            ImageData imageData = ImageDataFactory.create(chartImageBytes);

            //add the chart image to the PDF
            Image chartImage = new Image(imageData);
            document.add(chartImage);

            System.out.println("BufferedImage: " + bufferedImage);
        } else {
            // If too many invalid cards, mark the deck as "VOID"
            document.add(new com.itextpdf.layout.element.Paragraph("VOID"));
        }

        document.close();
        writer.close();
    }
    }
