package com.dsh105.echopet.compat.api.util;

import com.dsh105.echopet.compat.api.util.Logger;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.uuid.UUIDFetcher;
import com.dsh105.echopet.compat.api.plugin.uuid.UUIDMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Coordinates migration between table schemas.
 *
 * Table migration strategies are intended to be incremental: if a user has a table two-versions-old,
 * the table will be migrated twice (once for each version).
 */
public class TableMigrationUtil {
    public static final String LATEST_TABLE = "EchoPet_version3";
    private static final List<MigrationStrategy> tableMigrationStrategies = new ArrayList<MigrationStrategy>();

    // TODO: Perhaps migration strategies should be moved to their own classes.
    static {
        // Pets -> EchoPet
        // Migrates UUIDs as necessary
        tableMigrationStrategies.add(new MigrationStrategy("Pets") {
            @Override
            public String getMigratedTableSchema() {
                return "EchoPet ("
                     + "    OwnerName varchar(255),"
                     + "    PetType varchar(255),"
                     + "    PetName varchar(255),"
                     +      serialise(PetData.values(), false) + ", "
                     + "    RiderPetType varchar(255), RiderPetName varchar(255), "
                     +      serialise(PetData.values(), true) + ", "
                     + "    PRIMARY KEY (OwnerName)"
                     + ");";
            }

            @Override
            public void migrate(Connection conn) throws SQLException {
                // Copy all of our data over to the new table
                PreparedStatement copyStatement = conn.prepareStatement("INSERT INTO EchoPet SELECT * FROM Pets");
                copyStatement.executeUpdate();

                // Migrate to UUIDs in the new table if necessary
                if (ReflectionUtil.MC_VERSION_NUMERIC >= 172 && UUIDMigration.supportsUuid()) {
                    PreparedStatement getOwnerStatement = conn.prepareStatement("SELECT OwnerName FROM EchoPet");

                    PreparedStatement updateNameStatement = conn.prepareStatement("UPDATE EchoPet SET OwnerName = ? WHERE OwnerName = ?");
                    ResultSet resultSet = getOwnerStatement.executeQuery();
                    while (resultSet.next()) {
                        String ownerName = resultSet.getString("OwnerName");

                        try {
                            UUID.fromString(ownerName);
                            continue; // This name is already a UUID.
                        } catch (IllegalArgumentException ignored) {}

                        UUID playerUUID;
                        try {
                            playerUUID = UUIDFetcher.getUUIDOf(ownerName);
                        } catch (Exception e) {
                            continue;
                        }

                        if (playerUUID == null) {
                            continue;
                        }

                        updateNameStatement.setString(1, playerUUID.toString());
                        updateNameStatement.setString(2, ownerName);
                        updateNameStatement.addBatch();
                    }

                    updateNameStatement.executeBatch();
                }
            }

            // Implementation-compatible with old SQLUtil::serialise
            private String serialise(PetData[] data, boolean isRider) {
                String serialized = (isRider ? "Rider" : "") + data[0].toString() + " varchar(255)";

                for (int i = 1; i < data.length; i++) {
                    serialized += ", " + (isRider ? "Rider" : "") + data[i].toString() + " varchar(255)";
                }

                return serialized;
            }
        });

        // EchoPet -> EchoPet_version3
        // Reduction in columns -- no longer has a column (varchar 255) per PetData
        tableMigrationStrategies.add(new MigrationStrategy("EchoPet") {
            @Override
            public String getMigratedTableSchema() {
                return "EchoPet_version3 ("
                     + "    OwnerName varchar(36),"
                     + "    PetType varchar(255),"
                     + "    PetName varchar(255),"
                     + "    PetData BIGINT,"
                     + "    RiderPetType varchar(255),"
                     + "    RiderPetName varchar(255), "
                     + "    RiderPetData BIGINT,"
                     + "    PRIMARY KEY (OwnerName)"
                     + ");";
            }

            @Override
            public void migrate(Connection conn) throws SQLException {
                PreparedStatement selectAll = conn.prepareStatement("SELECT * FROM EchoPet");
                ResultSet resultSet = selectAll.executeQuery();

                PreparedStatement statement = conn.prepareStatement("INSERT INTO EchoPet_version3 (OwnerName, PetType, PetName, PetData, RiderPetType, RiderPetName, RiderPetData) VALUES (?, ?, ?, ?, ?, ?, ?)");
                while (resultSet.next()) {

                    statement.setString(1, resultSet.getString("OwnerName"));
                    statement.setString(2, resultSet.getString("PetType"));
                    statement.setString(3, resultSet.getString("PetName"));

                    List<PetData> dataList = new ArrayList<PetData>();
                    for (PetData data : PetData.values()) {
                        String dataValue = resultSet.getString(data.toString());

                        if (dataValue != null && Boolean.valueOf(dataValue)) {
                            dataList.add(data);
                        }
                    }

                    statement.setLong(4, SQLUtil.serializePetData(dataList));

                    statement.setString(5, resultSet.getString("RiderPetType"));
                    statement.setString(6, resultSet.getString("RiderPetName"));

                    List<PetData> riderDataList = new ArrayList<PetData>();
                    for (PetData data : PetData.values()) {
                        String dataValue = resultSet.getString("Rider" + data.toString());

                        if (dataValue != null && Boolean.valueOf(dataValue)) {
                            riderDataList.add(data);
                        }
                    }

                    statement.setLong(7, SQLUtil.serializePetData(riderDataList));
                    statement.addBatch();
                }
                statement.executeBatch();
            }
        });
    }

    /**
     * Migrate old tables using EchoPet's SQL connection pool
     *
     * In the process of migration, old tables will be dropped
     */
    public static void migrateTables() {
        Connection conn = null;

        try {
            conn = EchoPet.getPlugin().getDbPool().getConnection();
            for (MigrationStrategy strategy : tableMigrationStrategies) {
                if (conn.getMetaData().getTables(null, null, strategy.getTableName(), null).next()) {
                    strategy.createTargetTable(conn);
                    strategy.migrate(conn);
                    strategy.dropOldTable(conn);
                }
            }

        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to migrate old SQL table(s)", e, true);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }

    /**
     * Represents a table schema transition
     */
    abstract static class MigrationStrategy {
        private final String tableName;

        /**
         * @param tableName The name of the table that, if present, will be migrated.
         */
        public MigrationStrategy(String tableName) {
            this.tableName = tableName;
        }

        /**
         * @return The name of the table that, if present, will be migrated.
         */
        public String getTableName() {
            return tableName;
        }

        /**
         * Returns the SQL syntax of the migration target table as could be used
         * in a "CREATE TABLE" statement
         *
         * @return The SQL syntax of the migration target table
         */
        public abstract String getMigratedTableSchema();

        /**
         * Performs the table migration
         *
         * @param conn The connection that will be used when migrating the table
         * @throws SQLException
         */
        public abstract void migrate(Connection conn) throws SQLException;

        /**
         * Drops the table containing the unmigrated data.
         * This should be preceded by {@link #migrate(java.sql.Connection)}
         *
         * @param conn The connection that will be used to drop the table
         * @throws SQLException
         */
        public void dropOldTable(Connection conn) throws SQLException {
            conn.prepareStatement("DROP TABLE " + tableName).executeUpdate();
        }

        /**
         * Creates the table required for the migration to occur
         *
         * @param conn The connection that will be used to create the table
         * @throws SQLException
         */
        public void createTargetTable(Connection conn) throws SQLException {
            conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + getMigratedTableSchema()).executeUpdate();
        }
    }
}
