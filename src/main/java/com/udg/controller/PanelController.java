package com.udg.controller;

import com.udg.model.SJFProcess;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;

public class PanelController implements Observer {

    @FXML public VBox vBoxProcess;

    @FXML public ProgressIndicator piIsRunning;

    @FXML public TextField txtNombre;
    @FXML public TextField txtTiempo;

    @FXML public TableView<SJFProcess> processTable;
    @FXML public TableColumn<SJFProcess, Integer> columnId;
    @FXML public TableColumn<SJFProcess, String> columnName;
    @FXML public TableColumn<SJFProcess, Integer> columnTime;
    @FXML public TableColumn<SJFProcess, Integer> columnEntrada;
    @FXML public Button btnAddProces;
    @FXML public Button btnStartProcess;

    public Map<Integer, List<SJFProcess>> mapProcess = new HashMap<>();
    public ObservableList<SJFProcess> processList = FXCollections.observableArrayList();

    public boolean isRunning = false;

    public boolean pendingProcess = false;
    public double pendingProgress = 0.0;

    public double auxProgress = 0.0;
    public SJFProcess stoppedProcess = null;
    public SJFProcess currentProcess = null;
    public Thread currentThread = null;


    @FXML
    public void initialize() {
        this.createListOfProcess();

        //Add progress bar of the process
        processList.forEach(this::addProcessToUI);

        //Fil table columns
        columnEntrada.setCellValueFactory(new PropertyValueFactory<>("turn"));
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        processTable.setItems(processList);

        //Progress indicator
        piIsRunning.setVisible(false);
    }

    public void createListOfProcess(){

        SJFProcess sjf1 = new SJFProcess();
        sjf1.setId(1);
        sjf1.setName("Word");
        sjf1.setTime(1000);
        sjf1.setTurn(2);
        sjf1.setComplete(false);
        sjf1.addObserver(this);

        SJFProcess sjf2 = new SJFProcess();
        sjf2.setId(2);
        sjf2.setName("Excel");
        sjf2.setTime(1000);
        sjf2.setTurn(1);
        sjf2.setComplete(false);
        sjf2.addObserver(this);

        SJFProcess sjf3 = new SJFProcess();
        sjf3.setId(3);
        sjf3.setName("Power Point");
        sjf3.setTime(400);
        sjf3.setTurn(3);
        sjf3.setComplete(false);
        sjf3.addObserver(this);

        SJFProcess sjf4 = new SJFProcess();
        sjf4.setId(4);
        sjf4.setName("Visual Studio");
        sjf4.setTime(700);
        sjf4.setTurn(4);
        sjf4.setComplete(false);
        sjf4.addObserver(this);

        SJFProcess sjf5 = new SJFProcess();
        sjf5.setId(5);
        sjf5.setName("Terminal");
        sjf5.setTime(600);
        sjf5.setTurn(5);
        sjf5.setComplete(false);
        sjf5.addObserver(this);

        SJFProcess sjf6 = new SJFProcess();
        sjf6.setId(6);
        sjf6.setName("Java");
        sjf6.setTime(300);
        sjf6.setTurn(6);
        sjf6.setComplete(false);
        sjf6.addObserver(this);

        processList.addAll(sjf1,sjf2,sjf3,sjf4,sjf5,sjf6);
    }

    @FXML
    public void createProcess(){
        SJFProcess process = new SJFProcess();
        process.setId(processList.size() + 1);
        process.setName(txtNombre.getText());
        process.setTime(Integer.parseInt(txtTiempo.getText()));
        process.setTurn(processList.size() + 1);
        process.setComplete(false);
        process.addObserver(this);
        processList.add(process);

        txtNombre.setText("");
        txtTiempo.setText("");

        //60B38E

        //this.addProcessToMap(process);
        this.addProcessToUI(process);

        if(isRunning && process.getTime() < (currentProcess.getTime() - auxProgress)){
            currentThread.interrupt();
            stoppedProcess = currentProcess;
            pendingProcess = true;
            pendingProgress = auxProgress;
            process.setComplete(true);
            new Thread(process).start();
        }
        System.out.println("Aux progress: " + auxProgress);
    }

