/*
 * Copyright (c) 2015, 2016 Matthew J. Nicholls, Samuel Pengelly,
 * Parham Ghassemi, William R. Dix
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
