import javax.swing.*;
import java.awt.*;

public class ColourRing extends JFrame
{
    public static void main(final String... args)
    {   lspan = args.length > 0 ? Integer.parseInt(args[0]) : 1;
	hspan = args.length > 1 ? Integer.parseInt(args[1]) : 0;
	extra = args.length > 2 ? Integer.parseInt(args[2]) : 2;
	step  = args.length > 3 ? Integer.parseInt(args[3]) : 3;
	(new ColourRing()).setVisible(true);
    }
    
    static int lspan;
    static int hspan;
    static int extra;
    static int step;
    
    public ColourRing()
    {
	super("ColourRing");
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.pack();
	this.setSize(new Dimension(1000, 1000));
	this.setLocation(new Point(this.getLocation().x + 300,
				   this.getLocation().y + 100 - 25));
	
	double[] f0 = {205, 101, 108};  f0 = toLinear((int)(f0[0]), (int)(f0[1]), (int)(f0[2]));
	double[] f1 = {164, 110, 176};  f1 = toLinear((int)(f1[0]), (int)(f1[1]), (int)(f1[2]));
	double[] f2 = { 36, 149, 190};  f2 = toLinear((int)(f2[0]), (int)(f2[1]), (int)(f2[2]));
	double[] f3 = {  0, 169, 159};  f3 = toLinear((int)(f3[0]), (int)(f3[1]), (int)(f3[2]));
	double[] f4 = { 50, 166, 121};  f4 = toLinear((int)(f4[0]), (int)(f4[1]), (int)(f4[2]));
	double[] f5 = {156, 173,  81};  f5 = toLinear((int)(f5[0]), (int)(f5[1]), (int)(f5[2]));
	double[] f6 = {204, 173,  71};  f6 = toLinear((int)(f6[0]), (int)(f6[1]), (int)(f6[2]));
	double[] f7 = {218, 128,  77};  f7 = toLinear((int)(f7[0]), (int)(f7[1]), (int)(f7[2]));
	
	double[] fX = {f0[0], f1[0], f2[0], f3[0], f4[0], f5[0], f6[0], f7[0]};
	double[] fY = {f0[1], f1[1], f2[1], f3[1], f4[1], f5[1], f6[1], f7[1]};
	double[] fZ = {f0[2], f1[2], f2[2], f3[2], f4[2], f5[2], f6[2], f7[2]};
	
	rgb = new int[400 / step][];
	int index = rgb.length / 8;
	
	for (int hue = 450; hue < 850; hue += step)
	{
	    int midH = (hue / 50) & 7;
	    int lowH = (midH - lspan) & 7;
	    
	    int[] hs = new int[lspan + 1 + hspan];
	    for (int i = 0; i < hs.length; i++)
		hs[i] = (lowH + i) & 7;
	    
	    int hsl = extra * hs.length;
	    double[][] xs = new double[hsl][hsl];
	    double[] X = new double[hsl];
	    double[] Y = new double[hsl];
	    double[] Z = new double[hsl];
	    for (int y = 0; y < hsl; y++)
	    {
		X[y] = fX[(lowH + y) & 7];
		Y[y] = fY[(lowH + y) & 7];
		Z[y] = fZ[(lowH + y) & 7];
		double c = 1, m = 400 + (lowH + y) * 50;
		for (int x = 0; x < hsl; x++)
		    xs[y][x] = c *= m;
	    }
	    
	    X = eliminate(xs, X);
	    Y = eliminate(xs, Y);
	    Z = eliminate(xs, Z);
	    
	    double h = 1;
	    double x = 0;
	    double y = 0;
	    double z = 0;
	    
	    for (int i = 0; i < hsl; i++)
	    {
		h *= hue;
		x += h * X[i];
		y += h * Y[i];
		z += h * Z[i];
	    }
	    
	    rgb[(++index) % rgb.length] = toStandard(x, y, z);
	}
    }
    