    public void addProcessToUI(SJFProcess process){
        this.addProcessToMap(process);
        this.vBoxProcess.getChildren().add(process.getLabel());
        this.vBoxProcess.getChildren().add(process.getProgressBar());
    }

    @FXML
    public void startProcess(){
        isRunning = true;
        piIsRunning.setVisible(true);
        pendingProcess = false;
        pendingProgress = 0.0;

        auxProgress = 0.0;
        stoppedProcess = null;
        currentProcess = null;
        currentThread = null;

        btnStartProcess.setDisable(true);
        restartProcess();
        currentProcess = SJF();
        currentThread = new Thread(currentProcess);
        currentThread.start();
    }

    public void addProcessToMap(SJFProcess process){
        if(mapProcess.containsKey(process.getTime())){
            mapProcess.get(process.getTime()).add(process);
        } else {
            List<SJFProcess> listOfProcess = new ArrayList<>();
            listOfProcess.add(process);
            mapProcess.put(process.getTime(), listOfProcess);
        }
    }

    private void restartProcess(){
        processList.forEach(process -> {
            process.setComplete(false);
            process.setProgress(0.0);
            process.getProgressBar().setProgress(0.0);
        });
    }

    private SJFProcess SJF(){
        if(stoppedProcess != null){
            stoppedProcess.setProgress(pendingProgress);
            System.out.println("return stopped process");
            return stoppedProcess;
        }

        Map<Integer, List<SJFProcess>> mapSortedByKey = mapProcess.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        System.out.println("-----------------------------------------------");
        System.out.println("Process Sorted: " + mapSortedByKey);

        List<List<SJFProcess>> listOfList = new ArrayList<>(mapSortedByKey.values());
        listOfList.forEach( list -> {
            if(list.size() > 1){
                var sortedList = list.stream()
                        .sorted(Comparator.comparingInt(SJFProcess::getTurnAsInt))
                        .collect(Collectors.toList());
                list.clear();
                list.addAll(sortedList);
            }
        });

        System.out.println("listOfList: " + listOfList);

        List<SJFProcess> goodList = new ArrayList<>();
        for (List<SJFProcess> sjfProcesses : listOfList) {
            goodList.addAll(sjfProcesses);
        }

        goodList = goodList.stream().filter( v -> !v.isComplete()).collect(Collectors.toList());

        if(goodList.size() > 0){
            System.out.println("goodList: " + goodList);
            goodList.get(0).setComplete(true);
            System.out.println("selected: " + goodList.get(0));
            return goodList.get(0);
        } else {
            System.out.println("All the process completed");
            return null;
        }
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an {@code Observable} object's
     * {@code notifyObservers} method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     */
    @Override
    public void update(Observable o, Object arg) {
        double recorrido = (double) arg;
        SJFProcess sjfProcess = (SJFProcess) o;
        sjfProcess.setProgress(recorrido);

        if(pendingProcess && stoppedProcess.getId().equals(sjfProcess.getId())){
            stoppedProcess = null;
            pendingProcess = false;
            System.out.println("Proceso pendiente");
        }

        auxProgress = sjfProcess.getProgress();
        System.out.println( sjfProcess.getName() + " - " + sjfProcess.getProgress() );
        Platform.runLater(() -> sjfProcess.getProgressBar().setProgress(sjfProcess.getProgress()));

        if(recorrido >= 1.0){
            currentProcess = SJF();

            System.out.println("CHECK VALID: " + currentProcess);
            if(currentProcess != null){
               currentThread = new Thread(currentProcess);
               currentThread.start();
            } else {
                isRunning = false;
                piIsRunning.setVisible(false);
                btnStartProcess.setDisable(false);
                System.out.println("ALGORITMO COMPLETO");
            }
        }
    }
}
