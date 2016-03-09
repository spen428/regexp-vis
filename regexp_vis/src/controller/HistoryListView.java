package controller;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.CommandHistory;

/**
 * Extension of {@link ListView} for displaying the {@link CommandHistory}
 *
 * @author sp611
 *
 */
public class HistoryListView extends ListView<Label> {

    public static final int HISTORY_LIST_MIN_WIDTH_PX = 140;
    public static final double LISTVIEW_LABEL_WIDTH_OFFSET = -20;

    public HistoryListView() {
        super();
        this.setPadding(new Insets(0));
        this.setMinWidth(HISTORY_LIST_MIN_WIDTH_PX);
        this.setWidth(HISTORY_LIST_MIN_WIDTH_PX);
    }

    /**
     * The width property of this {@link ListView}, plus the
     * {@link #LISTVIEW_LABEL_WIDTH_OFFSET}
     */
    public final DoubleBinding offsetWidthProperty() {
        return this.widthProperty().add(LISTVIEW_LABEL_WIDTH_OFFSET);
    }

    /**
     * Adds an entry with the given text to the {@link ListView}, converting it
     * first to a {@link Label} so that the text can automatically wrap
     *
     * @param text
     *            the text
     */
    public void addItem(String text) {
        this.getItems().add(createListViewLabel(text));
    }

    private Label createListViewLabel(String text) {
        Label label = new Label(text);
        label.setPadding(new Insets(0));
        label.setWrapText(true);
        label.setMinWidth(
                HISTORY_LIST_MIN_WIDTH_PX + LISTVIEW_LABEL_WIDTH_OFFSET);
        label.prefWidthProperty().bind(this.offsetWidthProperty());
        return label;
    }

}
