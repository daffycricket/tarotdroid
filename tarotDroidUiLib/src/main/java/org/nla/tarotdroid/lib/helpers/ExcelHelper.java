package org.nla.tarotdroid.lib.helpers;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot5Game;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.GameStyleType;

import android.content.Context;
import android.os.Environment;

/**
 * Helper class aimed to export a GameSet into an Excel file. 
 * @author Nico
 */
public final class ExcelHelper {
    
    /**
     * File name date formatter.
     */
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyyMMdd'_'hhmm");

    /**
     * 'Leader player' string. 
     */
    private static String strLeaderPlayer;

    /**
     * 'Called player' string. 
     */
    private static String strCalledPlayer;

    /**
     * 'Called king' string. 
     */
    private static String strCalledColor;
    
    /**
     * 'Dead' string. 
     */
    private static String strDead;
    
    /**
     * Private default constructor. 
     */
    private ExcelHelper() {
    }
    
    /**
     * Get file path for a GameSet.
     * @param gameSet
     * @return
     */
    public static String getPath(final GameSet gameSet) {
    	return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + ExcelHelper.FORMATTER.format(gameSet.getCreationTs()) + ".xls";
    }
    
    /**
     * Returns file pat for all gamesets.
     * @return
     */
    public static String getPath() {
    	return Environment.getExternalStorageDirectory().getAbsolutePath() + "/allgamesets.xls";
    }
    
    /**
     * Exports a GameSet into an excel file located at the root of the internal storage with the name YYYYMMDD_HHMM.xls.
     * @param context
     * @param gameSet
     * @return
     * @throws IOException
     * @throws RowsExceededException
     * @throws WriteException
     */
    public static String exportToExcel(final Context context, final List<GameSet> gameSets) throws Exception {
        checkArgument(gameSets != null, "gameSets is null");
        checkArgument(context != null, "context is null");
        
        // initialize the necessary strings if not done yet
        if (ExcelHelper.strLeaderPlayer == null) {
            ExcelHelper.initializeStaticStrings(context);
        }
        
        // create excel file and objects
		File sdcard = Environment.getExternalStorageDirectory();
		File tarotDroidDir = new File(sdcard.getAbsolutePath(), "TarotDroid");
		if (!tarotDroidDir.exists()) {
			tarotDroidDir.mkdir();
		}
        
        File excelFile = new File(tarotDroidDir, "allgamesets.xls");
        excelFile.createNewFile();
        WritableWorkbook workbook = Workbook.createWorkbook(excelFile);
        
        // generates excel reports
        int index = 0;
        for (GameSet gameSet : gameSets) {
        	exportToExcel(context, gameSet, excelFile, workbook, index);
        	index += 2;
        }
        
        // write excel file
        workbook.write();
        workbook.close();
        return excelFile.getAbsolutePath();
    }
    
    /**
     * Exports a GameSet into an excel file located at the root of the internal storage with the name YYYYMMDD_HHMM.xls.
     * @param context
     * @param gameSet
     * @return
     * @throws IOException
     * @throws RowsExceededException
     * @throws WriteException
     */
    public static String exportToExcel(final Context context, final GameSet gameSet) throws Exception {
    	File sdcard = Environment.getExternalStorageDirectory();
		File tarotDroidDir = new File(sdcard.getAbsolutePath(), "TarotDroid");
		if (!tarotDroidDir.exists()) {
			tarotDroidDir.mkdir();
		}
    	File excelFile = new File(tarotDroidDir, ExcelHelper.FORMATTER.format(gameSet.getCreationTs()) + ".xls");
    	excelFile.createNewFile();
    	WritableWorkbook workbook = Workbook.createWorkbook(excelFile);
    	String fileName = exportToExcel(context, gameSet, excelFile, workbook, 0);
        workbook.write();
        workbook.close();
    	return fileName;
    }
    
