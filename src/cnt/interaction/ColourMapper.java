/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.interaction;

import java.awt.Color;


/**
 * Default 24-bit colour mapper
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class ColourMapper
{
    /**
     * Non-constructor
     */
    private ColourMapper()
    {
	assert false : "You may not create instances of this class [ColourMapper].";
    }
    
    
    
    /**
     * Polynomial interpolation degreee
     */
    private static final int degree = 4;
    
    /**
     * Fixed colours on NCS S 2050
     */
    private static final int[][] fixed0 = {{205, 101, 108}, {164, 110, 176}, { 36, 149, 190}, {  0, 169, 159},
					   { 50, 166, 121}, {156, 173,  81}, {204, 173,  71}, {218, 128,  77}};
    
    /**
     * Fixed colours on NCS S 2030
     */
    private static final int[][] fixed1 = {{210, 142, 143}, {184, 153, 188}, {124, 173, 197}, {111, 178, 175},
					   {126, 184, 153}, {175, 187, 128}, {203, 179, 114}, {222, 155, 118}};
    
    /**
     * Mapped colours
     */
    private static Color[] mappedColours = new Color[32];
    
    
    
    /**
     * Gets a colour depending an index
     * 
     * @param   index  The index
     * @return         The colour
     */
    public static Color getColour(final int index)
    {
	synchronized (ColourMapper.class)
	{
	    if (index >= mappedColours.length)
	    {
		int len = mappedColours.length;
		int len2 = len;
		while (len2 <= index)
		    len2 <<= 1;
		
		final Color[] tmp = new Color[len2];
		System.arraycopy(mappedColours, 0, tmp, 0, len);
		mappedColours = tmp;
	    }
	    
	    Color rc;
	    if ((rc = mappedColours[index]) == null)
		mappedColours[index] = rc = generateColour(index);
	    
	    return rc;
	}
    }
    
    
    /**
     * Generates a colour from index
     * 
     * @param   index  Colour index
     * @return         Colour
     */
    private static Color generateColour(final int index)
    {
	int hueIndex = (index & 7) | ((index >>> 4) & ~7);
	int satIndex = (index >>> 3) & 1;
	int lumIndex = (index >>> 4) & 7;
	
	double hue = selectHue(hueIndex);
	double sat = selectSat(satIndex);
	double lum = selectLum(lumIndex);
	    
	return generateColour(hue, sat, lum);
    }
    
    
    /**
     * Generates a colour from hue, saturation and luminosity
     * 
     * @param   hue  hue
     * @param   sat  saturation
     * @param   lum  luminosity
     * @return       Colour
     */
    private static Color generateColour(final double hue, final double sat, final double lum)
    {
	int[] rgb = hueToColour(hue, fixed0);
	int[] satrgb = hueToColour(hue, fixed1);
	int cr = rgb[0], scr = satrgb[0];
	int cg = rgb[1], scg = satrgb[1];
	int cb = rgb[2], scb = satrgb[2];
	double[] lrgb = toLinear(cr, cg, cb);
	double[] slrgb = toLinear(scr, scg, scb);
	
	double lr = lrgb[0] * lum, lg = lrgb[1] * lum, lb = lrgb[2] * lum;
	double slr = slrgb[0] * lum, slg = slrgb[1] * lum, slb = slrgb[2] * lum;
	
	int[] srgb = toStandard(lr * sat + slr * (1. - sat),
				lg * sat + slg * (1. - sat),
				lb * sat + slb * (1. - sat));
	return new Color(srgb[0], srgb[1], srgb[2]);
    }
    
    
    /**
     * Interpolates colour with a hue, using fixed colours evenly distributed in hue starting at 0
     * 
     * @param   hue    The hue of the colour to interpolate
     * @param   fixed  Fixed colours
     * @return         sRGB colour components: {red, green, blue}
     */
    private static int[] hueToColour(final double hue, final int[][] fixed)
    {    
	double[][] f = new double[fixed.length][];
	double[] frk = new double[f.length];
	double[] fgk = new double[f.length];
	double[] fbk = new double[f.length];
	for (int i = 0, n = f.length; i < n; i++)
	{
	    f[i] = toLinear(fixed[i][0], fixed[i][1], fixed[i][2]);
	    frk[i] = f[i][0];
	    fgk[i] = f[i][1];
	    fbk[i] = f[i][2];
	}
	
	int $hue = (int)(hue);
	$hue = ($hue % 400) +  400;
	if ($hue < 450)
	    $hue += 400;
	int midH = ($hue * fixed.length / 400) % fixed.length;
	if (midH < 0)
	    midH += fixed.length;
	
	int lowH = (midH - 1) % fixed.length;
	if (lowH < 0)
	    lowH += fixed.length;
	
	int[] hs = new int[2];
	for (int i = 0; i < hs.length; i++)
	{
	    int im = (lowH + i) % fixed.length;
	    hs[i] = im < 0 ? (im + fixed.length) : im;
	}
	
	double[][] xs = new double[degree][degree];
	double[] rk = new double[degree];
	double[] gk = new double[degree];
	double[] bk = new double[degree];
	for (int y = 0; y < degree; y++)
	{
	    int ym = (lowH + y) % fixed.length;
	    if (ym < 0)
		ym += fixed.length;
	    rk[y] = frk[ym];
	    gk[y] = fgk[ym];
	    bk[y] = fbk[ym];
	    double c = 1, m = 400 + (lowH + y) * 400 / fixed.length;
	    for (int x = 0; x < degree; x++)
		xs[y][x] = c *= m;
	}
	
	rk = eliminate(xs, rk);
	gk = eliminate(xs, gk);
	bk = eliminate(xs, bk);
	
	double h = 1, r = 0, g = 0, b = 0;
	
	for (int i = 0; i < degree; i++)
	{
	    h *= $hue;
	    r += h * rk[i];
	    g += h * gk[i];
	    b += h * bk[i];
	}
	
	return toStandard(r, g, b);
    }
    
    
    /**
     * Selects luminosity depending on luminosity index
     * 
     * @param   index  Luminosity index
     * @return         Luminosity [0, ∞[
     */
    private static double selectLum(final int index)
    {
	double llum = 0.95;
	double hlum = 1.05;
	
	int i = index;
	i = ((i & 12) >> 2) | ((i & 3) << 2);
	i = ((i & 10) >> 1) | ((i & 5) << 1);
	i ^= 2;
	
	return ((hlum - llum) * (8 - i) / 8.) + llum;
    }
    
    
    /**
     * Selects saturation depending on saturation index
     * 
     * @param   index  Saturation index
     * @return         Saturation [0, 1]
     */
    private static double selectSat(final int index)
    {
	return 1. - (index / 2.);
    }
    
    
    /**
     * Selects hue depending on hue index
     * 
     * @param   index  Hue index
     * @return         Hue [0, 400[
     */
    private static double selectHue(final int index)
    {
	int pln;
	int pow = 1 << (pln = lb(index));
	int j = index - pow;
	int jj = 0;
	
	for (int p = 0; p < pln; p++)
	{
	    int jl = ((j & (1 << p)) >>> p) << (pln - p - 1);
	    int jh = ((j & (1 << (pln - p - 1))) >>> (pln - p - 1)) << p;
	    
	    jj |= jl | jh;
	}
	
	return index == 0 ? 0. : (jj * 400. / pow + 200. / pow);
    }
    
    
    /**
     * Calculates the floored binary logarithm of an integer
     *
     * @param   value  The integer which logarithm should be calculated
     * @return         The floored binary logarithm of the input integer
     */
    public static int lb(int value)
    {
        if (value == 0)
            return -1;
        
        int rc = 0;
        int v = value;
        
        if ((v & 0xFFFF0000) != 0)  {  rc |= 16;  v >>= 16;  }
        if ((v & 0x0000FF00) != 0)  {  rc |=  8;  v >>=  8;  }
        if ((v & 0x000000F0) != 0)  {  rc |=  4;  v >>=  4;  }
        if ((v & 0x0000000C) != 0)  {  rc |=  2;  v >>=  2;  }
        if ((v & 0x00000002) != 0)  {  rc |=  1;             }
        
        return rc;
    }
    
    
    /**
     * Gaussian elimination
     * 
     * @param   x  Square matrix
     * @param   y  Matrix augment
     * @return     Coefficients
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
     * @param   red           The red   intensity [0, 255]
     * @param   green         The green intensity [0, 255]
     * @param   blue          The blue  intensity [0, 255]
     * @return                CIELAB colour components: {L*, a*, b*}
     */
    private static double[] toLab(final int red, final int green, final int blue)
    {
	//The weight of chromaticity [0, ∞[, 1 is unweighted.
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
     * @param   l  CIELAB colour component L*
     * @param   a  CIELAB colour component a*
     * @param   b  CIELAB colour component b*
     * @return     sRGB colour components: {red, green, blue}
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
    
    
    /**
     * Converts sRGB [0, 255] to linear RGB [0, 1]
     * 
     * @param   r  The red   intensity
     * @param   g  The green intensity
     * @param   b  The blue  intensity
     * @return     Linear RGB colours components
     */
    private static double[] toLinear(final int r, final int b, final int g)
    {
	return new double[] {
	            Math.pow(r / 255., 0.439764585),
		    Math.pow(g / 255., 0.439764585),
		    Math.pow(b / 255., 0.439764585)
	        };
    }
    
    
    /**
     * Converts linear RGB [0, 1] to sRGB [0, 255]
     * 
     * @param   r  The red   intensity
     * @param   g  The green intensity
     * @param   b  The blue  intensity
     * @return     sRGB colours components
     */
    private static int[] toStandard(final double r, final double b, final double g)
    {
	return new int[] {
	            (int)(0.5 + 255. * Math.pow(r, 2.273943909)),
		    (int)(0.5 + 255. * Math.pow(g, 2.273943909)),
		    (int)(0.5 + 255. * Math.pow(b, 2.273943909))
	        };
    }
    
}

