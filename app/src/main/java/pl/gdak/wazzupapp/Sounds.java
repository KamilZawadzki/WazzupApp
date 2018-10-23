package pl.gdak.wazzupapp;

import android.text.Layout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

class Sounds {
    LinearLayout eachHorizontal;

    public Sounds(LinearLayout eachHorizontal, ImageButton addToFavBtn) {
        this.eachHorizontal = eachHorizontal;
        this.addToFavBtn = addToFavBtn;
    }

    public LinearLayout getEachHorizontal() {

        return eachHorizontal;
    }

    public void setEachHorizontal(LinearLayout eachHorizontal) {
        this.eachHorizontal = eachHorizontal;
    }

    public ImageButton getAddToFavBtn() {
        return addToFavBtn;
    }

    public void setAddToFavBtn(ImageButton addToFavBtn) {
        this.addToFavBtn = addToFavBtn;
    }

    ImageButton addToFavBtn;
}
