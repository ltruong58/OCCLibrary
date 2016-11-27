package edu.orangecoastcollege.cs273.occlibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created as a helper class to assist with database management using SQLite.
 */
class DBHelper extends SQLiteOpenHelper {

    //TASK 1: DEFINE THE DATABASE VERSION, NAME AND TABLE NAME
    static final String DATABASE_NAME = "OCCLibrary";
    private static final String DATABASE_BOOK_TABLE = "Books";
    private static final String DATABASE_ROOM_TABLE = "Rooms";
    private static final String DATABASE_ROOMBOOKING_TABLE = "RoomBooking";
    private static final String DATABASE_STUDENT_TABLE = "Student";
    private static final int DATABASE_VERSION = 1;


    //TASK 2: DEFINE THE FIELDS (COLUMN NAMES) FOR THE BOOK TABLE
    private static final String BOOK_KEY_FIELD_ID = "id";
    private static final String BOOK_FIELD_TITLE = "title";
    private static final String BOOK_FIELD_DESCRIPTION = "description";
    private static final String BOOK_FIELD_AUTHOR = "author";
    private static final String BOOK_FIELD_ISBN = "isbn";
    private static final String BOOK_FIELD_QTY_AVAIL = "quantity";
    private static final String BOOK_FIELD_IMAGE_URI = "image_uri";

    // DEFINE THE FIELDS (COLUMN NAMES) FOR THE ROOM TABLE
    private static final String ROOM_KEY_FIELD_ID = "id";
    private static final String ROOM_FIELD_NAME = "name";
    private static final String ROOM_FIELD_DESCRIPTION = "description"; // location and list of support devices
    private static final String ROOM_FIELD_CAPACITY = "capacity";

    // DEFINE THE FIELDS (COLUMN NAMES) FOR THE ROOM BOOKING TABLE
    private static final String ROOM_BOOKING_KEY_FIELD_ID = "room_booking_id";
    private static final String ROOM_BOOKING_FIELD_ROOM_ID = "room_id";
    private static final String ROOM_BOOKING_FIELD_STUDENT_ID = "student_id";
    private static final String ROOM_BOOKING_FIELD_DATE = "date"; // location and list of support devices
    private static final String ROOM_BOOKING_FIELD_START_TIME = "start_time";
    private static final String ROOM_BOOKING_FIELD_HOURS_USED = "hours_used";


    public DBHelper(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase database){
        // BOOK
        String table = "CREATE TABLE " + DATABASE_BOOK_TABLE + "("
                + BOOK_KEY_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BOOK_FIELD_TITLE + " TEXT, "
                + BOOK_FIELD_DESCRIPTION + " TEXT, "
                + BOOK_FIELD_AUTHOR + " TEXT, "
                + BOOK_FIELD_ISBN + " INTEGER, "
                + BOOK_FIELD_QTY_AVAIL + " INTEGER, "
                + BOOK_FIELD_IMAGE_URI + " TEXT" + ")";
        database.execSQL (table);

        // ROOM
        table = "CREATE TABLE " + DATABASE_ROOM_TABLE + "("
                + ROOM_KEY_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROOM_FIELD_NAME + " TEXT, "
                + ROOM_FIELD_DESCRIPTION + " TEXT, "
                + ROOM_FIELD_CAPACITY + " INTEGER" + ")";
        database.execSQL (table);

        //STUDENT

        //ROOM_BOOKING
        table = "CREATE TABLE " + DATABASE_ROOMBOOKING_TABLE_TABLE + "("
                + ROOM_BOOKING_KEY_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "FOREIGN KEY(" + ROOM_BOOKING_FIELD_ROOM_ID + ") REFERENCES "
                + DATABASE_ROOM_TABLE + "(" + ROOM_KEY_FIELD_ID + "),"
                + "FOREIGN KEY(" + ROOM_BOOKING_FIELD_STUDENT_ID + ") REFERENCES "
                + DATABASE_STUDENT_TABLE + "(" + STUDENT_KEY_FIELD_ID + "),"
                + ROOM_BOOKING_FIELD_DATE + " TEXT, "
                + ROOM_BOOKING_FIELD_START_TIME + " TEXT, "
                + ROOM_BOOKING_FIELD_HOURS_USED + " INTEGER" + ")";
        database.execSQL (table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_BOOK_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_ROOMBOOKING_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_ROOM_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_STUDENT_TABLE_TABLE);
        onCreate(database);
    }

