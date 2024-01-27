package classtimer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClassTimer extends Application {
    boolean konecHodiny = false;

    
    StackPane root = new StackPane();
    Text informaceKonec = new Text("Je konec hodiny! Zmacknete \'K\' pro ukonceni.");

    int minuty = 0;
    int sekundy;
    int uplynuleProcenta = 0;
    int zbyvajiciCas = 2700;
    int x1000 = 0;
    int x2700 = 0;
    int x27000 = 0;
    AudioClip theme = new AudioClip(ClassTimer.class.getClassLoader().getResource("sounds/VictorySound.wav").toString());
    
    Text procenta = new Text(uplynuleProcenta + "%");
    Text vteriny = new Text("Zbyva vterin: " + zbyvajiciCas);
    Text naskok = new Text(Integer.toString(minuty));
    Button start = new Button("Start");
    Rectangle r = new Rectangle();
    Rectangle white = new Rectangle();
    Rectangle konec = new Rectangle(5, 20, Color.BLACK);
    Rectangle zacatek = new Rectangle(5, 20, Color.BLACK);
    Button pluse = new Button("+");
    Button minuse = new Button("-");
    
    private void dimissAll() {
        procenta.setVisible(false);
        vteriny.setVisible(false);
        naskok.setVisible(false);
        start.setVisible(false);
        r.setVisible(false);
        white.setVisible(false);
        konec.setVisible(false);
        zacatek.setVisible(false);
        pluse.setVisible(false);
        minuse.setVisible(false);
    }
    
    @Override
    public void start(Stage primaryStage) { 
            Scene scene = new Scene(root, 1050, 200);        
//nastavit pocatecni cas a znelku
        r.setWidth(1000);
        r.setHeight(20);
        r.setFill(Color.GREEN);
        r.setTranslateX(r.getTranslateX() - r.getWidth());

        naskok.setTranslateX(-300);
        naskok.setTranslateY(-50);

        //informaceKonec.setVisible(false);
        informaceKonec.setFont(Font.font(50));
        informaceKonec.setVisible(false);
        
        pluse.setTranslateX(naskok.getTranslateX() + 30);
        pluse.setTranslateY(naskok.getTranslateY());
        pluse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                minuty++;
                naskok.setText(Integer.toHexString(minuty));
            }
        });
        minuse.setTranslateX(naskok.getTranslateX() + 60);
        minuse.setTranslateY(naskok.getTranslateY());
        minuse.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (minuty > 0) {
                    minuty--;
                    naskok.setText(Integer.toHexString(minuty));
                }
            }
        });

        konec.setTranslateX(500);

        zacatek.setTranslateX(-502);

        white.setFill(Color.rgb(244, 244, 244));
        white.setWidth(1000);
        white.setHeight(20);
        white.setTranslateX(white.getTranslateX() - white.getWidth());

        procenta.setTranslateY(-50);
        procenta.setFont(Font.font(50));
        
        vteriny.setTranslateY(50);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.K) {
                        if (konecHodiny) {
                            primaryStage.close();
                        }
                    }
                }
            });
        
        start.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                start.setTranslateY(500);
                pluse.setVisible(false);
                minuse.setVisible(false);
                sekundy = minuty * 60;
                for (int i = 0; i < sekundy; i++) {
                    zbyvajiciCas--;
                    vteriny.setText("Zbyva vterin: " + zbyvajiciCas);
                }
                for (int i = 0; i < sekundy / 2.7; i++) {
                    r.setTranslateX(r.getTranslateX() + 1);
                    x2700 = 0;
                }
                for (int i = 0; i < sekundy / 27; i++) {
                    uplynuleProcenta++;
                    procenta.setText(uplynuleProcenta + "%");
                    x27000 = 0;
                }

                startTimer(100, new Runnable() {
                        
                    @Override
                    public void run() {
                        x1000++;
                        x2700++;
                        x27000++;
                        if (x1000 == 10) {
                            zbyvajiciCas--;
                            vteriny.setText("Zbyva vterin: " + zbyvajiciCas);
                            x1000 = 0;
                        }
                        if (x2700 == 27) {
                            r.setTranslateX(r.getTranslateX() + 1);
                            x2700 = 0;
                        }
                        if (x27000 == 270) {
                            uplynuleProcenta++;
                            procenta.setText(uplynuleProcenta + "%");
                            x27000 = 0;
                        }
                    }
                });

            }
        });

        root.getChildren().addAll(start, r, white, vteriny, procenta, konec, zacatek, naskok, pluse, minuse, informaceKonec);

        primaryStage.setTitle("ClassTimer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void startTimer(final int interval, final Runnable action) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (uplynuleProcenta != 100) {
                            Thread.sleep(interval);
                        }else {
                            theme.play();   
                            konecHodiny = true;
                            informaceKonec.setVisible(true);
                            dimissAll();
                            
                        }
                    } catch (InterruptedException ex) {
                    }
                    javafx.application.Platform.runLater(action);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

}
