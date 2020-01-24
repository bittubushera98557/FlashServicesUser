package com.FlashScreen.flashservice.InnnerClass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.ContactAdapter;
import com.FlashScreen.flashservice.adapter.Contacts;

import java.util.ArrayList;

public class ContactUs extends AppCompatActivity {
    ProgressDialog progressDialog;
    Shaved_shared_preferences shaved_shared_preferences;
  ImageView iv_contactCall1,iv_contactCall2 ;
    TextView        tv_contactInfo1,tv_contactInfo2;

    String TAG="TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());
        iv_contactCall1=(ImageView) findViewById(R.id.contact1_call);
        iv_contactCall2=(ImageView)findViewById(R.id.contact2_call);
        tv_contactInfo1=(TextView)findViewById(R.id.contact1_info);
        tv_contactInfo2=(TextView)findViewById(R.id.contact2_info);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        // actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        progressDialog = new ProgressDialog(ContactUs.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait.....");
        iv_contactCall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = Const.adminPhone_First;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                 startActivity(intent);
            }
        });
        iv_contactCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = Const.adminPhone_Second;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                startActivity(intent);
            }
        });

        tv_contactInfo1.setText("Contact a"+ "\n" +Const.adminPhone_First);
        tv_contactInfo2.setText("Contact b"+ "\n" +Const.adminPhone_Second);

//        contact_info.setText(dataModel.getName() + "\n" + dataModel.getPhone());





       /* for (int i = 0; i < 2; i++) {
            Contacts contacts = new Contacts();


            String Id,name,phone_number;

            if(i==0) {
                Id = "" + i;
                name = "Contact a";
                phone_number = Const.adminPhone_First  ;

                contacts.setId(Id);
                contacts.setName(name);
                contacts.setPhone(phone_number);

                arrayList.add(contacts);

            }

            if(i==1) {
                Id = "" + i;
                name = "Contact b";
                phone_number = Const.adminPhone_Second;

                contacts.setId(Id);
                contacts.setName(name);
                contacts.setPhone(phone_number);

                arrayList.add(contacts);

            }
    }*/




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(ContactUs.this, NavigationScreen.class);
                startActivity(intent);
                finish();
              //  overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ContactUs.this, NavigationScreen.class);
        startActivity(intent);
        finish();
       // overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);

    }
}
