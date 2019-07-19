package com.mayhew3.taskmaster;

import com.mayhew3.postgresobject.dataobject.DataSchema;
import com.mayhew3.taskmaster.model.Person;
import com.mayhew3.taskmaster.model.Task;

public class TaskMasterSchema {
  public static DataSchema schema = new DataSchema(
      new Person(),
      new Task()
  );
}
