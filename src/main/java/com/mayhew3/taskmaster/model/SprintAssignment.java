package com.mayhew3.taskmaster.model;

import com.mayhew3.postgresobject.dataobject.FieldValueForeignKey;
import com.mayhew3.postgresobject.dataobject.Nullability;
import com.mayhew3.postgresobject.dataobject.RetireableDataObject;

public class SprintAssignment extends RetireableDataObject {

  public SprintAssignment() {
    super();
    FieldValueForeignKey taskID = registerForeignKey(new Task(), Nullability.NOT_NULL);
    FieldValueForeignKey sprintID = registerForeignKey(new Sprint(), Nullability.NOT_NULL);
    addUniqueConstraint(taskID, sprintID);
  }

  @Override
  public String getTableName() {
    return "sprint_assignment";
  }
}
