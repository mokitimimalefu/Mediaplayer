module game.com.fitnessmediaplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens game.com.fitnessmediaplayer to javafx.fxml;
    exports game.com.fitnessmediaplayer;
}