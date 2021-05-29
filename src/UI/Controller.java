package UI;

import edu.princeton.cs.algs4.DirectedEdge;
import geocaching.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

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
    private TableColumn<Cache, String> cacheIdCol;

    @FXML
    private TableColumn<Cache, String> cacheRegionCol;

    @FXML
    public void initialize() {
        System.out.println("initialize");

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
            int x = (int) (Math.random() * 500);
            int y = (int) (Math.random() * 250);
            int radius = 10;
            xx[i] = x;
            yy[i] = y;

            Circle c = new Circle(x, y, radius);
            c.setFill(Color.WHITE);

            StackPane stack = new StackPane();
            stack.setLayoutX(x - radius);
            stack.setLayoutY(y - radius);
            stack.getChildren().addAll(c, new Text(i + ""));

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

        User.utilizadores_por_nome.delete(user.getNome());
        User.utilizadores_por_id.delete(user.getId());
        for (String id: Cache.caches_por_id.keys()) {
            Cache cache = Cache.caches_por_id.get(id);
            cache.removerVisita(user);
        }

        updateUsersTable();
    }
}
