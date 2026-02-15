package dev.artixdev.practice.models;

public class DatabaseConfig {
    
    private boolean useMongoDB;
    private String databaseName;
    private boolean useMySQL;
    private boolean useSQLite;

    public DatabaseConfig() {
        this.useMongoDB = false;
        this.databaseName = "practice";
        this.useMySQL = false;
        this.useSQLite = true;
    }

    public boolean isUseMongoDB() {
        return useMongoDB;
    }

    public void setUseMongoDB(boolean useMongoDB) {
        this.useMongoDB = useMongoDB;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public boolean isUseMySQL() {
        return useMySQL;
    }

    public void setUseMySQL(boolean useMySQL) {
        this.useMySQL = useMySQL;
    }

    public boolean isUseSQLite() {
        return useSQLite;
    }

    public void setUseSQLite(boolean useSQLite) {
        this.useSQLite = useSQLite;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DatabaseConfig{");
        sb.append("useMongoDB=").append(useMongoDB);
        sb.append(", useMySQL=").append(useMySQL);
        sb.append(", useSQLite=").append(useSQLite);
        sb.append(", databaseName='").append(databaseName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        DatabaseConfig that = (DatabaseConfig) obj;
        
        if (useMongoDB != that.useMongoDB) return false;
        if (useMySQL != that.useMySQL) return false;
        if (useSQLite != that.useSQLite) return false;
        return databaseName != null ? databaseName.equals(that.databaseName) : that.databaseName == null;
    }

    @Override
    public int hashCode() {
        int result = (useMongoDB ? 1 : 0);
        result = 31 * result + (databaseName != null ? databaseName.hashCode() : 0);
        result = 31 * result + (useMySQL ? 1 : 0);
        result = 31 * result + (useSQLite ? 1 : 0);
        return result;
    }
}
