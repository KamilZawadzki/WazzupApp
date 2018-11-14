package pl.gdak.wazzupapp;

import android.widget.ImageButton;
import android.widget.LinearLayout;

class Sounds {
    private LinearLayout eachHorizontal;

    public Sounds(LinearLayout eachHorizontal, ImageButton addToFavBtn) {
        this.eachHorizontal = eachHorizontal;
        this.addToFavBtn = addToFavBtn;
    }

    public LinearLayout getEachHorizontal() {

        return eachHorizontal;
    }

    public ImageButton getAddToFavBtn() {
        return addToFavBtn;
    }

    private ImageButton addToFavBtn;
}
