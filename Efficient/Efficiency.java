package Efficient;

import performance.HashTable;
import performance.HashTableWithDoubleHashing;
import performance.Participant;
import performance.ParticipantsGeneration;

import javax.print.attribute.HashAttributeSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;

public class Efficiency {
    public static final String FINISH_COMMAND = "finishCommand";
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("gui.messages");

    private final BlockingQueue resultsLogger = new SynchronousQueue();
    private final Semaphore semaphore = new Semaphore(-1);

    private final String[] TYRIMU_VARDAI = {"addMapSo", "addMapKVSo","addMapSt", "addMapKVSt", "remMapSo", "remMapKVSo","remMapSt", "remMapKVSt"};
    private final int[] TIRIAMI_KIEKIAI = {1, 2, 3, 4};
    //private final int[] TIRIAMI_KIEKIAI = {10000, 20000, 40000, 80000};
    public static final ArrayList<Long> times = new ArrayList<>( 16 );


    // put

    private final HashTable<String, Participant> hashTablePart
            = new HashTable(10, 0.75f);
    private final HashTableWithDoubleHashing<String, Participant> hashTableDoublePart
            = new HashTableWithDoubleHashing(10, 0.75f);
    private final HashTable<String, String> hashTableStr
            = new HashTable( 10, 0.75f);
    private  final HashTableWithDoubleHashing<String, String> hashTableDoubleStr
            = new HashTableWithDoubleHashing(10, 0.75f);

    public Efficiency() {
        SisteminisTyrimas();
    }

    public void SisteminisTyrimas() {

        for (int k : TIRIAMI_KIEKIAI) {
            Participant[] partArray = ParticipantsGeneration.generate(k);
            String[] IdArray =  ParticipantsGeneration.generateIds(k);

            ArrayList<String> words = null;
            try {
                words = words(k);
            } catch (Exception e) {
                e.printStackTrace();
            }

            hashTablePart.clear();
            hashTableDoublePart.clear();
            hashTableStr.clear();
            hashTableDoubleStr.clear();

            try {
                ArrayList<String> wordsList = words( k );
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(int i = 0; i < k; i++){
                hashTablePart.put( IdArray[i], partArray[i] );
                hashTableDoublePart.put( IdArray[i], partArray[i] );
                hashTableStr.put( String.valueOf( i ), words.get( i ) );
                hashTableDoubleStr.put( String.valueOf( i ), words.get( i ) );
            }


            long t0=System.nanoTime();
            for (int i = 0; i < k; i++) {
                hashTablePart.contains(IdArray[i]);
            }
            long t1=System.nanoTime();

            for (int i = 0; i < k; i++) {
                hashTableDoublePart.contains(IdArray[i]);
            }
            long t2=System.nanoTime();
            for (int i = 0; i < k; i++) {
                hashTableStr.contains(IdArray[i]);
            }
            long t3=System.nanoTime();

            for (int i = 0; i < k; i++) {
                hashTableDoubleStr.contains(IdArray[i]);
            }
             long t4=System.nanoTime();

            times.add( t1-t0 );
            times.add( t2-t1 );
            times.add( t3-t2 );
            times.add( t4-t3 );

        }
    }

    private ArrayList<String> words(int k) throws Exception {
        ArrayList<String> words = new ArrayList<>(k);

        File file = new File("D:\\Lab3\\src\\Efficient\\zodynas.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null)
            words.add(st);
        return  words;
    }
}
