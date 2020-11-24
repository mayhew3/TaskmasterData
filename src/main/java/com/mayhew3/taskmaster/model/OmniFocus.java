package com.mayhew3.taskmaster.model;

import com.mayhew3.postgresobject.dataobject.*;

public class OmniFocus extends DataObject {

  public FieldValueString taskID = registerStringField("TaskID", Nullability.NULLABLE);
  public FieldValueString type = registerStringField("Type", Nullability.NULLABLE);
  public FieldValueString name = registerStringField("Name", Nullability.NULLABLE);
  public FieldValueString status = registerStringField("Status", Nullability.NULLABLE);
  public FieldValueString project = registerStringField("Project", Nullability.NULLABLE);
  public FieldValueString context = registerStringField("Context", Nullability.NULLABLE);

  public FieldValueTimestamp startDate = registerTimestampField("StartDate", Nullability.NULLABLE);
  public FieldValueTimestamp dueDate = registerTimestampField("DueDate", Nullability.NULLABLE);
  public FieldValueTimestamp completionDate = registerTimestampField("CompletionDate", Nullability.NULLABLE);

  public FieldValueString duration = registerStringField("Duration", Nullability.NULLABLE);

  public FieldValueInteger flagged = registerIntegerField("Flagged", Nullability.NULLABLE);

  public FieldValueInteger recurNumber = registerIntegerField("Recur_Num", Nullability.NULLABLE);
  public FieldValueString recurUnit = registerStringField("Recur_Unit", Nullability.NULLABLE);
  public FieldValueInteger recurWait = registerIntegerField("Recur_Sched", Nullability.NULLABLE);

  public FieldValueString description = registerStringField("Notes", Nullability.NULLABLE);
  public FieldValueString tags = registerStringField("Tags", Nullability.NULLABLE);

  @Override
  public String getTableName() {
    return "omnifocus";
  }
}