    /**
     * Exports a GameSet into an excel file located at the root of the internal storage with the name YYYYMMDD_HHMM.xls.
     * @param context
     * @param gameSet
     * @param excelFile
     * @param workbook
     * @return
     * @throws IOException
     * @throws RowsExceededException
     * @throws WriteException
     */
    public static String exportToExcel(final Context context, final GameSet gameSet, final File excelFile, final WritableWorkbook workbook, final int startIndex) throws Exception {
        checkArgument(gameSet != null, "gameSet is null");
        checkArgument(context != null, "context is null");
        checkArgument(excelFile != null, "excelFile is null");
        checkArgument(workbook != null, "workbook is null");
        
        // initialize the necessary strings if not done yet
        if (ExcelHelper.strLeaderPlayer == null) {
            ExcelHelper.initializeStaticStrings(context);
        }
        
        // generate reports
        WritableSheet globalSheet = workbook.createSheet(ExcelHelper.FORMATTER.format(gameSet.getCreationTs()) + "Global", startIndex);
        WritableSheet deltaSheet = workbook.createSheet(ExcelHelper.FORMATTER.format(gameSet.getCreationTs()) + "Delta", startIndex + 1);
        
        // export player names as labels
        for (int column = 1; column <= gameSet.getPlayers().size(); ++column) {
            Player player = gameSet.getPlayers().get(column);
            globalSheet.addCell(new Label(column, 0, player.getName()));
            deltaSheet.addCell(new Label(column, 0, player.getName()));
        }
        
        // export other colums titles depending in the game style type
        globalSheet.addCell(new Label(gameSet.getPlayers().size() + 1, 0, ExcelHelper.strLeaderPlayer));
        deltaSheet.addCell(new Label(gameSet.getPlayers().size() + 1, 0, ExcelHelper.strLeaderPlayer));
        if (gameSet.getGameStyleType() == GameStyleType.Tarot5) {
            globalSheet.addCell(new Label(gameSet.getPlayers().size() + 2, 0, ExcelHelper.strCalledPlayer));
            deltaSheet.addCell(new Label(gameSet.getPlayers().size() + 2, 0, ExcelHelper.strCalledPlayer));
            globalSheet.addCell(new Label(gameSet.getPlayers().size() + 3, 0, ExcelHelper.strCalledColor));
            deltaSheet.addCell(new Label(gameSet.getPlayers().size() + 3, 0, ExcelHelper.strCalledColor));
        }
        
        // export games
        int row = 1;
        for (BaseGame game : gameSet.getGames()) {
            if (game instanceof StandardTarot5Game) {
                ExcelHelper.exportStandardTarot5Game(context, gameSet, (StandardTarot5Game)game, globalSheet, deltaSheet, row);
            }
            else if (game instanceof StandardBaseGame) {
                ExcelHelper.exportStandardGame(context, gameSet, (StandardBaseGame)game, globalSheet, deltaSheet, row);
            }
            else {
                ExcelHelper.exportBelgianGame(context, gameSet, game, globalSheet, deltaSheet, row);
            }

            ++row;
        }
        
        return excelFile.getAbsolutePath();
    }
    
    /**
     * Export into the excel workbook the game with a standard template.
     * @param context
     * @param stdGame
     * @param globalSheet
     * @param deltaSheet
     * @param row
     * @throws Exception
     */
    private static void exportStandardGame(final Context context, final GameSet gameSet, final StandardBaseGame stdGame, final WritableSheet globalSheet, final WritableSheet deltaSheet, final int row) throws Exception {
        // game description
        globalSheet.addCell(new Label(0, row, ExcelHelper.buildStandardDescription(context, stdGame.getIndex(), stdGame.getBet().getBetType(), (int)stdGame.getDifferentialPoints())));
        deltaSheet.addCell(new Label(0, row, ExcelHelper.buildStandardDescription(context, stdGame.getIndex(), stdGame.getBet().getBetType(), (int)stdGame.getDifferentialPoints())));

        // each individual player score
        for (int column = 1; column <= gameSet.getPlayers().size(); ++column) {
            Player player = gameSet.getPlayers().get(column);
            
            // dead player
            if (!stdGame.getPlayers().contains(player)) {
                globalSheet.addCell(new Label(column, row, ExcelHelper.strDead));
                deltaSheet.addCell(new Label(column, row, ExcelHelper.strDead));
                continue;
            }
            
            else {
                Number globalIndividualScore = new Number(column, row, gameSet.getGameSetScores().getIndividualResultsAtGameOfIndex(stdGame.getIndex(), player));
                Number deltaIndividualScore = new Number(column, row, stdGame.getGameScores().getIndividualResult(player));
                
                globalSheet.addCell(globalIndividualScore);
                deltaSheet.addCell(deltaIndividualScore);
            }
        }
        
        // preneur
        globalSheet.addCell(new Label(gameSet.getPlayers().size() + 1, row, stdGame.getLeadingPlayer().getName()));
        deltaSheet.addCell(new Label(gameSet.getPlayers().size() + 1, row, stdGame.getLeadingPlayer().getName()));
    }
    
