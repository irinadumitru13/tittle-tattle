package com.example.tittle_tattle.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tittle_tattle.data.DAOs.MessageDAO;
import com.example.tittle_tattle.data.DAOs.SubscriptionDAO;
import com.example.tittle_tattle.data.DAOs.UserDAO;
import com.example.tittle_tattle.data.models.Message;
import com.example.tittle_tattle.data.models.Subscription;
import com.example.tittle_tattle.data.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Subscription.class, Message.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String db_name = "tittle_tattle";
    private static final int NUMBER_OF_THREADS = 4;

    private static AppDatabase database;

    public abstract UserDAO userDAO();

    public abstract SubscriptionDAO subscriptionDAO();

    public abstract MessageDAO messageDAO();

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized AppDatabase getInstance(Context context) {
        if (database == null) {
            database = create(context);
        }

        return database;
    }

    protected AppDatabase() {
    }

    @NotNull
    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, db_name).build();
    }

    public User findUserById(String user_id) {
         return database.userDAO().findUserById(user_id);
    }

    public void insertUser(User user) {
        database.userDAO().insert(user);
    }

    public void subscribe(Subscription subscription) {
        database.subscriptionDAO().insert(subscription);
    }

    public void unsubscribe(int subscription_id, String user_id) {
        database.subscriptionDAO().delete(subscription_id, user_id);
    }

    public void publishMessage(Message message) {
        database.messageDAO().insert(message);
    }
}
