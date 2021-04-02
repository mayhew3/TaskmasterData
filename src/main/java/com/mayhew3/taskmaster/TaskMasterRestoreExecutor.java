package com.mayhew3.taskmaster;

import com.mayhew3.postgresobject.ArgumentChecker;
import com.mayhew3.postgresobject.db.*;
import com.mayhew3.postgresobject.exception.MissingEnvException;
import com.mayhew3.taskmaster.db.DatabaseEnvironments;
import com.mayhew3.taskmaster.db.HerokuDatabaseEnvironment;
import org.joda.time.DateTime;

import java.io.IOException;

public class TaskMasterRestoreExecutor {

  private static final DateTime backupDate = new DateTime(2021, 3, 8, 20, 45, 0);

  private final DatabaseEnvironment backupEnvironment;
  private final DatabaseEnvironment restoreEnvironment;
  private final boolean oldBackup;

  public TaskMasterRestoreExecutor(DatabaseEnvironment backupEnvironment, DatabaseEnvironment restoreEnvironment, boolean oldBackup) {
    this.backupEnvironment = backupEnvironment;
    this.restoreEnvironment = restoreEnvironment;
    this.oldBackup = oldBackup;
  }

  public static void main(String... args) throws MissingEnvException, InterruptedException, IOException {

    ArgumentChecker argumentChecker = new ArgumentChecker(args);
    argumentChecker.removeExpectedOption("db");
    argumentChecker.addExpectedOption("backupEnv", true, "Name of environment to backup (local, heroku, heroku-staging)");
    argumentChecker.addExpectedOption("restoreEnv", true, "Name of environment to restore (local, heroku, heroku-staging)");

    String backupEnv = argumentChecker.getRequiredValue("backupEnv");
    String restoreEnv = argumentChecker.getRequiredValue("restoreEnv");

    boolean oldBackup = Boolean.parseBoolean(argumentChecker.getRequiredValue("oldBackup"));

    DatabaseEnvironment backupEnvironment = DatabaseEnvironments.environments.get(backupEnv);
    DatabaseEnvironment restoreEnvironment = DatabaseEnvironments.environments.get(restoreEnv);

    if (backupEnvironment == null) {
      throw new IllegalArgumentException("Invalid backupEnv: " + backupEnv);
    }
    if (restoreEnvironment == null) {
      throw new IllegalArgumentException("Invalid restoreEnv: " + restoreEnv);
    }

    TaskMasterRestoreExecutor taskMasterRestoreExecutor = new TaskMasterRestoreExecutor(backupEnvironment, restoreEnvironment, oldBackup);
    taskMasterRestoreExecutor.runUpdate();
  }

  public void runUpdate() throws InterruptedException, IOException, com.mayhew3.postgresobject.exception.MissingEnvException {
    if (restoreEnvironment.isLocal()) {
      updateLocal();
    } else {
      updateRemote();
    }
  }

  private void updateLocal() throws MissingEnvException, InterruptedException, IOException {
    LocalDatabaseEnvironment localRestoreEnvironment = (LocalDatabaseEnvironment) restoreEnvironment;

    DataRestoreExecutor dataRestoreExecutor;
    if (oldBackup) {
      dataRestoreExecutor = new DataRestoreLocalExecutor(localRestoreEnvironment, backupEnvironment, GlobalConstants.appLabel, backupDate);
    } else {
      dataRestoreExecutor = new DataRestoreLocalExecutor(localRestoreEnvironment, backupEnvironment, GlobalConstants.appLabel);
    }
    dataRestoreExecutor.runUpdate();

  }

  private void updateRemote() throws MissingEnvException, IOException, InterruptedException {
    HerokuDatabaseEnvironment herokuRestoreEnvironment = (HerokuDatabaseEnvironment) restoreEnvironment;

    DataRestoreExecutor dataRestoreExecutor;
    if (oldBackup) {
      dataRestoreExecutor = new DataRestoreRemoteExecutor(herokuRestoreEnvironment, backupEnvironment, GlobalConstants.appLabel, backupDate);
    } else {
      dataRestoreExecutor = new DataRestoreRemoteExecutor(herokuRestoreEnvironment, backupEnvironment, GlobalConstants.appLabel);
    }
    dataRestoreExecutor.runUpdate();
  }

}
