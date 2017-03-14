package com.example.svatts.my22;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by svatts on 23-Feb-17.
 */

public class Utilities {

    public static void setIcons(String transDesc, ImageView imageView) {
        if (transDesc.contains("BOB E-Banking")) {
            Glide.with(getApplicationContext())
                    .load("http://www.govtjobsofindia.com/wp-content/uploads/2016/12/Bank-of-Baroda-Recruitment.jpg")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("crownit")) {
            // holder.imageView.setImageResource(R.drawable.bob);
            Glide.with(getApplicationContext())
                    .load("http://www.hiva26.com/wp-content/uploads/2016/06/crownit-app-logo-hiva26.png")
                    .asBitmap()
                    .into(imageView);
        } else if (transDesc.toLowerCase().contains("paytm") || transDesc.toLowerCase().contains("one97")) {
            Glide.with(getApplicationContext())
                    .load("http://www.irctcstationcode.com/wp-content/uploads/2015/11/Paytm-Wallet.jpg")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("google")) {
            Glide.with(getApplicationContext())
                    .load("https://4.bp.blogspot.com/-DV3oSkBGKVw/VWdWwTZ0DSI/AAAAAAAABrs/KizNkOcUhGw/s1600/Google%2BPlay%2Blogo.png")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("social")) {
            Glide.with(getApplicationContext())
                    .load("http://awards.kyoorius.com/2015/design/wp-content/uploads/2015/08/26691.jpg")
                    .asBitmap()
                    .into(imageView);
        } else if (transDesc.toLowerCase().contains("swiggy")) {
            Glide.with(getApplicationContext())
                    .load("https://2q72xc49mze8bkcog2f01nlh-wpengine.netdna-ssl.com/pune/wp-content/uploads/sites/6/2015/09/swiggy-logo.png")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("ganesh shetty") || transDesc.toLowerCase().contains("dinner")) {
            Glide.with(getApplicationContext())
                    .load("http://www.freeiconspng.com/uploads/restaurant-menu-icon-png-28.png")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("zomato")) {
            Glide.with(getApplicationContext())
                    .load("http://transcend.sibmpune.edu.in/images/sponsor/Zomato.png")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("pizza hut") || transDesc.toLowerCase().contains("pizzahut")) {
            Glide.with(getApplicationContext())
                    .load("http://vignette3.wikia.nocookie.net/disney/images/4/4e/2000px-Pizza_Hut_logo.png/revision/latest?cb=20140531215537")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("beer")) {
            Glide.with(getApplicationContext())
                    .load("http://www.powersdistributing.com/sites/default/files/Heineken_1.png")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("naturals haircut")) {
            Glide.with(getApplicationContext())
                    .load("https://floatingpointlogoimages.s3.amazonaws.com/actual/nat.png.jpg")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("imps")) {
            Glide.with(getApplicationContext())
                    .load("https://upload.wikimedia.org/wikipedia/commons/thumb/7/71/IMPS_new_logo.jpg/1200px-IMPS_new_logo.jpg")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("atm")) {
            Glide.with(getApplicationContext())
                    .load("http://www.realsec.com/en/wp-content/themes/base/includes/timthumb/timthumb.php?src=http://www.realsec.com/en/wp-content/uploads/2014/11/ATM-sign.png&w=800&h=600&zc=2")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("salary")) {
            Glide.with(getApplicationContext())
                    .load("http://pngimg.com/uploads/money/money_PNG3545.png")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("dth")) {
            Glide.with(getApplicationContext())
                    .load("https://3.imimg.com/data3/KW/VR/MY-10887585/airtel-dth-recharge-500x500.png")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("box8")) {
            Glide.with(getApplicationContext())
                    .load("https://upload.wikimedia.org/wikipedia/en/9/96/Box8_company_logo.png")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("myntra")) {
            Glide.with(getApplicationContext())
                    .load("http://s3.india.com/wp-content/uploads/2015/07/myntra.png")
                    .asBitmap()
                    .into(imageView);

        } else if (transDesc.toLowerCase().contains("freecharge")) {
            Glide.with(getApplicationContext())
                    .load("https://blog.freecharge.in/wp-content/uploads/2016/11/fc-Blog-Circle-logo.png")
                    .asBitmap()
                    .into(imageView);

        }
        else {
            imageView.setImageResource(R.drawable.com_facebook_button_send_icon_blue);
        }

    }
}
