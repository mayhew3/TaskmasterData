package com.mayhew3.taskmaster;

import com.google.common.collect.Lists;
import com.mayhew3.postgresobject.ArgumentChecker;
import com.mayhew3.postgresobject.EnvironmentChecker;
import com.mayhew3.postgresobject.db.DataRestoreExecutor;
import com.mayhew3.postgresobject.db.DataRestoreLocalExecutor;
import com.mayhew3.postgresobject.db.DataRestoreRemoteExecutor;
import com.mayhew3.postgresobject.exception.MissingEnvException;

import java.io.IOException;

public class TaskMasterRestoreExecutor {

  private static String restoreEnv;

  public static void main(String... args) throws MissingEnvException, InterruptedException, IOException {

    com.mayhew3.postgresobject.ArgumentChecker argumentChecker = new ArgumentChecker(args);
    argumentChecker.removeExpectedOption("db");
    argumentChecker.addExpectedOption("backupEnv", true, "Name of environment to backup (local, heroku, heroku-staging)");
    argumentChecker.addExpectedOption("restoreEnv", true, "Name of environment to restore (local, heroku, heroku-staging)");

    String backupEnv = argumentChecker.getRequiredValue("backupEnv");
    restoreEnv = argumentChecker.getRequiredValue("restoreEnv");

    if (isLocal()) {
      String localDBName = getLocalDBNameFromEnv(restoreEnv);
      DataRestoreExecutor dataRestoreExecutor = new DataRestoreLocalExecutor(
          restoreEnv,
          backupEnv,
          11,
          "TaskMaster",
          localDBName);
      dataRestoreExecutor.runUpdate();
    } else {
      String appNameFromEnv = getAppNameFromEnv(restoreEnv);
      String databaseUrl = EnvironmentChecker.getOrThrow("DATABASE_URL");
      DataRestoreExecutor dataRestoreExecutor = new DataRestoreRemoteExecutor(
          restoreEnv,
          backupEnv,
          11,
          "TaskMaster",
          appNameFromEnv,
          databaseUrl);
      dataRestoreExecutor.runUpdate();
    }
  }

  private static boolean isLocal() {
    return Lists.newArrayList("local", "e2e").contains(restoreEnv);
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

  private static String getAppNameFromEnv(String backupEnv) {
    if ("heroku".equalsIgnoreCase(backupEnv)) {
      return "taskmaster-general";
    } else if ("heroku-staging".equalsIgnoreCase(backupEnv)) {
      return "taskmaster-staging";
    } else {
      return null;
    }
  }

}
