package Assignment1;

/*************************************************************************
 *  Compilation:  javac ArtCollage.java
 *  Execution:    java ArtCollage Flo2.jpeg
 *
 *  @author:
 *
 *************************************************************************/

import java.awt.Color;

public class ArtCollage {

    // The orginal picture
    private Picture original;

    // The collage picture
    private Picture collage;

    // The collage Picture consists of collageDimension X collageDimension tiles
    private int collageDimension;

    // A tile consists of tileDimension X tileDimension pixels
    private int tileDimension;

    /*
     * One-argument Constructor
     * 1. set default values of collageDimension to 4 and tileDimension to 100
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension,
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename) {
        this.collageDimension = 4;
        this.tileDimension = 100;
        this.original = new Picture(filename);

        int dimensions = this.collageDimension * this.tileDimension;
        this.collage = new Picture(dimensions, dimensions);

        scaleImage(dimensions, dimensions, this.original, this.collage);
    }


    /*
     * Three-arguments Constructor
     * 1. set default values of collageDimension to cd and tileDimension to td
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension,
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename, int td, int cd) {
        this.collageDimension = cd;
        this.tileDimension = td;
        this.original = new Picture(filename);

        int dimensions = this.collageDimension * this.tileDimension;
        this.collage = new Picture(dimensions, dimensions);

        scaleImage(dimensions, dimensions, this.original, this.collage);
    }

    private void scaleImage(int height, int width, Picture source, Picture output) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int ycol1 = y * source.width() / height;
                int xrow1 = x * source.height() / width;

                Color c = source.get(ycol1, xrow1);
                output.set(y, x, c);
            }
        }
    }

    /*
     * Returns the collageDimension instance variable
     *
     * @return collageDimension
     */
    public int getCollageDimension() {
        return this.collageDimension;
    }

    /*
     * Returns the tileDimension instance variable
     *
     * @return tileDimension
     */
    public int getTileDimension() {
        return this.tileDimension;
    }

    /*
     * Returns original instance variable
     *
     * @return original
     */
    public Picture getOriginalPicture() {
        return this.original;
    }

    /*
     * Returns collage instance variable
     *
     * @return collage
     */
    public Picture getCollagePicture() {
        return this.collage;
    }

    /*
     * Display the original image
     * Assumes that original has been initialized
     */
    public void showOriginalPicture() {
        this.original.show();
    }

    /*
     * Display the collage image
     * Assumes that collage has been initialized
     */
    public void showCollagePicture() {
        this.collage.show();
    }

    /*
     * Replaces the tile at collageCol,collageRow with the image from filename
     * Tile (0,0) is the upper leftmost tile
     *
     * @param filename image to replace tile
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void replaceTile (String filename,  int collageCol, int collageRow) {
        Picture input = new Picture(filename);
        Picture replacementImage = new Picture(this.tileDimension, this.tileDimension);

        scaleImage(this.tileDimension, this.tileDimension, input, replacementImage);

        int startingXPixel = collageCol * this.tileDimension;
        int startingYPixel = collageRow * this.tileDimension;

        for (int y = startingYPixel, i = 0; y < startingYPixel + this.tileDimension; y++, i++) {
            for (int x = startingXPixel, k = 0; x < startingXPixel + this.tileDimension; x++, k++) {
                Color c = replacementImage.get(k, i);
                this.collage.set(x, y, c);
            }
        }
    }

    /*
     * Makes a collage of tiles from original Picture
     * original will have collageDimension x collageDimension tiles, each tile
     * has tileDimension X tileDimension pixels
     */
    public void makeCollage () {
        Picture tilePicture = new Picture(this.tileDimension, this.tileDimension);
        scaleImage(this.tileDimension, this.tileDimension, this.original, tilePicture);

        for (int y = 0; y < this.collageDimension * this.tileDimension; y++) {
            for (int x = 0; x < this.collageDimension * this.tileDimension; x++) {
                int yPixel = y % this.tileDimension;
                int xPixel = x % this.tileDimension;

                Color c = tilePicture.get(yPixel, xPixel);

                this.collage.set(y, x, c);
            }
        }
    }

    /*
     * Colorizes the tile at (collageCol, collageRow) with component
     * (see CS111 Week 9 slides, the code for color separation is at the
     *  book's website)
     *
     * @param component is either red, blue or green
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void colorizeTile (String component,  int collageCol, int collageRow) {
        int startingXPixel = collageCol * this.tileDimension;
        int startingYPixel = collageRow * this.tileDimension;

        for (int y = startingYPixel; y < startingYPixel + this.tileDimension; y++) {
            for (int x = startingXPixel; x < startingXPixel + this.tileDimension; x++) {
                Color current = this.collage.get(x, y);
                int temp = 0;
                if (component.equals("red")) {
                    temp = current.getRed();
                    this.collage.set(x, y, new Color(temp, 0, 0));
                } else if (component.equals("green")) {
                    temp = current.getGreen();
                    this.collage.set(x, y, new Color(0, temp, 0));
                } else if (component.equals("blue")) {
                    temp = current.getBlue();;
                    this.collage.set(x, y, new Color(0, 0, temp));
                }
            }
        }
    }

    /*
     * Grayscale tile at (collageCol, collageRow)
     * (see CS111 Week 9 slides, the code for luminance is at the book's website)
     *
     * @param collageCol tile column
     * @param collageRow tile row
     */

    public void grayscaleTile (int collageCol, int collageRow) {
        int startingXPixel = collageCol * this.tileDimension;
        int startingYPixel = collageRow * this.tileDimension;



        for (int y = startingYPixel; y < startingYPixel + this.tileDimension; y++) {
            for (int x = startingXPixel; x < startingXPixel + this.tileDimension; x++) {
                Color c = this.collage.get(x, y);
                Color g = Luminance.toGray(c);
                this.collage.set(x, y, g);
            }
        }

    }


    /*
     *
     *  Test client: use the examples given on the assignment description to test your ArtCollage
     */
    public static void main (String[] args) {
        ArtCollage art = new ArtCollage("Flo.jpg", 200, 4);
        art.makeCollage();
        art.showCollagePicture();
    }
}
