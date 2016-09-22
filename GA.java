import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import java.util.ArrayList;
import java.lang.InterruptedException;
import java.util.*;

// Each MyPolygon has a color and a Polygon object
class MyPolygon {

	Polygon polygon;
	Color color;

	public MyPolygon(Polygon _p, Color _c) {
		polygon = _p;
		color = _c;
	}

	public Color getColor() {
		return color;
	}

	public Polygon getPolygon() {
		return polygon;
	}
  // @Override
   //protected MyPolygon clone() throws CloneNotSupportedException{    
     // return(MyPolygon) super.clone();  
      //}   

}


// Each GASolution has a list of MyPolygon objects
class GASolution {

	ArrayList<MyPolygon> shapes;

	// width and height are for the full resulting image
	int width, height;

	public GASolution(int _width, int _height) {
		shapes = new ArrayList<MyPolygon>();
		width = _width;
		height = _height;
	}

	public void addPolygon(MyPolygon p) {
		shapes.add(p);
	}	

	public ArrayList<MyPolygon> getShapes() {
		return shapes;
	}

	public int size() {
		return shapes.size();
	}
   
	// Create a BufferedImage of this solution
	// Use this to compare an evolved solution with 
	// a BufferedImage of the target image
	//
	// This is almost surely NOT the fastest way to do this...
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (MyPolygon p : shapes) {
			Graphics g2 = image.getGraphics();			
			g2.setColor(p.getColor());
			Polygon poly = p.getPolygon();
			if (poly.npoints > 0) {
				g2.fillPolygon(poly);
			}
		}
		return image;
	}

	public String toString() {
		return "" + shapes;
	}
}


// A Canvas to draw the highest ranked solution each epoch
class GACanvas extends JComponent{

    int width, height;
    GASolution solution;

    public GACanvas(int WINDOW_WIDTH, int WINDOW_HEIGHT) {
    	width = WINDOW_WIDTH;
    	height = WINDOW_HEIGHT;
    }
 
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void setImage(GASolution sol) {
  	    solution = sol;
    }

    public void paintComponent(Graphics g) {
		BufferedImage image = solution.getImage();
		g.drawImage(image, 0, 0, null);
    }
}


public class GA extends JComponent{
	
    GACanvas canvas;
    int width, height;
    BufferedImage realPicture;
    ArrayList<GASolution> population;
    LinkedList pop_fitness= new LinkedList();
        // Adjust these parameters as necessary for your simulation
    double MUTATION_RATE = 0.05;
    double CROSSOVER_RATE = .95;
    int MAX_POLYGON_POINTS = 5;
    int MAX_POLYGONS = 10;

    public GA(GACanvas _canvas, BufferedImage _realPicture) {
        canvas = _canvas;
        realPicture = _realPicture;
        width = realPicture.getWidth();
        height = realPicture.getHeight();
        //ask about this line
        population = new ArrayList<GASolution>();

        // You'll need to define the following functions
        createPopulation(100);	// Make 50 new, random chromosomes
    }
    
    public MyPolygon MyPolygon_Maker()
    {
         /** This method makes the polygons */
         Polygon p = new Polygon();
         Random random= new Random();
         int r = random.nextInt(256);
         int g = random.nextInt(256);
         int b = random.nextInt(256);
         Color c = new Color(r,g,b);
         for(int i=0; i<MAX_POLYGON_POINTS; i++)
         {
            int x=random.nextInt(width+50);
            int y=random.nextInt(height+50);
            p.addPoint(x,y);
         }//end of for loop
         MyPolygon p_object= new MyPolygon(p, c);
         return p_object;
    }//end of method
    
    public void createPopulation(int size)
    {
      /** This method creates the initial population. */
      for (int i=0; i< size; i++)
      {
         GASolution chromosome= new GASolution(width,height);
         for (int j=0;j <MAX_POLYGONS; j++)
         {
            MyPolygon p = MyPolygon_Maker();
            //add each polygon into a chromosome
            chromosome.addPolygon(p);
         }//end of inner for loop
         population.add(chromosome);
      }//end of outer for loop
     
    }//end of createPopulation
    
