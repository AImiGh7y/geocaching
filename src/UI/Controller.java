package UI;

import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import geocaching.*;
import javafx.application.Platform;
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
    private ComboBox userTypeCombo;

    @FXML
    private TableColumn<Cache, String> cacheIdCol;

    @FXML
    private TableColumn<Cache, String> cacheRegionCol;

    @FXML
    private TableColumn<Cache, String> cacheTipoCol;

    @FXML
    private TableColumn<Cache, String> cacheLatCol;

    @FXML
    private TableColumn<Cache, String> cacheLonCol;

    @FXML
    private TextField cache1Field, cache2Field, cachePartidaField, caixeiroTempoField;

    @FXML
    private ComboBox searchCombo, caixeiroCombo;

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
        cacheTipoCol.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        cacheTipoCol.setCellFactory(TextFieldTableCell.forTableColumn());
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

        // adicionar vertices
        for(int i = 0; i < grafo.getNvertices(); i++) {
            int x = (int) (Math.random() * 650);
            int y = (int) (Math.random() * 400);
            int radius = 25;
            xx[i] = x;
            yy[i] = y;

            Circle c = new Circle(x, y, radius);
            if(Cache.caches_por_id.get(grafo.nameOf(i)) instanceof CachePremium)
                c.setFill(Color.GOLD);
            else
                c.setFill(Color.LIGHTSKYBLUE);

            StackPane stack = new StackPane();
            stack.setLayoutX(x - radius);
            stack.setLayoutY(y - radius);
            stack.getChildren().addAll(c, new Text(grafo.nameOf(i)));

            graphGroup.getChildren().add(stack);
        }

        // adicionar aresta
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
        String type = (String) userTypeCombo.getSelectionModel().getSelectedItem();

        if(name.length() > 0) {
            User user;
            if(type.equals("basic"))
                user = new UserBasic(name);
            else if(type.equals("premium"))
                user = new UserPremium(name);
            else
                user = new UserAdmin(name);
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
        Cache.grafo_distancias.addVertex(cache.getId());
        Cache.grafo_tempos.addVertex(cache.getId());

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

    @FXML
    void onAboutMenu(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Developed by Nuno e Pedro Industries", ButtonType.OK);
        alert.setHeaderText("About");
        alert.setTitle("About");
        alert.showAndWait();
    }

    @FXML
    void onExitMenu(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void onSearch(ActionEvent event) {
/*
        private TextField cache1Field, cache2Field;
        private ComboBox searchCombo;
*/
        String id1 = cache1Field.getText();
        String id2 = cache2Field.getText();
        if(Cache.caches_por_id.contains(id1) && Cache.caches_por_id.contains(id2)) {
            String opcao = (String)searchCombo.getSelectionModel().getSelectedItem();
            SymbolDigraphLP grafo;
            if(opcao.equals("time"))
                grafo = Cache.grafo_tempos;
            else
                grafo = Cache.grafo_distancias;
            int i1 = grafo.indexOf(id1);
            int i2 = grafo.indexOf(id2);
            DijkstraSP dijkstra = new DijkstraSP(grafo.digraph(), i1);
            if(dijkstra.hasPathTo(i2)) {
                double custoMin = dijkstra.distTo(i2);
                String text = "Custo minimo = " + custoMin + "\n";
                text += id1;
                for(DirectedEdge e: dijkstra.pathTo(i2)) {
                    text += " -> " + grafo.nameOf(e.to());
                }
                Alert msg = new Alert(Alert.AlertType.INFORMATION, text, ButtonType.OK);
                msg.showAndWait();
            }
        }
        else {
            Alert error = new Alert(Alert.AlertType.ERROR, "Cache nao existe", ButtonType.OK);
            error.showAndWait();
        }
    }

    @FXML
    void onPartida(ActionEvent event){
        String partida = cachePartidaField.getText();
        if(Cache.caches_por_id.contains(partida)) {
            SymbolDigraphLP grafo;
            if(((String)caixeiroCombo.getSelectionModel().getSelectedItem()).equals("time"))
                grafo = Cache.grafo_tempos;
            else
                grafo = Cache.grafo_distancias;

            int origem = grafo.indexOf(partida);
            String tempoStr = caixeiroTempoField.getText();
            int tempoMax = Integer.MAX_VALUE;
            if(tempoStr.length() > 0)
                tempoMax = Integer.valueOf(tempoStr);
            ArrayList<Integer> caminho = Cache.caixeiro(grafo, origem, origem, new ArrayList<Integer>(), tempoMax, System.currentTimeMillis());

            String texto;
            if(caminho == null) {
                texto = "Nao encontrou caminho";
            }
            else {
                texto = "";
                boolean first = true;
                for (int v : caminho) {
                    if (!first)
                        texto += " -> ";
                    texto += grafo.nameOf(v);
                    first = false;
                }
            }

            Alert mensagem = new Alert(Alert.AlertType.INFORMATION, texto, ButtonType.OK);
            mensagem.showAndWait();
        }

    }
}
