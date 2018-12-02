package performance;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

/**
 *
 * @author Indrė
 */
public class ParticipantsGeneration {

    private static final String ID_CODE = "ES";

    private static int serNr = 10000;

    public static Participant[] generate(int size) {
        Participant[] participants = IntStream.range( 0, size )
                .mapToObj( i -> new Participant.Builder().buildRandom() )
                .toArray( Participant[] ::new );
        Collections.shuffle( Arrays.asList( participants ) );
        return participants;
    }

    public static String[] generateIds(int size) {
        String[] keys = IntStream.range( 0, size )
                .mapToObj( i -> ID_CODE + (serNr++) )
                .toArray( String[] ::new );
        Collections.shuffle( Arrays.asList( keys ) );
        return keys;
    }
}