    public GASolution pickfitparent()
    { 
      /** This method picks the parent by weighing it with its fitness */
      double total_fitness=0;
      for (int i=0; i< pop_fitness.size();i++)
      {
         total_fitness=total_fitness+(double)pop_fitness.get(i);
      }//end of for loop 
      //the following four lines of code I referenced from stack overflow to pick random
      //doubles in a a certain range
      double start = 0;
      double end = total_fitness;
      double random = new Random().nextDouble();
      double result = start + (random * (end - start));
      int index=-1; 
      //I create a weighted spinner like we did in class, referenced
      while (result>0)
      {
         index+=1;
         result= result-(double)pop_fitness.get(index);
      }//end of while loop
      return population.get(index);
             
    }//end of pick fit parent
    
    
    public int pick_points(int input)
    {
      /** This method picks the cordinate for the polygons for mutation. */
      double far_or_not= .8;
      Random ran= new Random();
      double chance=ran.nextDouble();
      double shrink_or_expand_rate=.5;
      //has 20 percent chance of pickign a random points, 80% chance
      //of pickign a point that is +-50
      if (chance < far_or_not)
      {
         int num=ran.nextInt(50);
         double shrink_or_expand=ran.nextDouble();
         //the point has a 50% of getting smaller or larger
         if(shrink_or_expand> shrink_or_expand_rate)
         {
             return input-num;
         }
         else{
             return input+num;
         }//end of inner else
      }//end of if
      else {
            return ran.nextInt(width);
       
      }//end of else
      }//end of pick points
      
      public int pick_points_color(int input)
    {
      /** This method picks a value for the RBG for mutation. Similar to pick_points */
      double far_or_not= .8;
      Random ran= new Random();
      double chance=ran.nextDouble();
      double shrink_or_expand_rate=.5;
      //80 percent chance of picking a value that is +=50
      if (chance < far_or_not)
      {
         int num=ran.nextInt(50);
         double shrink_or_expand=ran.nextDouble();
         //50-50 chance of increasing or decreasing color value
         if(shrink_or_expand> shrink_or_expand_rate)
         {
            if (input +num > 255)
            {
             int difference=  (input+num)-255;
             return input+ (num-difference);
            }
            else{return input+num; }
         }
         else{
            if (input -num < 0)
            {
             int difference= 0 - (input-num);
             return input- (num-difference);
            }
            else{return input-num; }

            
         }//end of inner else
      }//end of if
      //20% chance of picking a completely random value
      else {
            return ran.nextInt(width);
       
      }//end of else
      }//end of pick color points
      
    public GASolution mutate(GASolution child)
    {
      /** This method mutate each chromosome by calling pick points and pick color points. */
        Random ran= new Random();
        //goes through all the polygon in the chidl and mutate them
        for (int num=0; num<child.shapes.size(); num++){
        MyPolygon polygon_obj=child.shapes.get(num);
        Polygon polygon=polygon_obj.getPolygon();
        int num2=ran.nextInt(MAX_POLYGON_POINTS);
        //call pick_points to mutate size
        for (int i=0;i<MAX_POLYGON_POINTS;i++){
        polygon.xpoints[i]=pick_points(polygon.xpoints[i]);
        polygon.ypoints[i]=pick_points(polygon.ypoints[i]);
        }

        Color color= polygon_obj.getColor();
        
        int difference=0;
        int r =color.getRed();
        int g= color.getGreen();
        int b=color.getBlue();
        //call pick_points_color to mutate color
        r=pick_points_color(r);
        g=pick_points_color(g);
        b=pick_points_color(b);
        Color new_c = new Color(r,g,b);
        polygon_obj.color= new_c; 
        child.shapes.set(num,polygon_obj);
        }
        
        return child;
    }//end of mutate
    
     public GASolution copyparent(GASolution p1)
    {
      /** This method does the deep copy of a parent by calling deepcopy, which does
      a deep copy of a sinle mypolygon object in the parent */
      GASolution child= new GASolution (width, height);
      for (int i=0; i< MAX_POLYGONS; i++)
      {
        
         child.shapes.add(deepcopy(p1.getShapes().get(i)));
      }
      return child;
    }//end of cross over
    
    public void generate_newpop()
    {
     /** This method generates new population by calling pikfitparent and crossover as well as
     mutation */
      ArrayList<GASolution> new_pop= new ArrayList();
      for (int i=0; i< population.size(); i++)
      {
         Random r =new Random();
         double num=r.nextDouble();
         //System.out.println(num+ " random cross rate");
         if (num < CROSSOVER_RATE){
         GASolution p1= pickfitparent();
         GASolution p2= pickfitparent();
         GASolution child=crossover(p1,p2);
         num=r.nextDouble();
         if(num<MUTATION_RATE)
         {
            child=mutate(child);
         }
         new_pop.add(child);
         }
         else {
          new_pop.add( copyparent(pickfitparent()));
         }
         
      }//end of for loop
      population=new_pop;
    
    }//end of generate_newpop method
    
    public MyPolygon deepcopy(MyPolygon p)
    {
      /** This method does the deep copy of the actual mypolygon object. */
      Color color=p.getColor();
      Polygon polygon=p.getPolygon();
      int [] x_points=polygon.xpoints.clone();
      int [] y_points=polygon.ypoints.clone();
      int red = color.getRed();
      int green= color.getGreen();
      int blue= color.getBlue();
      Color new_color= new Color(red, green, blue);
      Polygon new_pol= new Polygon(x_points, y_points,polygon.npoints);
      MyPolygon new_pol_obj = new MyPolygon(new_pol, new_color);
      return new_pol_obj;
    }//end of deepcopy
    
