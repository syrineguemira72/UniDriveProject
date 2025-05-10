package edu.unidrive.controllers;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;

public class ComboBox<T> extends Node {
    public ChoiceDialog<Object> getSelectionModel() {
        return null;
    }

    public void setItems(ObservableList<T> genderOptions) {
    }

    public <E> ObservableList<E> getItems() {
        return null;
    }

    public boolean getValue() {
        return false;
    }

    public void setValue(Object o) {
    }

    public void setOnAction(Object o) {
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
