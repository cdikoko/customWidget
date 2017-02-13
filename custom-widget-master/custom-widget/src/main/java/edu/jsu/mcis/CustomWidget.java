package edu.jsu.mcis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.lang.Math;

public class CustomWidget extends JPanel implements MouseListener {
    private java.util.List<ShapeObserver> observers;
    private final Color OCTAGON_SELECTED_COLOR = Color.red;
	private final Color HEXAGON_SELECTED_COLOR = Color.green;
    private final Color DEFAULT_COLOR = Color.white;
	private boolean hexagonSelected = true;
	private boolean octagonSelected = false;
    private Point[] hexagonVertex;
	private Point[] octagonVertex;

    
    public CustomWidget() {
        observers = new ArrayList<>();
        hexagonVertex = new Point[6];
		octagonVertex = new Point[8];
        for(int i = 0; i < hexagonVertex.length; i++) { hexagonVertex[i] = new Point(); }
		for(int i = 0; i < octagonVertex.length; i++) { octagonVertex[i] = new Point(); }
        Dimension dim = getPreferredSize();
		calculateVertices(hexagonVertex,dim.width, dim.height, 0, dim.width/3, dim.height/2);
		calculateVertices(octagonVertex,dim.width, dim.height, Math.PI * 0.125, dim.width - (dim.width/3), dim.height/2);
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(this);
    }

    
    public void addShapeObserver(ShapeObserver observer) {
        if(!observers.contains(observer)) observers.add(observer);
    }
    public void removeShapeObserver(ShapeObserver observer) {
        observers.remove(observer);
    }
    private void notifyObservers() {
        ShapeEvent event = new ShapeEvent(hexagonSelected, octagonSelected);
        for(ShapeObserver obs : observers) {
            obs.shapeChanged(event);
        }
    }
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

	private void calculateVertices(Point[] vertices, int width, int height, double offsetRad, int offsetX, int offsetY){
        int size = Math.min(width, height) / 2;

        for(int i = 0; i < vertices.length; i++){
            double rads = offsetRad + (i * (Math.PI / (vertices.length / 2)));
            double x = Math.cos(rads);
            double y = Math.sin(rads);
            vertices[i].setLocation(
                offsetX + (x * (size/4)),
                offsetY + (y * (size/4))
            );
        }
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
		Dimension dim = getPreferredSize();
		calculateVertices(hexagonVertex,dim.width, dim.height, 0, dim.width/3, dim.height/2);
		calculateVertices(octagonVertex,dim.width, dim.height, Math.PI * 0.125, dim.width - (dim.width/3), dim.height/2);
        Shape [] shapes = getShapes();
        g2d.setColor(Color.black);
        g2d.draw(shapes[0]);
		g2d.draw(shapes[1]);
        if(hexagonSelected == true) {
            g2d.setColor(HEXAGON_SELECTED_COLOR);
			g2d.fill(shapes[0]);
			g2d.setColor(DEFAULT_COLOR);
			g2d.fill(shapes[1]);
			hexagonSelected = false;
			octagonSelected = true;
        }
		else if(octagonSelected == true){
			g2d.setColor(OCTAGON_SELECTED_COLOR);
			g2d.fill(shapes[1]);
			g2d.setColor(DEFAULT_COLOR);
			g2d.fill(shapes[0]);
			hexagonSelected = true;
			octagonSelected = false;
		}
    }

    public void mouseClicked(MouseEvent event) {
        Shape [] shape = getShapes();
        if(shape[0].contains(event.getX(), event.getY())) {
			hexagonSelected = true;
			octagonSelected = false;
            notifyObservers();
        }
		else if(shape[1].contains(event.getX(), event.getY())){
				hexagonSelected = false;
				octagonSelected = true;
				notifyObservers();
		}
		
        repaint(shape[0].getBounds());
		repaint(shape[1].getBounds());
    }
    public void mousePressed(MouseEvent event) {}
    public void mouseReleased(MouseEvent event) {}
    public void mouseEntered(MouseEvent event) {}
    public void mouseExited(MouseEvent event) {}
    
    public Shape [] getShapes() {
		Shape [] shapes = new Shape[2];
        int[] x = new int[hexagonVertex.length];
        int[] y = new int[hexagonVertex.length];
        for(int i = 0; i < hexagonVertex.length; i++) {
            x[i] = hexagonVertex[i].x;
            y[i] = hexagonVertex[i].y;
        }
        shapes[0] = new Polygon(x, y, hexagonVertex.length);
		
        x = new int[octagonVertex.length];
        y = new int[octagonVertex.length];
        for(int i = 0; i < octagonVertex.length; i++) {
            x[i] = octagonVertex[i].x;
            y[i] = octagonVertex[i].y;
        }
        shapes[1] = new Polygon(x, y, octagonVertex.length);
        return shapes;
    }
	


	public boolean IsSelected(){
		return hexagonIsSelected() || octagonIsSelected();
	}
	public boolean hexagonIsSelected(){
		return hexagonSelected;
	}
	public boolean octagonIsSelected(){
		return octagonSelected;
	}

	public static void main(String[] args) {
		JFrame window = new JFrame("Custom Widget");
        window.add(new CustomWidget());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(300, 300);
        window.setVisible(true);
	}
}