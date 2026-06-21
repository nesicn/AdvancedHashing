public class Entry {
    long key;
    int value;
    int psl; // Probe sequence length

    Entry(long key, int value, int pslength) {
            this.key = key;
            this.value = value;
            this.psl = pslength;
        }
}
