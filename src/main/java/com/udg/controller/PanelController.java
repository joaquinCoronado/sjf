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

    @FXML public ProgressBar pbProceso1;
    @FXML public ProgressBar pbProceso2;
    @FXML public ProgressBar pbProceso3;
    @FXML public ProgressBar pbProceso4;
    @FXML public ProgressBar pbProceso5;
    @FXML public ProgressBar pbProceso6;
    public ProgressBar progressBar = null;

    @FXML public Label lblProces1;
    @FXML public Label lblProces2;
    @FXML public Label lblProces3;
    @FXML public Label lblProces4;
    @FXML public Label lblProces5;
    @FXML public Label lblProces6;

    @FXML public TableView<SJFProcess> processTable;
    @FXML public TableColumn<SJFProcess, Integer> columnId;
    @FXML public TableColumn<SJFProcess, String> columnName;
    @FXML public TableColumn<SJFProcess, Integer> columnTime;
    @FXML public TableColumn<SJFProcess, Integer> columnEntrada;

    @FXML public Button btnAddProces;

    SJFProcess newProcess = null;

    public Map<Integer, List<SJFProcess>> mapProcess = new HashMap<>();
    public Map<Integer, SJFProcess> mapProcessById;
    public ObservableList<SJFProcess> tableViewList;

    @FXML
    public void initialize() {
        inicializarProcesos(true);
    }

    @FXML
    public void startProcess(){
        inicializarProcesos(false);
        new Thread(SJF()).start();
    }

    @FXML
    public void addProcess(){
        System.out.println("Proceso agregado");

        //Create event runnable object
        newProcess = new SJFProcess();
        newProcess.setId(7);
        newProcess.setName("Intellij");
        newProcess.setTime(350);
        newProcess.setTurn(7);
        newProcess.setComplete(false);
        newProcess.addObserver(this);
        addProcessToMap(newProcess);

        tableViewList.add(newProcess);
        //processTable.setItems(tableViewList);

        //Add to the queue

        //Generate UI of the event
        Label label = new Label();
        label.setText(newProcess.getName());
        VBox.setMargin(label, new Insets(0,0,2,0));

        progressBar = new ProgressBar();
        progressBar.setPrefWidth(newProcess.getTime());
        progressBar.setProgress(0.0);
        VBox.setMargin(progressBar, new Insets(0,0,10,0));

        this.vBoxProcess.getChildren().add(label);
        this.vBoxProcess.getChildren().add(progressBar);

        btnAddProces.setDisable(true);
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

    private void inicializarProcesos(boolean cleanList){

        SJFProcess sjf1 = new SJFProcess();
        sjf1.setId(1);
        sjf1.setName("Word");
        sjf1.setTime(1000);
        sjf1.setTurn(2);
        sjf1.setComplete(false);
        sjf1.addObserver(this);
        pbProceso1.setPrefWidth(sjf1.getTime());
        pbProceso1.setProgress(0.0);
        lblProces1.setText(sjf1.getName());
        addProcessToMap(sjf1);

        SJFProcess sjf2 = new SJFProcess();
        sjf2.setId(2);
        sjf2.setName("Excel");
        sjf2.setTime(1000);
        sjf2.setTurn(1);
        sjf2.setComplete(false);
        sjf2.addObserver(this);
        pbProceso2.setPrefWidth(sjf2.getTime());
        pbProceso2.setProgress(0.0);
        lblProces2.setText(sjf2.getName());
        addProcessToMap(sjf2);

        SJFProcess sjf3 = new SJFProcess();
        sjf3.setId(3);
        sjf3.setName("Power Point");
        sjf3.setTime(400);
        sjf3.setTurn(3);
        sjf3.setComplete(false);
        sjf3.addObserver(this);
        pbProceso3.setPrefWidth(sjf3.getTime());
        pbProceso3.setProgress(0.0);
        lblProces3.setText(sjf3.getName());
        addProcessToMap(sjf3);

        SJFProcess sjf4 = new SJFProcess();
        sjf4.setId(4);
        sjf4.setName("Visual Studio");
        sjf4.setTime(700);
        sjf4.setTurn(4);
        sjf4.setComplete(false);
        sjf4.addObserver(this);
        pbProceso4.setPrefWidth(sjf4.getTime());
        pbProceso4.setProgress(0.0);
        lblProces4.setText(sjf4.getName());
        addProcessToMap(sjf4);

        SJFProcess sjf5 = new SJFProcess();
        sjf5.setId(5);
        sjf5.setName("Terminal");
        sjf5.setTime(600);
        sjf5.setTurn(5);
        sjf5.setComplete(false);
        sjf5.addObserver(this);
        pbProceso5.setPrefWidth(sjf5.getTime());
        pbProceso5.setProgress(0.0);
        lblProces5.setText(sjf5.getName());
        addProcessToMap(sjf5);

        SJFProcess sjf6 = new SJFProcess();
        sjf6.setId(6);
        sjf6.setName("Java");
        sjf6.setTime(300);
        sjf6.setTurn(6);
        sjf6.setComplete(false);
        sjf6.addObserver(this);
        pbProceso6.setPrefWidth(sjf6.getTime());
        pbProceso6.setProgress(0.0);
        lblProces6.setText(sjf6.getName());
        addProcessToMap(sjf6);

        if(cleanList){
            tableViewList = FXCollections.observableArrayList(sjf1,sjf2,sjf3,sjf4,sjf5,sjf6);
            if(progressBar != null){
                progressBar.setProgress(0.0);
            }
            columnEntrada.setCellValueFactory(new PropertyValueFactory<>("turn"));
            columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
            columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            processTable.setItems(tableViewList);
        }

    }

    synchronized private SJFProcess SJF(){
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
        System.out.println( sjfProcess.getName() + recorrido);

        switch(sjfProcess.getId()){
            case 1: Platform.runLater(() -> pbProceso1.setProgress(recorrido)); break;
            case 2: Platform.runLater(() -> pbProceso2.setProgress(recorrido)); break;
            case 3: Platform.runLater(() -> pbProceso3.setProgress(recorrido)); break;
            case 4: Platform.runLater(() -> pbProceso4.setProgress(recorrido)); break;
            case 5: Platform.runLater(() -> pbProceso5.setProgress(recorrido)); break;
            case 6: Platform.runLater(() -> pbProceso6.setProgress(recorrido)); break;
            case 7: Platform.runLater(() -> progressBar.setProgress(recorrido)); break;
        }

        if(recorrido >= 1.0){
            SJFProcess thread = SJF();

            System.out.println("CHECK VALID: " + thread);
            if(thread != null){
                new Thread(thread).start();
            } else {
                System.out.println("ALGORITMO COMPLETO");
            }
        }
    }
}