    private int[][] rgb;
    
    
    public void paint(final Graphics g)
    {
	int x = 40;
	int y = 40;
	
	double[] last = toLab(rgb[rgb.length - 1][0], rgb[rgb.length - 1][1], rgb[rgb.length - 1][2]);
	double[] diffs = new double[rgb.length + 1];
	double adiff = 0;
	
	for (int i = 0; i < rgb.length; i++)
	{
	    double[] cur = toLab(rgb[i][0], rgb[i][1], rgb[i][2]);
	    
	    double dl = cur[0] - last[0];
	    double da = cur[1] - last[1];
	    double db = cur[2] - last[2];
	    double diff = dl * dl + da * da + db * db;
	    diff = Math.pow(diff, 0.5);
	    adiff += diffs[i] = diff;
	    
	    last = cur;
	}
	diffs[rgb.length] = diffs[0];
	adiff /= rgb.length;
	for (int i = 0; i <= rgb.length; i++)
	    diffs[i] = Math.log(diffs[i] / adiff + 1) / Math.log(2.);
	
	Color[] colours = new Color[800];
	
	int arc = 0;
	for (int i = 0; i < rgb.length; i++)
	{
	    double z = i - rgb.length * 5 / 8;
	    if (z < 0.)
		z = -z;
	    z = z * 2 / rgb.length;
	    if (z > 1.)
		z = 2 - z;
	    z /= 3 * 2;
	    z += 1;
	    
	    Color colour;
	    g.setColor(colour = new Color(rgb[i][0], rgb[i][1], rgb[i][2]));
	    int x1 = x;
	    x += (step << 1) * z;
	    for (int j = x1 - 40, n = x - 40; j < n; j++)
		colours[j] = colour;
	    g.fillRect(x1, y, x - x1, 100);
	    int arc1 = arc;
	    arc += 360 * (z / 1.5) / rgb.length;
	    g.fillArc(300, 500, 400, 400, -arc1 * 2 + 90, (arc - arc1) * -3);
	}
	
	y += 100;
	x = 40;
	g.setColor(Color.BLACK);
	for (int i = 0; i < diffs.length - 1; i++)
	{
	    double z = i - rgb.length * 5 / 8;
	    if (z < 0.)
		z = -z;
	    z = z * 2 / rgb.length;
	    if (z > 1.)
		z = 2 - z;
	    z /= 3 * 2;
	    z += 1;
	    
	    int x1 = x;
	    x += (step << 1) * z;
	    g.drawLine(x1, y + (int)(diffs[i] * 100), x, y + (int)(diffs[i + 1] * 100));
	    if (diffs[i] == 0.)
		g.fillRect(x1, 40, step << 1, 100);
	}
	if (diffs[diffs.length - 1] == 0.)
	    g.fillRect(x, 40, step << 1, 100);
	
	int pow = 1, j = 0, pln = -1;
	for (int i = 0, n = 36; i < n; i++, j++)
	{
	    int c = j;
	    if (i > 0)
	    {
		if ((i & -i) == i)
		{
		    pln++;
		    pow = i;
		    j = 0;
		    System.out.println("--" + pow);
		}
		
		int jj = 0;
		for (int p = 0; p < pow; p++)
		{
		    int jl = ((j & (1 << p)) >>> p) << (pln - p - 1);
		    int jh = ((j & (1 << (pln - p - 1))) >>> (pln - p - 1)) << p;
		    
		    jj |= jl | jh;
		}
		
		c = jj * 800 / pow + 400 / pow;
		System.out.println(i + ": " + j + "." + jj + ": " + c);
	    }
	    
	    g.setColor(colours[c]);
	    g.fillRect(40 + 900 * i / n, 940, 900 / n + 1, 60);
	}
    }
    
    
    /**
     * Gaussian elimination
     */
    private static double[] eliminate(final double[][] x, final double[] y)
    {
	int xl = x[0].length;
	final double[] r = new double[y.length];
	final double[][] b = new double[y.length][xl];
	
	System.arraycopy(y, 0, r, 0, y.length);
	for (int i = 0; i < y.length; i++)
	    System.arraycopy(x[i], 0, b[i], 0, x[0].length);
	
	for (int k = 0, m = y.length - 1; k < m; k++)
	    for (int i = k + 1, n = y.length; i < n; i++)
	    {
		double mul = b[i][k] / b[k][k];
		for (int j = k + 1; j < xl; j++)
		    b[i][j] -= b[k][j] * mul;
		r[i] -= r[k] * mul;
	    }
	
	for (int k = y.length - 1; k > 0; k--)
	    for (int i = 0; i < k; i++)
		r[i] -= r[k] * b[i][k] / b[k][k];
	
	for (int k = 0; k < y.length; k++)
	    r[k] /= b[k][k];
	
	return r;
    }
    
    
    /**
     * Converts from sRGB to CIELAB
     * 
     * @param   red           The red   intensity [0–255].
     * @param   green         The green intensity [0–255].
     * @param   blue          The blue  intensity [0–255].
     * @return                {L*, a*, b*}
     */
    private static double[] toLab(final int red, final int green, final int blue)
    {
	//The weight of chromaticity [0–∞[, 1 is unweighted.
	final double chromaWeight = 1;
	
	int ir = red  ;  if (ir < 0)  ir += 1 << 8;
	int ig = green;  if (ig < 0)  ig += 1 << 8;
	int ib = blue ;  if (ib < 0)  ib += 1 << 8;
        
	double r = ir / 255.;  r = r <= 0.4045 ? r / 12.92 : Math.pow((r + 0.055) / 1.055, 2.4);
	double g = ig / 255.;  g = g <= 0.4045 ? g / 12.92 : Math.pow((g + 0.055) / 1.055, 2.4);
	double b = ib / 255.;  b = b <= 0.4045 ? b / 12.92 : Math.pow((b + 0.055) / 1.055, 2.4);
        
	double x = (0.4124564 * r + 0.3575761 * g + 0.1804375 * b) / 0.95047;
	double y = (0.2126729 * r + 0.7151522 * g + 0.0721750 * b);
	double z = (0.0193339 * r + 0.1191920 * g + 0.9503041 * b) / 1.08883;
        
	x = x > 0.00885642 ? Math.pow(x, 1. / 3.) : (7.78 + 703. / 99900.) * x + 0.1379310;
	y = y > 0.00885642 ? Math.pow(y, 1. / 3.) : (7.78 + 703. / 99900.) * y + 0.1379310;
	z = z > 0.00885642 ? Math.pow(z, 1. / 3.) : (7.78 + 703. / 99900.) * z + 0.1379310;
	
	final double rcL = 116 * y - 16;
	final double rca = 500 * (x - y) * chromaWeight;
	final double rcb = 200 * (y - z) * chromaWeight;
	
	return new double[] {rcL, rca, rcb};
    }
    
