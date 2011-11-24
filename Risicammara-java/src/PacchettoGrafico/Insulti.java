package PacchettoGrafico;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Classe che carica un file di testo che ha come riga un insulto. Ogni riga
 * viene scelta a random.
 * @author stengun
 */
class Insulti {
    private ArrayList<String> sentences;
    private String temp;
    private int numLines;
    
    public Insulti(InputStream infile) throws IOException{
        numLines = 0;
        Scanner input = new Scanner(infile);
        sentences = new ArrayList<String>();
        while (input.hasNextLine())
        {
            temp = input.nextLine();
            sentences.add(temp);
            numLines++;
        }
        infile.close();
    }
    
    public String getinsulto(){
        Random r = new Random();   
        return sentences.get(r.nextInt(sentences.size()));
    }
    
    
    


}
