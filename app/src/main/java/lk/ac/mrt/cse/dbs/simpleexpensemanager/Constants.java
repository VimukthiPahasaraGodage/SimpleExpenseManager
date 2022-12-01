/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

/**
 *
 */
public class Constants {
    public static final String EXPENSE_MANAGER = "expense-manager";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "simple_expense_manager_database";

    public static final String ACCOUNTS_TABLE_NAME = "accounts";
    public static final String TRANSACTIONS_TABLE_NAME = "transactions";

    public static final String ACCOUNT_NUMBER_COLUMN_NAME = "account_number";
    public static final String BANK_NAME_COLUMN_NAME = "bank_name";
    public static final String ACCOUNT_HOLDER_NAME_COLUMN_NAME = "account_holder_name";
    public static final String BALANCE_COLUMN_NAME = "balance";
    public static final String TRANSACTION_ID_COLUMN_NAME = "transaction_id";
    public static final String TRANSACTION_DATE_COLUMN_NAME = "transaction_date";
    public static final String EXPENSE_TYPE_COLUMN_NAME = "expense_type";
    public static final String TRANSACTION_AMOUNT_COLUMN_NAME = "transaction_amount";
}
