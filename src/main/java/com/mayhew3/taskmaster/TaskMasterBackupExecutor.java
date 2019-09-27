package com.mayhew3.taskmaster;

import com.google.common.collect.Lists;
import com.mayhew3.postgresobject.ArgumentChecker;
import com.mayhew3.postgresobject.EnvironmentChecker;
import com.mayhew3.postgresobject.db.DataBackupExecutor;
import com.mayhew3.postgresobject.db.DataBackupLocalExecutor;
import com.mayhew3.postgresobject.db.DataBackupRemoteExecutor;
import com.mayhew3.postgresobject.exception.MissingEnvException;

import java.io.IOException;

public class TaskMasterBackupExecutor {
  private static String backupEnv;


  public static void main(String[] args) throws MissingEnvException, InterruptedException, IOException {

    com.mayhew3.postgresobject.ArgumentChecker argumentChecker = new ArgumentChecker(args);
    argumentChecker.removeExpectedOption("db");
    argumentChecker.addExpectedOption("backupEnv", true, "Name of environment to backup (local, heroku, heroku-staging)");

    backupEnv = argumentChecker.getRequiredValue("backupEnv");

    if (isLocal()) {
      String localDBName = getLocalDBNameFromEnv(backupEnv);

      DataBackupExecutor executor = new DataBackupLocalExecutor(
          backupEnv,
          11,
          "TaskMaster",
          localDBName);
      executor.runUpdate();
    } else {
      String databaseUrl = EnvironmentChecker.getOrThrow("DATABASE_URL");
      DataBackupExecutor executor = new DataBackupRemoteExecutor(
          backupEnv,
          11,
          "TaskMaster",
          databaseUrl);
      executor.runUpdate();
    }
  }

  private static boolean isLocal() {
    return Lists.newArrayList("local", "e2e").contains(backupEnv);
  }

  private static String getLocalDBNameFromEnv(String backupEnv) {
    if ("local".equalsIgnoreCase(backupEnv)) {
      return "taskmaster";
    } else if ("e2e".equalsIgnoreCase(backupEnv)) {
      return "taskmaster_e2e";
    } else {
      return null;
    }
  }

}
