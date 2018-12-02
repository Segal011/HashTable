package performance;

import gui.*;

public class PerformanceClass {

    private static Table table;

    public PerformanceClass() {  }

    public HashTable mapGeneration(int sizeOfGenSet, int initialCapacity, float loadFactor) {
        HashTable participantsMap  = new HashTable<>(initialCapacity, loadFactor);
        table = new Table();
        Participant[] participants = ParticipantsGeneration.generate( sizeOfGenSet  );
        for (Participant p : participants) {
            participantsMap.put( p.getPartNo(), p );
        }
        return participantsMap;
    }

    public HashTableWithDoubleHashing mapGenerationDH(int sizeOfGenSet, int initialCapacity, float loadFactor) {
        HashTableWithDoubleHashing participantsMap = new HashTableWithDoubleHashing<>(initialCapacity, loadFactor);
        table = new Table();
        Participant[] participants = ParticipantsGeneration.generate( sizeOfGenSet );
        int i = 0;
        for (Participant p : participants) {
            System.out.print( i++ + " " );
            participantsMap.put( p.getPartNo(), p );
        }
        return participantsMap;
    }

    public void mapAdd(HashTable participantsMap, Participant p)
    {
        participantsMap.put( p.getPartNo(), p );
    }

    public void mapAdd(HashTableWithDoubleHashing participantsMap, Participant p){
        participantsMap.put( p.getPartNo(), p );
    }

    public String isMapContains(HashTable participantsMap, String key){
        if(key.equals( "" )){ return "Please, write ID";}
        if(participantsMap.contains( key )){
            return "The map contains participant No " + key;
        }else {
            return "The map does not contain participant No " + key;
        }
    }

    public String isMapContains(HashTableWithDoubleHashing participantsMap, String key){
        if(key.equals( "" )){ return "Please, write ID";}
        if(participantsMap.contains( key )){
            return "The map contains participant No " + key;
        }else {
            return "The map does not contain participant No " + key;
        }
    }

    public String removeParticipant(HashTable participantsMap, String key){
        if(key.equals( "" )){ return "Please, write ID";}
        if(participantsMap.remove( key ) != null){
            return "The map contains participant No " + key;
        }else {
            return "The map does not contain participant No " + key;
        }
    }

    public String removeParticipant(HashTableWithDoubleHashing participantsMap, String key){
        if(key.equals( "" )){ return "Please, write ID";}
        if(participantsMap.remove( key ) != null){
            return "Participant No " + key + " is removed";
        }else {
            return "The map does not contain participant No " + key;
        }
    }

    public String getParticipantData(HashTable participantsMap, String key){
        if(key.equals( "" )){ return "Please, write ID";}
        if(participantsMap.get( key ) != null){
            return participantsMap.get( key).toString();
        }else {
            return "The map does not contain participant No " + key;
        }
    }

    public String getParticipantData(HashTableWithDoubleHashing participantsMap, String key){
        if(key.equals( "" )){ return "Please, write ID";}
        if(participantsMap.get( key ) != null){
            return participantsMap.get( key).toString();
        }else {
            return "The map does not contain participant No " + key;
        }
    }
}
