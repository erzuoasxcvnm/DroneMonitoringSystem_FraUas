module drone.monitoring.system {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires org.yaml.snakeyaml;
    requires org.json;
    requires eu.hansolo.tilesfx;
    requires eu.hansolo.fx.countries;
    requires eu.hansolo.fx.heatmap;
    requires eu.hansolo.toolbox;
    requires eu.hansolo.toolboxfx;
    requires java.logging;

    exports main;
    exports main.java.api;
    exports main.java.gui.controllers;
    exports main.java.gui.components.factory;
    exports main.java.gui.components.buttons;
    exports main.java.gui.components.panels;
    exports main.java.gui.components.other;
    exports main.java.gui.components.gauges;
    exports main.java.gui.views;
    exports main.java.gui.interfaces;
    exports main.java.managers;
    exports main.java.model;
    exports main.java.services;
    exports main.java.util;

}