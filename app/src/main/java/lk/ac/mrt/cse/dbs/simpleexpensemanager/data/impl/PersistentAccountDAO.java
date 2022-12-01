package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.SQLiteDatabaseHandler;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.*;

public class PersistentAccountDAO implements AccountDAO {

    SQLiteDatabaseHandler sqLiteDatabaseHandler;

    public PersistentAccountDAO(SQLiteDatabaseHandler sqLiteDatabaseHandler) {
        this.sqLiteDatabaseHandler = sqLiteDatabaseHandler;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new ArrayList<>();
        SQLiteDatabase database = sqLiteDatabaseHandler.getReadableDatabase();
        Cursor cursor = database.query(ACCOUNTS_TABLE_NAME, new String[]{ACCOUNT_NUMBER_COLUMN_NAME},
                null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            do {
                accountNumbers.add(cursor.getString(cursor.getColumnIndex(ACCOUNT_NUMBER_COLUMN_NAME)));
            }while (cursor.moveToNext());
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
        if(cursor != null){
            cursor.moveToFirst();
            do {
                Account account = new Account(
                        cursor.getString(cursor.getColumnIndex(ACCOUNT_NUMBER_COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(BANK_NAME_COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(ACCOUNT_HOLDER_NAME_COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(BALANCE_COLUMN_NAME)));
                accounts.add(account);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return null;
    }

    @Override
    public void addAccount(Account account) {

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

    }
}