    /**
     * Converts from CIELAB to sRGB
     * 
     * @param   l  L*
     * @param   a  a*
     * @param   b  b*
     * @return     {red, green, blue}
     */
    private static int[] toRGB(final double l, final double a, final double b)
    {
	double y = (l + 16) / 116;
	double x = a / 500 + y;
	double z = y - b / 200;
	
	final double x1 = Math.pow(x, 3.);
	final double y1 = Math.pow(y, 3.);
	final double z1 = Math.pow(z, 3.);
	
	final double x2 = (x - 0.1379310) / (7.78 + 703. / 99900.);
	final double y2 = (y - 0.1379310) / (7.78 + 703. / 99900.);
	final double z2 = (z - 0.1379310) / (7.78 + 703. / 99900.);
	
	x = (x2 > 0.00885642) ? x1 : x2;
	y = (y2 > 0.00885642) ? y1 : y2;
	z = (z2 > 0.00885642) ? z1 : z2;
	
	double xr = 0.4124564, xg = 0.3575761, xb = 0.1804375, R = x * 0.95047;
	double yr = 0.2126729, yg = 0.7151522, yb = 0.0721750, G = y;
	double zr = 0.0193339, zg = 0.1191920, zb = 0.9503041, B = z * 1.08883;
	
	double[] RGB = eliminate(new double[][] {{xr,xg,xb,},{yr,yg,yb,},{zr,zg,zb,},}, new double[] {R, G, B});
	R = RGB[0];
	G = RGB[1];
	B = RGB[2];
	
	//yg -= xg * yr / xr;
	//yb -= xb * yr / xr;
	//G -= R * yr / xr;
	//
	//zg -= xg * zr / xr;
	//zb -= xb * zr / xr;
	//B -= R * zr / xr;
	//
	//zb -= yb * zg / yg;
	//B -= G * zg / yg;
	//
	//G -= B * yb / zb;
	//R -= B * xb / zb;
	//R -= G * xg / yg;
	//
	//R /= xr;
	//G /= yg;
	//B /= zb;
	
	final double r1 = R * 12.92;
	final double g1 = G * 12.92;
	final double b1 = B * 12.92;
	
	final double r2 = Math.pow(R, 1. / 2.4) * 1.055 - 0.055;
	final double g2 = Math.pow(G, 1. / 2.4) * 1.055 - 0.055;
	final double b2 = Math.pow(B, 1. / 2.4) * 1.055 - 0.055;
	
	R = (r1 <= 0.4045 ? r1 : r2) * 255.;
	G = (g1 <= 0.4045 ? g1 : g2) * 255.;
	B = (b1 <= 0.4045 ? b1 : b2) * 255.;
	
	int rcR = (int)(R + 0.5);  rcR = rcR < 0 ? 0 : rcR > 255 ? 255 : rcR;
	int rcG = (int)(G + 0.5);  rcG = rcG < 0 ? 0 : rcG > 255 ? 255 : rcG;
	int rcB = (int)(B + 0.5);  rcB = rcB < 0 ? 0 : rcB > 255 ? 255 : rcB;
	
	return new int[] {rcR, rcG, rcB};
    }
    
    private static double[] toLinear(final int r, final int b, final int g)
    {
	return new double[] {
	    Math.pow(r / 255., 0.439764585),
	    Math.pow(g / 255., 0.439764585),
	    Math.pow(b / 255., 0.439764585)
	};
    }
    
    private static int[] toStandard(final double r, final double b, final double g)
    {
	return new int[] {
	    (int)(0.5 + 255. * Math.pow(r, 2.273943909)),
	    (int)(0.5 + 255. * Math.pow(g, 2.273943909)),
	    (int)(0.5 + 255. * Math.pow(b, 2.273943909))
	};
    }
    
}

