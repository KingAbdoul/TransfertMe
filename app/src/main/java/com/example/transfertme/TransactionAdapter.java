package com.example.transfertme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TransactionAdapter extends BaseAdapter {

    private Context context;
    private List<HistoryTransaction> transactionList;

    public TransactionAdapter(Context context, List<HistoryTransaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @Override
    public int getCount() {
        return transactionList.size();
    }

    @Override
    public Object getItem(int position) {
        return transactionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_transaction, parent, false);

            holder = new ViewHolder();
            holder.transactionStatusTextView = convertView.findViewById(R.id.transactionStatusTextView);
            holder.transactionDateTextView = convertView.findViewById(R.id.transactionDateTextView);
            holder.transactionTimeTextView = convertView.findViewById(R.id.transactionTimeTextView);
            holder.senderNumberTextView = convertView.findViewById(R.id.senderNumberTextView);
            holder.receiverNumberTextView = convertView.findViewById(R.id.receiverNumberTextView);
            holder.amountTextView = convertView.findViewById(R.id.amountTextView);
            holder.feesTextView = convertView.findViewById(R.id.feesTextView);
            holder.recipientReceiveTextView = convertView.findViewById(R.id.recipientReceiveTextView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HistoryTransaction transaction = transactionList.get(position);

        holder.transactionStatusTextView.setText("Statut: " + (transaction.isTransactionStatus() ? "Succès" : "Échec"));
        holder.transactionDateTextView.setText("Date : " + transaction.getTransactionDate());
        holder.transactionTimeTextView.setText("Heure: " + transaction.getTransactionTime());
        holder.senderNumberTextView.setText("Numéro de l'expéditeur: " + transaction.getSenderNumber());
        holder.receiverNumberTextView.setText("Numéro du récepteur: " + transaction.getReceiverNumber());
        holder.amountTextView.setText("Montant: " + String.valueOf(transaction.getAmount()));
        holder.feesTextView.setText("Frais: " + String.valueOf(transaction.getFees()));
        holder.recipientReceiveTextView.setText("Le destinataire recevra: " + String.valueOf(transaction.getRecipientReceive()));

        return convertView;
    }

    private static class ViewHolder {
        TextView transactionStatusTextView;
        TextView transactionDateTextView;
        TextView transactionTimeTextView;
        TextView senderNumberTextView;
        TextView receiverNumberTextView;
        TextView amountTextView;
        TextView feesTextView;
        TextView recipientReceiveTextView;
    }
}
