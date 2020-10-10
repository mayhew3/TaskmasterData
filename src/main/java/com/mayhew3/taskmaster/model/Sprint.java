package com.mayhew3.taskmaster.model;

import com.mayhew3.postgresobject.dataobject.Nullability;
import com.mayhew3.postgresobject.dataobject.RetireableDataObject;

public class Sprint extends RetireableDataObject {

  public Sprint() {
    super();
    registerForeignKey(new Person(), Nullability.NOT_NULL);
    registerTimestampField("start_date", Nullability.NOT_NULL);
    registerTimestampField("end_date", Nullability.NOT_NULL);
  }

  @Override
  public String getTableName() {
    return "sprint";
  }
}
