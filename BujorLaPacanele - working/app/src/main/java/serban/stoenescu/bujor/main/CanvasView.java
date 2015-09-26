package serban.stoenescu.bujor.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import main.temporary.InvalidSymbolNumberException;
import main.temporary.Main;
import serban.stoenescu.bujor.src.paytable.InvalidNumberOfSymbolsException;
import serban.stoenescu.bujor.src.paytable.InvalidPositionException;
import serban.stoenescu.bujor.src.paytable.PayTable;
import serban.stoenescu.nivele.Levels;

enum State {STOPPED, REELS_SPINNING, MONEY_FALLING};

class Joc {
    private static Joc instance = null;
    private int suma;
    private int sumaAfisata;

    private Joc() {
        System.out.println("Sa-mi bag pula-n ma-ta");
        suma = 500;
        sumaAfisata = suma;
    }

    public static Joc getInstance() {
        if (instance == null) instance = new Joc();
        return instance;
    }

    private int getNumberOfDigits(int n) {
        int res = 0;
        while (n > 0) {
            res++;
            n = n / 10;
        }
        return res;
    }

    public void updateDisplayedSum() {
        int digits;
        digits = getNumberOfDigits(Math.abs(suma - sumaAfisata));
        int updateAmount;
        if (digits <= 2) updateAmount = 1;
        else updateAmount = (int) Math.pow(10, digits - 2);

        if (sumaAfisata < suma)
            sumaAfisata += updateAmount;
        else if (sumaAfisata > suma)
            sumaAfisata -= updateAmount;
    }

    public int getSuma() {
        return suma;
    }

    public void setSuma(int suma) {
        this.suma = suma;
        sumaAfisata = suma;
    }

    public int getDisplayedSum() {
        return sumaAfisata;
    }

    public void updateSum(int amount) {
        suma += amount;
    }//atentie, o sa pui si negative
}

public class CanvasView extends View {

    private static final double REFERENCE_HEIGHT = 960;
    private static final double REFERENCE_WIDTH = 540;
    private static final int Y_CORRECTION_PLAY = 15;
    private final int INIT_LINES = 5;
    private final int PLAY_BUTTON_SIZE = 100;
    private final int WHO_IS_BUJOR_ICON_SIZE_X = 499, WHO_IS_BUJOR_ICON_SIZE_Y = 90;
    private final int FACEBOOK_ICON_SIZE = 60;
    private final double DIMENSIUNE_BAN = 0.5;
    private final int DIMENSIUNE_CADRU = 5;
    private final int MARGINE = 50;
    private final int SPATIU = 5;
    private final int NUMAR_SIMBOLURI = 5;
    private final int LINII = 5; //3 de joc + 2 pentru simbolurile care se vad partial (1 pentru cele de sus + 1 pentru cele de jos)
    private final int MOVE_UP_MARGIN = 0;//35;//TODO: ! ATENTIE MARE! MARGINEA SI VITEZA TREBUIE SA FIE DIVIZIBILE
    private final double SPEED = 7;
    private final int coinRadius = 15;//TODO: scale this for large screens
    private final int DAILY_BONUS = 1000;
    public  static String pula = "PULA";
    Bitmap[] icons = new Bitmap[25];//TODO: ba copile, tu esti cretin?;
    private static double scaleX, scaleY, scaleFactor;
    private Joc joc;
    private State state = State.STOPPED;
    private boolean cenzurat = true;
    private MoneyAnimation moneyAnimation = null;
    private Bitmap coinBitmap, scaledCoinBitmap;
    private Bitmap plusBitmap, scaledPlusBitmap, minusBitmap, scaledMinusBitmap;
    private Bitmap frameBitmap = null, frameBitmapUnscaled;
    private Bitmap playIcon, playIconScaled;
    private Bitmap whoIsBujor, whoIsBujorScaled;
    private Bitmap cenzuratBitmap, cenzuratScaled, necenzurat, necenzuratScaled;
    private Bitmap facebookIcon, facebookIconScaled;
    private Context context;
    private int wonSum = 0;
    private int bet = 1, lines = INIT_LINES, lastBet = 1;
    private Paint paint = new Paint();
    private double scale = 1.0f;
    private int raza = 1;
    private int[] xSymbol = new int[NUMAR_SIMBOLURI];
    private int[] ySymbol = new int[LINII];
    private int[][] preselectedIcons = new int[LINII][NUMAR_SIMBOLURI];
    private int threshold = 0;
    private boolean initialized = false;
    private GraphicSymbol[][] symbols = new GraphicSymbol[LINII][NUMAR_SIMBOLURI];
    private Column[] columns = new Column[NUMAR_SIMBOLURI];
    private IconFactory iconFactory;
    private int MAX_LINES = 15;
    private int lastBonusDay = -1;

