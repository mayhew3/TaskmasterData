package com.mayhew3.taskmaster.model;

import com.mayhew3.postgresobject.dataobject.*;

public class Task extends DataObject {

  public FieldValueForeignKey person_id = registerForeignKey(new Person(), Nullability.NOT_NULL);

  public FieldValueString name = registerStringField("name", Nullability.NOT_NULL);
  public FieldValueString description = registerStringField("description", Nullability.NULLABLE);
  public FieldValueString project = registerStringField("project", Nullability.NULLABLE);
  public FieldValueString context = registerStringField("context", Nullability.NULLABLE);

  public FieldValueInteger urgency = registerIntegerField("urgency", Nullability.NOT_NULL);
  public FieldValueInteger priority = registerIntegerField("priority", Nullability.NOT_NULL);

  public FieldValueInteger duration = registerIntegerField("duration", Nullability.NULLABLE);

  public FieldValueTimestamp startDate = registerTimestampField("start_date", Nullability.NULLABLE);
  public FieldValueTimestamp targetDate = registerTimestampField("target_date", Nullability.NULLABLE);
  public FieldValueTimestamp dueDate = registerTimestampField("due_date", Nullability.NULLABLE);
  public FieldValueTimestamp completionDate = registerTimestampField("completion_date", Nullability.NULLABLE);
  public FieldValueTimestamp urgentDate = registerTimestampField("urgent_date", Nullability.NULLABLE);

  public FieldValueInteger gamePoints = registerIntegerField("game_points", Nullability.NOT_NULL);

  public FieldValueInteger recurNumber = registerIntegerField("recur_number", Nullability.NULLABLE);
  public FieldValueString recurUnit = registerStringField("recur_unit", Nullability.NULLABLE);
  public FieldValueBoolean recurWait = registerBooleanField("recur_wait", Nullability.NULLABLE);



  @Override
  public String getTableName() {
    return "task";
  }
}
