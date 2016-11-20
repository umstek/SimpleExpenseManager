package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExpenseOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "140004E";
    private static final int DATABASE_VERSION = 1;
    private static final String ACCOUNT_TABLE_NAME = "account";
    private static final String ACCOUNT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE_NAME +
            "(" +
            "account_no TEXT PRIMARY KEY, " +
            "bank_name TEXT, " +
            "account_holder_name TEXT, " +
            "balance DECIMAL(12,2)" +
            ");";
    private static final String TRANSACTION_TABLE_NAME = "ac_transaction";
    private static final String TRANSACTION_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE_NAME +
            "(" +
            "id INT PRIMARY KEY, " +
            "transaction_date DATE, " +
            "account_no TEXT, " +
            "expense_type INT, " +
            "amount DECIMAL(12,2), " +
            "FOREIGN KEY (account_no) REFERENCES " + ACCOUNT_TABLE_NAME + "(account_no)" +
            ");";

    public ExpenseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ACCOUNT_TABLE_CREATE);
        db.execSQL(TRANSACTION_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