    public CanvasView(Context context) {
        super(context);
        this.context = context;

        iconFactory = new IconFactory(this);


        //call this whenever you want to display an interstitial
             /*Log.v("LEADBOLT","Incerc sa incarc o reclama din CanvasView");
             AppTracker.loadModule(context.getApplicationContext(),"inapp");
             Log.v("LEADBOLT","Am incercat sa incarc o reclama din CanvasView");*/

        joc = Joc.getInstance();

             /*try
             {
             writeFile();
             }
             catch(IOException e)
             {
            	 e.printStackTrace();
             }*/

        plusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plus);
        minusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.minus);

        pula = pula +"1 coinRadius = "+coinRadius+"\n scaleFactor = "+scaleFactor;
        scaledPlusBitmap = Bitmap.createScaledBitmap(plusBitmap, (int) (3 * (double) coinRadius * scaleFactor), (int) (3 * (double) coinRadius * scaleFactor), true);

        pula = pula +"2";
        scaledMinusBitmap = Bitmap.createScaledBitmap(minusBitmap, (int) (3 * (double) coinRadius * scaleFactor), (int) (3 * (double) coinRadius * scaleFactor), true);


        pula = pula +"3";
        coinBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.coin);

        pula = pula +"4";
        scaledCoinBitmap = Bitmap.createScaledBitmap(coinBitmap, (int) (2.5 * (double) coinRadius * scaleFactor), (int) (2.5 * (double) coinRadius * scaleFactor), true);

        pula = pula +"5";
        frameBitmapUnscaled = BitmapFactory.decodeResource(getResources(), R.drawable.frame);

        playIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_play);
        whoIsBujor = BitmapFactory.decodeResource(getResources(), R.drawable.cine_e_bujor);

        facebookIcon = BitmapFactory.decodeResource(getResources(), R.drawable.facebook);


        pula = pula +"6";
        facebookIconScaled = Bitmap.createScaledBitmap(facebookIcon, (int) ((double) FACEBOOK_ICON_SIZE * scaleFactor), (int) ((double) FACEBOOK_ICON_SIZE * scaleFactor), true);


        pula = pula +"7";
        cenzuratBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cen2);
        necenzurat = BitmapFactory.decodeResource(getResources(), R.drawable.nec2);
        cenzuratScaled = Bitmap.createScaledBitmap(cenzuratBitmap, (int) (scaleFactor * 1.5f * (double) WHO_IS_BUJOR_ICON_SIZE_X / 2), (int) (scaleFactor * 0.55f * (double) WHO_IS_BUJOR_ICON_SIZE_Y), true);

        pula = pula +"8";
        necenzuratScaled = Bitmap.createScaledBitmap(necenzurat, (int) (scaleFactor * 1.5f * (double) WHO_IS_BUJOR_ICON_SIZE_X / 2), (int) (scaleFactor * 0.55f * (double) WHO_IS_BUJOR_ICON_SIZE_Y), true);


        readFile();

        lines = getMaxLines();
        bet = getMaxBet();
    }

    private int getMinLines() {
        return INIT_LINES - 2;
    }

    private int getRealBet() {
        int mult = (int) Math.pow(10, (bet - 1) / 3);
        if (mult == 0) mult = 1;
        double crap;
        switch (bet % 3) {
            case 1:
                crap = 1;
                break;
            case 2:
                crap = 2.5;
                break;
            default:
                crap = 5;
                break;
        }
        return (int) (mult * crap);
    }

    private int getRealBet(int hypotheticBet) {
        int mult = (int) Math.pow(10, (hypotheticBet - 1) / 3);
        if (mult == 0) mult = 1;
        double crap;
        switch (hypotheticBet % 3) {
            case 1:
                crap = 1;
                break;
            case 2:
                crap = 2.5;
                break;
            default:
                crap = 5;
                break;
        }
        return (int) (mult * crap);
    }

    public GraphicSymbol[][] getSymbols() {
        return symbols;
    }

    public static double displayHeight;
    public static double displayWidth;
    public static void initializeScaleFactor()
    {
        scaleX = (double) ((double) displayWidth / (double) REFERENCE_WIDTH);
        scaleY = (double) ((double) displayHeight / (double) REFERENCE_HEIGHT);
        if (scaleX > scaleY) scaleFactor = scaleY;
        else scaleFactor = scaleX;
        if (scaleFactor <= 0.1) scaleFactor = 1;
        //scaleFactor = 2;
    }
    private void initialize() {


        Log.v("SIZE", "ScaleFactor = " + scaleFactor);
        int width = getWidth();
        int i, j;
        int lungimeTotala = width - 2 * MARGINE;
        int diametru = (lungimeTotala - (NUMAR_SIMBOLURI - 1) * SPATIU) / NUMAR_SIMBOLURI;

        threshold = MARGINE + (LINII - 2) * SPATIU + (LINII - 1) * 2 * raza + raza;
        raza = diametru / 2;
        xSymbol[0] = MARGINE + raza - 4 * DIMENSIUNE_CADRU;
        for (i = 1; i < NUMAR_SIMBOLURI; i++) {
            xSymbol[i] = xSymbol[i - 1] + SPATIU + 2 * raza;
        }
        final int Y_CORRECTION = -2 * raza - SPATIU - 5 * DIMENSIUNE_CADRU;
        ySymbol[0] = MARGINE + raza + Y_CORRECTION;
        for (i = 1; i < LINII; i++) {
            ySymbol[i] = ySymbol[i - 1] + SPATIU + 2 * raza;
        }

        if (!initialized) {
            int currentBitmap = 0;

            for (i = 0; i < LINII; i++)
                for (j = 0; j < NUMAR_SIMBOLURI; j++) {
                    symbols[i][j] = new GraphicSymbol(xSymbol[j], ySymbol[i], currentBitmap, iconFactory);
                    currentBitmap++;
                }
            for (i = 0; i < NUMAR_SIMBOLURI; i++)
                columns[i] = new Column();
            initialized = true;
        } else {
            GraphicSymbol.raza = raza;
            GraphicSymbol.setRestartPoint(ySymbol[0]);
            GraphicSymbol.setThreshold(threshold + MOVE_UP_MARGIN);

            if (state != State.STOPPED) GraphicPayLine.punePulaPeZero();
            if (state == State.STOPPED) ;
            else if (state == State.REELS_SPINNING) {
                int stopped = 0;
                for (i = 0; i < LINII; i++)
                    for (j = 0; j < NUMAR_SIMBOLURI; j++) {
                        if (columns[j].getPasses() < 4 + 2 * j)
                            symbols[i][j].updateY((int) (SPEED), columns[j], j, preselectedIcons);
                        else {
                            symbols[i][j].align((int) 1, ySymbol);
                            if (symbols[i][j].isStopped((int) 1, ySymbol))
                                stopped++;
                        }
                    }
                if (stopped == LINII * NUMAR_SIMBOLURI) {
                    if (wonSum != 0) {
                        state = State.MONEY_FALLING;

                        joc.updateSum(wonSum);
                        SoundManager.getInstance().playWinningSound(wonSum, getRealBet() * lines, cenzurat);
                    } else {
                        state = State.STOPPED;
                        SoundManager.getInstance().playLosingSound();
                    }
                    moneyAnimation = new MoneyAnimation(MARGINE, getWidth() - (int) (raza * DIMENSIUNE_BAN), threshold, (int) (raza * DIMENSIUNE_BAN), scaledCoinBitmap);
                }
            } else if (state == State.MONEY_FALLING) {
                if (moneyAnimation.done()) state = State.STOPPED;
            }
            makeBitmaps();
        }
    }

    private void makeBitmaps() {

        pula = pula +"makeBitmaps";
        if (frameBitmap == null) {
            frameBitmap = Bitmap.createScaledBitmap(frameBitmapUnscaled, getWidth(), threshold - 2 * raza, true);
            iconFactory.makeBitmaps(raza);
        }
        if (playIconScaled == null) {
            playIconScaled = Bitmap.createScaledBitmap(playIcon, (int) ((double) PLAY_BUTTON_SIZE * scaleFactor), (int) ((double) PLAY_BUTTON_SIZE * scaleFactor), true);
        }
        if (whoIsBujorScaled == null) {
            scale = (double) getWidth() / (double) WHO_IS_BUJOR_ICON_SIZE_X;
            if (scale > 1)
                scale = 1;
            whoIsBujorScaled = Bitmap.createScaledBitmap(whoIsBujor, (int) ((double) scale * (double) WHO_IS_BUJOR_ICON_SIZE_X * scaleFactor), (int) ((double) scale * (double) WHO_IS_BUJOR_ICON_SIZE_Y * scaleFactor), true);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initialize();

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        // Use Color.parseColor to define HTML colors
        paint.setColor(Color.parseColor("#CD5C5C"));

        try {
            int i, j;
            for (i = 0; i < NUMAR_SIMBOLURI; i++) {
                for (j = 0; j < LINII; j++) {
                    symbols[i][j].drawSymbol(canvas, paint);
                }
            }

            if (state == State.MONEY_FALLING)
                moneyAnimation.draw(canvas, paint);

            if (frameBitmap != null)
                canvas.drawBitmap(frameBitmap, 0, 0, paint);
            if (state != State.REELS_SPINNING && wonSum >= 1) {
                GraphicPayLine.drawWinningLines(canvas, paint, MARGINE + (int) (raza * 1.5) - 4 * DIMENSIUNE_CADRU,
                        getWidth() - MARGINE + (int) (raza * 1.5),
                        ySymbol,
                        threshold - (int) (1.5 * raza),
                        (int) (1.0 * raza),
                        raza,
                        getRealBet(lastBet));
                paint.setColor(Color.BLACK);
                canvas.drawText(GraphicPayLine.getString(), MARGINE, threshold - MARGINE + coinRadius * 19, paint);
            }
            if (state == State.STOPPED) {
                GraphicPayLine.drawAllLines(canvas, paint, MARGINE + (int) (raza * 1.5) - 4 * DIMENSIUNE_CADRU,
                        getWidth() - MARGINE + (int) (raza * 1.5),
                        ySymbol,
                        threshold - (int) (1.5 * raza),
                        (int) (1.0 * raza),
                        raza,
                        lines);
            }
            Coverups.drawCoverUps(canvas, paint, getWidth(), threshold, DIMENSIUNE_CADRU * 2, threshold - 2 * raza - 2 * DIMENSIUNE_CADRU, threshold + 2 * raza);


        } catch (InvalidSymbolNumberException | InvalidNumberOfSymbolsException | InvalidPositionException e) {
            e.printStackTrace();
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        //canvas.drawText("State = "+state.toString()+" won:"+wonSum,0,500, paint);

        if (bet < getMaxBet())
            canvas.drawBitmap(scaledPlusBitmap, MARGINE, threshold - MARGINE + coinRadius * 8, paint);
        if (bet > 1)
            canvas.drawBitmap(scaledMinusBitmap, MARGINE, threshold - MARGINE + coinRadius * 11, paint);
        canvas.drawText("Miza: " + getRealBet(), MARGINE + 3 * coinRadius, threshold - MARGINE + coinRadius * 12, paint);

        if (lines < getMaxLines())
            canvas.drawBitmap(scaledPlusBitmap, getWidth() / 2, threshold - MARGINE + coinRadius * 8, paint);
        if (lines > getMinLines())
            canvas.drawBitmap(scaledMinusBitmap, getWidth() / 2, threshold - MARGINE + coinRadius * 11, paint);
        canvas.drawText("Linii de castig: " + lines, getWidth() / 2 + 3 * coinRadius, threshold - MARGINE + coinRadius * 12, paint);
        canvas.drawText("Cost per runda: " + getRealBet() + " x " + lines + " = " + (getRealBet() * lines), MARGINE, threshold - MARGINE + coinRadius * 16, paint);

        canvas.drawBitmap(facebookIconScaled, getWidth() - FACEBOOK_ICON_SIZE - 20, threshold - MARGINE + coinRadius * 15, paint);


        if (playIconScaled != null)
            canvas.drawBitmap(playIconScaled, MARGINE, threshold - MARGINE + Y_CORRECTION_PLAY, paint);
        if (whoIsBujorScaled != null)
            canvas.drawBitmap(whoIsBujorScaled, 0, getHeight() - WHO_IS_BUJOR_ICON_SIZE_Y, paint);
        if (cenzurat) {
            if (cenzuratScaled != null)
                canvas.drawBitmap(cenzuratScaled, getWidth() / 2, threshold - MARGINE + 2 * Y_CORRECTION_PLAY, paint);
        } else if (necenzuratScaled != null)
            canvas.drawBitmap(necenzuratScaled, getWidth() / 2, threshold - MARGINE + 2 * Y_CORRECTION_PLAY, paint);


        if (wonSum != 0 && state != State.REELS_SPINNING) {
            canvas.drawBitmap(scaledCoinBitmap, 5 * DIMENSIUNE_CADRU + 100, threshold - raza - raza / 2, paint);
            canvas.drawText("+" + wonSum, 6 * DIMENSIUNE_CADRU + (int)(2 * coinRadius * scaleFactor) + 100, threshold - raza + 5, paint);
        }


        canvas.drawBitmap(scaledCoinBitmap, getWidth() / 2, threshold - raza - raza / 2, paint);
        canvas.drawText("  " + joc.getDisplayedSum(), getWidth() / 2 + (int)(2 * coinRadius*scaleFactor), threshold - raza + 5, paint);
        joc.updateDisplayedSum();

        canvas.drawText("Nivelul " + Levels.getInstance().getLevel(), 2 * DIMENSIUNE_CADRU, threshold - raza, paint);
        ProgressLine.draw(canvas, 2 * DIMENSIUNE_CADRU, threshold - raza + 5, paint);


        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN: {
                int x = (int) event.getX();
                int y = (int) event.getY();


                if (state != State.REELS_SPINNING) {
                    if (x >= MARGINE && x <= MARGINE + PLAY_BUTTON_SIZE*scaleFactor &&
                            y >= threshold - MARGINE && y <= threshold - MARGINE + PLAY_BUTTON_SIZE*scaleFactor) {
                        if (getRealBet() * lines <= joc.getSuma()) {
                            Levels.getInstance().caller = this;
                            Levels.getInstance().addXp(bet + lines - 3);

                            joc.updateSum(-getRealBet() * lines);

                            PayTable.resetFourSound();
                            wonSum = (int) Main.playARound(getRealBet(), lines);
                            lastBet = bet;
                            //joc.updateSum(wonSum);
                            preselectIcons();
                            for (int i = 0; i < NUMAR_SIMBOLURI; i++)
                                columns[i].reset();
                            state = State.REELS_SPINNING;
                            SoundManager.getInstance().playStartSound();
                        } else {
                            showNotEnoughDialog();
                        }
                    }
                }
                /*getWidth()-FACEBOOK_ICON_SIZE-20,threshold-MARGINE+coinRadius*15*/
                if (x >= getWidth() - (FACEBOOK_ICON_SIZE - 20)*scaleFactor &&
                        y >= threshold - MARGINE + coinRadius * 15 && y <= threshold - MARGINE + coinRadius * 15 + FACEBOOK_ICON_SIZE*scaleFactor) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/bujor.android"));
                    context.startActivity(browserIntent);
                }
                if (x >= getWidth() / 2 &&
                        y >= threshold - MARGINE && y <= threshold - MARGINE + PLAY_BUTTON_SIZE*scaleFactor) {
                    cenzurat = !cenzurat;
                }
                if (x >= MARGINE && x <= MARGINE + (int) (scale * WHO_IS_BUJOR_ICON_SIZE_X * scaleFactor) &&
                        y >= getHeight() - WHO_IS_BUJOR_ICON_SIZE_Y) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=lBacMvh9xYo"));
                    context.startActivity(browserIntent);
                }

        		/*  canvas.drawBitmap(scaledPlusBitmap, MARGINE,threshold-MARGINE+coinRadius*8, paint);
		   canvas.drawBitmap(scaledMinusBitmap, MARGINE,threshold-MARGINE+coinRadius*11, paint);

		   canvas.drawBitmap(scaledPlusBitmap, getWidth()/2,threshold-MARGINE+coinRadius*8, paint);
		   canvas.drawBitmap(scaledMinusBitmap, getWidth()/2,threshold-MARGINE+coinRadius*11, paint);*/
                if (x >= MARGINE && x <= MARGINE + 4 * coinRadius *scaleFactor &&
                        y >= threshold - MARGINE + coinRadius * 8 && y <= threshold - MARGINE + coinRadius * 8 + 3*coinRadius*scaleFactor) {
                    //Toast.makeText(context, "+ stanga", Toast.LENGTH_SHORT).show();
                    if (bet < getMaxBet())
                        bet++;
                }
                if (x >= MARGINE && x <= MARGINE + 4 * coinRadius * scaleFactor &&
                        y >= threshold - MARGINE + coinRadius * 11 && y <= threshold - MARGINE + coinRadius * 11 + 3*coinRadius*scaleFactor) {
                    //Toast.makeText(context, "- stanga", Toast.LENGTH_SHORT).show();
                    if (bet > 1)
                        bet--;
                }
                if (x >= getWidth() / 2 && x <= getWidth() / 2 + 4 * coinRadius * scaleFactor&&
                        y >= threshold - MARGINE + coinRadius * 8 && y <= threshold - MARGINE + coinRadius * 8 + 3 *coinRadius*scaleFactor) {
                    //Toast.makeText(context, "+ dreapta", Toast.LENGTH_SHORT).show();

                    if (lines < getMaxLines()) {
                        lines++;
                        GraphicPayLine.reseteazaPula();
                    }
                }
                if (x >= getWidth() / 2 && x <= getWidth() / 2 + 4 * coinRadius * scaleFactor&&
                        y >= threshold - MARGINE + coinRadius * 11 && y <= threshold - MARGINE + coinRadius * 11 + 3*scaleFactor*coinRadius) {
                    //Toast.makeText(context, "- dreapta", Toast.LENGTH_SHORT).show();
                    if (lines > getMinLines()) {
                        lines--;
                        GraphicPayLine.reseteazaPula();
                    }
                }
            }
            break;
        }
        invalidate();

        return true;
    }

    private int getMaxLines() {
        int lim = INIT_LINES - 1 + Levels.getInstance().getLevel() + 7;//de ce 7? ca sa imi sugi pula
        int level = Levels.getInstance().getLevel();
        lim = (level - 1) / 2 + INIT_LINES;
        if (lim > MAX_LINES) lim = MAX_LINES;
        return lim;
    }
		
		/*public void writeFile() throws IOException {
			
			try
			{
				SoundManager.getInstance().playStartSound();
				Thread.sleep(3000);
			}catch(InterruptedException e){}
			
			File fout = new File("bujor_persistence");
			FileOutputStream fos = new FileOutputStream(fout);
		 
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		 

			bw.write("level "+Levels.getInstance().getLevel());
			bw.write("xp "+Levels.getInstance().getXp());
			bw.write("suma "+joc.getSuma());
			bw.newLine();
		 
			bw.close();
			

			try
			{
				SoundManager.getInstance().playStartSound();
				Thread.sleep(3000);
			}catch(InterruptedException e){}
		}*/

    private void preselectIcons() {
        preselectedIcons = Main.getSymbols();
    }

    public void writeFile() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(MainActivity.getContext().openFileOutput("bujor_persistence.txt", Context.MODE_PRIVATE));

            outputStreamWriter.write("level " + Levels.getInstance().getLevel() + "\n");
            outputStreamWriter.write("xp " + Levels.getInstance().getXp() + "\n");
            outputStreamWriter.write("suma " + joc.getSuma() + "\n");
            outputStreamWriter.write("lastBonusDay " + lastBonusDay);

            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void showNotEnoughDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getContext());
        String message = "Din pacate, nu ai destul credit. Joaca o suma mai mica sau asteapta bonusul periodic";
        String greeting = "Iara se strica...";

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                })
                .setTitle(greeting)
                .setIcon(R.drawable.ic_launcher);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getContext());
        String message = "Ai ajuns la nivelul " + Levels.getInstance().getLevel() + "!\n";
        message += "Acum poti paria " + getRealBet(getMaxBet()) + " credite\n";
        message += "si " + getMaxLines() + " linii. Mai multe linii inseamna mai multe sanse de castig!";
        String greeting;
        if (cenzurat) greeting = "Manca-ti-as p*** ta!";
        else greeting = "Manca-ti-as pula ta!";

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                })
                .setTitle(greeting)
                .setIcon(R.drawable.ic_launcher);
        AlertDialog alert = builder.create();
        alert.show();


        lines = getMaxLines();
        bet = getMaxBet();
    }

    private int getMaxBet() {
        return Levels.getInstance().getLevel() / 2 + 1 + 2;
    }

    private String readFile() {

        String ret = "";

        try {
            InputStream inputStream = MainActivity.getContext().openFileInput("bujor_persistence.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receivedString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receivedString = bufferedReader.readLine()) != null) {
                    String[] pieces = receivedString.split(" ");
                    if (pieces[0].equals("level")) {
                        Levels.getInstance().setLevel(Integer.parseInt(pieces[1]));
                    }
                    if (pieces[0].equals("xp")) {
                        Levels.getInstance().setXp(Integer.parseInt(pieces[1]));
                    }
                    if (pieces[0].equals("suma")) {
                        joc.setSuma(Integer.parseInt(pieces[1]));
                    }
                    if (pieces[0].equals("lastBonusDay")) {
                        lastBonusDay = Integer.parseInt(pieces[1]);
                    }

                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public void checkForBonus() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        if (day != lastBonusDay) {
            lastBonusDay = day;
            joc.updateSum(DAILY_BONUS);
            showBonusAlert();
        }
    }

    public void showBonusAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getContext());
        String message = "Bine ai venit! Ai primit bonusul tau zilnic de " + DAILY_BONUS + " credite\n";

        String greeting;
        if (cenzurat) greeting = "Manca-ti-as p*** ta!";
        else greeting = "Manca-ti-as pula ta!";

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                })
                .setTitle(greeting)
                .setIcon(R.drawable.ic_launcher);
        AlertDialog alert = builder.create();
        alert.show();
    }

}
 