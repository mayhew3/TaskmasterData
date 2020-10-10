package com.mayhew3.taskmaster.model;

import com.mayhew3.postgresobject.dataobject.Nullability;
import com.mayhew3.postgresobject.dataobject.RetireableDataObject;

public class Snooze extends RetireableDataObject {

  public Snooze() {
    super();
    registerForeignKey(new Task(), Nullability.NOT_NULL);
    registerIntegerField("snooze_number", Nullability.NOT_NULL);
    registerStringField("snooze_units", Nullability.NOT_NULL);
    registerStringField("snooze_anchor", Nullability.NOT_NULL);
    registerTimestampField("previous_anchor", Nullability.NOT_NULL);
    registerTimestampField("new_anchor", Nullability.NOT_NULL);
  }

  @Override
  public String getTableName() {
    return "snooze";
  }
}
