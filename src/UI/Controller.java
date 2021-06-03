package UI;

import edu.princeton.cs.algs4.DirectedEdge;
import geocaching.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

public class Controller {
    @FXML
    private Group graphGroup;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableView<Cache> cachesTable;

    @FXML
    private TableColumn<User, String> userIdCol;

    @FXML
    private TableColumn<User, String> userNameCol;

    @FXML
    private TableColumn<User, String> userTypeCol;

    @FXML
    private TextField userNameField;

    @FXML
    private Spinner<Double> cacheLatField, cacheLonField;

    @FXML
    private CheckBox cachePremiumCheck;

    @FXML
    private TextField cacheIdField;

    @FXML
    private ComboBox cacheRegionCombo;

    @FXML
    private TableColumn<Cache, String> cacheIdCol;

    @FXML
    private TableColumn<Cache, String> cacheRegionCol;


    @FXML
    private TableColumn<Cache, String> cacheLatCol;

    @FXML
    private TableColumn<Cache, String> cacheLonCol;

    @FXML
    public void initialize() {
        IO.readFile("input.txt");

        // inicializar colunas das tabelas
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("idstr"));
        userIdCol.setCellFactory(TextFieldTableCell.forTableColumn());
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        userNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        userTypeCol.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        userTypeCol.setCellFactory(TextFieldTableCell.forTableColumn());

        cacheIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        cacheIdCol.setCellFactory(TextFieldTableCell.forTableColumn());
        cacheRegionCol.setCellValueFactory(new PropertyValueFactory<>("regiao"));
        cacheRegionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        cacheLatCol.setCellValueFactory(new PropertyValueFactory<>("latstr"));
        cacheLatCol.setCellFactory(TextFieldTableCell.forTableColumn());
        cacheLonCol.setCellValueFactory(new PropertyValueFactory<>("lonstr"));
        cacheLonCol.setCellFactory(TextFieldTableCell.forTableColumn());

        // preencher
        updateGraphGroup();
        updateUsersTable();
        updateCachesTable();
    }

    private void updateGraphGroup(){
        graphGroup.getChildren().clear();

        SymbolDigraphLP grafo = Cache.grafo_distancias;
        // Cache.grafo_tempos = new SymbolDigraphLP();
        int xx[] = new int[grafo.getNvertices()];
        int yy[] = new int[grafo.getNvertices()];

        for(int i = 0; i < grafo.getNvertices(); i++) {
            int x = (int) (Math.random() * 650);
            int y = (int) (Math.random() * 400);
            int radius = 10;
            xx[i] = x;
            yy[i] = y;

            Circle c = new Circle(x, y, radius);
            c.setFill(Color.WHITE);

            StackPane stack = new StackPane();
            stack.setLayoutX(x - radius);
            stack.setLayoutY(y - radius);
            stack.getChildren().addAll(c, new Text(grafo.nameOf(i)));

            graphGroup.getChildren().add(stack);
        }

        for(int i = 0; i < grafo.getNvertices(); i++) {
            for(DirectedEdge e: grafo.digraph().adj(i)) {
                Line line = new Line(xx[e.from()], yy[e.from()], xx[e.to()], yy[e.to()]);
                line.setStroke(Color.LIGHTGRAY);
                graphGroup.getChildren().add(line);
            }
        }
    }

    private void updateUsersTable() {
        usersTable.getItems().clear();

        ArrayList<User> usersList = new ArrayList<>();
        for(int id: User.utilizadores_por_id.keys()) {
            User user = User.utilizadores_por_id.get(id);
            usersList.add(user);
        }

        usersTable.getItems().addAll(usersList);
    }

    private void updateCachesTable() {
        cachesTable.getItems().clear();

        ArrayList<Cache> cacheList = new ArrayList<>();
        for (String id : Cache.caches_por_id.keys()) {
            Cache cache = Cache.caches_por_id.get(id);
            cacheList.add(cache);
        }
        cachesTable.getItems().addAll(cacheList);
    }

    @FXML
    void onUserAdd(ActionEvent event) {
        String name = userNameField.getText();

        if(name.length() > 0) {
            User user = new UserBasic(name);
            User.utilizadores_por_nome.put(name, user);
            User.utilizadores_por_id.put(user.getId(), user);

            updateUsersTable();
        }
    }

    @FXML
    void onUserRemove(ActionEvent event) {
        User user = usersTable.getSelectionModel().getSelectedItem();
        if(user == null) return;

        User.utilizadores_por_nome.delete(user.getNome());
        User.utilizadores_por_id.delete(user.getId());
        for (String id: Cache.caches_por_id.keys()) {
            Cache cache = Cache.caches_por_id.get(id);
            cache.removerVisita(user);
        }

        updateUsersTable();
    }

    @FXML
    void onCacheAdd(ActionEvent event) {
        Cache cache;
        String id = cacheIdField.getText();
        String region = (String) cacheRegionCombo.getSelectionModel().getSelectedItem();
        if(cachePremiumCheck.isSelected())
            cache = new CachePremium(id, region, cacheLatField.getValue(), cacheLonField.getValue());
        else
            cache = new CacheBasic(id, region, cacheLatField.getValue(), cacheLonField.getValue());
        Cache.caches_por_id.put(id, cache);
        if (Cache.caches_por_regiao.contains(region)) {
            Cache.caches_por_regiao.get(region).add(cache);
        } else {
            ArrayList<Cache> lista = new ArrayList<Cache>();
            lista.add(cache);
            Cache.caches_por_regiao.put(region, lista);
        }

        updateCachesTable();
        updateGraphGroup();
    }

    @FXML
    void onCacheRemove(ActionEvent event) {
        Cache cache = cachesTable.getSelectionModel().getSelectedItem();
        if(cache == null) return;

        Cache.caches_por_regiao.delete(cache.getRegiao());
        Cache.caches_por_id.delete(cache.getId());

        updateCachesTable();
        updateGraphGroup();
    }

    @FXML
    void onOpenText(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null) {
            IO.readFile(selectedFile.getAbsolutePath());
        }
        updateGraphGroup();
        updateCachesTable();
        updateUsersTable();
    }

    @FXML
    void onOpenBin(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Binary File");
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null) {
            IO.readBinFile(selectedFile.getAbsolutePath());
        }
        updateGraphGroup();
        updateCachesTable();
        updateUsersTable();
    }

    @FXML
    void onSaveText(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Text File");
        File selectedFile = fileChooser.showSaveDialog(null);
        if(selectedFile != null) {
            IO.writeFile(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    void onSaveBin(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Binary File");
        File selectedFile = fileChooser.showSaveDialog(null);
        if(selectedFile != null) {
            IO.writeBinFile(selectedFile.getAbsolutePath());
        }
    }
}
