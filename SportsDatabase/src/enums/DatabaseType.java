package enums;

public enum DatabaseType {
    FOOTBALL {
        @Override
        public String toString() {
            return "FootballDatabase";
        }
    },
    BASKETBALL {
        @Override
        public String toString() {
            return "BasketballDatabase";
        }
    }
}
