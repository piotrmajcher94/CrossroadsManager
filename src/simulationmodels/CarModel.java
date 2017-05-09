package simulationmodels;

import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import util.NWSE;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Piotrek on 16.04.2017.
 */
public class CarModel extends Rectangle{

    private Queue<Transition> transitionsList;
    private Queue<NWSE> directionsList;
    private Transition currentTransition = null;
    private Boolean done = false;
    private Boolean stopped = true;
    private static final double bumperBuffer = 10.0;

    public CarModel(double x, double y, double width, double height) {
        super(x, y, width, height);
        transitionsList = new ArrayDeque<>();
        directionsList = new ArrayDeque<>();
    }

    public void addTransition(double moveX, double moveY, double speed){
        //Create directions queue
        NWSE direction = null;
        if(moveX > 0)
            direction = NWSE.E;
        if(moveX < 0)
            direction = NWSE.W;
        if(moveY > 0)
            direction = NWSE.S;
        if(moveY < 0)
            direction = NWSE.N;

        this.directionsList.add(direction);

        //Crete trnslations queue
        TranslateTransition t = new TranslateTransition();
        t.setNode(this);
        t.setByX(moveX);
        t.setByY(moveY);


        double distance = Math.sqrt(moveX*moveX + moveY*moveY);
        double duration = distance/speed;

        t.setDuration(Duration.seconds(duration));
        t.setCycleCount(1);
        t.setOnFinished(e ->{
            //one transition finished
            System.out.println("finished");
            this.transitionsList.poll();
            this.directionsList.poll();
            this.start();
        });

        this.transitionsList.add(t);


    }

    public void start() {
        this.stopped = false;
        if(!this.transitionsList.isEmpty()) {
            this.transitionsList.peek().play();
        }else{
            this.done = true;
        }
    }
    public void stop(){
        this.stopped = true;
        if(!this.transitionsList.isEmpty()) {
            this.transitionsList.peek().pause();
        }
        else {
            this.done = true;
        }
    }

    public Boolean getStopped() {
        return stopped;
    }

    public double getBumperX(){
        try {
            switch (this.directionsList.peek()) {
                case E: {
                    return this.getBoundsInParent().getMaxX() + 1.0;
                }
                case W: {
                    return this.getBoundsInParent().getMinX() - 1.0;
                }
                case N: {
                    return this.getBoundsInParent().getMinX();
                }
                case S: {
                    return this.getBoundsInParent().getMinX();
                }

            }
        }
        catch (NullPointerException e){
            return 0;
        }
        return this.getBoundsInParent().getMinX();
    }

    public double getBumperY(){
        try {
            switch (this.directionsList.peek()) {
                case E: {
                    return this.getBoundsInParent().getMinY();
                }
                case W: {
                    return this.getBoundsInParent().getMinY();
                }
                case N: {
                    return this.getBoundsInParent().getMinY() - 1.0;
                }
                case S: {
                    return this.getBoundsInParent().getMaxY() + 1.0;
                }

            }
        }
        catch (NullPointerException e){
            return 0;
        }
        return this.getBoundsInParent().getMinY();
    }

    public double getBumberWidth(){
        try {
            switch (this.directionsList.peek()) {
                case E: {
                    return bumperBuffer;
                }
                case W: {
                    return bumperBuffer;
                }
                case N: {
                    return this.getBoundsInParent().getWidth();
                }
                case S: {
                    return this.getBoundsInParent().getWidth();
                }

            }
        }
        catch (NullPointerException e){
            return 0;
        }
        return bumperBuffer;
    }

    public double getBumperHeight() {
        try {
            switch (this.directionsList.peek()) {
                case E: {
                    return this.getBoundsInParent().getHeight();
                }
                case W: {
                    return this.getBoundsInParent().getHeight();
                }
                case N: {
                    return bumperBuffer;
                }
                case S: {
                    return bumperBuffer;
                }

            }
        }
        catch (NullPointerException e){
            return 0;
        }
        return bumperBuffer;
    }

    public NWSE getDirection() {
        return this.directionsList.peek();
    }

    public Boolean getDone() {
        return done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarModel carModel = (CarModel) o;

        if (transitionsList != null ? !transitionsList.equals(carModel.transitionsList) : carModel.transitionsList != null)
            return false;
        if (directionsList != null ? !directionsList.equals(carModel.directionsList) : carModel.directionsList != null)
            return false;
        if (currentTransition != null ? !currentTransition.equals(carModel.currentTransition) : carModel.currentTransition != null)
            return false;
        if (done != null ? !done.equals(carModel.done) : carModel.done != null) return false;
        return stopped != null ? stopped.equals(carModel.stopped) : carModel.stopped == null;
    }

    @Override
    public int hashCode() {
        int result = transitionsList != null ? transitionsList.hashCode() : 0;
        result = 31 * result + (directionsList != null ? directionsList.hashCode() : 0);
        result = 31 * result + (currentTransition != null ? currentTransition.hashCode() : 0);
        result = 31 * result + (done != null ? done.hashCode() : 0);
        result = 31 * result + (stopped != null ? stopped.hashCode() : 0);
        return result;
    }
}
