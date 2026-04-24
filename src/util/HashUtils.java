package util;

public class HashUtils {
    public static boolean isValidLinearProbingTable(Integer[] table) {
        for (Integer key : table) {
            if (key == null) {
                continue;
            }

            int index = Math.floorMod(key, table.length);
            int probes = 0;
            boolean found = false;

            while (probes < table.length && table[index] != null) {
                if (table[index].equals(key)) {
                    found = true;
                    break;
                }
                index = (index + 1) % table.length;
                probes++;
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

    public static int countCollisionsForKey(Integer[] table, int key) {
        int index = Math.floorMod(key, table.length);
        int collisions = 0;

        while (collisions < table.length && table[index] != null && !table[index].equals(key)) {
            collisions++;
            index = (index + 1) % table.length;
        }

        return collisions;
    }
}
