package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.SQLiteDatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.*;

public class PersistentTransactionDAO implements TransactionDAO {

    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Context context;
    public PersistentTransactionDAO(Context context, SQLiteDatabaseHandler sqLiteDatabaseHandler) {
        this.sqLiteDatabaseHandler = sqLiteDatabaseHandler;
        this.context = context;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase database = sqLiteDatabaseHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTION_DATE_COLUMN_NAME, date.getTime());
        contentValues.put(ACCOUNT_NUMBER_COLUMN_NAME, accountNo);
        if(expenseType == ExpenseType.EXPENSE){
            contentValues.put(EXPENSE_TYPE_COLUMN_NAME, EXPENSE_TYPE_EXPENSE);
        }else{
            contentValues.put(EXPENSE_TYPE_COLUMN_NAME, EXPENSE_TYPE_INCOME);
        }
        contentValues.put(TRANSACTION_AMOUNT_COLUMN_NAME, amount);
        if(database.insert(TRANSACTIONS_TABLE_NAME, null, contentValues) != -1){
            Toast.makeText(context, "Successfully inserted the transaction associated with account: " + accountNo, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "An error while inserting the transaction associated with account: " + accountNo, Toast.LENGTH_SHORT).show();
        }
        database.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase database = sqLiteDatabaseHandler.getReadableDatabase();
        Cursor cursor = database.query(TRANSACTIONS_TABLE_NAME, new String[]{TRANSACTION_DATE_COLUMN_NAME, ACCOUNT_NUMBER_COLUMN_NAME, EXPENSE_TYPE_COLUMN_NAME, TRANSACTION_AMOUNT_COLUMN_NAME},
                null, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()){
            do{
                ExpenseType expenseType;
                if(cursor.getInt(cursor.getColumnIndex(EXPENSE_TYPE_COLUMN_NAME)) == 0){
                    expenseType = ExpenseType.EXPENSE;
                }else {
                    expenseType = ExpenseType.INCOME;
                }
                Transaction transaction = new Transaction(
                        new Date(cursor.getLong(cursor.getColumnIndex(TRANSACTION_DATE_COLUMN_NAME))),
                        cursor.getString(cursor.getColumnIndex(ACCOUNT_NUMBER_COLUMN_NAME)),
                        expenseType,
                        cursor.getDouble(cursor.getColumnIndex(TRANSACTION_AMOUNT_COLUMN_NAME)));
                transactions.add(transaction);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase database = sqLiteDatabaseHandler.getReadableDatabase();
        Cursor cursor = database.query(TRANSACTIONS_TABLE_NAME, new String[]{TRANSACTION_DATE_COLUMN_NAME, ACCOUNT_NUMBER_COLUMN_NAME, EXPENSE_TYPE_COLUMN_NAME, TRANSACTION_AMOUNT_COLUMN_NAME},
                null, null, null, null, null, String.valueOf(limit));
        if(cursor != null && cursor.moveToFirst()){
            do{
                ExpenseType expenseType;
                if(cursor.getInt(cursor.getColumnIndex(EXPENSE_TYPE_COLUMN_NAME)) == 0){
                    expenseType = ExpenseType.EXPENSE;
                }else {
                    expenseType = ExpenseType.INCOME;
                }
                String x = cursor.getString(cursor.getColumnIndex(TRANSACTION_DATE_COLUMN_NAME));
                Transaction transaction = new Transaction(
                        new Date(cursor.getLong(cursor.getColumnIndex(TRANSACTION_DATE_COLUMN_NAME))),
                        cursor.getString(cursor.getColumnIndex(ACCOUNT_NUMBER_COLUMN_NAME)),
                        expenseType,
                        cursor.getDouble(cursor.getColumnIndex(TRANSACTION_AMOUNT_COLUMN_NAME)));
                transactions.add(transaction);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return transactions;
    }
}
