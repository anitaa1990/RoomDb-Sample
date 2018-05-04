# RoomDb-Sample
This is a demo app on how to implement Room persistance library, making use of LiveData in Android app</br></br>

<img src="https://github.com/anitaa1990/RoomDb-Sample/blob/master/media/1.png" width="200" style="max-width:100%;">   <img src="https://github.com/anitaa1990/RoomDb-Sample/blob/master/media/2.png" width="200" style="max-width:100%;">   <img src="https://github.com/anitaa1990/RoomDb-Sample/blob/master/media/3.png" width="200" style="max-width:100%;"></br></br>

<h3>How to implement Room: a SQLite object mapping library in your Android app?</h3>
Step 1: Add following library and annotation processor to your app gradle file.

```
compile "android.arch.persistence.room:runtime:1.0.0"
annotationProcessor "android.arch.persistence.room:compiler:1.0.0" 
```
</br>

<b>Note:</b> The reason why annotation processor is needed is because all operations like Insert, Delete, Update etc are annotated.</br></br>

Step 2: Component 1 in room - Create an entity class:</br>
This is nothing but a model class annotated with @Entity where all the variable will becomes column name for the table and name of the model class becomes name of the table. The name of the class is the table name and the variables are the columns of the table</br>
Example: ```Note.java```</br>
```
@Entity
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;


    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    private Date createdAt;

    @ColumnInfo(name = "modified_at")
    @TypeConverters({TimestampConverter.class})
    private Date modifiedAt;

    private boolean encrypt;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```
</br>


Step 3: Component 2 in room - Create a DAO class</br>
This is an interface which acts is an intermediary between the user and the database. All the operation to be performed on a table has to be defined here. We define the list of operation that we would like to perform on table</br>
Example: ```DaoAccess.java```</br>
```
@Dao
public interface DaoAccess {

    @Insert
    Long insertTask(Note note);


    @Query("SELECT * FROM Note ORDER BY created_at desc")
    LiveData<List<Note>> fetchAllTasks();


    @Query("SELECT * FROM Note WHERE id =:taskId")
    LiveData<Note> getTask(int taskId);


    @Update
    void updateTask(Note note);


    @Delete
    void deleteTask(Note note);
}
```
</br>


Step 4: Component 3 in room - Create Database class</br>
This is an abstract class where you define all the entities that means all the tables that you want to create for that database. We define the list of operation that we would like to perform on table</br>
Example: ```NoteDatabase.java```</br>
```
@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract DaoAccess daoAccess();
}
```
</br>


Step 5: Create the Repository class</br>
A Repository mediates between the domain and data mapping layers, acting like an in-memory domain object collection. We access the database class and the DAO class from the repository and perform list of operations such as insert, update, delete, get</br>
Example: ```NoteRepository.java```</br>
```
public class NoteRepository {

    private String DB_NAME = "db_task";

    private NoteDatabase noteDatabase;
    public NoteRepository(Context context) {
        noteDatabase = Room.databaseBuilder(context, NoteDatabase.class, DB_NAME).build();
    }

    public void insertTask(String title,
                           String description) {

        insertTask(title, description, false, null);
    }

    public void insertTask(String title,
                           String description,
                           boolean encrypt,
                           String password) {

        Note note = new Note();
        note.setTitle(title);
        note.setDescription(description);
        note.setCreatedAt(AppUtils.getCurrentDateTime());
        note.setModifiedAt(AppUtils.getCurrentDateTime());
        note.setEncrypt(encrypt);


        if(encrypt) {
            note.setPassword(AppUtils.generateHash(password));
        } else note.setPassword(null);

        insertTask(note);
    }

    public void insertTask(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoAccess().insertTask(note);
                return null;
            }
        }.execute();
    }

    public void updateTask(final Note note) {
        note.setModifiedAt(AppUtils.getCurrentDateTime());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoAccess().updateTask(note);
                return null;
            }
        }.execute();
    }

    public void deleteTask(final int id) {
        final LiveData<Note> task = getTask(id);
        if(task != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    noteDatabase.daoAccess().deleteTask(task.getValue());
                    return null;
                }
            }.execute();
        }
    }

    public void deleteTask(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoAccess().deleteTask(note);
                return null;
            }
        }.execute();
    }

    public LiveData<Note> getTask(int id) {
        return noteDatabase.daoAccess().getTask(id);
    }

    public LiveData<List<Note>> getTasks() {
        return noteDatabase.daoAccess().fetchAllTasks();
    }
}
```
</br>


<h4>Note: DO NOT PERFORM OPERATION ON MAIN THREAD AS APP WILL CRASH</h4>
</br>

Sample Implementation of basic CRUD operations using ROOM</br>
<b>1. Insert:</b> </br>
```
   NoteRepository noteRepository = new NoteRepository(getApplicationContext());
   String title = "This is the title of the third task";
   String description = "This is the description of the third task";
   noteRepository.insertTask(title, description);
```
</br>


<b>2. Update:</b> </br>
```
   NoteRepository noteRepository = new NoteRepository(getApplicationContext());
   Note note = noteRepository.getTask(2);
   note.setEncrypt(true);
   note.setPassword(AppUtils.generateHash("Password@1"));
   note.setTitle("This is an example of modify");
   note.setDescription("This is an example to modify the second task");

   noteRepository.updateTask(note);
```
</br>


<b>3. Delete:</b> </br>
```
   NoteRepository noteRepository = new NoteRepository(getApplicationContext());
   noteRepository.deleteTask(3);
```
</br>


<b>4. Get all notes:</b> </br>
```
   NoteRepository noteRepository = new NoteRepository(getApplicationContext());

   noteRepository.getTasks().observe(appContext, new Observer<List<Note>>() {
       @Override
       public void onChanged(@Nullable List<Note> notes) {
           for(Note note : notes) {
               System.out.println("-----------------------");
               System.out.println(note.getId());
               System.out.println(note.getTitle());
               System.out.println(note.getDescription());
               System.out.println(note.getCreatedAt());
               System.out.println(note.getModifiedAt());
               System.out.println(note.getPassword());
               System.out.println(note.isEncrypt());
            }
        }
    });
```
</br>


<b>5. Get single note by id:</b> </br>
```
   NoteRepository noteRepository = new NoteRepository(getApplicationContext());
   Note note = noteRepository.getTask(2);
```
</br>


And that's it! It's super simple. You can check out the official documentation <a href="https://developer.android.com/topic/libraries/architecture/room" target="_blank">here</a>
