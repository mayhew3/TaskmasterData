package com.mayhew3.taskmaster.db;

import com.mayhew3.postgresobject.ArgumentChecker;
import com.mayhew3.postgresobject.db.DatabaseEnvironment;
import com.mayhew3.postgresobject.db.LocalDatabaseEnvironment;

import java.util.HashMap;
import java.util.Map;

public class DatabaseEnvironments {
  public static Map<String, DatabaseEnvironment> environments = new HashMap<>();

  static {
    addLocal("local", "taskmaster", 13);
    addHeroku("heroku", "postgresURL_taskmaster", 11, "taskmaster-general");
  }

  @SuppressWarnings("SameParameterValue")
  private static void addLocal(String environmentName, String databaseName, Integer pgVersion) {
    Integer port = 5432 - 9 + pgVersion;
    LocalDatabaseEnvironment local = new LocalDatabaseEnvironment(environmentName, databaseName, port, pgVersion);
    environments.put(environmentName, local);
  }

  @SuppressWarnings("SameParameterValue")
  private static void addHeroku(String environmentName, String databaseName, Integer pgVersion, String herokuAppName) {
    HerokuDatabaseEnvironment local = new HerokuDatabaseEnvironment(environmentName, databaseName, pgVersion, herokuAppName);
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
