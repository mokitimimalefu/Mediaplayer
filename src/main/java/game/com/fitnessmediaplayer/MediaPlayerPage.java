package game.com.fitnessmediaplayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaPlayerPage extends Application {

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private Label startTimeLabel, endTimeLabel, volPercentLabel;
    private Slider progressSlider, volSlider;
    private boolean isSliding = false;
    private ObservableList<VideoItem> videoList = FXCollections.observableArrayList();
    private Pane listContainer;
    private Pane videopPane;
    private Pane progPane;
    private int currentIndex = 0;
    private boolean isMuted = false;
    private double beforeMuteVol = 0.7;

    private List<Button> controlButtons = new ArrayList<>();
    private Button activeControlBtn = null;
    private final String BLUE_STYLE = "-fx-background-color: linear-gradient(to bottom, #2a8cff, #1560b0);" +
                                      " -fx-text-fill: white;" +
                                      " -fx-font-weight: bold;" +
                                      " -fx-background-radius: 12;" +
                                      "-fx-border-color: #3a9bff; -fx-border-radius: 12;" +
                                      " -fx-font-size: 11px; -fx-cursor: hand;";

    private final String DARK_STYLE = "-fx-background-color: #132a44;" +
                                      " -fx-text-fill: white;" +
                                      " -fx-background-radius: 12;" +
                                      " -fx-border-color: #1e3a5a;" +
                                      " -fx-border-radius: 12;" +
                                      " -fx-font-size: 11px;" +
                                      " -fx-cursor: hand;";

    @Override
    public void start(Stage stage) {
        Pane root = buildUI(stage);
        Scene scene = new Scene(root, 1100, 680);
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
        stage.setTitle("FITNESS MEDIA PLAYER");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(650);
        stage.show();
        root.requestFocus();
    }

    public Pane buildUI(Stage stage) {
        Pane mainPane = new Pane();
        mainPane.setPrefSize(1100, 680);
        mainPane.setStyle("-fx-background-color: #09192a;" +
                          "-fx-border-color: #1a3a5a;" +
                          "-fx-border-width: 2;" +
                          " -fx-border-radius: 12;" +
                          " -fx-background-radius: 12;");
        mainPane.setFocusTraversable(true);

        Pane header = new Pane();
        header.setLayoutX(10);
        header.setLayoutY(10);
        header.setPrefSize(1080, 75);
        header.setStyle("-fx-background-color: #0a1c30;" +
                        " -fx-border-color: transparent transparent #1a3a5a transparent;" +
                        " -fx-border-width: 1;");

        Image logoImage = new Image(getClass().getResourceAsStream("/game/com/fitnessmediaplayer/images/fitness-logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setLayoutX(15);
        logoView.setLayoutY(10);
        logoView.setFitWidth(55);
        logoView.setFitHeight(55);

        Label title1 = new Label("FITNESS");
        title1.setLayoutX(85);
        title1.setLayoutY(12);
        title1.setStyle("-fx-text-fill: white;" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;");

        Label title2 = new Label("MEDIA PLAYER");
        title2.setLayoutX(220);
        title2.setLayoutY(12);
        title2.setStyle("-fx-text-fill: #3a9bff;" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;");

        Label subtitle = new Label("Your Workout • Your Playlist • Your Progress");
        subtitle.setLayoutX(85);
        subtitle.setLayoutY(45);
        subtitle.setStyle("-fx-text-fill: #8aa0b8;" +
                          "-fx-font-size: 13px;");

        Button canceButton = new Button();
        ImageView cancelIcon = new ImageView(new Image(getClass().getResourceAsStream("/game/com/fitnessmediaplayer/icons/cancel.png")));
        canceButton.setGraphic(cancelIcon);
        canceButton.setLayoutX(830);
        canceButton.setLayoutY(5);
        canceButton.setPrefWidth(80);
        canceButton.setPrefHeight(45);
        canceButton.setStyle("-fx-background-color: #0f233a;" +
                              "-fx-border-color: #1a3a5a;" +
                              "-fx-border-radius: 10;" +
                              "-fx-background-radius: 10;");
        canceButton.setOnAction(e -> {
            if (mediaPlayer!= null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            Stage currentStage = (Stage) canceButton.getScene().getWindow();
            currentStage.close();
        });

        Button musicButton = new Button();
        ImageView musicIcon = new ImageView(new Image(getClass().getResourceAsStream("/game/com/fitnessmediaplayer/icons/music.png")));
        musicButton.setGraphic(musicIcon);
        musicButton.setLayoutX(740);
        musicButton.setLayoutY(5);
        musicButton.setPrefWidth(80);
        musicButton.setPrefHeight(45);
        musicButton.setStyle("-fx-background-color: #0f233a;" +
                              "-fx-border-color: #1a3a5a;" +
                              "-fx-border-radius: 10;" +
                              "-fx-background-radius: 10;");
        musicButton.setOnAction(e -> {
            if (mediaPlayer!= null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            try {
                Stage currentStage = (Stage) musicButton.getScene().getWindow();
                MusicPage musicPage = new MusicPage();
                Pane musicRoot = musicPage.buildUI(currentStage);
                Scene sc = new Scene(musicRoot, currentStage.getWidth(), currentStage.getHeight());
                musicRoot.prefWidthProperty().bind(sc.widthProperty());
                musicRoot.prefHeightProperty().bind(sc.heightProperty());
                currentStage.setScene(sc);
                currentStage.setTitle("FITNESS MUSIC PLAYER");
                musicRoot.requestFocus();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Label motto = new Label("Stronger\nHealthier\nHappier");
        motto.setLayoutX(930); motto.setLayoutY(8);
        motto.setStyle("-fx-text-fill: #8aa0b8;" +
                       "-fx-font-size: 11px;" +
                       "-fx-border-color: transparent transparent transparent #3a9bff;" +
                       "-fx-border-width: 0 0 0 2;" +
                       "-fx-padding: 0 0 0 10;");

        header.getChildren().addAll(logoView, title1, title2, subtitle, canceButton, motto, musicButton);

        videopPane = new Pane();
        videopPane.setLayoutX(15);
        videopPane.setLayoutY(95);
        videopPane.setPrefSize(680, 360);
        videopPane.setStyle("-fx-background-color: black;" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-color: #1e3a5a;" +
                            "-fx-border-radius: 10;");

        mediaView = new MediaView();
        mediaView.setFitWidth(680); mediaView.setFitHeight(315);
        mediaView.setPreserveRatio(true);

        progPane = new Pane();
        progPane.setLayoutX(0);
        progPane.setLayoutY(315);
        progPane.setPrefSize(680, 45);
        progPane.setStyle("-fx-background-color:" +
                          " rgba(10,28,48,0.95); "+
                         " -fx-background-radius: 0 0 10 10;");

        startTimeLabel = new Label("00:00");
        startTimeLabel.setLayoutX(10);
        startTimeLabel.setLayoutY(15);
        startTimeLabel.setStyle("-fx-text-fill: white;" +
                                "-fx-font-size: 12px;");

        endTimeLabel = new Label("00:00");
        endTimeLabel.setLayoutX(600);
        endTimeLabel.setLayoutY(15);
        endTimeLabel.setStyle("-fx-text-fill: white;" +
                              "-fx-font-size: 12px;");

        progressSlider = new Slider(0,100,0);
        progressSlider.setLayoutX(60);
        progressSlider.setLayoutY(12);
        progressSlider.setPrefWidth(520);
        progressSlider.setStyle("-fx-control-inner-background: #1a3a5a;");
        progressSlider.setOnMousePressed(e -> isSliding = true);
        progressSlider.setOnMouseReleased(e -> { if(mediaPlayer!=null) mediaPlayer.seek(Duration.seconds(progressSlider.getValue())); isSliding=false; });

        Label full = new Label("⛶");
        full.setLayoutX(650);
        full.setLayoutY(12);
        full.setStyle("-fx-text-fill: white;" +
                      "-fx-font-size: 16px;" +
                      "-fx-cursor: hand;");

        progPane.getChildren().addAll(startTimeLabel, progressSlider, endTimeLabel, full);
        videopPane.getChildren().addAll(mediaView, progPane);

        Pane controlspane = new Pane();
        controlspane.setLayoutX(15);
        controlspane.setLayoutY(465);
        controlspane.setPrefSize(680, 125);
        controlspane.setStyle("-fx-background-color: #0f233a;" +
                              "-fx-border-color: #1a3a5a;" +
                              "-fx-border-radius: 10;" +
                              "-fx-background-radius: 10;");

        Button playButton = new Button();
        ImageView playIcon = new ImageView(new Image(getClass().getResourceAsStream("/game/com/fitnessmediaplayer/icons/play.png")));
        playButton.setLayoutX(10);
        playButton.setLayoutY(10);
        playButton.setPrefSize(122, 62);
        playButton.setGraphic(playIcon);
        playButton.setOnAction(e -> {
            if(mediaPlayer!=null) mediaPlayer.play();
            setActiveButton(playButton);
        });

        Button pauseButton = new Button();
        ImageView pauseIcon = new ImageView(new Image(getClass().getResourceAsStream("/game/com/fitnessmediaplayer/icons/pause.png")));
        pauseButton.setLayoutX(144);
        pauseButton.setLayoutY(10);
        pauseButton.setPrefSize(122, 62);
        pauseButton.setGraphic(pauseIcon);
        pauseButton.setOnAction(e -> {
            if(mediaPlayer!=null) mediaPlayer.pause();
            setActiveButton(pauseButton);
        });

        Button stopBtn = new Button();
        ImageView stopIcon = new ImageView(new Image(getClass().getResourceAsStream("/game/com/fitnessmediaplayer/icons/square.png")));
        stopBtn.setLayoutX(278);
        stopBtn.setLayoutY(10);
        stopBtn.setPrefSize(122, 62);
        stopBtn.setGraphic(stopIcon);
        stopBtn.setOnAction(e -> {
            if(mediaPlayer!=null) mediaPlayer.stop();
            setActiveButton(stopBtn);
        });

        Button prevBtn = new Button();
        ImageView prevIcon = new ImageView(new Image(getClass().getResourceAsStream("/game/com/fitnessmediaplayer/icons/skip-back.png")));
        prevBtn.setLayoutX(412);
        prevBtn.setLayoutY(10);
        prevBtn.setPrefSize(122, 62);
        prevBtn.setGraphic(prevIcon);
        prevBtn.setOnAction(e -> {
            playPrevious();
            setActiveButton(prevBtn);
        });

        Button nextBtn = new Button();
        ImageView nextIcon = new ImageView(new Image(getClass().getResourceAsStream("/game/com/fitnessmediaplayer/icons/skip-foreward.png")));
        nextBtn.setLayoutX(546);
        nextBtn.setLayoutY(10);
        nextBtn.setGraphic(nextIcon);
        nextBtn.setPrefSize(122, 62);
        nextBtn.setOnAction(e -> {
            playNext();
            setActiveButton(nextBtn);
        });

        controlButtons.clear();
        controlButtons.add(playButton);
        controlButtons.add(pauseButton);
        controlButtons.add(stopBtn);
        controlButtons.add(prevBtn);
        controlButtons.add(nextBtn);
        activeControlBtn = playButton;
        updateControlButtons();

        Label volumeIcon = new Label("🔊");
        volumeIcon.setLayoutX(15);
        volumeIcon.setLayoutY(90);
        volumeIcon.setStyle("-fx-text-fill: white;" +
                            "-fx-font-size: 16px;");

        Label volumeTextLabel= new Label("Volume:");
        volumeTextLabel.setLayoutX(45);
        volumeTextLabel.setLayoutY(92);
        volumeTextLabel.setStyle("-fx-text-fill: #b0c4d8;" +
                                 "-fx-font-size: 12px;");

        volSlider = new Slider(0,100,70);
        volSlider.setLayoutX(110);
        volSlider.setLayoutY(90);
        volSlider.setPrefWidth(470);
        volSlider.valueProperty().addListener((o,old,n)-> { if(mediaPlayer!=null) mediaPlayer.setVolume(n.doubleValue()/100); if(volPercentLabel!=null) volPercentLabel.setText(String.format("%.0f%%", n.doubleValue())); });
        volPercentLabel = new Label("70%");
        volPercentLabel.setLayoutX(600); volPercentLabel.setLayoutY(92); volPercentLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        controlspane.getChildren().addAll(playButton,
                                          pauseButton,
                                          stopBtn,
                                          prevBtn,
                                          nextBtn,
                                          volumeIcon,
                                          volumeTextLabel,
                                          volSlider,
                                          volPercentLabel);

        Pane playlistPane = new Pane();
        playlistPane.setLayoutX(710);
        playlistPane.setLayoutY(95);
        playlistPane.setPrefSize(375, 495);
        playlistPane.setStyle("-fx-background-color: #0f233a;" +
                              "-fx-border-color: #1a3a5a;" +
                              "-fx-border-radius: 10;" +
                              "-fx-background-radius: 10;");

        Label plTitle = new Label("PLAYLIST");
        plTitle.setLayoutX(15);
        plTitle.setLayoutY(12);
        plTitle.setStyle("-fx-text-fill: #8db4d8;" +
                         "-fx-font-size: 18px;" +
                         "-fx-font-weight: bold;" +
                         "-fx-letter-spacing: 1px;");

        Label listIco = new Label("☰");
        listIco.setLayoutX(335);
        listIco.setLayoutY(12);
        listIco.setStyle("-fx-text-fill: #3a9bff;" +
                         "-fx-font-size: 18px;");

        Pane headerLine = new Pane();
        headerLine.setLayoutX(0);
        headerLine.setLayoutY(45);
        headerLine.setPrefSize(375,1);
        headerLine.setStyle("-fx-background-color: #1a3a5a;");

        listContainer = new Pane();
        listContainer.setLayoutX(5);
        listContainer.setLayoutY(50);
        listContainer.setPrefSize(365, 380);

        Button addBtn = new Button("+ Add Video");
        addBtn.setLayoutX(15);
        addBtn.setLayoutY(430);
        addBtn.setPrefSize(160, 42);
        addBtn.setStyle("-fx-background-color: transparent;" +
                        "-fx-text-fill: #3a9bff;" +
                        "-fx-border-color: #3a9bff;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-weight: bold; -fx-cursor: hand;");

        addBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files","*.mp4","*.m4v","*.avi","*.mov","*.mkv"));
            List<File> files = chooser.showOpenMultipleDialog(stage);
            if(files!=null) {
                for(File f: files) videoList.add(new VideoItem(f.getName(), "00:00", f.getAbsolutePath()));
                refreshPlaylist();
            }
        });

        Button remBtn = new Button("— Remove Video");
        remBtn.setLayoutX(195);
        remBtn.setLayoutY(430);
        remBtn.setPrefSize(160, 42);
        remBtn.setStyle("-fx-background-color: transparent;" +
                        "-fx-text-fill: #ff5a5a;" +
                        "-fx-border-color: #ff5a5a;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;");
        remBtn.setOnAction(e -> {
            if(currentIndex>=0 && currentIndex < videoList.size()) {
                videoList.remove(currentIndex);
                if(currentIndex >= videoList.size()) currentIndex = videoList.size()-1;
                refreshPlaylist();
                if(videoList.isEmpty() && mediaPlayer!=null) mediaPlayer.stop();
            }
        });

        playlistPane.getChildren().addAll(plTitle, listIco, headerLine, listContainer, addBtn, remBtn);

        Pane kbPane = new Pane();
        kbPane.setLayoutX(15);
        kbPane.setLayoutY(597);
        kbPane.setPrefSize(1070, 75);
        kbPane.setStyle("-fx-background-color: #0f233a;" +
                        "-fx-border-color: #1a3a5a;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;");

        Label kbIco = new Label();
        ImageView kbIcon = new ImageView(new Image(getClass().getResourceAsStream("/game/com/fitnessmediaplayer/images/keyboard.png")));
        kbIco.setGraphic(kbIcon);
        kbIco.setLayoutX(15);
        kbIco.setLayoutY(15);
        kbIco.setPrefSize(40,40);
        kbIco.setStyle("-fx-background-color: #123a5a;" +
                       "-fx-text-fill: #3a9bff;" +
                       "-fx-font-size: 20px;" +
                       "-fx-alignment: center;" +
                       "-fx-background-radius: 8;" +
                       "-fx-border-color: #3a9bff;" +
                       "-fx-border-radius: 8;");
        Label kbTitle = new Label("Keyboard Shortcuts");
        kbTitle.setLayoutX(65);
        kbTitle.setLayoutY(27);
        kbTitle.setStyle("-fx-text-fill: #8aa0b8;" +
                         "-fx-font-size: 12px;");

        String[][] shortcuts = {
                {"SPACE","Play / Pause"},
                {"S","Stop"},
                {"N","Next"},
                {"P","Previous"},
                {"↑","Volume Up"},
                {"↓","Volume Down"},
                {"M","Mute / Unmute"}
        };
        for(int i=0;i<shortcuts.length;i++) {
            double x = 200 + i*125;
            Label keyLab = new Label(shortcuts[i][0]);
            keyLab.setLayoutX(x); keyLab.setLayoutY(12);
            keyLab.setPrefSize(55,28);
            keyLab.setStyle("-fx-background-color: #1a324f;" +
                            "-fx-text-fill: white;" +
                            "-fx-border-color: #2a5a8a;" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;" +
                            "-fx-alignment: center;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 11px;");

            Label descLab = new Label(shortcuts[i][1]);
            descLab.setLayoutX(x-5); descLab.setLayoutY(45);
            descLab.setPrefWidth(80);
            descLab.setStyle("-fx-text-fill: #8aa0b8;" +
                             "-fx-font-size: 10px;" +
                             "-fx-alignment: center;");

            if(i>0) {
                Pane sep = new Pane();
                sep.setLayoutX(x-18); sep.setLayoutY(15); sep.setPrefSize(1,40);
                sep.setStyle("-fx-background-color: #1a3a5a;");
                kbPane.getChildren().add(sep);
            }
            kbPane.getChildren().addAll(keyLab, descLab);
        }
        kbPane.getChildren().addAll(kbIco, kbTitle);

        mainPane.getChildren().addAll(header, videopPane, controlspane, playlistPane, kbPane);

        mainPane.widthProperty().addListener((o,oldW,newW) -> {
            double w = newW.doubleValue();
            header.setPrefWidth(w - 20);
            double leftW = w * 0.618;
            double rightW = w - leftW - 35;
            videopPane.setPrefWidth(leftW);
            controlspane.setPrefWidth(leftW);
            progPane.setPrefWidth(leftW);
            playlistPane.setLayoutX(leftW + 25);
            playlistPane.setPrefWidth(rightW);
            kbPane.setPrefWidth(w - 30);
            listContainer.setPrefWidth(rightW - 10);
            progressSlider.setPrefWidth(leftW - 160);
            volSlider.setPrefWidth(leftW - 210);
            endTimeLabel.setLayoutX(leftW - 80);
            volPercentLabel.setLayoutX(leftW - 80);
            full.setLayoutX(leftW - 30);
            motto.setLayoutX(w - 170);
            canceButton.setLayoutX(w - 270);
            musicButton.setLayoutX(w - 360);
            mediaView.setFitWidth(leftW);
        });

        mainPane.heightProperty().addListener((o,oldH,newH) -> {
            double h = newH.doubleValue();
            double videoH = h * 0.529;
            videopPane.setPrefHeight(videoH);
            mediaView.setFitHeight(videoH - 45);
            progPane.setLayoutY(videoH - 45);
            controlspane.setLayoutY(95 + videoH + 10);
            playlistPane.setPrefHeight(videoH + 135);
            kbPane.setLayoutY(h - 83);
        });

        mainPane.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.SPACE) {
                if (mediaPlayer!= null) {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.pause();
                        setActiveButtonByText("Pause");
                    } else {
                        mediaPlayer.play();
                        setActiveButtonByText("Play");
                    }
                }
                ev.consume();
            } else {
                switch (ev.getCode()) {
                    case S:
                        if (mediaPlayer!= null) mediaPlayer.stop();
                        setActiveButtonByText("Stop");
                        ev.consume();
                        break;
                    case N:
                        playNext();
                        setActiveButtonByText("Next");
                        ev.consume();
                        break;
                    case P:
                        playPrevious();
                        setActiveButtonByText("Previous");
                        ev.consume();
                        break;
                    case UP:
                        if (volSlider!= null) volSlider.setValue(Math.min(100, volSlider.getValue() + 5));
                        ev.consume();
                        break;
                    case DOWN:
                        if (volSlider!= null) volSlider.setValue(Math.max(0, volSlider.getValue() - 5));
                        ev.consume();
                        break;
                    case M:
                        if (mediaPlayer!= null) {
                            if (isMuted) {
                                mediaPlayer.setMute(false);
                                mediaPlayer.setVolume(beforeMuteVol);
                                volSlider.setValue(beforeMuteVol * 100);
                                isMuted = false;
                            } else {
                                beforeMuteVol = mediaPlayer.getVolume();
                                mediaPlayer.setMute(true);
                                isMuted = true;
                            }
                        }
                        ev.consume();
                        break;
                    default: break;
                }
            }
        });

        refreshPlaylist();
        Platform.runLater(() -> mainPane.requestFocus());
        return mainPane;
    }

    private void setActiveButton(Button btn) {
        activeControlBtn = btn;
        updateControlButtons();
    }

    private void setActiveButtonByText(String text) {
        for(Button b : controlButtons) {
            if(b.getText().equals(text)) { setActiveButton(b); break; }
        }
    }

    private void updateControlButtons() {
        for(Button b : controlButtons) {
            if(b == activeControlBtn) b.setStyle(BLUE_STYLE);
            else b.setStyle(DARK_STYLE);
        }
    }

    private void refreshPlaylist() {
        listContainer.getChildren().clear();
        for(int i=0;i<videoList.size();i++) {
            VideoItem item = videoList.get(i);
            Pane row = new Pane();
            row.setLayoutX(0); row.setLayoutY(i*52);
            row.setPrefSize(365, 50);
            boolean sel = (i==currentIndex);
            if(sel) row.setStyle("-fx-background-color: #1e4a7a;" +
                                 "-fx-background-radius: 8;" +
                                 "-fx-cursor: hand;");
            else row.setStyle("-fx-background-color: transparent;" +
                              "-fx-border-color: transparent transparent #1a2e4a transparent;" +
                              "-fx-border-width: 0.5;" +
                              "-fx-cursor: hand;");

            Label arrow = new Label("▶");
            arrow.setLayoutX(10);
            arrow.setLayoutY(17);
            arrow.setStyle("-fx-text-fill: #8aa0b8;" +
                           "-fx-font-size: 10px;");

            Label thumb = new Label("►");
            thumb.setLayoutX(30);
            thumb.setLayoutY(14);
            thumb.setPrefSize(26,22);
            thumb.setStyle("-fx-background-color: #c0d0e0;" +
                           "-fx-text-fill: #0f233a;" +
                           "-fx-alignment: center;" +
                           "-fx-background-radius: 4;" +
                           "-fx-font-size: 10px;");

            Label name = new Label(item.name.length()>20?item.name.substring(0,20)+"...":item.name);
            name.setLayoutX(68); name.setLayoutY(16);
            if(sel) name.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold;");
            else name.setStyle("-fx-text-fill: #d0dce8; -fx-font-size: 12px;");

            Label dur = new Label(item.duration);
            dur.setLayoutX(290); dur.setLayoutY(16);
            dur.setStyle("-fx-text-fill: #8aa0b8;" +
                         "-fx-font-size: 12px;");

            int idx = i;
            row.setOnMouseClicked(e -> { currentIndex=idx; playVideoAt(idx); });

            row.getChildren().addAll(arrow, thumb, name, dur);
            listContainer.getChildren().add(row);
        }
    }

    public void playVideoAt(int index) {
        if(index<0 || index>=videoList.size()) return;
        currentIndex=index;
        VideoItem item = videoList.get(index);
        File f = new File(item.filePath);
        if(!f.exists()) return;
        loadMedia(new Media(f.toURI().toString()));
        refreshPlaylist();
    }

    private void playNext() {
        if(videoList.isEmpty()) return;
        currentIndex = (currentIndex+1)%videoList.size();
        playVideoAt(currentIndex);
    }
    private void playPrevious() {
        if(videoList.isEmpty()) return;
        currentIndex = (currentIndex-1+videoList.size())%videoList.size();
        playVideoAt(currentIndex);
    }

    private void loadMedia(Media media) {
        if(mediaPlayer!=null) { mediaPlayer.stop(); mediaPlayer.dispose(); }
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setOnReady(() -> {
            Duration total = mediaPlayer.getTotalDuration();
            endTimeLabel.setText(formatTime(total));
            progressSlider.setMax(total.toSeconds());
            videoList.get(currentIndex).duration = formatTime(total);
            refreshPlaylist();
            double vw = media.getWidth(); double vh = media.getHeight();
            double areaW = 680;
            double areaH = 335;
            if(vw>0 && vh>0) {
                double scale = Math.min(areaW/vw, areaH/vh);
                double nw = vw*scale; double nh = vh*scale;
                mediaView.setFitWidth(nw); mediaView.setFitHeight(nh);
                mediaView.setLayoutX((areaW-nw)/2); mediaView.setLayoutY((areaH-nh)/2);
            }
        });
        mediaPlayer.currentTimeProperty().addListener((o,old,n)-> {
            if(!isSliding) { startTimeLabel.setText(formatTime(n)); progressSlider.setValue(n.toSeconds()); }
        });
        mediaPlayer.setOnEndOfMedia(() -> playNext());
        mediaPlayer.setVolume(volSlider.getValue()/100);
        mediaPlayer.play();
        setActiveButtonByText("Play");
    }

    private Button createControlBtn(String text, String iconPath, boolean highlighted) {
        Button btn = new Button(text);
        if(highlighted) btn.setStyle(BLUE_STYLE);
        else btn.setStyle(DARK_STYLE);
        try {
            var s = getClass().getResourceAsStream(iconPath);
            if(s==null) s = getClass().getResourceAsStream(iconPath.replace("/icon/","/icons/"));
            if(s==null) s = getClass().getResourceAsStream(iconPath.replace("skip-foreward","skip-forward"));
            if(s!=null) {
                ImageView iv = new ImageView(new Image(s));
                iv.setFitWidth(18); iv.setFitHeight(18);
                btn.setGraphic(iv);
                btn.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
            }
        } catch (Exception e) {}
        return btn;
    }

    private String formatTime(Duration d) {
        if(d==null || d.isUnknown()) return "00:00";
        int m = (int)d.toMinutes(); int s = (int)(d.toSeconds()%60);
        return String.format("%02d:%02d", m,s);
    }

    public static class VideoItem {
        public String name, duration, filePath;
        public VideoItem(String n, String dur, String path){ name=n; duration=dur; filePath=path; }
    }

    public static void main(String[] args){ launch(args); }
}