package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //Edit text field to enter the product's name;
    private EditText mNameEditText;

    //Edit text field to enter product's quantity
    private EditText mQuantityEditText;

    //Edit text field to enter product's price
    private EditText mPriceEditText;

    //Button chooser for picture from local storage
    private Button mButtonImageChooser;

    //Image view to display the picture of the product;
    private ImageView mImageView;

    //Increase/Decrease views
    private Button mBtnIncrease;
    private Button mBtnDecrease;
    private EditText mEtIncrease;
    private EditText mEtDecrease;
    
    //Sale -1, Receive +1, Order mail, EditText receive shipment quantity
    private Button mBtnSale;
    private Button mBtnReceive;
    private Button mBtnOrder;
    private EditText mEtReceiveQuantity;

    //Constant for picture picker
    private static final int REQUEST_PICKER = 2;

    private static final int EXISTING_PRODUCT_LOADER = 1;

    //Content URI for the existing product;
    private Uri mCurrentProductUri;

    //Boolean flag that keeps track of whether the product has been edited (true) or not (false)
    private boolean mProductHasChanged = false;

    //OnTouchListener that listens for any user touches on a View, implying that they are modifying
    //the view, and we change the mProductHasChanged boolean to true.
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Use getIntent() and getData() to get the associated URI
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        //If the intent DOES NOT contain a product content URI, then a new product is created
        if (mCurrentProductUri == null){
            //This is a new product, so change the AppBar to say "Add a Product"
            setTitle(getString(R.string.editor_activity_title_new_product));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a product that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            //Otherwise is an exiting product, so change the AppBar to say "Edit Product"
            setTitle(getString(R.string.editor_activity_title_edit_product));

            // Initialize a loader to read the product data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        //Find all relevant views that are needed to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mQuantityEditText = (EditText) findViewById(R.id.edit_product_quantity);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mImageView = (ImageView) findViewById(R.id.edit_product_image);
        mEtIncrease = (EditText) findViewById(R.id.edit_product_quantity_increase);
        mEtDecrease = (EditText) findViewById(R.id.edit_product_quantity_decrease);
        mBtnIncrease = (Button) findViewById(R.id.btnIncreaseQuantity);
        mBtnDecrease = (Button) findViewById(R.id.btnDecreaseQuantity);
        mBtnSale = (Button) findViewById(R.id.btnSale);
        mBtnReceive = (Button) findViewById(R.id.btnReceiveShipment);
        mEtReceiveQuantity = (EditText) findViewById(R.id.edit_product_quantity_receive);
        mBtnOrder = (Button) findViewById(R.id.btnOrder);

        // Setup OnTouchListeners on all the input fields, in order to determine if the user
        // has touched or modified them. This will let programmer know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mImageView.setOnTouchListener(mTouchListener);
        mEtIncrease.setOnTouchListener(mTouchListener);
        mEtDecrease.setOnTouchListener(mTouchListener);
        mBtnIncrease.setOnTouchListener(mTouchListener);
        mBtnDecrease.setOnTouchListener(mTouchListener);
        mBtnSale.setOnTouchListener(mTouchListener);
        mBtnReceive.setOnTouchListener(mTouchListener);
        mEtReceiveQuantity.setOnTouchListener(mTouchListener);
        mBtnOrder.setOnTouchListener(mTouchListener);

        //Get the chooser button for the image from the local storage
        mButtonImageChooser = (Button) findViewById(R.id.btnGetPicture);
        mButtonImageChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invokePicker();
            }
        });

        mBtnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEtIncrease.requestFocus();
                mEtIncrease.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_DONE){
                            int increasingQ = Integer.valueOf(mEtIncrease.getText().toString());
                            if (increasingQ < 0){
                                Toast.makeText(DetailsActivity.this, "Number must be positive", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                            int initialQ = Integer.valueOf(mQuantityEditText.getText().toString());
                            int finalQ = increasingQ + initialQ;
                            mQuantityEditText.setText(Integer.toString(finalQ));
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        mBtnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEtDecrease.requestFocus();
                mEtDecrease.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_DONE){
                            int decreasingQ = Integer.valueOf(mEtDecrease.getText().toString());
                            if (decreasingQ < 0){
                                Toast.makeText(DetailsActivity.this, "Number must be positive", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                            int initialQ = Integer.valueOf(mQuantityEditText.getText().toString());
                            int finalQ = initialQ - decreasingQ;
                            if (finalQ < 0){
                                Toast.makeText(DetailsActivity.this, "Negative quantity not allowed", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                            mQuantityEditText.setText(Integer.toString(finalQ));
                        }
                        return false;
                    }
                });
            }
        });
        
        mBtnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int initialValueSale = Integer.valueOf(mQuantityEditText.getText().toString());
                int finalValueSale = initialValueSale - 1;
                if (finalValueSale > 0){
                    mQuantityEditText.setText(Integer.toString(finalValueSale));
                } else {
                    Toast.makeText(DetailsActivity.this, "Empty stock", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEtReceiveQuantity.requestFocus();
                mEtReceiveQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_DONE){
                            int increasingQ = Integer.valueOf(mEtReceiveQuantity.getText().toString());
                            if (increasingQ < 0){
                                Toast.makeText(DetailsActivity.this, "Number must be positive", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                            int initialQ = Integer.valueOf(mQuantityEditText.getText().toString());
                            int finalQ = increasingQ + initialQ;
                            mQuantityEditText.setText(Integer.toString(finalQ));
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Order product");
                intent.putExtra(Intent.EXTRA_TEXT, mNameEditText.getText().toString());
                intent.setData(Uri.parse("mailto:"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(DetailsActivity.this, "Invalid data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new product, hide unnecessary xml components.
        if (mCurrentProductUri == null){
            //Hide "Delete" menu item
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);

            //Hide "Increase Quantity" button
            Button btnIncrease = (Button) findViewById(R.id.btnIncreaseQuantity);
            btnIncrease.setVisibility(View.GONE);

            //Hide "Decrease Quantity" button
            Button btnDecrease = (Button) findViewById(R.id.btnDecreaseQuantity);
            btnDecrease.setVisibility(View.GONE);

            //Hide "Sale" button
            Button btnSale = (Button) findViewById(R.id.btnSale);
            btnSale.setVisibility(View.GONE);

            //Hide "Receive" button
            Button btnReceive = (Button) findViewById(R.id.btnReceiveShipment);
            btnReceive.setVisibility(View.GONE);

            //Hide "Order" button
            Button btnOrder = (Button) findViewById(R.id.btnOrder);
            btnOrder.setVisibility(View.GONE);

            //Hide edit text for increasing/decreasing category
            EditText etIncrease = (EditText) findViewById(R.id.edit_product_quantity_increase);
            etIncrease.setVisibility(View.GONE);

            EditText etDecrease = (EditText) findViewById(R.id.edit_product_quantity_decrease);
            etDecrease.setVisibility(View.GONE);

            EditText etReceive = (EditText) findViewById(R.id.edit_product_quantity_receive);
            etReceive.setVisibility(View.GONE);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_details.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // If the product hasn't changed, continue with handling back button press
        if (!mProductHasChanged){
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // User clicked "Discard" button, close the current activity.
                    finish();
                }
        };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()){
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //Save product to database
                saveProduct();
                //Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the product hasn't changed, continue with navigating up to parent activity
                if (!mProductHasChanged){
                    NavUtils.navigateUpFromSameTask(DetailsActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(DetailsActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all product attributes, define a projection that contains
        // all columns from the product table
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_PICTURE
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this, mCurrentProductUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()){
            // Find the columns of product attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int pictureColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PICTURE);

            //Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            Bitmap imageBitmap = getImage(pictureColumnIndex, cursor);

            //Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mQuantityEditText.setText(Integer.toString(quantity));
            mPriceEditText.setText(Integer.toString(price));
            mImageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mQuantityEditText.setText("");
        mPriceEditText.setText("");
        mImageView.setImageBitmap(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check if the picture was retrieved successfully and if not return null
        if (resultCode != RESULT_OK){
            Log.e("DetailsActivity", "Error while retrieving picture");
            return;
        }

        //Otherwise set image on image view
        if (requestCode == REQUEST_PICKER){
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                mImageView = (ImageView) findViewById(R.id.edit_product_image);
                mImageView.setImageBitmap(bitmap);
            } catch (IOException e){
                Log.e("DetailsActivity", "IOException while retrieving image");
            }
        }
    }

    private void saveProduct(){
        String nameString = mNameEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        Bitmap imageBitmap;
        byte[] imageData;
        try {
            imageBitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            imageData = getBitmapAsByteArray(imageBitmap);
        } catch(NullPointerException e){
            Toast.makeText(this, "All fieldsare required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(nameString) ||
                TextUtils.isEmpty(quantityString) ||
                TextUtils.isEmpty(priceString) ||
                imageData.length == 0){
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantityString);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, priceString);
        values.put(ProductEntry.COLUMN_PRODUCT_PICTURE, imageData);

        // Current uri is null then a new product is added
        if (mCurrentProductUri == null){
            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
            
            if (newUri != null){
                Toast.makeText(this, getString(R.string.toast_insert), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.toast_insert_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING product, so update the product with content URI: mCurrentProductUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentProductUri will already identify the correct row in the database that
            // will be modified.
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0){
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.toast_update_error), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.toast_update), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Perform the deletion of the product in the database.
    private void deleteProduct(){
        // Only perform the delete if this is an existing product.
        if (mCurrentProductUri != null){
            // Call the ContentResolver to delete the product at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the product needed.
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0){
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_product_failed), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_product_successful), Toast.LENGTH_SHORT).show();
            }

            finish();
        }
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener){
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int id){
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the product.
                if (dialogInterface != null){
                    dialogInterface.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog(){
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int id){
                // User clicked the "Delete" button, so delete the pet.
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private Bitmap getImage(int id, Cursor cursor){
        byte[] imageByte = cursor.getBlob(id);
        return BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
    }

    private void invokePicker(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), REQUEST_PICKER);
    }
}
