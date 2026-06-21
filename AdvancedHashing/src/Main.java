import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Advanced Hash Table Evaluation...");
        
        // configuration parameters
        int tableSize = 100_000;
        double[] loadFactors = {0.5, 0.8, 0.9}; // Testing loads
        int trials = 30; // N=30 trials for statistical significance
        
        try (FileWriter csv = new FileWriter("experiment_results.csv")) {
            csv.write("Algorithm;LoadFactor;Trial;OpType;AvgTimeNs;AvgProbes\n");

            for (double lf : loadFactors) {
                System.out.println("Evaluating Load Factor: " + lf);
                runExperiment(csv, "LinearProbing", lf, tableSize, trials);
                runExperiment(csv, "RobinHood", lf, tableSize, trials);
            }
            System.out.println("Results saved to experiment_results.csv");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runExperiment(FileWriter writer, String algoType, double lf, int cap, int trials) throws IOException {
        int numElements = (int) (cap * lf);
        Random rand = new Random(42); 

        for (int t = 1; t <= trials; t++) {
            HashTable ht = algoType.equals("RobinHood") ? new RobinHoodHT(cap) : new LinearProbingHT(cap);
            
            // workload 1: random insertion (build phase)
            long start = System.nanoTime();
            long totalProbes = 0;
            for (int i = 0; i < numElements; i++) {
                ht.put(rand.nextLong(), i);
                totalProbes += ht.getLastProbeCount();
            }
            long end = System.nanoTime();
            // calculate averages
            writer.write(String.format("%s;%.2f;%d;Insert;%d;%.2f\n", 
                algoType, lf, t, (end - start) / numElements, (double) totalProbes / numElements));

            // workload 2: random search (normal use case)
            start = System.nanoTime();
            totalProbes = 0;
            for (int i = 0; i < 10000; i++) {
                ht.get(rand.nextLong());
                totalProbes += ht.getLastProbeCount();
            }
            end = System.nanoTime();
            writer.write(String.format("%s;%.2f;%d;Search;%d;%.2f\n", 
                algoType, lf, t, (end - start) / 10000, (double) totalProbes / 10000));

            // workload 3: clusteres search (pathological case)
            // we search for keys that map to the same vicinity to test collision handling.
            start = System.nanoTime();
            totalProbes = 0;
            long baseKey = rand.nextLong(); 
            for (int i = 0; i < 1000; i++) {
                 // searching in a tight cluster forces linear probing to scan long chains
                ht.get(baseKey + i); 
                totalProbes += ht.getLastProbeCount();
            }
            end = System.nanoTime();
            writer.write(String.format("%s;%.2f;%d;ClusteredSearch;%d;%.2f\n", 
                algoType, lf, t, (end - start) / 1000, (double) totalProbes / 1000));
        }
    }
}
