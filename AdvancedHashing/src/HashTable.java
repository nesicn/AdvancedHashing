public interface HashTable {
    
    void put(long key, int value);
    Integer get(long key);
    Integer remove(long key);
    int size();
    long getLastProbeCount(); 
    
}
