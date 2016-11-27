package com.example.android.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import com.example.android.inventoryapp.data.ProductContract.ProductEntry;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;

/**
 * Created by john on 20.11.2016.
 */

public class ProductProvider extends ContentProvider {

    //Tag for log messages
    public static final String LOG_TAG = ProductProvider.class.getSimpleName();

    //Database helper object
    private ProductDbHelper mDbHelper;

    //URI matcher code for the content URI for the products table
    private static final int PRODUCTS = 100;

    //URI mathcer code for the content URI for a single product in the products table
    private static final int PRODUCTS_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS, PRODUCTS);
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS + "/#", PRODUCTS_ID);
    }

    //Initialize the provider and the database helper object.
    @Override
    public boolean onCreate() {
        mDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    //Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Get the database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //The cursor which will hold the result of the query
        Cursor cursor = null;

        //Test if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match){
            case PRODUCTS:
                //For the products code, query the products table directly with the given projection, selection, selection arguments,
                //and sort order. The cursor could contain multiple rows of the products table.
                //This will perform database query on products table.
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCTS_ID:
                //For the PRODUCTS_ID code, extract out the ID from the URI. For an example URI such as
                //"content://com.example.android.inventoryapp/products/3", the selection will be "_id=?" and the selection argument will be
                //a String array containing the actual ID of 3 in this case.
                //For every "?" in the selection, is needed to have an element in the selection arguments that will fill in the "?".
                //Since there is 1 question mark in the selection, there is 1 String in the selection argument's String array.
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                //This will perform a query on the products table where the _id equals 3 to return a Cursor containing that row of the table.
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }

        //Set the notification URI on the Cursor,
        //so we know what content URI the Cursor was created for.
        //If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        //Return the cursor
        return cursor;
    }

    //Insert new data into the provider with the given ContentValues.
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //Check if the URI matches
        final int match = sUriMatcher.match(uri);
        switch (match){
            case PRODUCTS:
                return insertProduct(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for: " + uri);
        }
    }

    //Insert a product into the db with the given content values. Return the new content URI for that specific row in the db.
    private Uri insertProduct(Uri uri, ContentValues values){
        //Get writable databse
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Data validation
        //Check that name is not null
        String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if (name == null){
            throw new IllegalArgumentException("Product requires a name");
        }

        //Check that quantity is not null
        Integer quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity == null || quantity < 0){
            throw new IllegalArgumentException("Product requires a quantity");
        }

        //Check that price is not null
        Integer price = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
        if (price == null || price < 0){
            throw new IllegalArgumentException("Product requires a price");
        }

        //Check that image is not null
        byte[] picture = values.getAsByteArray(ProductEntry.COLUMN_PRODUCT_PICTURE);
        if (picture.length == 0){
            throw new IllegalArgumentException("Product requires an image");
        }

        //Insert a new product into products table with the given ContentValues
        long id = database.insert(ProductEntry.TABLE_NAME, null, values);

        //If the ID is -1, then th insertion has failed. Log error and return null
        if (id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //Notify all listeners that the data has been changed for the product content URI
        getContext().getContentResolver().notifyChange(uri, null);

        //Return the new URI with the ID appended at the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    //Updates the data at the given selection and selection arguments, with the new ContentValues.
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case PRODUCTS:
                return updateProduct(uri, values, selection, selectionArgs);
            case PRODUCTS_ID:
                //For the PRODUCT_ID code, extract out the ID from the URI in order to know which row to update. Selection wil be "_id=?"
                //and selectionArgs will be a String array containing the actual ID.
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                int result = updateProduct(uri, values, selection, selectionArgs);
                return result;
            default:
                throw new IllegalArgumentException("Update is not supported for: " + uri);
        }
    }

    //Update products in db with the given content values. Apply the changes to the rows specified in the selection and selectionArgs
    //(which could be 0, 1 or more products). Return the number of rows that were successfully updated.
    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        //Check that name is not null, if key name is present
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)){
            String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
            if (name == null){
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        //Check that the quantity is not null, if present
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY)){
            Integer quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity == null || quantity < 0){
                throw new IllegalArgumentException("Product requires quantity");
            }
        }

        //Check that the price is not null, if present
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE)){
            Integer price = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
            if (price == null || price < 0){
                throw new IllegalArgumentException("Product requires a price");
            }
        }

        //Check that picture is not null, if present
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_PICTURE)){
            byte[] picture = values.getAsByteArray(ProductEntry.COLUMN_PRODUCT_PICTURE);
            if (picture.length == 0){
                throw new IllegalArgumentException("Product requires an image");
            }
        }

        //If there are no values to update, then don't try to update the database
        if (values.size() == 0){
            return 0;
        }

        //Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Perform the update on the database and get he number of rows affected
        int rowsUpdated = database.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);

        //If 1 or more rows were updated, then notify all listeners that the data at given URI has changed
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    //Delete the data at the given selection and selection arguments.
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;

        //Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match){
            case PRODUCTS:
                //Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);

                //If 1 or more rows were deleted, then notify all listeners that the data at the given URI has changed
                if (rowsDeleted != 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                //Return the number of rows deleted
                return rowsDeleted;
            case PRODUCTS_ID:
                //Delete a single row given by the ID in the URI
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                //Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);

                //If 1 or more rows were deleted, then notify all listeners that the data at the given URI has changed
                if (rowsDeleted != 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                //Return the number of rows deleted
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for: " + uri);
        }
    }

    //Returns the MIME type of data for the content URI
    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case PRODUCTS:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCTS_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }
}
