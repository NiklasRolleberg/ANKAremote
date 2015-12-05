package anka.niklas.ankaremote;

/**
 * Created by Niklas on 2015-12-05.
 */

import android.app.Application;
import Model.Model;

public class MyApplication extends Application {

    private Model model = new Model();

    public Model getModel() {
        return this.model;
    }

}
