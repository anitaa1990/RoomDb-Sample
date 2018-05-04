package com.an.room;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.an.room.repository.NoteRepository;
import com.an.room.util.AppUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.an.room", appContext.getPackageName());

        String password = "Password@1";
        System.out.println(AppUtils.generateHash(password));
    }


    private Context appContext;
    private NoteRepository noteRepository;

    @Test
    public void insertTaskTest() throws InterruptedException {
        appContext = InstrumentationRegistry.getTargetContext();
        noteRepository = new NoteRepository(appContext);

        String title = "This is the title of the third task";
        String description = "This is the description of the third task";
        noteRepository.insertTask(title, description);

//        Task task = taskRepository.getTask(3);
//        assertEquals("Task is inserted successfully", title, task.getTitle());
        Thread.sleep(3500);
    }


    @Test
    public void updateTaskTest() throws InterruptedException {
        appContext = InstrumentationRegistry.getTargetContext();
        noteRepository = new NoteRepository(appContext);


//        Task task = taskRepository.getTask(2);
//        task.setEncrypt(true);
//        task.setPassword(AppUtils.generateHash("Password@1"));
//        task.setTitle("This is an example of modify");
//        task.setDescription("This is an example to modify the second task");
//
//        taskRepository.updateTask(task);
//        Thread.sleep(3500);
    }


    @Test
    public void deleteTaskTest() throws InterruptedException {
        appContext = InstrumentationRegistry.getTargetContext();
        noteRepository = new NoteRepository(appContext);

        noteRepository.deleteTask(3);
        Thread.sleep(3500);
    }


    @Test
    public void getTaskFromIdTest() {
        appContext = InstrumentationRegistry.getTargetContext();
        noteRepository = new NoteRepository(appContext);

//        Task task = taskRepository.getTask(2);
//        System.out.println("-----------------------");
//        System.out.println(task.getId());
//        System.out.println(task.getTitle());
//        System.out.println(task.getDescription());
//        System.out.println(task.getCreatedAt());
//        System.out.println(task.getModifiedAt());
//        System.out.println(task.getPassword());
//        System.out.println(task.isEncrypt());
//        System.out.println("-----------------------");
    }


    @Test
    public void getAllTasksTest() {
        appContext = InstrumentationRegistry.getTargetContext();
        noteRepository = new NoteRepository(appContext);

//        taskRepository.getTasks().observe(appContext, new Observer<List<Task>>() {
//            @Override
//            public void onChanged(@Nullable List<Task> tasks) {
//                for(Task task : tasks) {
//                    System.out.println("-----------------------");
//                    System.out.println(task.getId());
//                    System.out.println(task.getTitle());
//                    System.out.println(task.getDescription());
//                    System.out.println(task.getCreatedAt());
//                    System.out.println(task.getModifiedAt());
//                    System.out.println(task.getPassword());
//                    System.out.println(task.isEncrypt());
//                }
//            }
//        });
    }
}