    /**
     * Export into the excel workbook the game with a standard 5 player style template.
     * @param context
     * @param stdGame
     * @param globalSheet
     * @param deltaSheet
     * @param row
     * @throws Exception
     */
    private static void exportStandardTarot5Game(final Context context, final GameSet gameSet, final StandardTarot5Game std5Game, final WritableSheet globalSheet, final WritableSheet deltaSheet, final int row) throws Exception {
        // game description
        globalSheet.addCell(new Label(0, row, ExcelHelper.buildStandardDescription(context, std5Game.getIndex(), std5Game.getBet().getBetType(), (int)std5Game.getDifferentialPoints())));
        deltaSheet.addCell(new Label(0, row, ExcelHelper.buildStandardDescription(context, std5Game.getIndex(), std5Game.getBet().getBetType(), (int)std5Game.getDifferentialPoints())));

        // each individual player score
        for (int column = 1; column <= gameSet.getPlayers().size(); ++column) {
        	Player player = gameSet.getPlayers().get(column);
            
            // dead player
            if (!std5Game.getPlayers().contains(player)) {
                globalSheet.addCell(new Label(column, row, ExcelHelper.strDead));
                deltaSheet.addCell(new Label(column, row, ExcelHelper.strDead));
                continue;
            }
            
            else {
                Number globalIndividualScore = new Number(column, row, gameSet.getGameSetScores().getIndividualResultsAtGameOfIndex(std5Game.getIndex(), player));
                Number deltaIndividualScore = new Number(column, row, std5Game.getGameScores().getIndividualResult(player));
                
                globalSheet.addCell(globalIndividualScore);
                deltaSheet.addCell(deltaIndividualScore);
            }
        }
        
        // preneur
        globalSheet.addCell(new Label(gameSet.getPlayers().size() + 1, row, std5Game.getLeadingPlayer().getName()));
        deltaSheet.addCell(new Label(gameSet.getPlayers().size() + 1, row, std5Game.getLeadingPlayer().getName()));
        
        // joueur appelé
        globalSheet.addCell(new Label(gameSet.getPlayers().size() + 2, row, std5Game.getCalledPlayer().getName()));
        deltaSheet.addCell(new Label(gameSet.getPlayers().size() + 2, row, std5Game.getCalledPlayer().getName()));
        
        // roi appelé
        globalSheet.addCell(new Label(gameSet.getPlayers().size() + 3, row, UIHelper.getKingTranslation(std5Game.getCalledKing().getKingType())));
        deltaSheet.addCell(new Label(gameSet.getPlayers().size() + 3, row, UIHelper.getKingTranslation(std5Game.getCalledKing().getKingType())));
    }
    
    /**
     * Export into the excel workbook the game with a belgian template.
     * @param context
     * @param game
     * @param globalSheet
     * @param deltaSheet
     * @param row
     * @throws Exception
     */
    private static void exportBelgianGame(final Context context, final GameSet gameSet, final BaseGame game, final WritableSheet globalSheet, final WritableSheet deltaSheet, final int row) throws Exception {
        // game description
        globalSheet.addCell(new Label(0, row, ExcelHelper.buildBelgianDescription(context, game.getIndex())));
        deltaSheet.addCell(new Label(0, row, ExcelHelper.buildBelgianDescription(context, game.getIndex())));
  
        // each individual player score
        for (int column = 1; column <= gameSet.getPlayers().size(); ++column) {
        	Player player = gameSet.getPlayers().get(column);
      
            // dead player
            if (!game.getPlayers().contains(player)) {
                globalSheet.addCell(new Label(column, row, ExcelHelper.strDead));
                deltaSheet.addCell(new Label(column, row, ExcelHelper.strDead));
                continue;
            }
      
            else {
                Number globalIndividualScore = new Number(column, row, gameSet.getGameSetScores().getIndividualResultsAtGameOfIndex(game.getIndex(), player));
                Number deltaIndividualScore = new Number(column, row, game.getGameScores().getIndividualResult(player));
                
                globalSheet.addCell(globalIndividualScore);
                deltaSheet.addCell(deltaIndividualScore);
            }
        }
    }
    
    /**
     * Builds and returns the description for a standard game.
     * @param context
     * @param gameIndex
     * @param bet
     * @param points
     * @return
     */
    private static String buildStandardDescription(final Context context, final int gameIndex, final BetType bet, final int points) {
        String toReturn = String.format(
                context.getResources().getString(R.string.lblStandardGameSynthesis), 
                Integer.toString(gameIndex),
                UIHelper.getBetTranslation(bet),
                (points >= 0 ? "+" + points : Integer.toString(points))
        );

        return toReturn;
    }
    
    /**
     * Builds and returns the description for a belgian game.
     * @param context
     * @param gameIndex
     * @return
     */
    private static String buildBelgianDescription(final Context context, final int gameIndex) {
        return gameIndex + " " + context.getResources().getString(R.string.belgeDescription); 
    }
    
    /**
     * Sets the static strings necessary to the export.
     */
    private static void initializeStaticStrings(final Context context) {
        ExcelHelper.strLeaderPlayer = context.getResources().getString(R.string.lblExportLeaderPlayer);
        ExcelHelper.strCalledColor = context.getResources().getString(R.string.lblExportCalledColor);
        ExcelHelper.strCalledPlayer = context.getResources().getString(R.string.lblExportCalledPlayer);
        ExcelHelper.strDead = context.getResources().getString(R.string.lblExportDeadAdjective);
    }
}
