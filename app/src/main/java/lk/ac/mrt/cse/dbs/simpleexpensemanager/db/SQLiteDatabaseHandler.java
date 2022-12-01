package lk.ac.mrt.cse.dbs.simpleexpensemanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "simple_expense_manager_database";

    private static final String ACCOUNTS_TABLE_NAME = "accounts";
    private static final String TRANSACTIONS_TABLE_NAME = "transactions";

    private static final String ACCOUNT_NUMBER_COLUMN_NAME = "account_number";
    private static final String BANK_NAME_COLUMN_NAME = "bank_name";
    private static final String ACCOUNT_HOLDER_NAME_COLUMN_NAME = "account_holder_name";
    private static final String BALANCE_COLUMN_NAME = "balance";
    private static final String TRANSACTION_ID_COLUMN_NAME = "transaction_id";
    private static final String TRANSACTION_DATE_COLUMN_NAME = "transaction_date";
    private static final String EXPENSE_TYPE_COLUMN_NAME = "expense_type";
    private static final String TRANSACTION_AMOUNT_COLUMN_NAME = "transaction_amount";

    private static final String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + ACCOUNTS_TABLE_NAME +
            " ( " + ACCOUNT_NUMBER_COLUMN_NAME + " INTEGER PRIMARY KEY, " +
            BANK_NAME_COLUMN_NAME + " TEXT NOT NULL, " +
            ACCOUNT_HOLDER_NAME_COLUMN_NAME + " TEXT NOT NULL, " +
            BALANCE_COLUMN_NAME + " REAL NOT NULL );";

    private static final String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TRANSACTIONS_TABLE_NAME +
            " ( " + TRANSACTION_ID_COLUMN_NAME + " INTEGER PRIMARY KEY, " +
            ACCOUNT_NUMBER_COLUMN_NAME + " INTEGER NOT NULL, " +
            TRANSACTION_DATE_COLUMN_NAME + " TEXT NOT NULL, " +
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
