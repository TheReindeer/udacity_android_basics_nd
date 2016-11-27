package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract;

/**
 * Created by john on 20.11.2016.
 */

public class ProductCursorAdapter extends CursorAdapter {

    private TextView tvQuantity;

    //Constructor
    public ProductCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(final View view, Context context, final Cursor cursor) {
        //Find the fields to be updated
        TextView tvName = (TextView) view.findViewById(R.id.name);
        tvQuantity = (TextView) view.findViewById(R.id.quantity);
        TextView tvPrice = (TextView) view.findViewById(R.id.price);

        //Extract the properties name, quantity and price from the Cursor
        String name = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
        int quantity = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
        int price = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE));

        Button btnSale = (Button) view.findViewById(R.id.btnMainSale);
        btnSale.setTag(cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry._ID)));
        btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current position from getView() method
                int cursorPosition = (int)view.getTag();
                cursor.moveToPosition(cursorPosition);

                //Decrease quantity by one and update TextView
                int currentValue = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
                if (currentValue < 1){
                    return;
                }
                int finalValue = currentValue - 1;
                tvQuantity.setText(Integer.toString(finalValue));
                String quantityString = String.valueOf(finalValue).trim();

                //Get id of position
                int position = (int) v.getTag();

                //Create the URI for the column quantity to be updated
                Uri currentContentUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, position);

                //Save the new value into database
                saveProduct(v.getContext(), quantityString, position, currentContentUri);
            }
        });

        //Update the views
        tvName.setText(name);
        tvQuantity.setText(Integer.toString(quantity));
        tvPrice.setText(Integer.toString(price) + view.getResources().getString(R.string.price_append_dollars));
    }

    //Override method in order to set current position that will be retrieved in bindView()
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }
        convertView.setTag(position);
        return super.getView(position, convertView, parent);
    }

    private void saveProduct(Context context, String quantity, int position, Uri mCurrentProductUri){
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);

        String whereClause = ProductContract.ProductEntry._ID + "=" + position;

        int rowsAffected = context.getContentResolver().update(mCurrentProductUri, values, whereClause, null);

        // Show a toast message depending on whether or not the update was successful.
        if (rowsAffected == 0){
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(context, context.getString(R.string.toast_update_error), Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(context, context.getString(R.string.toast_update), Toast.LENGTH_SHORT).show();
        }
    }
}
