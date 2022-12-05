package lk.ac.mrt.cse.dbs.simpleexpensemanager.db;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.ACCOUNTS_TABLE_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.ACCOUNT_HOLDER_NAME_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.ACCOUNT_NUMBER_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.BALANCE_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.BANK_NAME_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.DATABASE_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.DATABASE_VERSION;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.EXPENSE_TYPE_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.TRANSACTIONS_TABLE_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.TRANSACTION_AMOUNT_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.TRANSACTION_DATE_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.TRANSACTION_ID_COLUMN_NAME;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + ACCOUNTS_TABLE_NAME +
            " ( " + ACCOUNT_NUMBER_COLUMN_NAME + " TEXT PRIMARY KEY, " +
            BANK_NAME_COLUMN_NAME + " TEXT NOT NULL, " +
            ACCOUNT_HOLDER_NAME_COLUMN_NAME + " TEXT NOT NULL, " +
            BALANCE_COLUMN_NAME + " REAL NOT NULL );";

    private static final String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TRANSACTIONS_TABLE_NAME +
            " ( " + TRANSACTION_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ACCOUNT_NUMBER_COLUMN_NAME + " TEXT NOT NULL, " +
            TRANSACTION_DATE_COLUMN_NAME + " INTEGER NOT NULL, " +
            EXPENSE_TYPE_COLUMN_NAME + " INTEGER NOT NULL, " +
            TRANSACTION_AMOUNT_COLUMN_NAME + " REAL NOT NULL );";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNTS_TABLE);
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS_TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTIONS_TABLE_NAME + ";");
        onCreate(db);
    }
}
