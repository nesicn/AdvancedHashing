public class LinearProbingHT implements HashTable {
     private Entry[] table;
        private int size = 0;
        private int capacity;
        private long lastProbeCount = 0;
        
        // We use a "Tombstone" to mark deleted spots so searches don't stop early
        private final Entry TOMBSTONE = new Entry(-1, -1, -1);

        public LinearProbingHT(int capacity) {
            this.capacity = capacity;
            this.table = new Entry[capacity];
        }

        private int hash(long key) {
            return (int) (Math.abs(key) % capacity);
        }

        @Override
        public void put(long key, int value) {
            int idx = hash(key);
            int probes = 0;
            
            // Standard Linear Probing: keep going until we find null or a tombstone
            while (table[idx] != null && table[idx] != TOMBSTONE) {
                if (table[idx].key == key) {
                    table[idx].value = value; // Update existing
                    return;
                }
                idx = (idx + 1) % capacity;
                probes++;
            }
            table[idx] = new Entry(key, value, 0);
            size++;
            lastProbeCount = probes;
        }

        @Override
        public Integer get(long key) {
            int idx = hash(key);
            int probes = 0;
            
            while (table[idx] != null) {
                // Skip tombstones, but don't stop
                if (table[idx] != TOMBSTONE && table[idx].key == key) {
                    lastProbeCount = probes;
                    return table[idx].value;
                }
                idx = (idx + 1) % capacity;
                probes++;
                // break to prevent infinite loops if table is full
                if (probes >= capacity) break;
            }
            lastProbeCount = probes;
            return null;
        }

        @Override
        public Integer remove(long key) {
            int idx = hash(key);
            while (table[idx] != null) {
                if (table[idx] != TOMBSTONE && table[idx].key == key) {
                    int val = table[idx].value;
                    table[idx] = TOMBSTONE; // Mark as deleted (Lazy Deletion)
                    size--;
                    return val;
                }
                idx = (idx + 1) % capacity;
            }
            return null;
        }

        public int size() { return size; }
        public long getLastProbeCount() { return lastProbeCount; }
    }