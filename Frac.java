import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
 
class Frac extends JFrame {
	
	int width;
	int height;
	public int nodes=8;
	//public static int nodes=0;
	
	public Graphics gr;
	
    public Frac() {
        super("Fractal Tree");
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int)screenSize.getWidth();
        height = (int)screenSize.getHeight();
        
        setBounds(0, 0, (int)width, (int)height);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        this.setVisible(true);
        this.setOpacity(1);
        this.setBackground(Color.blue);
        
    }
    
    public static int randInt(int min, int max) {
    	Random rand = new Random();
    	int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    public List<Integer> getSequence(int iterations) {
        List<Integer> turnSequence = new ArrayList<Integer>();
        for (int i = 0; i < iterations; i++) {
            List<Integer> copy = new ArrayList<Integer>(turnSequence);
            Collections.reverse(copy);
            turnSequence.add(1);
            for (Integer turn : copy) {
                turnSequence.add(-turn);
            }
        }
        return turnSequence;
    }
    
    private void drawBranch(Graphics g, int x1, int y1, double angle, int depth, int max){
    	
    	g.setColor(new Color(83,53,10));
    	Graphics2D g2D = (Graphics2D) g; 
    	g2D.setStroke(new BasicStroke(2*depth));
    	
        int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * depth * 10.0);
        int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * depth * 10.0);
        g2D.drawLine(x1, y1, x2, y2);
        drawTree(g, x2, y2, angle - 20, depth - 1, max);
        drawTree(g, x2, y2, angle + 20, depth - 1, max);
    	
    }
    
    private void drawLeafs(Graphics g, int x1,int y1, double angle, int depth){
    	
    	Random rand = new Random();
    	
    	float re = rand.nextFloat();
    	float gr = rand.nextFloat();
    	float bl = rand.nextFloat();
    	
    	Color randomColor = new Color(re, gr, bl);
        g.setColor(randomColor);
        
        Graphics2D g2D = (Graphics2D) g; 
    	g2D.setStroke(new BasicStroke(2));
    	
    	List<Integer> turns;
        double startingAngle, side;
        int iter = 6;
        
        turns = getSequence(iter * (depth+nodes) / nodes);
        //startingAngle = -iter * (Math.PI / 4);
        startingAngle = angle;
        //side = 150 / Math.pow(2, iter / 2.);
    	//System.out.println(side);
        side = 1.5 * (depth+nodes) / nodes;
        
        
    	//double angle = startingAngle;
        //int x1 = 130, y1 = 150;
        int x2 = x1 + (int) (Math.cos(angle) * side);
        int y2 = y1 + (int) (Math.sin(angle) * side);
        g.drawLine(x1, y1, x2, y2);
        x1 = x2;
        y1 = y2;
        for (Integer turn : turns) {
            angle += turn * (Math.PI / 2);
            x2 = x1 + (int) (Math.cos(angle) * side);
            y2 = y1 + (int) (Math.sin(angle) * side);
            g.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }
    	
    }
 
    private void drawTree(Graphics g, int x1, int y1, double angle, int depth, int max) {
  
    	 
        if (depth == 0){ //draw only leafs 
        	drawLeafs(g,x1,y1,angle,depth);
        }
        
        else{ //draw branches
        	drawBranch(g,x1,y1,angle,depth,max);
        	if(depth<=max-3)
        		drawLeafs(g,x1,y1,angle,depth);
        }
    }
 
    class Pereche{
    	public int h,w;
    	public Pereche(int h,int w){
    		this.h=h;
    		this.w=w;
    	}
    }
    
    public void drawForest(Graphics g){
    	
    	//versiunea random
    	/*int RANGE = 3;
    	List<Pereche> sack = new ArrayList<>();
    	for (int i = 1; i <= RANGE; i++)
    		for(int j = 0; j < RANGE; j++)
    			sack.add(new Pereche(i,j));
    	Collections.shuffle(sack);
    	
    	
    	int size=3;
    	List<Pereche> lst = new ArrayList<>();
    	for(int i=0;i<size;i++){
    		lst.add(sack.get(i));
    	}
    	
    	
    	Collections.sort(lst, new Comparator<Pereche>() {
    	    @Override
    	    public int compare(final Pereche o1, final Pereche o2) {
    	        // TODO: implement your logic here
    	    	return o1.h - o2.h;
    	    }
    	});
    	
    	for(int i=0;i<lst.size();i++){
    		int rand_h=lst.get(i).h;
    		int rand_w=lst.get(i).w;
    		
    		int h = rand_h * (height/3);
        	int w = rand_w * (width/3)+(width/6);
        	
        	drawTree(g, w, h, -90, nodes-(3-rand_h),nodes-(3-rand_h));
        }*/

    	//versiunea non-random
    	drawTree(g, 0*(width/3)+(width/6), 1*(height/3), -90, nodes-2, nodes-2);
    	drawTree(g, 2*(width/3)+(width/6), 2*(height/3), -90, nodes-1, nodes-1);
    	drawTree(g, 1*(width/3)+(width/6), 3*(height/3), -90, nodes, nodes);
    	
    }
    @Override
    public void paint(Graphics g) {
    	
    	//getContentPane().setBackground(new Color(0,123,12));
    	g.setColor(new Color(0,123,12));
    	Rectangle r = new Rectangle(0,0,width,height);
        g.fillRect((int)r.getX(), (int)r.getY(), (int)r.getWidth(), (int)r.getHeight());  
    	
    	drawForest(g);
    	
    }
    
    public static void main(String[] args){
    	Frac f = new Frac();
    	f.setVisible(true);
    }
 
}
