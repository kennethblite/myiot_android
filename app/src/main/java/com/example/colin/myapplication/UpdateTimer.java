package com.example.colin.myapplication;

import java.util.TimerTask;

/**
 * Created by Colin on 3/13/2017.
 */
public class UpdateTimer extends TimerTask{
    UpdateListener u;
    public UpdateTimer(UpdateListener u){
        this.u = u;
    }
    public void run(){
        u.Update();
    }
}
