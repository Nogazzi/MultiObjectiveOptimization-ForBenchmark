package main.project;

import main.project.tasks.Mop;
import main.project.tasks.MopOneImpl;

/**
 * Created by Nogaz on 28.05.2017.
 */
public class Main {
    public static void main(String[] args){
        Mop mop = new MopOneImpl();
        mop.doSimulation();
    }
}