    public GASolution crossover(GASolution p1, GASolution p2)
    {
      /** This method does the crossover by pick 5 mypolygon object from each parent,
      in an alternating manner */
      GASolution child= new GASolution (width, height);
      for (int i=0; i< MAX_POLYGONS/2; i++)
      {
        
         child.shapes.add(deepcopy(p1.getShapes().get(i)));
         child.shapes.add(deepcopy(p2.getShapes().get(i+5)));
      }
      return child;
    }//end of cross over

    // YOUR CODE GOES HERE!
    public void fitness()
    {
      /** This method calculates the fitness of each chromosome for the whole population. */
     LinkedList new_fitness= new LinkedList();
     for (int k=0; k<population.size();k++)
     {
      GASolution chromosome = population.get(k);
      double sum=0;
      double fitness=0;
      int size=500;
      BufferedImage solution_image=chromosome.getImage();
      for (int i=0;i <size;i++)
      {
        Random random= new Random();
        //grab the color pixels from target image
        int x=random.nextInt(width);
        int y=random.nextInt(height);
        int [] target_RGB=get_color(realPicture,x,y);
        int [] sol_RGB=get_color(solution_image,x,y);
        sum+=cal_distance(target_RGB, sol_RGB);        
      }//end of for loop
      //adding the chromosome's fitness to the list of population fitness
      double average= sum/(double)size;
      fitness= 1/average;
      new_fitness.add(fitness);
      }//end of for outer loop
      pop_fitness=new_fitness;
    }//end of fitness method
    
    public double cal_distance(int [] target_RGB,int [] sol_RGB)
    {
      /** This method calculates the Euclidean distance between RGB values. */
      double distance=0;
      for (int i=0; i<3; i++)
      {
         int difference=target_RGB[i]-sol_RGB[i];
         difference= difference*difference;
         distance+=(double)difference;
      }//end of for loop
     // double d = (double) distance;
      distance = Math.sqrt(distance);
      return distance;
    }//end of cal_distance
    
	 public int [] get_color(BufferedImage image, int x, int y)
    {
       /** This method get the color from the pixels */
        Color mycolor= new Color (image.getRGB(x,y));
        int red = mycolor.getRed();
        int green= mycolor.getGreen();
        int blue= mycolor.getBlue();
        int [] color_array= new int [3];
        color_array[0]= red;
        color_array[1]= green;
        color_array[2]=blue;
        return color_array;
    }//end of method
    
    public int bestSol()
    {
      /** This method find the best solution. */
      double max=0;
      int max_index=0;
      for(int i=0;i<population.size();i++)
      {
         if((double)pop_fitness.get(i)>max)
         {
            max=(double)pop_fitness.get(i);
            max_index=i;
         }
      }
      System.out.println(max);
      
      return max_index;
    }//end of bestSol

    public void runSimulation() {
     /** This method runs the simulation*/
      fitness();
     long start = System.currentTimeMillis();
    for (int i=0; i< 10000; i++)
    {
         generate_newpop();
         fitness();
         //display every 50 generations
         if (i%50==0 ){
         int best=bestSol();
         canvas.setImage(population.get(best));
         canvas.repaint();
         System.out.println(i + " generations");
         }//end of if statement
    }
     long end = System.currentTimeMillis();
     long total_time= end-start;
     double t= 60000;
     System.out.println((double)total_time/t +" running time in minutes");

    }//end of method

    public static void main(String[] args) throws IOException {

        String realPictureFilename = "test.jpg";

        BufferedImage realPicture = ImageIO.read(new File(realPictureFilename));

        JFrame frame = new JFrame();
        frame.setSize(realPicture.getWidth(), realPicture.getHeight());
        frame.setTitle("GA Simulation of Art");
	
        GACanvas theCanvas = new GACanvas(realPicture.getWidth(), realPicture.getHeight());
        //Make an empty solution here
        GASolution chromosome= new GASolution(realPicture.getWidth(),realPicture.getHeight());
        for (int i=0; i<10;i++)
        {
          Polygon p = new Polygon();
          Color c = new Color(0,0,0);
          MyPolygon p_object= new MyPolygon(p, c);
          chromosome.addPolygon(p_object);

         
        }
        theCanvas.setImage(chromosome);
        theCanvas.repaint();
        frame.add(theCanvas);
        frame.setVisible(true);
  

        GA pt = new GA(theCanvas, realPicture);
            pt.runSimulation();
    }
}




