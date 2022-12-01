package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.SQLiteDatabaseHandler;

public class PersistentExpenseManager extends ExpenseManager{

    SQLiteDatabaseHandler sqLiteDatabaseHandler;

    public PersistentExpenseManager(SQLiteDatabaseHandler sqLiteDatabaseHandler) {
        this.sqLiteDatabaseHandler = sqLiteDatabaseHandler;
    }

    @Override
    public void setup() throws ExpenseManagerException {
        AccountDAO persistentAccountDAO = new PersistentAccountDAO(sqLiteDatabaseHandler);
        setAccountsDAO(persistentAccountDAO);

        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO();
        setTransactionsDAO(persistentTransactionDAO);
    }
}
