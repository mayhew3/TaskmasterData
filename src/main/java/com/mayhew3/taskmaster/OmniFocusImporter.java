package com.mayhew3.taskmaster;

import com.mayhew3.postgresobject.ArgumentChecker;
import com.mayhew3.postgresobject.EnvironmentChecker;
import com.mayhew3.postgresobject.db.DatabaseEnvironment;
import com.mayhew3.postgresobject.db.PostgresConnectionFactory;
import com.mayhew3.postgresobject.db.SQLConnection;
import com.mayhew3.taskmaster.db.DatabaseEnvironments;
import com.mayhew3.taskmaster.model.OmniFocus;
import com.mayhew3.taskmaster.model.Task;

import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class OmniFocusImporter {

  private static SQLConnection connection;

  public static void main(String... args) throws URISyntaxException, SQLException, com.mayhew3.postgresobject.exception.MissingEnvException {
    ArgumentChecker argumentChecker = new ArgumentChecker(args);
    DatabaseEnvironment environment = DatabaseEnvironments.getEnvironmentForDBArgument(argumentChecker);
    connection = PostgresConnectionFactory.createConnection(environment);

    String sql = "SELECT * " +
        "FROM omnifocus " +
        "WHERE \"Flagged\" = ? " +
        "AND \"Type\" = ? ";
    ResultSet resultSet = connection.prepareAndExecuteStatementFetch(sql, 1, "Action");

    while (resultSet.next()) {
      OmniFocus omniFocus = new OmniFocus();
      omniFocus.initializeFromDBObject(resultSet);

      Task task = new Task();
      task.initializeForInsert();

      String durationStr = omniFocus.duration.getValue();
      Integer asMinutes = null;
      if (durationStr != null) {
        String replaced = durationStr.replace("m", "");
        asMinutes = Integer.parseInt(replaced);
      }

      Map<String, String> projects = new HashMap<>();
      projects.put("Apartment", "Maintenance");
      projects.put("Computer", "Maintenance");
      projects.put("Family", "Family");
      projects.put("Fantasy Baseball", "Hobby");
      projects.put("Finances", "Maintenance");
      projects.put("Friends", "Friends");
      projects.put("Holidays", "Family");
      projects.put("Issues", "Health");
      projects.put("Maintenance", "Maintenance");
      projects.put("TV Blog", "Hobby");

      Map<String, String> contexts = new HashMap<>();
      contexts.put("Apartment", "Home");
      contexts.put("Computer", "Computer");
      contexts.put("Email", "E-Mail");
      contexts.put("Outside", "Outside");
      contexts.put("Phone", "Phone");
      contexts.put("Priority", null);
      contexts.put("Thinking", "Planning");
      contexts.put("Writing", null);

      Boolean scheduled = omniFocus.recurWait.getValue() == null ? null : omniFocus.recurWait.getValue() == 0;

      String omniProject = omniFocus.project.getValue();
      String taskProject = projects.get(omniProject);

      String omniContext = omniFocus.context.getValue();
      String taskContext = contexts.get(omniContext);

      Integer recurrence_id = omniFocus.recurNumber.getValue() == null ? null : getNextRecurrenceID();

      String recurUnit = omniFocus.recurUnit.getValue();


      task.name.changeValue(omniFocus.name.getValue());
      task.project.changeValue(taskProject);
      task.context.changeValue(taskContext);
      task.startDate.changeValue(omniFocus.startDate.getValue());
      task.dueDate.changeValue(omniFocus.dueDate.getValue());
      task.completionDate.changeValue(omniFocus.completionDate.getValue());
      task.duration.changeValue(asMinutes);
      task.recurNumber.changeValue(omniFocus.recurNumber.getValue());
      task.recurUnit.changeValue("Year".equals(recurUnit) ? "Years" : recurUnit);
      task.recurWait.changeValue(scheduled);
      task.description.changeValue(omniFocus.description.getValue());
      task.recurrence_id.changeValue(recurrence_id);

      // MAYHEW
      task.person_id.changeValue(1);

      task.commit(connection);
    }
  }

  private static int getNextRecurrenceID() throws SQLException {
    String sql = "SELECT MAX(recurrence_id) as max_recurrence_id " +
        "FROM task ";
    ResultSet resultSet = connection.prepareAndExecuteStatementFetch(sql);

    if (resultSet.next()) {
      return resultSet.getInt("max_recurrence_id") + 1;
    } else {
      throw new IllegalStateException("No rows returned from recurrence query.");
    }
  }

}
