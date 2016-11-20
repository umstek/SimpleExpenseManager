package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager {
    private Context context;
    SQLiteOpenHelper helper;

    public PersistentExpenseManager(Context context) {
        this.context = context;
        this.helper = new ExpenseOpenHelper(context);
        setup();
    }

    @Override
    public void setup() {
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(this.helper);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(this.helper);
        setAccountsDAO(persistentAccountDAO);

        // No need to load data. Context will resume it.
    }
}
