import com.mayhew3.postgresobject.EnvironmentChecker;
import com.mayhew3.postgresobject.dataobject.DataSchema;
import com.mayhew3.postgresobject.exception.MissingEnvException;
import com.mayhew3.postgresobject.model.SchemaTest;
import com.mayhew3.taskmaster.TaskMasterSchema;

public class SchemaLocalTest extends SchemaTest {
  @Override
  public DataSchema getDataSchema() {
    return TaskMasterSchema.schema;
  }

  @Override
  public String getDBConnectionString() {
    try {
      return EnvironmentChecker.getOrThrow("postgresURL_local_taskmaster");
    } catch (MissingEnvException e) {
      e.printStackTrace();
      throw new IllegalStateException(e);
    }
  }
}
