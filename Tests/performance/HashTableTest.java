package performance;




import org.junit.jupiter.api.Test;

import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.assertEquals;


class HashTableTest {


    HashTable hashTable;

    public HashTable hashTable() {
        HashTable participantsMap = new HashTable();
        participantsMap = new HashTable<>(10, 0.75f);
        Participant[] participants = ParticipantsGeneration.generate( 10 );
        for (Participant p : participants) {
            participantsMap.put( p.getPartNo(), p );
        }
        return participantsMap;
    }

    @Test
    public void isEmptyWhenTrue() {
        HashTable table = new HashTable();
        assertEquals(true, table.isEmpty());
    }

    @Test
    public void isEmptyWhenFalse() {
        assertEquals(false, hashTable().isEmpty());
    }

    @Test
    public void putAndContainsWhenTrue() {
        Participant part = new Participant.Builder().buildRandom();
        HashTable table = hashTable();
        table.put( part.getPartNo(), part );

        assertEquals( true,  table.contains( part.getPartNo() ));
    }

    @Test
    public void putAndContainsWhenFalse() {
        Participant part = new Participant.Builder().buildRandom();

        assertEquals( false,  hashTable().contains( part.getPartNo() ));
    }

    @Test
    void clear() {
        HashTable table = hashTable();
        table.clear();
        assertEquals(0, table.size()); //10
    }

    @Test
    public void put0Participants() {
        HashTable table = new HashTable();
        Participant[] participants = ParticipantsGeneration.generate( 0 );
        for (Participant p : participants) {
            table.put( p.getPartNo(), p );
        }
        assertEquals(0, table.size());
    }

    @Test
    public void put10Participants() {
        HashTable table = new HashTable(10);
        Participant[] participants = ParticipantsGeneration.generate( 10 );
        for (Participant p : participants) {
            table.put( p.getPartNo(), p );
        }
        assertEquals(10, table.size());
    }

    @Test
    public void put1000Participants() {
        HashTable table = new HashTable(1000);
        Participant[] participants = ParticipantsGeneration.generate( 1000 );
        for (Participant p : participants) {
            table.put( p.getPartNo(), p );
        }
        assertEquals(1000, table.size());
    }

    @Test
    public void getWhenTrue() {
        Participant part = new Participant.Builder().buildRandom();
        HashTable table = hashTable();
        table.put( part.getPartNo(), part );
        assertEquals(part, table.get( part.getPartNo() )); //null
    }

    @Test
    public void getWhenFalse() {
        Participant part = new Participant.Builder().buildRandom();
        assertEquals(null, hashTable().get( part.getPartNo() ));
    }

    @Test
    public void removeWhenTrue() {
        Participant part = new Participant.Builder().buildRandom();
        HashTable table = hashTable();
        table.put( part.getPartNo(), part );
        assertEquals(part, table.remove( part.getPartNo() )); //null
    }

    @Test
    public void removeWhenFalse() {
        Participant part = new Participant.Builder().buildRandom();
        assertEquals(null, hashTable().remove( part.getPartNo() ));
    }

    @Test
    public void containsValueWhenTrue() {
        Participant part = new Participant.Builder().buildRandom();
        HashTable table = hashTable();
        table.put( part.getPartNo(), part );
        assertEquals(true, table.containsValue( part)); //false
    }

    @Test
    public void containsValueWhenFalse() {
        Participant part = new Participant.Builder().buildRandom();
        assertEquals(false, hashTable().containsValue( part));
    }

    @Test
    public void replaceWhenTrue() {
        Participant part1 = new Participant.Builder().buildRandom();
        Participant part2 = new Participant.Builder().buildRandom();
        HashTable table = hashTable();
        table.put( part1.getPartNo(), part1 );
        assertEquals(true, table.replace( part1.getPartNo(), part1, part2));
    }

    @Test
    public void replaceWhenFalse() {
        Participant part1 = new Participant.Builder().buildRandom();
        Participant part2 = new Participant.Builder().buildRandom();
        assertEquals(false, hashTable().replace( part1.getPartNo(), part1, part2));
    }

}