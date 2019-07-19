package com.mayhew3.taskmaster.model;

import com.mayhew3.postgresobject.dataobject.*;

public class Task extends DataObject {

  public FieldValueString name = registerStringField("name", Nullability.NOT_NULL);
  public FieldValueForeignKey person_id = registerForeignKey(new Person(), Nullability.NOT_NULL);

  public FieldValueTimestamp date_completed = registerTimestampField("date_completed", Nullability.NULLABLE);

  @Override
  public String getTableName() {
    return "task";
  }
}
