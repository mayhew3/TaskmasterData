package com.mayhew3.taskmaster;

import com.mayhew3.postgresobject.ArgumentChecker;
import com.mayhew3.postgresobject.db.*;
import com.mayhew3.postgresobject.exception.MissingEnvException;
import com.mayhew3.taskmaster.db.DatabaseEnvironments;
import com.mayhew3.taskmaster.db.HerokuDatabaseEnvironment;

import java.io.IOException;
import java.sql.SQLException;

public class TaskMasterBackupExecutor {

  private final DatabaseEnvironment databaseEnvironment;

  public static void main(String[] args) throws MissingEnvException, InterruptedException, IOException, SQLException {

    com.mayhew3.postgresobject.ArgumentChecker argumentChecker = new ArgumentChecker(args);
    argumentChecker.removeExpectedOption("db");
    argumentChecker.addExpectedOption("backupEnv", true, "Name of environment to backup (local, heroku, heroku-staging)");

    String backupEnv = argumentChecker.getRequiredValue("backupEnv");
    DatabaseEnvironment databaseEnvironment = DatabaseEnvironments.environments.get(backupEnv);

    TaskMasterBackupExecutor taskMasterBackupExecutor = new TaskMasterBackupExecutor(databaseEnvironment);
    taskMasterBackupExecutor.runUpdate();
  }

  public TaskMasterBackupExecutor(DatabaseEnvironment databaseEnvironment) {
    this.databaseEnvironment = databaseEnvironment;
  }

  public void runUpdate() throws MissingEnvException, InterruptedException, IOException, SQLException {
    if (databaseEnvironment.isLocal()) {
      updateLocal();
    } else {
      updateRemote();
    }
  }

  private void updateLocal() throws MissingEnvException, InterruptedException, IOException, SQLException {
    LocalDatabaseEnvironment localDatabaseEnvironment = (LocalDatabaseEnvironment) databaseEnvironment;

    DataBackupExecutor executor = new DataBackupLocalExecutor(localDatabaseEnvironment, GlobalConstants.appLabel);
    executor.runUpdate();
  }

  private void updateRemote() throws MissingEnvException, IOException, InterruptedException, SQLException {
    HerokuDatabaseEnvironment herokuDatabaseEnvironment = (HerokuDatabaseEnvironment) databaseEnvironment;

    DataBackupExecutor executor = new DataBackupRemoteSchemaExecutor(herokuDatabaseEnvironment, GlobalConstants.appLabel);
    executor.runUpdate();
  }
}
