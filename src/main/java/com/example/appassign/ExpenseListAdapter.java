package com.example.appassign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ExpenseListAdapter extends BaseAdapter {
    private Context context;
    private List<Expense> expenseList;

    public ExpenseListAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @Override
    public int getCount() {
        return expenseList.size();
    }

    @Override
    public Object getItem(int position) {
        return expenseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_expense, null);
        }
        TextView txtExpenseID = convertView.findViewById(R.id.expenseID);
        TextView txtExpenseName = convertView.findViewById(R.id.expenseNameText);
        TextView txtExpenseAmount = convertView.findViewById(R.id.expenseAmountText);

        Expense expense = expenseList.get(position);

        txtExpenseID.setText(String.valueOf(expense.getId()));
        txtExpenseName.setText(expense.getName());
        txtExpenseAmount.setText(String.valueOf(expense.getAmount()));

        return convertView;
    }
}
