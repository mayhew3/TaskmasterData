package com.mayhew3.taskmaster.model;

import com.mayhew3.postgresobject.dataobject.FieldValueString;
import com.mayhew3.postgresobject.dataobject.Nullability;
import com.mayhew3.postgresobject.dataobject.RetireableDataObject;

public class Person extends RetireableDataObject {

  public FieldValueString last_name = registerStringField("last_name", Nullability.NOT_NULL);
  public FieldValueString first_name = registerStringField("first_name", Nullability.NOT_NULL);
  public FieldValueString email = registerStringField("email", Nullability.NULLABLE);

  @Override
  public String getTableName() {
    return "person";
  }

  @Override
  public String toString() {
    return first_name.getValue() + " " + last_name.getValue();
  }
}
