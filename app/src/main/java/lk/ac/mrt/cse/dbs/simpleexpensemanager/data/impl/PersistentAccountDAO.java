package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private final SQLiteDatabase db;

    public PersistentAccountDAO(SQLiteOpenHelper helper) {
        this.db = helper.getWritableDatabase();
    }

    @Override
    public List<String> getAccountNumbersList() {
        // use raw queries
        Cursor results = db.rawQuery("SELECT account_no FROM account", null);
        List<String> account_nos = new ArrayList<>();

        if (results.moveToFirst()) {
            do {
                account_nos.add(results.getString(results.getColumnIndex("account_no")));
            } while (results.moveToNext());
        }

        results.close();
        return account_nos;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor results = db.rawQuery("SELECT * FROM account", null);
        List<Account> accounts = new ArrayList<>();

        if (results.moveToFirst()) {
            do {
                Account account = new Account(
                        results.getString(results.getColumnIndex("account_no")),
                        results.getString(results.getColumnIndex("bank_name")),
                        results.getString(results.getColumnIndex("account_holder_name")),
                        results.getDouble(results.getColumnIndex("balance"))
                );

                accounts.add(account);
            } while (results.moveToNext());
        }

        results.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor results = db.rawQuery("SELECT * FROM account where account_no = " + accountNo, null);
        List<Account> accounts = new ArrayList<>();

        if (results.moveToFirst()) {
            do {
                Account account = new Account(
                        results.getString(results.getColumnIndex("account_no")),
                        results.getString(results.getColumnIndex("bank_name")),
                        results.getString(results.getColumnIndex("account_holder_name")),
                        results.getDouble(results.getColumnIndex("balance"))
                );

                accounts.add(account);
            } while (results.moveToNext());
        }

        results.close();
        // There have to be one or zero elements with the given account number because it is the
        // primary key.
        return accounts.size() > 0 ? accounts.get(0) : null;
    }

    @Override
    public void addAccount(Account account) {
        String sql = "INSERT INTO account (account_no,bank_name,account_holder_name,balance) VALUES (?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);

        // one based indices
        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());

        statement.executeInsert();
        statement.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sql = "DELETE FROM account WHERE account_no = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, accountNo);

        statement.executeUpdateDelete();
        statement.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String sql = "UPDATE account SET balance = balance + ?";
        SQLiteStatement statement = db.compileStatement(sql);

        if (expenseType == ExpenseType.EXPENSE) {
            statement.bindDouble(1, -amount);
        } else {
            statement.bindDouble(1, amount);
        }

        statement.executeUpdateDelete();
        statement.close();
    }
}