    //********** DATABASE OPERATIONS:  ADD, GETALL, EDIT, DELETE

    public void addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //ADD KEY-VALUE PAIR INFORMATION FOR THE TABLE
        values.put(BOOK_FIELD_TITLE, book.getTitle());
        values.put(BOOK_FIELD_DESCRIPTION, book.getDescription());
        values.put(BOOK_FIELD_AUTHOR, book.getAuthor());
        values.put(BOOK_FIELD_ISBN, book.getISBN());
        values.put(BOOK_FIELD_QTY_AVAIL, book.getQty());
        values.put(BOOK_FIELD_IMAGE_URI, book.getImageUri().toString());

        // INSERT THE ROW IN THE TABLE
        db.insert(DATABASE_BOOK_TABLE, null, values);

        // CLOSE THE DATABASE CONNECTION
        db.close();
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> bookList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        //Cursor cursor = database.rawQuery(queryList, null);
        Cursor cursor = database.query(
                DATABASE_BOOK_TABLE,
                new String[]{BOOK_KEY_FIELD_ID, BOOK_FIELD_TITLE, BOOK_FIELD_DESCRIPTION, BOOK_FIELD_AUTHOR, BOOK_FIELD_ISBN, BOOK_FIELD_QTY_AVAIL, BOOK_FIELD_IMAGE_URI},
                null,
                null,
                null, null, null, null );

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.moveToFirst()){
            do {
                Book book =
                        new Book(cursor.getInt(0), //iD
                                cursor.getString(1),//title
                                cursor.getString(2),//desc
                                cursor.getString(3),//author
                                cursor.getInt(4),//isbn
                                cursor.getInt(5),//available
                                Uri.parse(cursor.getString(6))//imageUri
                        );
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        return bookList;
    }

    public void deleteBook(Book book){
        SQLiteDatabase db = this.getWritableDatabase();

        // DELETE THE TABLE ROW
        db.delete(DATABASE_BOOK_TABLE, BOOK_KEY_FIELD_ID + " = ?",
                new String[] {String.valueOf(book.getId())});
        db.close();
    }

    public void deleteAllBooks()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_BOOK_TABLE, null, null);
        db.close();
    }

    public void updateBook(Book book){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BOOK_FIELD_TITLE, book.getTitle());
        values.put(BOOK_FIELD_DESCRIPTION, book.getDescription());
        values.put(BOOK_FIELD_AUTHOR, book.getAuthor());
        values.put(BOOK_FIELD_ISBN, book.getISBN());
        values.put(BOOK_FIELD_QTY_AVAIL, book.getQty());
        values.put(BOOK_FIELD_IMAGE_URI, book.getImageUri().toString());

        db.update(DATABASE_BOOK_TABLE, values, KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(book.getId())});
        db.close();
    }

    public Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                DATABASE_BOOK_TABLE,
                new String[]{BOOK_KEY_FIELD_ID, BOOK_FIELD_TITLE, BOOK_FIELD_DESCRIPTION, BOOK_FIELD_AUTHOR, BOOK_FIELD_ISBN, BOOK_FIELD_QTY_AVAIL, BOOK_FIELD_IMAGE_URI},
                KEY_FIELD_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        Book book = new Book(
                cursor.getInt(0), //iD
                cursor.getString(1),//title
                cursor.getString(2),//desc
                cursor.getString(3),//author
                cursor.getInt(4),//isbn
                cursor.getInt(5),//available
                Uri.parse(cursor.getString(6))//imageUri
        );

        db.close();
        return book;
    }





}
