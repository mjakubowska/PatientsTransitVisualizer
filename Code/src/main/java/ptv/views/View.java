package ptv.views;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import ptv.models.borders.InBorders;
import ptv.models.data.*;
import ptv.models.reader.CountryFileReader;
import ptv.models.reader.PatientsFileReader;
import ptv.models.simulation.Simulator;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

//TODO sprint 2
// MARTYNA
// Drawing Junctions and borders

//need to append scale

public class View {
    private Canvas canvas;
    private Affine affine;
    private Simulator simulator;
    private InBorders polygon;

    public View(Canvas canvas) {
        this.canvas = canvas;
        this.affine = new Affine();
        this.simulator = new Simulator();
    }

    public void loadMap(String filePath) throws FileNotFoundException {
        CountryFileReader countryFileReader = new CountryFileReader();
        try {
            Country country = countryFileReader.readFile(filePath);
            country.setJunctionsList(new JunctionFinder().findJunctions(country.getDistancesList()));
            this.simulator.setCountry(country);
        }catch (IllegalArgumentException exception) {
            System.out.println("Invalid data");
        }
        this.affine.appendScale(countAffine(), countAffine());
        paintMap();
    }

    public void addPatientsList(String filePath) throws FileNotFoundException {
        PatientsFileReader patientsFileReader = new PatientsFileReader();
        List<Patient> patients = patientsFileReader.readFile(filePath);
        Iterator<Patient> iterator = patients.iterator();
        while(iterator.hasNext()) {
            this.simulator.addPatient(iterator.next());
        }
        System.out.println(this.simulator.getPatients());
    }

    public void paintMap(){
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setTransform(this.affine);
        g.setStroke(Color.LIGHTGRAY);
        g.setLineWidth(0.1);
        for (int i=0; i<this.canvas.getHeight(); i++) {
            g.strokeLine(i, 0, i, this.canvas.getWidth());
        }
        for (int i=0; i<this.canvas.getWidth(); i++) {
            g.strokeLine(0,i, this.canvas.getHeight(), i);
        }
        this.paintDistances(g);
        this.paintHospitals(g);
        this.paintFacilities(g);
    }

    public void paintHospitals(GraphicsContext g){
        Iterator<Hospital> iterator = this.simulator.getCountry().getHospitalsList().iterator();
        Hospital currentHospital;
        double xCoord, yCoord;
        g.setFill(Color.RED);
        g.setStroke(Color.RED);
        g.setLineWidth(0.1);
        while (iterator.hasNext()) {
            currentHospital = iterator.next();
            xCoord = currentHospital.getCoordinates().getX();
            yCoord = currentHospital.getCoordinates().getY();
            g.strokeOval(xCoord-0.4, yCoord-0.4, 0.8, 0.8);
            g.setFill(Color.WHITE);
            g.fillOval(xCoord-0.4, yCoord-0.4, 0.8, 0.8);
        }

    }

    public void paintFacilities(GraphicsContext g){
        Iterator<Facility> iterator = this.simulator.getCountry().getFacilitiesList().iterator();
        Facility currentFacility;
        double xCoord, yCoord;
        g.setFill(Color.GREEN);
        g.setStroke(Color.GREEN);
        g.setLineWidth(0.1);
        while (iterator.hasNext()) {
            currentFacility = iterator.next();
            xCoord = currentFacility.getCoordinates().getX();
            yCoord = currentFacility.getCoordinates().getY();
            g.strokeOval(xCoord-0.2, yCoord-0.2, 0.4, 0.4);
            g.setFill(Color.WHITE);
            g.fillOval(xCoord-0.2, yCoord-0.2, 0.4, 0.4);
        }
    }

    public void paintDistances(GraphicsContext g){
        Iterator<Distance> iterator = this.simulator.getCountry().getDistancesList().iterator();
        Distance currentDistance;
        double firstXCoord, firstYCoord, secondXCoord, secondYCoord;
        g.setFill(Color.BLACK);
        g.setStroke(Color.BLACK);
        g.setLineWidth(0.1);
        while (iterator.hasNext()) {
            currentDistance = iterator.next();
            firstXCoord = currentDistance.getFirstNode().getCoordinates().getX();
            firstYCoord = currentDistance.getFirstNode().getCoordinates().getY();
            secondXCoord = currentDistance.getSecondNode().getCoordinates().getX();
            secondYCoord = currentDistance.getSecondNode().getCoordinates().getY();
            g.strokeLine(firstXCoord, firstYCoord, secondXCoord, secondYCoord);
        }
    }

    public void paintPatient(){}

    public int countAffine(){
        //need to add function
        return 20;
    }

    private Point2D findCenter(){return null;}

    public Affine getAffine() {
        return this.affine;
    }


}

