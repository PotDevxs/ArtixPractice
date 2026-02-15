package dev.artixdev.practice.utils;

public class ELOCalculator {
    
    private static final int MIN_ELO = 0;
    private static final int MAX_ELO = 2200;
    private static final int DEFAULT_ELO = 1000;
    private static final int K_FACTOR = 25;
    
    private static final ELOData[] ELO_RANGES = {
        new ELOData(0, 1000, 25.0),
        new ELOData(1001, 1400, 20.0),
        new ELOData(1401, 1800, 15.0),
        new ELOData(1801, 2200, 10.0)
    };

    public static int calculateELO(int playerELO, int opponentELO, boolean won) {
        if (!won) {
            return calculateELOChange(playerELO, opponentELO, 0);
        } else {
            return calculateELOChange(playerELO, opponentELO, 1);
        }
    }

    private static double getExpectedScore(int playerELO, int opponentELO) {
        return 1.0 / (1.0 + Math.pow(10.0, (double)(opponentELO - playerELO) / 400.0));
    }

    private static double getKFactor(int elo) {
        for (ELOData data : ELO_RANGES) {
            if (elo >= data.getMinELO() && elo <= data.getMaxELO()) {
                return data.getKFactor();
            }
        }
        return 25.0; // Default K factor
    }

    private static int calculateELOChange(int playerELO, int opponentELO, int result) {
        double expectedScore = getExpectedScore(playerELO, opponentELO);
        double kFactor = getKFactor(playerELO);
        
        double eloChange = kFactor * (result - expectedScore);
        int newELO = playerELO + (int)Math.round(eloChange);
        
        // Ensure ELO stays within bounds
        if (newELO < MIN_ELO) {
            newELO = MIN_ELO;
        } else if (newELO > MAX_ELO) {
            newELO = MAX_ELO;
        }
        
        // If ELO didn't change and player won, increase by 1
        if (result == 1 && newELO == playerELO) {
            newELO++;
        }
        
        return newELO;
    }

    private static class ELOData {
        private final int minELO;
        private final int maxELO;
        private final double kFactor;

        public ELOData(int minELO, int maxELO, double kFactor) {
            this.minELO = minELO;
            this.maxELO = maxELO;
            this.kFactor = kFactor;
        }

        public int getMinELO() {
            return minELO;
        }

        public int getMaxELO() {
            return maxELO;
        }

        public double getKFactor() {
            return kFactor;
        }
    }
}
