package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private final SQLiteDatabase db;

    public PersistentTransactionDAO(SQLiteOpenHelper helper) {
        this.db = helper.getWritableDatabase();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String sql = "INSERT INTO ac_transaction (transaction_date,account_no,expense_type,amount) VALUES (?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindLong(1, date.getTime());
        statement.bindString(2, accountNo);
        statement.bindLong(3, (expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        statement.bindDouble(4, amount);

        statement.executeInsert();
        statement.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor results = db.rawQuery("SELECT * FROM ac_transaction", null);
        List<Transaction> transactions = new ArrayList<>();

        if (results.moveToFirst()) {
            do {
                Transaction transaction = new Transaction(
                        new Date(results.getLong(results.getColumnIndex("transaction_date"))),
                        results.getString(results.getColumnIndex("account_no")),
                        (results.getInt(results.getColumnIndex("expense_type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        results.getDouble(results.getColumnIndex("amount")));

                transactions.add(transaction);
            } while (results.moveToNext());
        }

        results.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor results = db.rawQuery("SELECT * FROM ac_transaction LIMIT " + limit, null);
        List<Transaction> transactions = new ArrayList<>();

        if (results.moveToFirst()) {
            do {
                Transaction transaction = new Transaction(
                        new Date(results.getLong(results.getColumnIndex("transaction_date"))),
                        results.getString(results.getColumnIndex("account_no")),
                        (results.getInt(results.getColumnIndex("expense_type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        results.getDouble(results.getColumnIndex("amount")));

                transactions.add(transaction);
            } while (results.moveToNext());
        }

        results.close();
        return transactions;
    }
}
