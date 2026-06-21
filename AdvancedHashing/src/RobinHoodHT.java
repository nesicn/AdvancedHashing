public class RobinHoodHT implements HashTable {
        private Entry[] table;
        private int size = 0;
        private int capacity;
        private long lastProbeCount = 0;

        public RobinHoodHT(int capacity) {
            this.capacity = capacity;
            this.table = new Entry[capacity];
        }

        private int hash(long key) {
            return (int) (Math.abs(key) % capacity);
        }

        @Override
        public void put(long key, int value) {
            int idx = hash(key);
            Entry toInsert = new Entry(key, value, 0); // pslength starts at 0
            int currentPsl = 0;

            while (true) {
                if (table[idx] == null) {
                    // Found an empty spot
                    toInsert.psl = currentPsl;
                    table[idx] = toInsert;
                    size++;
                    lastProbeCount = currentPsl;
                    return;
                }

                if (table[idx].key == key) {
                    table[idx].value = value;
                    return;
                }

                // robin hood logic : "rich" vs "poor"
                // if the item currently in the slot has a smaller pslength (then it is richer) than the item we are holding, we swap them.
                if (currentPsl > table[idx].psl) {
                    Entry temp = table[idx];
                    toInsert.psl = currentPsl;
                    table[idx] = toInsert; // we take the spot
                    
                    // now we are holding the displaced item, looking for a new place for it
                    toInsert = temp;
                    currentPsl = toInsert.psl;
                }

                idx = (idx + 1) % capacity;
                currentPsl++;
            }
        }

        @Override
        public Integer get(long key) {
            int idx = hash(key);
            int dist = 0;
            
            while (true) {
                Entry e = table[idx];
                
                // early exit:
                // If we hit null, it's not here.
                // or, if the distance we traveled is greater than the pslength of the item here,
                // we know our key doesn't exist (because it would have swapped into this spot).
                if (e == null || dist > e.psl) {
                    lastProbeCount = dist;
                    return null;
                }
                
                if (e.key == key) {
                    lastProbeCount = dist;
                    return e.value;
                }
                
                idx = (idx + 1) % capacity;
                dist++;
            }
        }

        // backward shift deletion (Bonus
        // Instead of using tombstones, we shift elements back to fill the gap.
        // This keeps the table dense and fixes the "Tombstone pollution" problem.
        @Override
        public Integer remove(long key) {
            int idx = hash(key);
            int dist = 0;
            
            while (true) {
                Entry e = table[idx];
                if (e == null || dist > e.psl) return null; // Not found
                
                if (e.key == key) {
                    int val = e.value;
                    
                    // start shifting elements back
                    int curr = idx;
                    int next = (idx + 1) % capacity;
                    
                    // shile the next element is not null and is not at its home (pslength > 0)
                    while (table[next] != null && table[next].psl > 0) {
                        table[curr] = table[next]; // Move it back one slot
                        table[curr].psl--;         // It got closer to home, so decrease pslength
                        curr = next;
                        next = (next + 1) % capacity;
                    }
                    
                    table[curr] = null; // clear the final spot
                    size--;
                    return val;
                }
                
                idx = (idx + 1) % capacity;
                dist++;
            }
        }

        public int size() { return size; }
        public long getLastProbeCount() { return lastProbeCount; }
    }