package com.mayhew3.taskmaster;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mayhew3.postgresobject.ArgumentChecker;
import com.mayhew3.postgresobject.db.DatabaseEnvironment;
import com.mayhew3.postgresobject.db.PostgresConnection;
import com.mayhew3.postgresobject.db.PostgresConnectionFactory;
import com.mayhew3.postgresobject.db.SQLConnection;
import com.mayhew3.postgresobject.exception.MissingEnvException;
import com.mayhew3.taskmaster.db.DatabaseEnvironments;
import com.mayhew3.taskmaster.model.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class RecurrenceFixer {
  private final SQLConnection connection;

  private int updated = 0;

  private static final Logger logger = LogManager.getLogger(RecurrenceFixer.class);

  public RecurrenceFixer(SQLConnection connection) {
    this.connection = connection;
  }

  public static void main(String... args) throws SQLException, MissingEnvException, URISyntaxException {
    ArgumentChecker argumentChecker = new ArgumentChecker(args);
    DatabaseEnvironment dbEnv = DatabaseEnvironments.getEnvironmentForDBArgument(argumentChecker);
    PostgresConnection connection = PostgresConnectionFactory.createConnection(dbEnv);

    RecurrenceFixer recurrenceFixer = new RecurrenceFixer(connection);
    recurrenceFixer.runUpdate();
  }

  private void runUpdate() throws SQLException {
    String sql = "SELECT * " +
        "FROM task " +
        "WHERE recurrence_id IS NOT NULL " +
        "AND retired = ? " +
        "ORDER BY id ";

    ResultSet resultSet = connection.prepareAndExecuteStatementFetch(sql, 0);

    List<Task> allTasks = new ArrayList<>();

    while (resultSet.next()) {
      Task task = new Task();
      task.initializeFromDBObject(resultSet);

      allTasks.add(task);
    }

    Map<Integer, List<Task>> grouped = allTasks.stream()
        .collect(Collectors.groupingBy(task -> task.recurrence_id.getValue()));
    Multimap<Integer, Integer> oldIDsGrouped = ArrayListMultimap.create();
    Multimap<Integer, Integer> newIDsGrouped = ArrayListMultimap.create();

    grouped.forEach((Integer recurrenceId, List<Task> tasks) -> {
      for (Task task : tasks) {
        oldIDsGrouped.put(recurrenceId, task.id.getValue());
      }

      @SuppressWarnings("OptionalGetWithoutIsPresent")
      Integer minID = tasks.stream()
          .map(task -> task.id.getValue())
          .min(Comparator.naturalOrder())
          .get();

      for (Task task : tasks) {
        newIDsGrouped.put(minID, task.id.getValue());
      }
    });

    logger.debug("Grouped " + newIDsGrouped.values().size() + " items into " + newIDsGrouped.keySet().size() + " groups.");

    clearRecurrenceIDs();

    newIDsGrouped.keySet().forEach((Integer recurrenceId) -> {
      Collection<Integer> ids = newIDsGrouped.get(recurrenceId);
      logger.debug("Recurrence Group " + recurrenceId + ", updating " + ids.size() + " tasks...");
      List<Integer> sorted = ids.stream()
          .sorted()
          .collect(Collectors.toList());
      int i = 0;
      for (Integer id : sorted) {
        try {
          updateRecurrenceID(id, recurrenceId, i);
          i++;
          updated++;
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    });

    logger.debug("Ran updated on " + updated + " rows.");
  }

  private void updateRecurrenceID(Integer id, Integer recurrenceID, Integer recurIteration) throws SQLException {
    logger.debug("- Updating task with ID " + id + " to new recurrence_id " + recurrenceID + ", iteration " + recurIteration);

    String sql = "UPDATE task " +
        "SET recurrence_id = ? ," +
        "    recur_iteration = ? " +
        "WHERE id = ? ";

    connection.prepareAndExecuteStatementUpdate(sql, recurrenceID, recurIteration, id);
  }

  private void clearRecurrenceIDs() throws SQLException {
    String sql = "UPDATE task " +
        "SET recurrence_id = NULL, recur_iteration = NULL " +
        "WHERE recurrence_id IS NOT NULL " +
        "AND retired = ? ";

    connection.prepareAndExecuteStatementUpdate(sql, 0);
  }
}
