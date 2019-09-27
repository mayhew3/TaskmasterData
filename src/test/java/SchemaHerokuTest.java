import com.mayhew3.postgresobject.EnvironmentChecker;
import com.mayhew3.postgresobject.dataobject.DataSchema;
import com.mayhew3.postgresobject.exception.MissingEnvException;
import com.mayhew3.postgresobject.model.SchemaTest;
import com.mayhew3.taskmaster.TaskMasterSchema;

public class SchemaHerokuTest extends SchemaTest {
  @Override
  public DataSchema getDataSchema() {
    return TaskMasterSchema.schema;
  }

  @Override
  public String getDBConnectionString() {
    try {
      return EnvironmentChecker.getOrThrow("DATABASE_URL");
    } catch (MissingEnvException e) {
      e.printStackTrace();
      throw new IllegalStateException(e);
    }
  }
}
