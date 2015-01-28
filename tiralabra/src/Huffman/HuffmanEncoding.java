package Huffman;

import IO.BitWriter;
import Utils.StringBitConversions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * HuffmanEncoding on täyden palvelun luokka Huffman-koodaukseen. Luokan ydin,
 * staattinen encode-metodi saa parametreinaan pakattavan tiedoston ja halutun
 * pakatun tiedoston nimen, ja suorittaa pakkauksen.
 *
 * @author teemupitkanen1
 */
public class HuffmanEncoding {

    /**
     * Taulukkoon kerätään tieto kunkin merkin esiintymiskerroista Huffman-
     * koodausta varten
     */
    private static int[] freqs;
    /**
     * codes tallettaa kunkin merkin bittikoodauksen merkkijonona, esim "01010".
     * Bittikoodaukset ovat taulukossa ASCII-merkkien numeroinnin mukaisesti.
     */
    private static String[] codes;

    /**
     * Bitwriter-olio hoitaa bittitason tiedostoon kirjoittamisen.
     */
    private static BitWriter bwriter;
    /**
     * Pakattavan tiedoston lukemiseen käytettävä ByteReader
     */
    private static FileInputStream breader;

    /**
     * Luokan ydinmetodi, jota kutsumalla tiedoston pakkaus tapahtuu.
     *
     * @param inFile Pakattava tiedosto
     * @param outFile Pakatun tiedoston nimi
     * @throws java.io.IOException
     */
    public static void encode(String inFile, String outFile) throws IOException {
        freqs = new int[257];
        freqs[freqs.length - 1] = 1;
        try {
            breader = new FileInputStream(new File(inFile));
        } catch (Exception e) {
            System.out.println("No such file");
            System.exit(0);
        }
        countSymbols();
        breader.close();
        codes = HuffmanTree.huffmanCodewords(freqs);
        bwriter = new BitWriter(new File(outFile));
        writeEncodingToTheStartOfFile();
        writeToFileAsBits(inFile, outFile);

    }

    /**
     * Kun tavuittaiset koodisanat on selvitetty, tämä metodi huolehtii koko
     * tekstin koodauksesta ja tallentamisesta tiedostoon.
     *
     * @param outFile Kohdetiedosto
     * @throws IOException
     */
    private static void writeToFileAsBits(String inFile, String outFile) throws IOException {
        breader = new FileInputStream(new File(inFile));
        while (breader.available() > 0) {
            bwriter.writeBits(codes[breader.read()]);
        }
        bwriter.writeTheLastBits(codes[codes.length - 1]+"00000000");
        breader.close();
    }

    /**
     * Laskee kunkin erilaisen tavun esiintymismäärät pakattavassa tiedostossa.
     */
    private static void countSymbols() throws IOException {
        while (breader.available() > 0) {
            freqs[breader.read()]++;
        }
    }

    /**
     * Metodi kijoittaa tavuittaiset koodaukset pakatun tiedoston alkuun.
     * Pakatun tiedoston rakenne on seuraava: 1. Tavun "00000000" koodauksen
     * pituus 8 bitissä 2. Tavun "00000000" koodaus 3. Tavun "00000001"
     * koodauksen pituus ... ... ... 512. Tavun "11111111" koodaus 513.
     * EOF-merkin koodauksen pituus 514. EOF-merkin koodaus 515. Varsinainen
     * pakattu data. 516. EOF-merkki ja täyttö tasatavuihin. 517. Tyhjä tavu
     */
    private static void writeEncodingToTheStartOfFile() throws IOException {
        for (int i = 0; i < codes.length - 1; i++) {
            if (codes[i] != null) {
                bwriter.writeBits(StringBitConversions.positiveIntegerAsOneByteBitstring(codes[i].length()));
                bwriter.writeBits(codes[i]);
            } else {
                bwriter.writeBits("00000000");
            }
        }
    }
}
