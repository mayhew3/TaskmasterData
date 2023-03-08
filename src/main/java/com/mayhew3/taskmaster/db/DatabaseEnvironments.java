package com.mayhew3.taskmaster.db;

import com.mayhew3.postgresobject.ArgumentChecker;
import com.mayhew3.postgresobject.db.DatabaseEnvironment;
import com.mayhew3.postgresobject.db.LocalDatabaseEnvironment;
import com.mayhew3.taskmaster.GlobalConstants;

import java.util.HashMap;
import java.util.Map;

public class DatabaseEnvironments {
  public static Map<String, DatabaseEnvironment> environments = new HashMap<>();

  static {
    addLocal("local", "projects", GlobalConstants.schemaName, 14);
    addHeroku("heroku-staging", "postgresURL_staging", GlobalConstants.schemaName, 14, "taskmaster-staging");
    addHeroku("heroku", "postgresURL_heroku", GlobalConstants.schemaName, 14, "taskmaster-general");
  }

  @SuppressWarnings("SameParameterValue")
  private static void addLocal(String environmentName, String databaseName, String schemaName, Integer pgVersion) {
    Integer port = 5432 - 9 + pgVersion;
    LocalDatabaseEnvironment local = new LocalDatabaseEnvironment(environmentName, databaseName, schemaName, port, pgVersion);
    environments.put(environmentName, local);
  }

  @SuppressWarnings("SameParameterValue")
  private static void addHeroku(String environmentName, String databaseName, String schemaName, Integer pgVersion, String herokuAppName) {
    HerokuDatabaseEnvironment local = new HerokuDatabaseEnvironment(environmentName, databaseName, schemaName, pgVersion, herokuAppName);
    environments.put(environmentName, local);
  }

  public static DatabaseEnvironment getEnvironmentForDBArgument(ArgumentChecker argumentChecker) {
    String dbIdentifier = argumentChecker.getDBIdentifier();
    DatabaseEnvironment databaseEnvironment = environments.get(dbIdentifier);
    if (databaseEnvironment == null) {
      throw new IllegalArgumentException("No environment found with name: " + dbIdentifier);
    }
    return databaseEnvironment;
  }
}
