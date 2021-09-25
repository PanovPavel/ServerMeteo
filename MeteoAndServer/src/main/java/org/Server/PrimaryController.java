package org.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.lang.String.*;
import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PrimaryController{
    ServerSocket server = null;
    Boolean FlagForStartAndStopServer;
    HashMap<String, String> cityTemp;
    @FXML
    Label informationFromServer;
    public void initialize() {
        /**
         * Словарь городов
         */
        cityTemp = new HashMap<String, String>();
        cityTemp.put("Воронеж", "28");
        cityTemp.put("Липецк", "30");
        cityTemp.put("Москва", "32");
        cityTemp.put("Киев", "34");
        informationFromServer.setText(cityTemp.toString() + "\n");
    }

    public void switchToStartServer(ActionEvent actionEvent){
        FlagForStartAndStopServer = true;
        /*Создание сервер сокета*/
/*        try {
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        new Thread(()->{
            while (FlagForStartAndStopServer) {
                try {
                    server = new ServerSocket(8001);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            informationFromServer.setText(informationFromServer.getText() + "\nСервер запущен");
                        }
                    });
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            informationFromServer.setText(informationFromServer.getText() + "\nСервер Ожидает подключения");
                        }
                    });
                    Socket socket = server.accept();

                    /**
                     * Поток для отправки сообщения
                     */
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())
                    );
                    /**
                     * Поток для получения сообщения
                     */
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

                    /**
                     * messageClient строка полученная от клиента
                     */
                    String messageClient = reader.readLine();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            informationFromServer.setText(informationFromServer.getText() + "\nПолучение сообщения от клиента " + messageClient);
                        }
                    });

                    if (cityTemp.get(messageClient) != null) {
                        writer.write("Температура в городе " + messageClient + " равна " + cityTemp.get(messageClient));
                        writer.newLine();
                        writer.flush();
                    } else {
                        writer.write("Данного города нет");
                        writer.newLine();
                        writer.flush();
                        String newTempCity = reader.readLine();
                        System.out.println(newTempCity);
                        String[] massive = newTempCity.split(" ");
                        cityTemp.put(massive[0], massive[1]);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                informationFromServer.setText(cityTemp.toString());
                            }
                        });
                    }

                    /**
                     * Закрытие потоков
                     */
                    socket.close();
                    reader.close();
                    writer.close();
                    server.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void switchToStopServer(ActionEvent actionEvent) {
        FlagForStartAndStopServer = false;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                informationFromServer.setText(informationFromServer.getText() + "\n Сервер остановлен");
            }
        });
    }
}
