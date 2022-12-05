package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.ACCOUNTS_TABLE_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.ACCOUNT_HOLDER_NAME_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.ACCOUNT_NUMBER_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.BALANCE_COLUMN_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.BANK_NAME_COLUMN_NAME;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.SQLiteDatabaseHandler;

public class PersistentAccountDAO implements AccountDAO {

    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Context context;

    public PersistentAccountDAO(Context context, SQLiteDatabaseHandler sqLiteDatabaseHandler) {
        this.sqLiteDatabaseHandler = sqLiteDatabaseHandler;
        this.context = context;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new ArrayList<>();
        SQLiteDatabase database = sqLiteDatabaseHandler.getReadableDatabase();
        Cursor cursor = database.query(ACCOUNTS_TABLE_NAME, new String[]{ACCOUNT_NUMBER_COLUMN_NAME},
                null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                accountNumbers.add(cursor.getString(cursor.getColumnIndex(ACCOUNT_NUMBER_COLUMN_NAME)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<>();
        SQLiteDatabase database = sqLiteDatabaseHandler.getReadableDatabase();
        Cursor cursor = database.query(ACCOUNTS_TABLE_NAME,
                new String[]{ACCOUNT_NUMBER_COLUMN_NAME, BANK_NAME_COLUMN_NAME, ACCOUNT_HOLDER_NAME_COLUMN_NAME, BANK_NAME_COLUMN_NAME},
                null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getString(cursor.getColumnIndex(ACCOUNT_NUMBER_COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(BANK_NAME_COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(ACCOUNT_HOLDER_NAME_COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(BALANCE_COLUMN_NAME)));
                accounts.add(account);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase database = sqLiteDatabaseHandler.getReadableDatabase();
        Cursor cursor = database.query(ACCOUNTS_TABLE_NAME,
                new String[]{BANK_NAME_COLUMN_NAME, ACCOUNT_HOLDER_NAME_COLUMN_NAME, BALANCE_COLUMN_NAME},
                ACCOUNT_NUMBER_COLUMN_NAME + " = ?", new String[]{accountNo}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Account account = new Account(accountNo,
                    cursor.getString(cursor.getColumnIndex(BALANCE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(ACCOUNT_HOLDER_NAME_COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(BALANCE_COLUMN_NAME)));
            cursor.close();
            return account;
        } else {
            throw new InvalidAccountException("There is no account with the account number: " + accountNo);
        }
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase database = sqLiteDatabaseHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_NUMBER_COLUMN_NAME, account.getAccountNo());
        contentValues.put(BANK_NAME_COLUMN_NAME, account.getBankName());
        contentValues.put(ACCOUNT_HOLDER_NAME_COLUMN_NAME, account.getAccountHolderName());
        contentValues.put(BALANCE_COLUMN_NAME, account.getBalance());
        if (database.insert(ACCOUNTS_TABLE_NAME, null, contentValues) != -1) {
            showAlertDialog("Successfully added account: " + account.getAccountNo());
        } else {
            showAlertDialog("Error while adding account: " + account.getAccountNo());
        }
        database.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase database = sqLiteDatabaseHandler.getWritableDatabase();
        if (database.delete(ACCOUNTS_TABLE_NAME, ACCOUNT_NUMBER_COLUMN_NAME + " = ?", new String[]{accountNo}) == 0) {
            throw new InvalidAccountException("There is no account with the account number: " + accountNo);
        } else {
            showAlertDialog("Successfully removed the account: " + accountNo);
        }
        database.close();
    }

    @Override
    public boolean updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase database = sqLiteDatabaseHandler.getWritableDatabase();
        Account account = getAccount(accountNo);
        double balance = account.getBalance();
        if (expenseType == ExpenseType.EXPENSE) {
            balance -= amount;
            if (balance < 0) {
                showAlertDialog("Insufficient balance in account: " + accountNo);
                return false;
            }
        } else {
            balance += amount;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(BALANCE_COLUMN_NAME, balance);
        int rowsAffected = database.update(ACCOUNTS_TABLE_NAME, contentValues, ACCOUNT_NUMBER_COLUMN_NAME + " = ?", new String[]{accountNo});
        database.close();
        return rowsAffected >= 1;
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
