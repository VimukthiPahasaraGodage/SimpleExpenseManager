package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.ACCOUNT_NUMBER_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.EXPENSE_TYPE_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.EXPENSE_TYPE_EXPENSE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.EXPENSE_TYPE_INCOME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.TRANSACTIONS_TABLE_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.TRANSACTION_AMOUNT_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.TRANSACTION_DATE_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.TRANSACTION_ID_COLUMN_NAME;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.SQLiteDatabaseHandler;

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
        if (expenseType == ExpenseType.EXPENSE) {
            contentValues.put(EXPENSE_TYPE_COLUMN_NAME, EXPENSE_TYPE_EXPENSE);
        } else {
            contentValues.put(EXPENSE_TYPE_COLUMN_NAME, EXPENSE_TYPE_INCOME);
        }
        contentValues.put(TRANSACTION_AMOUNT_COLUMN_NAME, amount);
        if (database.insert(TRANSACTIONS_TABLE_NAME, null, contentValues) != -1) {
            showAlertDialog("Successfully logged the transaction to account: " + accountNo);
        }
        database.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase database = sqLiteDatabaseHandler.getReadableDatabase();
        Cursor cursor = database.query(TRANSACTIONS_TABLE_NAME, new String[]{TRANSACTION_DATE_COLUMN_NAME, ACCOUNT_NUMBER_COLUMN_NAME, EXPENSE_TYPE_COLUMN_NAME, TRANSACTION_AMOUNT_COLUMN_NAME},
                null, null, null, null, TRANSACTION_ID_COLUMN_NAME + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ExpenseType expenseType;
                if (cursor.getInt(cursor.getColumnIndex(EXPENSE_TYPE_COLUMN_NAME)) == 0) {
                    expenseType = ExpenseType.EXPENSE;
                } else {
                    expenseType = ExpenseType.INCOME;
                }
                Transaction transaction = new Transaction(
                        new Date(cursor.getLong(cursor.getColumnIndex(TRANSACTION_DATE_COLUMN_NAME))),
                        cursor.getString(cursor.getColumnIndex(ACCOUNT_NUMBER_COLUMN_NAME)),
                        expenseType,
                        cursor.getDouble(cursor.getColumnIndex(TRANSACTION_AMOUNT_COLUMN_NAME)));
                transactions.add(transaction);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase database = sqLiteDatabaseHandler.getReadableDatabase();
        Cursor cursor = database.query(TRANSACTIONS_TABLE_NAME, new String[]{TRANSACTION_DATE_COLUMN_NAME, ACCOUNT_NUMBER_COLUMN_NAME, EXPENSE_TYPE_COLUMN_NAME, TRANSACTION_AMOUNT_COLUMN_NAME},
                null, null, null, null, TRANSACTION_ID_COLUMN_NAME + " DESC", String.valueOf(limit));
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ExpenseType expenseType;
                if (cursor.getInt(cursor.getColumnIndex(EXPENSE_TYPE_COLUMN_NAME)) == 0) {
                    expenseType = ExpenseType.EXPENSE;
                } else {
                    expenseType = ExpenseType.INCOME;
                }
                Transaction transaction = new Transaction(
                        new Date(cursor.getLong(cursor.getColumnIndex(TRANSACTION_DATE_COLUMN_NAME))),
                        cursor.getString(cursor.getColumnIndex(ACCOUNT_NUMBER_COLUMN_NAME)),
                        expenseType,
                        cursor.getDouble(cursor.getColumnIndex(TRANSACTION_AMOUNT_COLUMN_NAME)));
                transactions.add(transaction);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return transactions;
    }

    private void showAlertDialog(String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }
}
