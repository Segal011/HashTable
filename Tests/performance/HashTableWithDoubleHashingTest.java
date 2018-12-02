package performance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableWithDoubleHashingTest {

    public static HashTableWithDoubleHashing hashTable() {
        HashTableWithDoubleHashing participantsMap = new HashTableWithDoubleHashing(10);
        participantsMap = new HashTableWithDoubleHashing<>(1000, 0.75f);
        Participant[] participants = ParticipantsGeneration.generate( 10 );
        for (Participant p : participants) {
            participantsMap.put( p.getPartNo(), p );
        }
        return participantsMap;
    }

    @Test
    public void isEmptyWhenTrue() {
        HashTableWithDoubleHashing table = new HashTableWithDoubleHashing();
        assertEquals(true, table.isEmpty());
    }

    @Test
    public void isEmptyWhenFalse() {
        HashTableWithDoubleHashing table = new HashTableWithDoubleHashing(  );
        table = hashTable();
        assertEquals(false, table.isEmpty());
    }

    @Test
    void size() {
        HashTableWithDoubleHashing table = new HashTableWithDoubleHashing(  );
        table = hashTable();
        assertEquals(10, table.size());
    }

    @Test
    void clear() {
        HashTableWithDoubleHashing table = hashTable();
        table.clear();
        assertEquals(0, table.size());
    }

    @Test
    public void putAndContainsWhenTrue() {
        Participant part = new Participant.Builder().buildRandom();
        HashTableWithDoubleHashing table = hashTable();
        table.put( part.getPartNo(), part );

        assertEquals( true,  table.contains( part.getPartNo() ));
    }

    @Test
    public void putAndContainsWhenFalse() {
        Participant part = new Participant.Builder().buildRandom();
        HashTableWithDoubleHashing table = hashTable();
        assertEquals( false,  table.contains( part.getPartNo() ));
    }

    @Test
    public void put0Participants() {
        HashTableWithDoubleHashing table = new HashTableWithDoubleHashing(10);
        Participant[] participants = ParticipantsGeneration.generate( 0 );
        for (Participant p : participants) {
            table.put( p.getPartNo(), p );
        }
        assertEquals(0, table.size());
    }

    @Test
    public void put10Participants() {
        HashTableWithDoubleHashing table = new HashTableWithDoubleHashing(1000);
        Participant[] participants = ParticipantsGeneration.generate( 10 );
        for (Participant p : participants) {
            table.put( p.getPartNo(), p );
        }
        assertEquals(10, table.size());
    }

    @Test
    public void put1000Participants() {
        HashTableWithDoubleHashing table = new HashTableWithDoubleHashing(10000000);
        Participant[] participants = ParticipantsGeneration.generate( 1000 );
        for (Participant p : participants) {
            table.put( p.getPartNo(), p );
        }
        assertEquals(1000, table.size());
    }


    @Test
    public void getWhenTrue() {
        Participant part = new Participant.Builder().buildRandom();
        HashTableWithDoubleHashing table = hashTable();
        table.put( part.getPartNo(), part );
        assertEquals(part, table.get( part.getPartNo() ));
    }

    @Test
    public void getWhenFalse() {
        Participant part = new Participant.Builder().buildRandom();
        HashTableWithDoubleHashing table = hashTable();
        assertEquals(null, table.get( part.getPartNo() ));
    }

    @Test
    public void removeWhenTrue() {
        Participant part = new Participant.Builder().buildRandom();
        HashTableWithDoubleHashing table = hashTable();
        table.put( part.getPartNo(), part );
        assertEquals(part, table.remove( part.getPartNo() ));
    }

    @Test
    public void removeWhenFalse() {
        Participant part = new Participant.Builder().buildRandom();
        HashTableWithDoubleHashing table = hashTable();
        assertEquals(null, table.remove( part.getPartNo() ));
    }
//
//    @Test
//    public void containsValueWhenTrue() {
//        Participant part = new Participant.Builder().buildRandom();
//        HashTableWithDoubleHashing table = hashTable();
//        table.put( part.getPartNo(), part );
//        assertEquals(true, table.containsValue( part));
//    }
//
//    @Test
//    public void containsValueWhenFalse() {
//        Participant part = new Participant.Builder().buildRandom();
//        HashTableWithDoubleHashing table = hashTable();
//        assertEquals(false, table.containsValue( part));
//    }



}