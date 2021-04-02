import com.mayhew3.postgresobject.dataobject.DataSchema;
import com.mayhew3.postgresobject.db.DatabaseEnvironment;
import com.mayhew3.postgresobject.model.PostgresSchemaTest;
import com.mayhew3.taskmaster.TaskMasterSchema;
import com.mayhew3.taskmaster.db.DatabaseEnvironments;

public class SchemaLocalTest extends PostgresSchemaTest {
  @Override
  public DataSchema getDataSchema() {
    return TaskMasterSchema.schema;
  }

  @Override
  public DatabaseEnvironment getDatabaseEnvironment() {
    return DatabaseEnvironments.environments.get("local");
  }
}
