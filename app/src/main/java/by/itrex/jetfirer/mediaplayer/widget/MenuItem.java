package by.itrex.jetfirer.mediaplayer.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.special.ResideMenu.ResideMenuItem;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.util.Utils;

/**
 * Created by Konstantin on 25.05.2015.
 */
public class MenuItem extends ResideMenuItem {
    private ImageView iv_icon;
    private TextView tv_title;

    public MenuItem(Context context) {
        super(context);
        this.initViews(context);
    }

    public MenuItem(Context context, int icon, int title) {
        super(context);
        this.initViews(context);
        this.iv_icon.setImageResource(icon);
        this.tv_title.setText(title);
    }

    public MenuItem(Context context, int icon, String title) {
        super(context);
        this.initViews(context);
        this.iv_icon.setImageResource(icon);
        this.tv_title.setText(title);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.menu_item, this);
        this.iv_icon = (ImageView)this.findViewById(com.special.ResideMenu.R.id.iv_icon);
        this.tv_title = (TextView)this.findViewById(com.special.ResideMenu.R.id.tv_title);

        tv_title.setTextColor(Utils.getSelectedColor(context));
    }

    public void setIcon(int icon) {
        this.iv_icon.setImageResource(icon);
    }

    public void setTitle(int title) {
        this.tv_title.setText(title);
    }

    public void setTitle(String title) {
        this.tv_title.setText(title);
    }

    public void setTextColor(int color) {
        tv_title.setTextColor(color);
    }
}
