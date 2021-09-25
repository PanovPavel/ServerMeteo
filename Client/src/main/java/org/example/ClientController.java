package org.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

public class ClientController {
    @FXML
    TextField CitiTextField;
    @FXML
    Label getAnswerCity;
    public void SpendCitiWithClientOnServer(ActionEvent actionEvent) {
        try {
            /*Подключение к сокету*/
            Socket socket = new Socket("localhost", 8001);
            /*Поток отправки сообщения на сервер*/
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );
            BufferedWriter writer2 = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );
            /*Получение сообщения с потока*/
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            writer.write(CitiTextField.getText());
            writer.newLine();
            writer.flush();
            /*Получение ответа с сервера*/
            String messageServer = reader.readLine();
            System.out.println(messageServer);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    getAnswerCity.setText(messageServer);
                }
            });
            if (messageServer.equals("Данного города нет")){
                TextInputDialog dialog = new TextInputDialog("Введите температуру в городе" + CitiTextField.getText());
                dialog.setTitle("Введите температуру в городе");
                dialog.setHeaderText("Введите температуру в городе");
                dialog.setContentText("Введите температуру в городе");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> System.out.println("Your" + name));
                writer.write(CitiTextField.getText() + " " + result.get());
                writer.newLine();
                writer.flush();
            }
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
