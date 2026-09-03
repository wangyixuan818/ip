package noms.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * A single chat bubble: a wrapped text label paired with the speaker's avatar.
 *
 * The control loads its own layout from {@code DialogBox.fxml} using the
 * {@code fx:root} technique, so each instance is a self-contained {@link HBox}.
 * User messages keep the default layout (avatar on the right); Noms' replies
 * are {@link #flip() flipped} so its avatar sits on the left, making the two
 * sides of the conversation easy to tell apart.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image image) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            // The FXML is bundled with the app, so a failure here is a
            // programming error; surface it rather than showing a blank bubble.
            e.printStackTrace();
        }
        dialog.setText(text);
        dialog.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(dialog, Priority.ALWAYS);
        displayPicture.setImage(image);
    }

    /**
     * Flips the bubble so the avatar appears to the left of the text, used to
     * distinguish Noms' replies from the user's messages.
     */
    private void flip() {
        ObservableList<Node> nodes = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(nodes);
        this.getChildren().setAll(nodes);
        this.setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Creates a bubble for the user's message (avatar on the right).
     *
     * @param text the user's message
     * @param image the user's avatar
     * @return the user's dialog box
     */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /**
     * Creates a bubble for one of Noms' replies (avatar on the left).
     *
     * @param text Noms' reply
     * @param image Noms' avatar
     * @return the flipped dialog box
     */
    public static DialogBox getNomsDialog(String text, Image image) {
        DialogBox box = new DialogBox(text, image);
        box.flip();
        return box;
    }
}
