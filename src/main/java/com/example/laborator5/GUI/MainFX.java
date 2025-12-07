//package com.example.laborator5.GUI;
//
//import javafx.application.Application;
//import javafx.stage.Stage;
//
//public class MainFX extends Application {
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        IRepository<Book, Integer> repo = getRepository();
//        BooksService serv = new BooksService(repo);
//        BooksGUIController controller = new BooksGUIController(serv);
//        FXMLLoader loader = new
//                FXMLLoader(getClass().getResource("/gui/BooksGUI.fxml"));
//        loader.setController(controller);
//        Scene scene = new Scene(loader.load());
//        stage.setScene(scene);
//        stage.show();
//    }
//}
