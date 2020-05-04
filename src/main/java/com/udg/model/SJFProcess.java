package com.udg.model;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Observable;

import static java.lang.Thread.sleep;

public class SJFProcess extends Observable implements Runnable {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty time;
    private SimpleIntegerProperty turn;
    private SimpleBooleanProperty complete = new SimpleBooleanProperty(false);

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        double recorrido = 0.0;

        while(recorrido < 1){
            recorrido = recorrido + 0.01;
            //System.out.println( this.name + " recorrido: " + recorrido);

            this.setChanged();
            this.notifyObservers(recorrido);
            this.clearChanged();

            try {
                sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Hilo interrumpido: " + this.name.get());
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "SJFProcess{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", turn=" + turn +
                ", complete=" + complete +
                '}';
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public Integer getTime() {
        return time.get();
    }

    public void setTime(Integer time) {
        this.time = new SimpleIntegerProperty(time);
    }

    public Integer getTurn() {
        return turn.get();
    }

    public Integer getTurnAsInt(){
        return turn.get();
    }

    public void setTurn(Integer turn) {
        this.turn = new SimpleIntegerProperty(turn);
    }

    public Boolean isComplete() {
        return complete.get();
    }

    public void setComplete(boolean complete) {
        this.complete = new SimpleBooleanProperty(complete);
    }
}
