package com.boatman.sync;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Created by shashikiranms on 14/03/18
 */

public class ContactAccessor {
    public static ArrayList<String> getContacts(Context context, String accountType) {
        String yourAccountType = accountType;//ex: "com.whatsapp"
        Cursor c = context.getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[] { ContactsContract.RawContacts.CONTACT_ID,
                        ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY,
                        ContactsContract.RawContacts.ACCOUNT_TYPE},
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[] { yourAccountType },
//                null, null,
                null);

        ArrayList<String> contactList = new ArrayList<String>();
        int contactNameColumn = c.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY);
        int accountTypeColumn = c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE);
        int idColumn = c.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID);
        while (c.moveToNext())
        {
            // You can also read RawContacts.CONTACT_ID to read the
            // ContactsContract.Contacts table or any of the other related ones.
            System.out.println("============");
            String temp = c.getString(contactNameColumn) + ":" +
                    mergeContact(context, c.getString(idColumn));
            contactList.add(temp);
        }
        c.close();
        return contactList;
    }

    public static String mergeContact(Context context, String contactId) {
        ContentResolver cr = context.getContentResolver();
        String number = "";
            //
            //  Get all phone numbers.
            //
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
        while (phones.moveToNext()) {
            number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            System.out.println("********" + number);
            break;
//            int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
//            switch (type) {
//                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
//                    // do something with the Home number here...
//                    break;
//                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
//                    // do something with the Mobile number here...
//                    break;
//                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
//                    // do something with the Work number here...
//                    break;
//            }
        }
        phones.close();
        return number;

    }
}
