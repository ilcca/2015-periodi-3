
package Tranforms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * BWT on myös yksi datalle tehtävä muunnos, joka ei vaikuta tiedoston kokoon,
 * mutta parantaa muiden menetelmien tehokkuutta.
 * @author teemupitkanen1
 */
public class BurrowsWheeler{
    
    private static byte[] data;
    
    public static void transform(String inputFile, String outputFile) throws IOException{
        Path pathToOriginalData = Paths.get(inputFile);
        data = Files.readAllBytes(pathToOriginalData);
        int[] lines = new int[data.length];
        for(int i=0;i<lines.length;i++){
            lines[i]=i;
        }
    }
}
